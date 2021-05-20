package com.hitqz.scds.biz.battle.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hitqz.scds.common.domain.CommonEntity;

@TableName("t_battle_team_config")
public class BattleTeamConfigEntity extends CommonEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long battleBaseConfigId;

    private String team;

    private Integer startNum;

    private Integer endNum;

    private String primaryWeapon;

    private String secondaryWeapon;

    public Long getBattleBaseConfigId() {
        return battleBaseConfigId;
    }

    public void setBattleBaseConfigId(Long battleBaseConfigId) {
        this.battleBaseConfigId = battleBaseConfigId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getEndNum() {
        return endNum;
    }

    public void setEndNum(Integer endNum) {
        this.endNum = endNum;
    }

    public String getPrimaryWeapon() {
        return primaryWeapon;
    }

    public void setPrimaryWeapon(String primaryWeapon) {
        this.primaryWeapon = primaryWeapon;
    }

    public String getSecondaryWeapon() {
        return secondaryWeapon;
    }

    public void setSecondaryWeapon(String secondaryWeapon) {
        this.secondaryWeapon = secondaryWeapon;
    }

    public static final class BattleTeamConfigEntityBuilder {
        private Long battleBaseConfigId;
        private String team;
        private Integer startNum;
        private Integer endNum;
        private String primaryWeapon;
        private String secondaryWeapon;

        private BattleTeamConfigEntityBuilder() {
        }

        public static BattleTeamConfigEntityBuilder aBattleTeamConfigEntity() {
            return new BattleTeamConfigEntityBuilder();
        }

        public BattleTeamConfigEntityBuilder battleBaseConfigId(Long battleBaseConfigId) {
            this.battleBaseConfigId = battleBaseConfigId;
            return this;
        }

        public BattleTeamConfigEntityBuilder team(String team) {
            this.team = team;
            return this;
        }

        public BattleTeamConfigEntityBuilder startNum(Integer startNum) {
            this.startNum = startNum;
            return this;
        }

        public BattleTeamConfigEntityBuilder endNum(Integer endNum) {
            this.endNum = endNum;
            return this;
        }

        public BattleTeamConfigEntityBuilder primaryWeapon(String primaryWeapon) {
            this.primaryWeapon = primaryWeapon;
            return this;
        }

        public BattleTeamConfigEntityBuilder secondaryWeapon(String secondaryWeapon) {
            this.secondaryWeapon = secondaryWeapon;
            return this;
        }

        public BattleTeamConfigEntity build() {
            BattleTeamConfigEntity battleTeamConfigEntity = new BattleTeamConfigEntity();
            battleTeamConfigEntity.setBattleBaseConfigId(battleBaseConfigId);
            battleTeamConfigEntity.setTeam(team);
            battleTeamConfigEntity.setStartNum(startNum);
            battleTeamConfigEntity.setEndNum(endNum);
            battleTeamConfigEntity.setPrimaryWeapon(primaryWeapon);
            battleTeamConfigEntity.setSecondaryWeapon(secondaryWeapon);
            return battleTeamConfigEntity;
        }
    }
}
