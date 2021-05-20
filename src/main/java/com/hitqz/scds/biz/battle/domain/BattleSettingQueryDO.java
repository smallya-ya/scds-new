package com.hitqz.scds.biz.battle.domain;

import com.hitqz.scds.common.domain.BaseQueryDO;

public class BattleSettingQueryDO extends BaseQueryDO {

    private String name;
    private String mapName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
