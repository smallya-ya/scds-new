package com.hitqz.scds.biz.battle.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;

@TableName("t_battle_record_detail")
public class BattleRecordDetailEntity extends ShootingVestModel {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long recordId;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
}
