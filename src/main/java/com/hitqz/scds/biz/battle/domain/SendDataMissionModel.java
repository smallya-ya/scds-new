package com.hitqz.scds.biz.battle.domain;

public class SendDataMissionModel {

    private byte[] data;
    private boolean isWait;
    private int vestNum;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isWait() {
        return isWait;
    }

    public void setWait(boolean wait) {
        isWait = wait;
    }

    public int getVestNum() {
        return vestNum;
    }

    public void setVestNum(int vestNum) {
        this.vestNum = vestNum;
    }

    public static final class SendDataMissionModelBuilder {
        private byte[] data;
        private boolean isWait;
        private int vestNum;

        private SendDataMissionModelBuilder() {
        }

        public static SendDataMissionModelBuilder aSendDataMissionModel() {
            return new SendDataMissionModelBuilder();
        }

        public SendDataMissionModelBuilder data(byte[] data) {
            this.data = data;
            return this;
        }

        public SendDataMissionModelBuilder isWait(boolean isWait) {
            this.isWait = isWait;
            return this;
        }

        public SendDataMissionModelBuilder vestNum(int vestNum) {
            this.vestNum = vestNum;
            return this;
        }

        public SendDataMissionModel build() {
            SendDataMissionModel sendDataMissionModel = new SendDataMissionModel();
            sendDataMissionModel.setData(data);
            sendDataMissionModel.setVestNum(vestNum);
            sendDataMissionModel.isWait = this.isWait;
            return sendDataMissionModel;
        }
    }
}
