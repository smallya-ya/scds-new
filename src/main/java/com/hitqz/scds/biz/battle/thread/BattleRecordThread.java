package com.hitqz.scds.biz.battle.thread;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.hitqz.scds.biz.battle.context.ShootingBattleContext;
import com.hitqz.scds.biz.battle.domain.BattleRecordDetailEntity;
import com.hitqz.scds.biz.battle.domain.BattleRecordEntity;
import com.hitqz.scds.biz.battle.mapper.BattleRecordDetailMapper;
import com.hitqz.scds.biz.battle.mapper.BattleRecordMapper;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BattleRecordThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ShootingBattleContext context;
    private Long battleId;
    private Collection<ShootingVestModel> vestModels;
    private BattleRecordMapper battleRecordMapper;
    private BattleRecordDetailMapper battleRecordDetailMapper;
    private LocalDateTime lastFrontEndHeartBeatTime;
    private int lostFrontEndHeartBeatLimit;

    public BattleRecordThread(ShootingBattleContext context, Long battleId
            , Collection<ShootingVestModel> vestModels) {
        this.context = context;
        this.battleId = battleId;
        this.vestModels = vestModels;

        this.lastFrontEndHeartBeatTime = LocalDateTime.now();

        this.battleRecordMapper = SpringUtil.getBean(BattleRecordMapper.class);
        this.battleRecordDetailMapper = SpringUtil.getBean(BattleRecordDetailMapper.class);
        this.lostFrontEndHeartBeatLimit = Integer.parseInt(SpringUtil.getProperty("lostFrontEndHeartBeatLimit"));
        log.info("[前端心跳监控]：最大允许失去心跳时间{}秒", lostFrontEndHeartBeatLimit);
    }

    @Override
    public void run() {
        int index = 1;
        while (!this.context.isStop()) {
            List<BattleRecordDetailEntity> vestDataList = new ArrayList<>(vestModels.size());
            for (ShootingVestModel model : vestModels) {
                vestDataList.add(BeanUtil.copyProperties(model, BattleRecordDetailEntity.class));
            }
            if (CollectionUtil.isNotEmpty(vestDataList)) {
                BattleRecordEntity battleRecordEntity = BattleRecordEntity.BattleRecordEntityBuilder
                        .aBattleRecordEntity()
                        .battleId(battleId)
                        .index(index)
                        .time(new Date())
                        .build();
                battleRecordMapper.insert(battleRecordEntity);
                vestDataList.forEach(vest -> {
                    vest.setRecordId(battleRecordEntity.getId());
                    battleRecordDetailMapper.insert(vest);
                });
                log.info("[战场回放记录线程]：第{}次记录数据", index);
                index++;
            }

            // 心跳超时处理
            if (lastFrontEndHeartBeatTime.plusSeconds(lostFrontEndHeartBeatLimit).isBefore(LocalDateTime.now())) {
                log.error("[前端心跳监控]：上次前端请求时间为{}，超过允许失去心跳时间{}，结束本次演习"
                        , lastFrontEndHeartBeatTime, lostFrontEndHeartBeatLimit);
                try {
                    this.context.stop();
                } catch (SerialPortException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("[战场回放记录线程]：结束");
    }

    /**
     * 刷新心跳时间
     */
    public void refeshHeartbeatTime() {
        this.lastFrontEndHeartBeatTime = LocalDateTime.now();
    }
}
