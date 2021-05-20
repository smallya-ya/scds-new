package com.hitqz.scds.biz.battle.domain;

import com.hitqz.scds.common.domain.BaseQueryDO;

public class BattleQueryDO extends BaseQueryDO {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
