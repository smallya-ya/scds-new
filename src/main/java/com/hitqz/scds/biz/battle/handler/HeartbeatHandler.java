package com.hitqz.scds.biz.battle.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.hitqz.scds.biz.battle.domain.BattleLogEntity;
import com.hitqz.scds.biz.battle.mapper.BattleLogMapper;
import com.hitqz.scds.biz.battle.service.BattleService;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import com.hitqz.scds.common.Constant;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class HeartbeatHandler extends CommonDataHandler {

    private Pattern pattern = Pattern.compile("^(FF03)\\w{18}(AFFA)$");
    private BattleLogMapper battleLogMapper = SpringUtil.getBean(BattleLogMapper.class);

    @Override
    protected boolean dataTypeVerification(String data) {
        return pattern.matcher(data).matches();
    }

    @Override
    public void handle(byte[] data) {
        super.handle(data);

        String str = DatatypeConverter.printHexBinary(data);
        int vestNum = Integer.parseInt(str.substring(6, 10), 16);
        ShootingVestModel vestEntity = BattleService.context.getVestEntityMap().get(vestNum);
        int ammo1 = Integer.parseInt(str.substring(10, 14), 16);
        int ammo2 = Integer.parseInt(str.substring(16, 20), 16);
        int hp = Integer.parseInt(str.substring(14, 16), 16);

        if (vestEntity.getManualLiveFlag() != 1 || (vestEntity.getManualLiveFlag() == 1 && hp != 0)) {
            vestEntity.setStatus(Constant.LIVE_STATUS);
            vestEntity.setAmmo1(ammo1);
            vestEntity.setAmmo2(ammo2);

            vestEntity.setHp(hp);
            vestEntity.setLastReportTime(new Date());
            if (hp == 0 && vestEntity.getManualLiveFlag() != 1) {
                vestEntity.setStatus(Constant.DIE_STATUS);
                BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                        .aBattleLogEntity()
                        .battleId(BattleService.context.getBattleEntity().getId())
                        .time(new Date())
                        .type(Constant.DIE_LOG_TYPE)
                        .log(vestNum + "号阵亡")
                        .isShow(0)
                        .build();
                if (Objects.nonNull(vestEntity.getHitRecordModel())) {
                    log.info("[指令处理]：{}号阵亡 击杀者：{}号", vestNum, vestEntity.getHitRecordModel().getShotNum());
                } else {
                    log.info("[指令处理]：{}号阵亡 未收到或未处理[0x05]击中指令就收到{}号生命值为0的数据帧，击杀者未知", vestNum, vestNum);
                }
                battleLogMapper.insert(battleLogEntity);
            }
            log.info("[指令处理]：收到{}号心跳上报 hp：{} 子弹1：{} 子弹2：{}", vestNum, hp, ammo1, ammo2);
        } else {
            log.info("[指令处理]：判活后未收到[0x05]击中指令就收到{}号生命值为0的数据帧，放弃", vestNum);
        }
    }
}
