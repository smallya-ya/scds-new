package com.hitqz.scds.biz.battle.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

public class BattleSettingVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private Integer mode;

    private List<BattleTeamConfigEntity> teamData;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long mapId;

    private String mapName;

    private String mapType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
