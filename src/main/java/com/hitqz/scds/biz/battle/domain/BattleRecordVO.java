package com.hitqz.scds.biz.battle.domain;

import java.util.List;

public class BattleRecordVO extends BattleRecordEntity {

    private List<BattleRecordDetailEntity> vestData;

    public List<BattleRecordDetailEntity> getVestData() {
        return vestData;
    }

    public void setVestData(List<BattleRecordDetailEntity> vestData) {
        this.vestData = vestData;
    }
}
