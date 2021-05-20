package com.hitqz.scds.biz.serialport.controller;

import com.hitqz.scds.biz.serialport.domain.SerialPortEntity;
import com.hitqz.scds.biz.serialport.service.SerialPortService;
import com.hitqz.scds.common.domain.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/socket")
public class SerialPortWebSocketController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static Map<String, Session> sessionMap = new ConcurrentHashMap();
    private static SerialPortService serialPortService;

    @Autowired
    public void setSerialPortService(SerialPortService serialPortService){
        SerialPortWebSocketController.serialPortService = serialPortService;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessionMap.put(session.getId(), session);
        log.info("有新连接加入！当前在线人数为{}", sessionMap.size());
        session.getBasicRemote()
                .sendText(BaseResponse.BaseResponseBuilder
                        .aBaseResponse()
                        .data(serialPortService.getNowComStatus())
                        .build()
                        .toString()
                );
    }

    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
        log.info("有一连接关闭！当前在线人数为{}", sessionMap.size());
    }

    @OnError
    public void onError(Session session, Throwable error){
        log.error("WebSocket通信发生异常", error);
    }

    public static void noticeComStatus(SerialPortEntity serialPortEntity) throws IOException {
        String msg = BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .data(serialPortEntity)
                .build()
                .toString();
        for (Session session : sessionMap.values()) {
            session.getBasicRemote().sendText(msg);
        }
    }
}
