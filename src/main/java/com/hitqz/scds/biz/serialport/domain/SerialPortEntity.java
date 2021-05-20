package com.hitqz.scds.biz.serialport.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hitqz.scds.common.domain.CommonEntity;

@TableName("t_com")
public class SerialPortEntity extends CommonEntity {

    private String portName;

    private Integer baudRate;

    private Integer status;

    private String msg;

    private int type;

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Integer getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(Integer baudRate) {
        this.baudRate = baudRate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static final class SerialPortEntityBuilder {
        private String portName;
        private Integer baudRate;
        private Integer status;
        private String msg;
        private int type;

        private SerialPortEntityBuilder() {
        }

        public static SerialPortEntityBuilder aSerialPortEntity() {
            return new SerialPortEntityBuilder();
        }

        public SerialPortEntityBuilder portName(String portName) {
            this.portName = portName;
            return this;
        }

        public SerialPortEntityBuilder baudRate(Integer baudRate) {
            this.baudRate = baudRate;
            return this;
        }

        public SerialPortEntityBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public SerialPortEntityBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public SerialPortEntityBuilder type(int type) {
            this.type = type;
            return this;
        }

        public SerialPortEntity build() {
            SerialPortEntity serialPortEntity = new SerialPortEntity();
            serialPortEntity.setPortName(portName);
            serialPortEntity.setBaudRate(baudRate);
            serialPortEntity.setStatus(status);
            serialPortEntity.setMsg(msg);
            serialPortEntity.setType(type);
            return serialPortEntity;
        }
    }
}
