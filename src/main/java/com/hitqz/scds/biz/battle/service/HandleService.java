package com.hitqz.scds.biz.battle.service;

import com.hitqz.scds.biz.battle.handler.DataHandlerContext;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

@Component
public class HandleService {

    public void handle(byte[] data) {
        String dataStr = DatatypeConverter.printHexBinary(data);
        int startFrame = 0;
        int endFrame;
        while (true) {
            if (startFrame >= dataStr.length()) {
                break;
            }
            startFrame = dataStr.indexOf("FF0", startFrame);
            endFrame = dataStr.indexOf("AFFA", startFrame);
            byte[] targetData = Arrays.copyOfRange(data, startFrame / 2, endFrame / 2 + 2);
            DataHandlerContext.getHandler(targetData[1]).handle(targetData);
            startFrame = endFrame + 4;
        }
    }
}
