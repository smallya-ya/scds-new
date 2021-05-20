package com.hitqz.scds.biz.battle.service;

import com.hitqz.scds.biz.battle.domain.BattleLogEntity;
import com.hitqz.scds.biz.battle.domain.SendDataMissionModel;
import com.hitqz.scds.biz.battle.mapper.BattleLogMapper;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.utils.DataFrameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;

@Service
public class ShootingVestService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BattleLogMapper battleLogMapper;

    public Collection<ShootingVestModel> getAllVest() {
        return BattleService.context.getVestEntityMap().values();
    }

    @Transactional
    public void sendDieData(int vestNum, String killer) throws InterruptedException {
        log.info("[演习管理]：收到{}号判死请求", vestNum);

        // hp:0
        BattleService.context.getVestEntityMap().get(vestNum).setHp(0);
        BattleService.context.getVestEntityMap().get(vestNum).setManualLiveFlag(0);

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(new Date())
                .type(Constant.DIE_LOG_TYPE)
                .log(vestNum + "号死亡 击杀者：" + killer)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .data(DataFrameUtils.createDieData(vestNum))
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
    }

    @Transactional
    public void sendLiveData(int vestNum) throws InterruptedException {
        log.info("[演习管理]：收到{}号判活请求", vestNum);

        // hp:100
        BattleService.context.getVestEntityMap().get(vestNum).setHp(100);
        BattleService.context.getVestEntityMap().get(vestNum).setManualLiveFlag(1);

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(new Date())
                .type(Constant.LIVE_LOG_TYPE)
                .log(vestNum + "号复活")
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .data(DataFrameUtils.createLiveData(vestNum))
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
    }

    @Transactional
    public void sendInjureData(int vestNum) throws InterruptedException {
        log.info("[演习管理]：收到{}号判伤请求", vestNum);

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(new Date())
                .type(Constant.INJURE_LOG_TYPE)
                .log(vestNum + "号判伤")
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .data(DataFrameUtils.createInjureData(vestNum))
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
    }

    @Transactional
    public void reloadAllVest(int ammo, int weapon) throws InterruptedException {
        log.info("[演习管理]：收到全体{}装弹{}请求", weapon == (byte) 0x02 ? "主武器" : "副武器", ammo);

        if (weapon == 2) {
            BattleService.context.getBattleEntity().setIsLoadAmmo(1);
        } else if (weapon == 3) {
            BattleService.context.getBattleEntity().setIsLoadAmmo2(1);
        }

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(new Date())
                .type(Constant.RELOAD_LOG_TPYE)
                .log("全体" + (weapon == (byte) 0x02 ? "主武器" : "副武器") + "充弹" + ammo)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .data(DataFrameUtils.createAllLoadingData(ammo, weapon))
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
    }

    @Transactional
    public void reloadVestAmmo(int vestNum, int ammo, int weapon) throws InterruptedException {
        log.info("[演习管理]：收到{}号的{}装弹{}请求", vestNum, weapon == (byte) 0x02 ? "主武器" : "副武器", ammo);

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(new Date())
                .type(Constant.RELOAD_LOG_TPYE)
                .log(vestNum + "号" + (weapon == (byte) 0x02 ? "主武器" : "副武器") + "充弹" + ammo)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .data(DataFrameUtils.createSingleLoadingData(vestNum, ammo, weapon))
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
    }
}
