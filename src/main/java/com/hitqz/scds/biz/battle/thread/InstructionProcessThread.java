package com.hitqz.scds.biz.battle.thread;

import cn.hutool.core.util.ArrayUtil;
import com.hitqz.scds.biz.battle.context.ShootingBattleContext;
import com.hitqz.scds.biz.battle.service.HandleService;
import com.hitqz.scds.common.exception.DataHandleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

/**
 * 实兵演习指令解析线程
 * @author hongjiasen
 */
public class InstructionProcessThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ShootingBattleContext context;
    private BlockingQueue<byte[]> dataQueue;
    private HandleService handleService;
    protected byte[] leftData;

    public InstructionProcessThread(ShootingBattleContext context, BlockingQueue<byte[]> dataQueue, HandleService handleService) {
        this.context = context;
        this.dataQueue = dataQueue;
        this.handleService = handleService;
    }

    @Override
    public void run() {
        while (!this.context.isStop()) {
            try {
                this.process(dataQueue.take());
            } catch (DataHandleException e) {
                log.error("[指令处理]：处理下位机指令时发生异常");
                continue;
            } catch (InterruptedException e) {
                log.error("[指令处理]：处理下位机指令时发生异常", e);
                continue;
            } catch (Exception e) {
                log.error("[指令处理]：处理下位机指令时发生异常", e);
                continue;
            }
        }
        dataQueue.clear();
        log.info("[指令处理]：结束");
    }

    private void process(byte[] data) {
        byte[] buff = null;
        if (null != leftData) {
            buff = new byte[leftData.length + data.length];
            System.arraycopy(leftData, 0, buff, 0, leftData.length);
            System.arraycopy(data, 0, buff, leftData.length, data.length);
            log.info("[指令处理]：缓冲区存在数据，合并后数据帧为[{}]，合并后继续解析，并清空缓冲区", DatatypeConverter.printHexBinary(buff));
            leftData = null;
        } else {
            buff = data;
        }

        String dataStr = DatatypeConverter.printHexBinary(buff);
        int startFrame = dataStr.indexOf("FF0");
        int endFrame = dataStr.lastIndexOf("AFFA");

        if ((startFrame == 0 && endFrame < startFrame) || (startFrame < 0 && endFrame > 0) || (startFrame < 0 && endFrame < 0)) {
            leftData = ArrayUtil.addAll(leftData, buff);
            log.info("[指令处理]：未收到完整数据帧，[{}]暂时放入缓冲区，缓冲区数据[{}]", DatatypeConverter.printHexBinary(buff), DatatypeConverter.printHexBinary(buff));
        }

        // 包含完整的一条或多条数据帧指令
        if (startFrame == 0 && endFrame == dataStr.length() - 4) {
            log.info("[指令处理]：接收到下位机完整数据帧[{}]，开始进行处理", DatatypeConverter.printHexBinary(buff));
            handleService.handle(buff);
        }

        // 包含完整数据帧指令，尾部还有数据帧的一部分
        if (startFrame == 0 && endFrame > 0 && endFrame < dataStr.length() - 4) {
            byte[] targetData = Arrays.copyOfRange(buff, startFrame, endFrame / 2 + 2);
            leftData = Arrays.copyOfRange(buff, endFrame / 2 + 2, buff.length);
            log.info("[指令处理]：未收到完整数据帧，截取完整数据帧[{}]，剩余部分[{}]暂时放入缓冲区，缓冲区数据[{}]"
                    , DatatypeConverter.printHexBinary(targetData)
                    , DatatypeConverter.printHexBinary(leftData)
                    , DatatypeConverter.printHexBinary(leftData));
            handleService.handle(targetData);
        }

        // 起始位置是其他的数据帧尾部，后面包含完整数据帧
        if (startFrame > 0 && endFrame == dataStr.length() - 4) {
            byte[] targetData = Arrays.copyOfRange(buff, startFrame / 2, buff.length);
            byte[] garbageData = Arrays.copyOfRange(buff, 0, startFrame / 2);
            log.info("[指令处理]：未收到完整数据帧，截取完整数据帧[{}]，剩余部分[{}]抛弃"
                    , DatatypeConverter.printHexBinary(targetData)
                    , DatatypeConverter.printHexBinary(garbageData));
            handleService.handle(targetData);
        }

        // 前后包含其余数据帧部分，中间包含完整数据帧
        if (startFrame > 0 && startFrame < endFrame && endFrame < dataStr.length() - 4) {
            byte[] targetData = Arrays.copyOfRange(buff, startFrame / 2, endFrame / 2 + 2);
            leftData = Arrays.copyOfRange(buff, endFrame / 2 + 2, buff.length);
            byte[] garbageData = Arrays.copyOfRange(buff, 0, startFrame / 2);
            log.info("[指令处理]：未收到完整数据帧，丢弃数据帧[{}]，截取完整数据帧[{}]，剩余部分[{}]暂时放入缓冲区，缓冲区数据[{}]"
                    , DatatypeConverter.printHexBinary(garbageData)
                    , DatatypeConverter.printHexBinary(targetData)
                    , DatatypeConverter.printHexBinary(leftData)
                    , DatatypeConverter.printHexBinary(leftData));
            handleService.handle(targetData);
        }
    }

    public BlockingQueue<byte[]> getDataQueue() {
        return dataQueue;
    }
}
