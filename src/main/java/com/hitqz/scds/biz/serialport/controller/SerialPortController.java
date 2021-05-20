package com.hitqz.scds.biz.serialport.controller;

import com.hitqz.scds.biz.serialport.service.SerialIndoorPortService;
import com.hitqz.scds.biz.serialport.service.SerialPortService;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.common.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/serialPort")
public class SerialPortController {

    @Autowired
    private SerialPortService serialPortService;

    @Autowired
    private SerialIndoorPortService serialIndoorPortService;

    @GetMapping(value = "/open")
    public BaseResponse openPort(@RequestParam("portName") String portName, @RequestParam("baudRate") int baudRate) {
        return serialPortService.openSerialPort(portName,baudRate);
    }

    @GetMapping(value = "/indoor/open")
    public BaseResponse openIndoorPort(@RequestParam("portName")String portName, @RequestParam("baudRate")int baudRate){
        return serialIndoorPortService.openSerialPort(portName,baudRate);
    }

    @GetMapping(value = "/close")
    public BaseResponse closePort() {
        serialPortService.closePort("主动关闭");
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @GetMapping(value = "/indoor/close")
    public BaseResponse closeIndoorPort() {
        serialIndoorPortService.closePort("主动关闭");
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @GetMapping(value = "/list")
    public BaseResponse listPort() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(serialPortService.findPorts())
                .build();
    }

    @GetMapping(value = "")
    public BaseResponse getCom(){
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(serialPortService.getNowComStatus())
                .build();
    }

    @GetMapping(value = "/indoor")
    public BaseResponse getVirtualCom(){
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(serialIndoorPortService.getNowComStatus())
                .build();
    }
}
