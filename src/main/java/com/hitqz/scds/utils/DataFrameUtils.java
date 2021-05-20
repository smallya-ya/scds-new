package com.hitqz.scds.utils;

import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;

/**
 * 实兵演习指令工具类
 */
public class DataFrameUtils {

    /**
     * 轮询指令
     * @param vestModel
     * @return
     */
    public static byte[] createPollingData(ShootingVestModel vestModel) {
        final byte[] data = new byte[13];
        data[0] = (byte) 0x02;
        data[1] = (byte) 0x00;
        data[2] = (byte) (vestModel.getNum() >> 8);
        data[3] = (byte) (vestModel.getNum() & 0xFF);
        data[4] = (byte) vestModel.getMode();
        data[5] = (byte) vestModel.getHp();
        data[6] = (byte) (vestModel.getAmmo1() >> 8);
        data[7] = (byte) (vestModel.getAmmo1() & 0xFF);
        data[8] = (byte) (vestModel.getAmmo2() >> 8);
        data[9] = (byte) (vestModel.getAmmo2() & 0xFF);
        data[10] = (byte) 0x00;
        data[11] = (byte) 0xAF;
        data[12] = (byte) 0xFA;

        for (int i = 0; i < data.length - 3; i++) {
            data[10] += data[i];
        }

        return data;
    }

    /**
     * 演习开始指令
     * @param battleMode
     * @return
     */
    public static byte[] createStartBattleData(int battleMode) {
        byte[] data = {(byte) 0x00, (byte) 0x06, (byte) 0xFF, (byte) battleMode, (byte) 0x00, (byte) 0xAF, (byte) 0xFA};

        for (int i = 0; i < data.length - 3; i++) {
            data[4] += data[i];
        }

        return data;
    }

    /**
     * 演习结束指令
     * @return
     */
    public static byte[] createFinishBattleData() {
        byte[] data = {(byte) 0x00, (byte) 0x0B, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0xAF, (byte) 0xFA};

        for (int i = 0; i < data.length - 3; i++) {
            data[4] += data[i];
        }

        return data;
    }

    /**
     * 判死指令
     * @param vestNum
     * @return
     */
    public static byte[] createDieData(int vestNum) {
        final byte[] data = new byte[8];
        data[0] = (byte) 0x00;
        data[1] = (byte) 0x09;
        data[2] = (byte) 0xFF;
        data[3] = (byte) (vestNum >> 8);
        data[4] = (byte) (vestNum & 255);
        data[5] = (byte) 0x00;
        data[6] = (byte) 0xAF;
        data[7] = (byte) 0xFA;

        for (int i = 0; i < data.length - 3; i++) {
            data[5] += data[i];
        }

        return data;
    }

    /**
     * 判活指令
     * @param vestNum
     * @return
     */
    public static byte[] createLiveData(int vestNum) {
        final byte[] data = new byte[8];
        data[0] = (byte) 0x00;
        data[1] = (byte) 0x08;
        data[2] = (byte) 0xFF;
        data[3] = (byte) (vestNum >> 8);
        data[4] = (byte) (vestNum & 255);
        data[5] = (byte) 0x00;
        data[6] = (byte) 0xAF;
        data[7] = (byte) 0xFA;

        for (int i = 0; i < data.length - 3; i++) {
            data[5] += data[i];
        }

        return data;
    }

    /**
     * 判伤指令
     * @param vestNum
     * @return
     */
    public static byte[] createInjureData(int vestNum) {
        final byte[] data = new byte[8];
        data[0] = (byte) 0x00;
        data[1] = (byte) 0xA1;
        data[2] = (byte) 0xFF;
        data[3] = (byte) (vestNum >> 8);
        data[4] = (byte) (vestNum & 255);
        data[5] = (byte) 0x00;
        data[6] = (byte) 0xAF;
        data[7] = (byte) 0xFA;

        for (int i = 0; i < data.length - 3; i++) {
            data[5] += data[i];
        }

        return data;
    }

    /**
     * 全体充弹指令
     * @param ammo
     * @param weapon
     * @return
     */
    public static byte[] createAllLoadingData(int ammo, int weapon) {
        byte[] data = new byte[11];
        data[0] = (byte) 0x01;
        data[1] = (byte) 0x14;
        data[2] = (byte) 0xFF;
        data[3] = (byte) 0xFF;
        data[4] = (byte) 0xFF;
        data[5] = (byte) (ammo >> 8);
        data[6] = (byte) (ammo & 255);
        data[7] = (byte) weapon;
        data[8] = (byte) 0x00;
        data[9] = (byte) 0xAF;
        data[10] = (byte) 0xFA;

        for (int i = 0; i < data.length - 3; i++) {
            data[8] += data[i];
        }
        
        return data;
    }

    /**
     * 单兵充弹指令
     * @param vestNum
     * @param ammo
     * @param weapon
     * @return
     */
    public static byte[] createSingleLoadingData(int vestNum, int ammo, int weapon) {
        byte[] data = new byte[11];
        data[0] = (byte) (0x01);
        data[1] = (byte) (0x14);
        data[2] = (byte) (0xFF);
        data[3] = (byte) (vestNum >> 8);
        data[4] = (byte) (vestNum & 255);
        data[5] = (byte) (ammo / 256);
        data[6] = (byte) (ammo & 255);
        data[7] = (byte) weapon;
        data[8] = (byte) 0x00;
        data[9] = (byte) 0xAF;
        data[10] = (byte) 0xFA;

        for (int i = 0; i < data.length - 3; i++) {
            data[8] += data[i];
        }

        return data;
    }

    /**
     * 室内演习超出范围预警
     * @param vestNum
     * @return
     */
    public static byte[] createOutOfRangeData(int vestNum) {
        byte[] data = new byte[8];
        data[0] = (byte) 0x00;
        data[1] = (byte) 0x0D;
        data[2] = (byte) 0xFF;
        data[3] = (byte) (vestNum >> 8);
        data[4] = (byte) (vestNum & 255);
        data[5] = (byte) 0x00;
        data[6] = (byte) 0xAF;
        data[7] = (byte) 0xFA;

        for (int i = 0; i < data.length - 3; i++) {
            data[5] += data[i];
        }

        return data;
    }

}
