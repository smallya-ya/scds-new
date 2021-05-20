package com.hitqz.scds.biz.battle.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hitqz.scds.common.domain.CommonEntity;

import java.util.Date;

@TableName("t_battle_record")
public class BattleRecordEntity extends CommonEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    protected Long battleId;

    @TableField("`index`")
    protected Integer index;

    protected Date time;

    public Long getBattleId() {
        return battleId;
    }

    public void setBattleId(Long battleId) {
        this.battleId = battleId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public static final class BattleRecordEntityBuilder {
        private Long battleId;
        private Integer index;
        private Date time;

        private BattleRecordEntityBuilder() {
        }

        public static BattleRecordEntityBuilder aBattleRecordEntity() {
            return new BattleRecordEntityBuilder();
        }

        public BattleRecordEntityBuilder battleId(Long battleId) {
            this.battleId = battleId;
            return this;
        }

        public BattleRecordEntityBuilder index(Integer index) {
            this.index = index;
            return this;
        }

        public BattleRecordEntityBuilder time(Date time) {
            this.time = time;
            return this;
        }

        public BattleRecordEntity build() {
            BattleRecordEntity battleRecordEntity = new BattleRecordEntity();
            battleRecordEntity.setBattleId(battleId);
            battleRecordEntity.setIndex(index);
            battleRecordEntity.setTime(time);
            return battleRecordEntity;
        }
    }
}
