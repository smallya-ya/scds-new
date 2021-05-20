package com.hitqz.scds.biz.battle.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hitqz.scds.common.domain.CommonEntity;

import java.util.Date;

@TableName("t_battle_log")
public class BattleLogEntity extends CommonEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long battleId;

    private Integer type;

    private Date time;

    private String log;

    @JsonIgnore
    private int isShow;

    public Long getBattleId() {
        return battleId;
    }

    public void setBattleId(Long battleId) {
        this.battleId = battleId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public static final class BattleLogEntityBuilder {
        private Long battleId;
        private Integer type;
        private Date time;
        private String log;
        private int isShow;

        private BattleLogEntityBuilder() {
        }

        public static BattleLogEntityBuilder aBattleLogEntity() {
            return new BattleLogEntityBuilder();
        }

        public BattleLogEntityBuilder battleId(Long battleId) {
            this.battleId = battleId;
            return this;
        }

        public BattleLogEntityBuilder type(Integer type) {
            this.type = type;
            return this;
        }

        public BattleLogEntityBuilder time(Date time) {
            this.time = time;
            return this;
        }

        public BattleLogEntityBuilder log(String log) {
            this.log = log;
            return this;
        }

        public BattleLogEntityBuilder isShow(int isShow) {
            this.isShow = isShow;
            return this;
        }

        public BattleLogEntity build() {
            BattleLogEntity battleLogEntity = new BattleLogEntity();
            battleLogEntity.setBattleId(battleId);
            battleLogEntity.setType(type);
            battleLogEntity.setTime(time);
            battleLogEntity.setLog(log);
            battleLogEntity.setIsShow(isShow);
            return battleLogEntity;
        }
    }
}
