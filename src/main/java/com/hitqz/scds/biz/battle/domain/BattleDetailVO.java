package com.hitqz.scds.biz.battle.domain;

import com.hitqz.scds.biz.map.domain.MapEntity;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;

import java.util.Collection;
import java.util.List;

public class BattleDetailVO {

    private String mapBase64;
    private Collection<ShootingVestModel> vestEntityList;
    private List<BattleLogEntity> battleLogEntityList;
    private MapEntity mapEntity;
    private int isLoadAmmo;
    private int isLoadAmmo2;

    public String getMapBase64() {
        return mapBase64;
    }

    public void setMapBase64(String mapBase64) {
        this.mapBase64 = mapBase64;
    }

    public Collection<ShootingVestModel> getVestEntityList() {
        return vestEntityList;
    }

    public void setVestEntityList(Collection<ShootingVestModel> vestEntityList) {
        this.vestEntityList = vestEntityList;
    }

    public List<BattleLogEntity> getBattleLogEntityList() {
        return battleLogEntityList;
    }

    public void setBattleLogEntityList(List<BattleLogEntity> battleLogEntityList) {
        this.battleLogEntityList = battleLogEntityList;
    }

    public MapEntity getMapEntity() {
        return mapEntity;
    }

    public void setMapEntity(MapEntity mapEntity) {
        this.mapEntity = mapEntity;
    }

    public int getIsLoadAmmo() {
        return isLoadAmmo;
    }

    public void setIsLoadAmmo(int isLoadAmmo) {
        this.isLoadAmmo = isLoadAmmo;
    }

    public int getIsLoadAmmo2() {
        return isLoadAmmo2;
    }

    public void setIsLoadAmmo2(int isLoadAmmo2) {
        this.isLoadAmmo2 = isLoadAmmo2;
    }

    public static final class BattleDetailVOBuilder {
        private String mapBase64;
        private Collection<ShootingVestModel> vestEntityList;
        private List<BattleLogEntity> battleLogEntityList;
        private MapEntity mapEntity;
        private int isLoadAmmo;
        private int isLoadAmmo2;

        private BattleDetailVOBuilder() {
        }

        public static BattleDetailVOBuilder aBattleDetailVO() {
            return new BattleDetailVOBuilder();
        }

        public BattleDetailVOBuilder mapBase64(String mapBase64) {
            this.mapBase64 = mapBase64;
            return this;
        }

        public BattleDetailVOBuilder vestEntityList(Collection<ShootingVestModel> vestEntityList) {
            this.vestEntityList = vestEntityList;
            return this;
        }

        public BattleDetailVOBuilder battleLogEntityList(List<BattleLogEntity> battleLogEntityList) {
            this.battleLogEntityList = battleLogEntityList;
            return this;
        }

        public BattleDetailVOBuilder mapEntity(MapEntity mapEntity) {
            this.mapEntity = mapEntity;
            return this;
        }

        public BattleDetailVOBuilder isLoadAmmo(int isLoadAmmo) {
            this.isLoadAmmo = isLoadAmmo;
            return this;
        }

        public BattleDetailVOBuilder isLoadAmmo2(int isLoadAmmo2) {
            this.isLoadAmmo2 = isLoadAmmo2;
            return this;
        }

        public BattleDetailVO build() {
            BattleDetailVO battleDetailVO = new BattleDetailVO();
            battleDetailVO.setMapBase64(mapBase64);
            battleDetailVO.setVestEntityList(vestEntityList);
            battleDetailVO.setBattleLogEntityList(battleLogEntityList);
            battleDetailVO.setMapEntity(mapEntity);
            battleDetailVO.setIsLoadAmmo(isLoadAmmo);
            battleDetailVO.setIsLoadAmmo2(isLoadAmmo2);
            return battleDetailVO;
        }
    }
}
