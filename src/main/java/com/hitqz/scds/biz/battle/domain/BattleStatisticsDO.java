package com.hitqz.scds.biz.battle.domain;

import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;

import java.util.List;

public class BattleStatisticsDO {

    private List<ShootingVestModel> red;
    private List<ShootingVestModel> blue;
    private List<ShootingVestModel> orange;
    private List<ShootingVestModel> yellow;

    public List<ShootingVestModel> getRed() {
        return red;
    }

    public void setRed(List<ShootingVestModel> red) {
        this.red = red;
    }

    public List<ShootingVestModel> getBlue() {
        return blue;
    }

    public void setBlue(List<ShootingVestModel> blue) {
        this.blue = blue;
    }

    public List<ShootingVestModel> getOrange() {
        return orange;
    }

    public void setOrange(List<ShootingVestModel> orange) {
        this.orange = orange;
    }

    public List<ShootingVestModel> getYellow() {
        return yellow;
    }

    public void setYellow(List<ShootingVestModel> yellow) {
        this.yellow = yellow;
    }
}
