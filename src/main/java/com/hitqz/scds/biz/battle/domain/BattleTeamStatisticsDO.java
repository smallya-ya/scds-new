package com.hitqz.scds.biz.battle.domain;

import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class BattleTeamStatisticsDO {

    public String teamName;

    public int normal = 0;

    public int slightWound = 0;

    public int middleWound = 0;

    public int heavyWound = 0;

    public int die = 0;

    public BattleTeamStatisticsDO(String teamName, List<ShootingVestModel> vestEntityList) {
        this.teamName = teamName;
        if (CollectionUtils.isNotEmpty(vestEntityList)) {
            for (ShootingVestModel vestModel : vestEntityList){
                if (vestModel.getStatus() ==  0){
                    continue;
                }
                if (vestModel.getHp() ==  100){
                    normal++;
                }
                if (vestModel.getHp() ==  75){
                    slightWound++;
                }
                if (vestModel.getHp() ==  50){
                    middleWound++;
                }
                if (vestModel.getHp() ==  25){
                    heavyWound++;
                }
                if (vestModel.getHp() ==  0){
                    die++;
                }
            }
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getSlightWound() {
        return slightWound;
    }

    public void setSlightWound(int slightWound) {
        this.slightWound = slightWound;
    }

    public int getMiddleWound() {
        return middleWound;
    }

    public void setMiddleWound(int middleWound) {
        this.middleWound = middleWound;
    }

    public int getHeavyWound() {
        return heavyWound;
    }

    public void setHeavyWound(int heavyWound) {
        this.heavyWound = heavyWound;
    }

    public int getDie() {
        return die;
    }

    public void setDie(int die) {
        this.die = die;
    }
}
