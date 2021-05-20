package com.hitqz.scds.biz.battle.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HitRecordModel {

    public static final Map<Integer, String> SOLIDER_SHOT_PART_MAP = new HashMap<>();

    static {
        SOLIDER_SHOT_PART_MAP.put(0,"未知");
        SOLIDER_SHOT_PART_MAP.put(1,"头部");
        SOLIDER_SHOT_PART_MAP.put(2,"胸部");
        SOLIDER_SHOT_PART_MAP.put(3,"左手");
        SOLIDER_SHOT_PART_MAP.put(4,"右手");
        SOLIDER_SHOT_PART_MAP.put(5,"腹部");
        SOLIDER_SHOT_PART_MAP.put(6,"背部");
        SOLIDER_SHOT_PART_MAP.put(7,"左腿");
        SOLIDER_SHOT_PART_MAP.put(8,"右腿");
    }

    public static final Map<Integer, String> ARMORED_CAR_SHOT_PART_MAP = new HashMap<>();

    static {
        ARMORED_CAR_SHOT_PART_MAP.put(0, "未知");
        ARMORED_CAR_SHOT_PART_MAP.put(1, "前");
        ARMORED_CAR_SHOT_PART_MAP.put(2, "后");
        ARMORED_CAR_SHOT_PART_MAP.put(3, "左前");
        ARMORED_CAR_SHOT_PART_MAP.put(4, "左后");
        ARMORED_CAR_SHOT_PART_MAP.put(5, "右前");
        ARMORED_CAR_SHOT_PART_MAP.put(6, "右后");
    }

    private Date hitTime;
    private int shotNum;
    private int shotPart;

    public Date getHitTime() {
        return hitTime;
    }

    public void setHitTime(Date hitTime) {
        this.hitTime = hitTime;
    }

    public int getShotNum() {
        return shotNum;
    }

    public void setShotNum(int shotNum) {
        this.shotNum = shotNum;
    }

    public int getShotPart() {
        return shotPart;
    }

    public void setShotPart(int shotPart) {
        this.shotPart = shotPart;
    }

    public static final class HitRecordModelBuilder {
        private Date hitTime;
        private int shotNum;
        private int shotPart;

        private HitRecordModelBuilder() {
        }

        public static HitRecordModelBuilder aHitRecordModel() {
            return new HitRecordModelBuilder();
        }

        public HitRecordModelBuilder hitTime(Date hitTime) {
            this.hitTime = hitTime;
            return this;
        }

        public HitRecordModelBuilder shotNum(int shotNum) {
            this.shotNum = shotNum;
            return this;
        }

        public HitRecordModelBuilder shotPart(int shotPart) {
            this.shotPart = shotPart;
            return this;
        }

        public HitRecordModel build() {
            HitRecordModel hitRecordModel = new HitRecordModel();
            hitRecordModel.shotNum = this.shotNum;
            hitRecordModel.hitTime = this.hitTime;
            hitRecordModel.shotPart = this.shotPart;
            return hitRecordModel;
        }
    }
}
