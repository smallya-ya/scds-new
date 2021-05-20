package com.hitqz.scds.biz.battle.thread;

import com.hitqz.scds.biz.battle.context.ShootingBattleContext;
import com.hitqz.scds.biz.battle.domain.SendDataMissionModel;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.util.concurrent.BlockingQueue;

public class SerialPortWriteDataThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ShootingBattleContext context;
    private SerialPort serialPort;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;

    public SerialPortWriteDataThread(ShootingBattleContext context, SerialPort serialPort, BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) {
        this.context = context;
        this.serialPort = serialPort;
        this.rollingMissionBlockingQueue = rollingMissionBlockingQueue;
    }

    @Override
    public void run() {
        log.info("[串口任务控制线程]：启动");
        while (!this.context.isStop()) {
            try {
                SendDataMissionModel sendDataMissionModel = rollingMissionBlockingQueue.take();
                log.info("[串口任务控制线程]：发送{}", DatatypeConverter.printHexBinary(sendDataMissionModel.getData()));
                serialPort.writeBytes(sendDataMissionModel.getData());
            } catch (InterruptedException | SerialPortException e) {
                log.error("[串口任务控制线程]：发送数据时出现异常", e);
                continue;
            } catch (Exception e) {
                log.error("[串口任务控制线程]：发送数据时出现异常", e);
                continue;
            }
        }
        log.info("[串口任务控制线程]：结束");
    }
}
