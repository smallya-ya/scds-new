package com.hitqz.scds.biz.battle.domain;

import java.util.List;

public class BattleSettingDO {

    protected String name;

    protected Long mapId;

    protected Integer mode;

    protected List<BattleTeamConfigEntity> teamData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public List<BattleTeamConfigEntity> getTeamData() {
        return teamData;
    }

    public void setTeamData(List<BattleTeamConfigEntity> teamData) {
        this.teamData = teamData;
    }
}
