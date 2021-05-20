package com.hitqz.scds.biz.serialport.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hitqz.scds.biz.serialport.controller.SerialPortWebSocketController;
import com.hitqz.scds.biz.serialport.domain.SerialPortEntity;
import com.hitqz.scds.biz.serialport.mapper.SerialPortMapper;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.common.domain.BaseResponse;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@DependsOn("flywayConfig")
@Service
public class SerialIndoorPortService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SerialPortMapper serialPortMapper;

    private SerialPort serialPort;

    @PostConstruct
    public void clearVirtualComTable() {
        LambdaQueryWrapper<SerialPortEntity> query = Wrappers.lambdaQuery();
        query.eq(SerialPortEntity::getType, 1);
        serialPortMapper.delete(query);
    }

    public SerialPortEntity getNowComStatus() {
        LambdaQueryWrapper<SerialPortEntity> query = Wrappers.lambdaQuery();
        query.eq(SerialPortEntity::getType, 1);
        return serialPortMapper.selectOne(query);
    }

    @Transactional
    public BaseResponse openSerialPort(String serialPortName, int baudRate) {
        try {
            serialPort = new SerialPort(serialPortName);
            serialPort.openPort();
            serialPort.setParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            log.info("[虚拟串口管理]：开启{}串口成功", serialPortName);
            this.clearVirtualComTable();
            SerialPortEntity serialPortEntity = SerialPortEntity.SerialPortEntityBuilder
                    .aSerialPortEntity()
                    .portName(serialPortName)
                    .baudRate(baudRate)
                    .status(Constant.NORMAL_STATUS)
                    .type(1)
                    .build();
            serialPortMapper.insert(serialPortEntity);
            SerialPortWebSocketController.noticeComStatus(serialPortEntity);
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_OK)
                    .msg("成功连接指定端口设备")
                    .build();
        } catch (SerialPortException e) {
            log.error("[虚拟串口管理]：连接串口时发生异常");
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .msg("连接串口时发生异常：" + e.getExceptionType())
                    .build();
        } catch (IOException e) {
            log.error("[虚拟串口管理]：WebSocket通信发生异常");
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .msg("WebSocket通信发生异常")
                    .build();
        }
    }

    @Transactional
    public void closePort(String msg) {
        log.info("[虚拟串口管理]：关闭串口{} {}", serialPort.getPortName(), msg);
        if (serialPort.isOpened()) {
            try {
                serialPort.closePort();
                this.clearVirtualComTable();
            } catch (SerialPortException e) {
                log.error("[虚拟串口管理]：关闭串口时发生异常", e);
            }
        }
    }

    public SerialPort getSerialPort() {
        Objects.requireNonNull(serialPort, "[虚拟串口管理]：串口未连接");
        Assert.state(serialPort.isOpened(), "[虚拟串口管理]：串口未打开");
        return serialPort;
    }
}
