package com.hitqz.scds.biz.battle.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.hitqz.scds.biz.battle.domain.BattleLogEntity;
import com.hitqz.scds.biz.battle.domain.HitRecordModel;
import com.hitqz.scds.biz.battle.mapper.BattleLogMapper;
import com.hitqz.scds.biz.battle.service.BattleService;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import com.hitqz.scds.common.Constant;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.regex.Pattern;

public class HitRecordHandler extends CommonDataHandler {

    private Pattern pattern = Pattern.compile("^(FF05)\\w{14}(AFFA)$");
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
        int shotNum = Integer.parseInt(str.substring(10, 14), 16);
        HitRecordModel hitRecordModel = HitRecordModel.HitRecordModelBuilder
                .aHitRecordModel()
                .hitTime(new Date())
                .shotNum(shotNum)
                .shotPart(Integer.parseInt(str.substring(14, 16), 16))
                .build();
        vestEntity.setHitRecordModel(hitRecordModel);
        vestEntity.setLastReportTime(new Date());
        vestEntity.setManualLiveFlag(-1);
        StringBuilder logStr = new StringBuilder(vestNum);
        logStr.append("号被");
        // 提取开枪者编号末位
        if (shotNum >= 1800) {
            switch (str.charAt(13)) {
                case 'C':
                    logStr.append("步兵地雷命中");
                    break;
                case 'D':
                    logStr.append("坦克地雷命中");
                    break;
                case 'E':
                    logStr.append("手雷命中");
                    break;
                case 'F':
                    logStr.append("爆炸物命中");
                    break;
                default:
                    logStr.append("未知物体命中");
                    break;
            }
        } else {
            logStr.append(shotNum).append("号击中");
        }
        String shotPart;
        if (vestNum >= 1800) {
            // 类为装甲车类，击中部位为 ARMORED_CAR_SHOT_PART_MAP
            shotPart = hitRecordModel.ARMORED_CAR_SHOT_PART_MAP.get(hitRecordModel.getShotPart());
        } else {
            // 类为士兵类，集中部位为 SOLIDER_SHOT_PART_MAP
            shotPart = hitRecordModel.SOLIDER_SHOT_PART_MAP.get(hitRecordModel.getShotPart());
        }
        logStr.append(shotPart);
        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(new Date())
                .type(Constant.SHOT_LOG_TYPE)
                .log(logStr.toString())
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);
        log.info("[指令处理]：收到{}号上报 射击者{}号 击中部位{}", vestNum, shotNum, shotPart);
    }
}
