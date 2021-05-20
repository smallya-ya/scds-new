package com.hitqz.scds.biz.battle.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hitqz.scds.biz.battle.context.ShootingBattleContext;
import com.hitqz.scds.biz.battle.context.ShootingBattleSerialPortContext;
import com.hitqz.scds.biz.battle.domain.*;
import com.hitqz.scds.biz.battle.mapper.*;
import com.hitqz.scds.biz.map.domain.MapEntity;
import com.hitqz.scds.biz.map.mapper.MapEntityMapper;
import com.hitqz.scds.biz.serialport.service.SerialIndoorPortService;
import com.hitqz.scds.biz.serialport.service.SerialPortService;
import com.hitqz.scds.biz.shootingvest.domian.Point;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.utils.ImageUtils;
import jssc.SerialPortException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BattleService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BattleBaseConfigMapper battleBaseConfigMapper;
    @Autowired
    private BattleTeamConfigMapper battleTeamConfigMapper;
    @Autowired
    private BattleMapper battleMapper;
    @Autowired
    private BattleLogMapper battleLogMapper;
    @Autowired
    private BattleRecordMapper battleRecordMapper;
    @Autowired
    private BattleRecordDetailMapper battleRecordDetailMapper;
    @Autowired
    private MapEntityMapper mapEntityMapper;
    @Autowired
    private SerialPortService serialPortService;
    @Autowired
    private SerialIndoorPortService serialIndoorPortService;

    public static ShootingBattleContext context;

    @PreDestroy
    @Transactional(noRollbackFor = {SerialPortException.class, InterruptedException.class})
    public void destroy() throws SerialPortException, InterruptedException {
        if (Objects.nonNull(context)) {
            log.error("[演习管理]：异常关闭程序，终止正在进行的实兵演习-{}", context.getBattleEntity().getName());
            context.getBattleEntity().setEndTime(new Date());
            battleMapper.updateById(context.getBattleEntity());
            context.stop();
            context = null;
        }
    }

    @Transactional
    public void createBattleSetting(BattleSettingDO battleSettingDO) {
        BattleBaseConfigEntity battleBaseConfigEntity = BattleBaseConfigEntity.BattleBaseConfigEntityBuilder
                .aBattleBaseConfigEntity()
                .mapId(battleSettingDO.getMapId())
                .name(battleSettingDO.getName())
                .mode(battleSettingDO.getMode())
                .build();
        battleBaseConfigMapper.insert(battleBaseConfigEntity);
        for (BattleTeamConfigEntity battleTeamConfigEntity : battleSettingDO.getTeamData()) {
            battleTeamConfigEntity.setBattleBaseConfigId(battleBaseConfigEntity.getId());
            battleTeamConfigMapper.insert(battleTeamConfigEntity);
        }
    }

    @Transactional
    public void startBattle(Long id) throws IOException, SerialPortException, InterruptedException {
        BattleBaseConfigEntity battleBaseConfigEntity = battleBaseConfigMapper.selectById(id);
        log.info("[演习管理]：开始演习{}", battleBaseConfigEntity.getName());
        LambdaQueryWrapper<BattleTeamConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(BattleTeamConfigEntity::getBattleBaseConfigId, id);
        List<BattleTeamConfigEntity> battleTeamConfigEntityList = battleTeamConfigMapper.selectList(query);
        MapEntity mapEntity = mapEntityMapper.selectById(battleBaseConfigEntity.getMapId());

        BattleEntity battleEntity = BattleEntity.BattleEntityBuilder
                .aBattleEntity()
                .name(battleBaseConfigEntity.getName())
                .beginTime(new Date())
                .isLoadAmmo(0)
                .isLoadAmmo2(0)
                .mapId(battleBaseConfigEntity.getMapId())
                .status(1)
                .mapType(mapEntity.getType())
                .remark(battleBaseConfigEntity.getId().toString())
                .build();
        battleMapper.insert(battleEntity);

        if (Objects.nonNull(context)) {
            log.error("[演习管理]：当前存在正在进行的演习{}，进行关闭", context.getBattleEntity().getName());
            this.endBattle();
        }
        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            context = new ShootingBattleSerialPortContext(battleEntity, battleBaseConfigEntity, battleTeamConfigEntityList
                    , mapEntity, serialPortService.getSerialPort(), serialIndoorPortService.getSerialPort());
        } else {
            context = new ShootingBattleSerialPortContext(battleEntity, battleBaseConfigEntity, battleTeamConfigEntityList
                    , mapEntity, serialPortService.getSerialPort(), null);
        }
        context.init();
        context.start();
    }

    @Transactional(noRollbackFor = {SerialPortException.class, InterruptedException.class})
    public void endBattle() throws SerialPortException, InterruptedException {
        if (Objects.nonNull(context)) {
            log.info("[演习管理]：结束演习{}", context.getBattleEntity().getName());
            context.getBattleEntity().setEndTime(new Date());
            battleMapper.updateById(context.getBattleEntity());
            context.stop();
            context = null;
        } else {
            log.info("[演习管理]：当前无正在进行的演习");
        }
    }

    @Transactional(readOnly = true)
    public PageInfo<BattleSettingVO> listBattleSettingByQuery(BattleSettingQueryDO battleSettingQueryDO){
        PageHelper.startPage(battleSettingQueryDO.getPage(), battleSettingQueryDO.getPageSize());
        return new PageInfo(battleBaseConfigMapper.listBattleSettingByQuery(battleSettingQueryDO));
    }

    @Transactional
    public void deleteBattleSetting(Long id) {
        LambdaQueryWrapper<BattleTeamConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(BattleTeamConfigEntity::getBattleBaseConfigId, id);
        battleTeamConfigMapper.delete(query);

        battleBaseConfigMapper.deleteById(id);
    }

    @Transactional
    public BattleDetailVO getNowBattleDetail(Date fromTime) {
        if (Objects.isNull(context)) {
            return null;
        } else {
            context.getBattleRecordThread().refeshHeartbeatTime();
        }

        LambdaQueryWrapper<BattleLogEntity> logQuery = Wrappers.lambdaQuery();
        logQuery.eq(BattleLogEntity::getBattleId, context.getBattleEntity().getId())
                .eq(BattleLogEntity::getIsShow, 0);
        if (Objects.nonNull(fromTime)) {
            logQuery.le(BattleLogEntity::getTime, fromTime);
        }
        List<BattleLogEntity> battleLogEntityList = battleLogMapper.selectList(logQuery);
        battleLogEntityList.forEach(log -> {
            log.setIsShow(1);
            battleLogMapper.updateById(log);
        });

        return BattleDetailVO.BattleDetailVOBuilder
                .aBattleDetailVO()
                .vestEntityList(context.getVestEntityMap().values())
                .mapBase64(Objects.nonNull(context.getMapDataThread()) ? context.getMapDataThread().getMapImgBase64() : null)
                .mapEntity(context.getMapEntity())
                .battleLogEntityList(battleLogEntityList)
                .isLoadAmmo(context.getBattleEntity().getIsLoadAmmo())
                .isLoadAmmo2(context.getBattleEntity().getIsLoadAmmo2())
                .build();
    }

    @Transactional(readOnly = true)
    public PageInfo<BattleEntity> listBattleByQuery(BattleQueryDO battleQueryDO) {
        LambdaQueryWrapper<BattleEntity> query = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(battleQueryDO.getName())) {
            query.like(BattleEntity::getName, battleQueryDO.getName());
        }
        query.orderByDesc(BattleEntity::getBeginTime);
        PageHelper.startPage(battleQueryDO.getPage(), battleQueryDO.getPageSize());
        return new PageInfo(battleMapper.selectList(query));
    }

    @Transactional(readOnly = true)
    public BattleRecordModelVO getBattleRecord(Long battleId, int index) throws IOException {
        BattleEntity battleEntity = battleMapper.selectById(battleId);

        LambdaQueryWrapper<BattleRecordEntity> recordQuery = Wrappers.lambdaQuery();
        recordQuery.eq(BattleRecordEntity::getBattleId, battleId);
        Integer count = battleRecordMapper.selectCount(recordQuery);

        recordQuery.eq(BattleRecordEntity::getIndex, index);
        BattleRecordEntity battleRecordEntity = battleRecordMapper.selectOne(recordQuery);

        LambdaQueryWrapper<BattleRecordDetailEntity> detailQuery = Wrappers.lambdaQuery();
        detailQuery.eq(BattleRecordDetailEntity::getRecordId, battleRecordEntity.getId());
        List<BattleRecordDetailEntity> battleRecordDetailEntityList = battleRecordDetailMapper.selectList(detailQuery);

        BattleRecordVO battleRecordVO = BeanUtil.copyProperties(battleRecordEntity, BattleRecordVO.class);
        battleRecordVO.setVestData(battleRecordDetailEntityList);

        MapEntity mapEntity = mapEntityMapper.selectById(battleEntity.getMapId());

        LambdaQueryWrapper<BattleLogEntity> logQuery = Wrappers.lambdaQuery();
        logQuery.eq(BattleLogEntity::getBattleId, battleId)
                .le(BattleLogEntity::getTime, battleRecordEntity.getTime());
        List<BattleLogEntity> battleLogEntityList = battleLogMapper.selectList(logQuery);

        BattleRecordModelVO battleRecordModelVO = BattleRecordModelVO.BattleRecordModelVOBuilder
                .aBattleRecordModelVO()
                .logList(battleLogEntityList)
                .battleRecordVO(battleRecordVO)
                .mapEntity(mapEntity)
                .nowIndex(index)
                .totIndex(count)
                .build();

        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            double width = Double.parseDouble(mapEntity.getLng());
            double highth = Double.parseDouble(mapEntity.getLat());
            byte[] data = Files.readAllBytes(Paths.get(mapEntity.getPath()));
            String originMapImgBase64 = new String(Base64.getEncoder().encode(data));

            Map<Integer, Point> vestPointMap = new HashMap<>();
            Map<Integer, Color> vestColorMap = new HashMap<>();
            ListMultimap<Integer, Point> pointMap = ArrayListMultimap.create();
            for(ShootingVestModel vestEntity : battleRecordDetailEntityList) {
                if (vestEntity.getLng().doubleValue() > 0 && vestEntity.getLng().doubleValue() <= width
                        && vestEntity.getLat().doubleValue() > 0 && vestEntity.getLat().doubleValue() <= highth) {
                    vestPointMap.put(vestEntity.getNum(), new Point(vestEntity.getLng().doubleValue(), vestEntity.getLat().doubleValue()));
                    pointMap.put(vestEntity.getNum(), new Point(vestEntity.getLng().doubleValue(), vestEntity.getLat().doubleValue()));
                    if (vestEntity.getHp() > 0) {
                        switch (vestEntity.getTeam()) {
                            case "red":
                                vestColorMap.put(vestEntity.getNum(), Color.red);
                            case "blue":
                                vestColorMap.put(vestEntity.getNum(), Color.blue);
                            case "orange":
                                vestColorMap.put(vestEntity.getNum(), Color.orange);
                            case "yellow":
                                vestColorMap.put(vestEntity.getNum(), new Color(165, 42, 42));
                        }
                    } else {
                        vestColorMap.put(vestEntity.getNum(), Color.black);
                    }
                } else {
                    continue;
                }
            }
            StringBuilder sb = new StringBuilder("data:image/jpg;base64,");
            sb.append(ImageUtils.pressBatchBackGroudText(vestPointMap, pointMap, originMapImgBase64
                    , "宋体", Font.BOLD, 40, Color.white, vestColorMap));
            battleRecordModelVO.setMapBase64(sb.toString());
        }

        return battleRecordModelVO;
    }

    @Transactional
    public void deleteBattle(Long id) {
        battleMapper.deleteById(id);

        LambdaQueryWrapper<BattleRecordEntity> recordQuery = Wrappers.lambdaQuery();
        recordQuery.eq(BattleRecordEntity::getBattleId, id);
        List<BattleRecordEntity> battleRecordEntityList = battleRecordMapper.selectList(recordQuery);

        battleRecordDetailMapper.deleteBatchIds(battleRecordEntityList
                .stream()
                .map(BattleRecordEntity::getId)
                .collect(Collectors.toList())
        );

        battleRecordMapper.delete(recordQuery);
    }

    @Transactional(readOnly = true)
    public void exportExcel(Long battleId, BattleStatisticsDO battleStatisticsDO, HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelWriter writer = ExcelUtil.getWriter(true);

        LambdaQueryWrapper<BattleLogEntity> logQuery = Wrappers.lambdaQuery();
        logQuery.eq(BattleLogEntity::getBattleId, battleId);
        List<BattleLogEntity> battleLogEntityList = battleLogMapper.selectList(logQuery);

        writer.addHeaderAlias("time", "时间");
        writer.addHeaderAlias("log", "演习日志");
        writer.setOnlyAlias(true);
        writer.autoSizeColumnAll();
        writer.write(battleLogEntityList, true);
        writer.renameSheet(0, "演习日志");

        if (CollectionUtils.isNotEmpty(battleStatisticsDO.getRed())) {
            writer.setSheet(1);
            writer.addHeaderAlias("num", "编号");
            writer.addHeaderAlias("name", "姓名");
            writer.addHeaderAlias("ammo1", "弹药量1");
            writer.addHeaderAlias("ammo2", "弹药量2");
            writer.addHeaderAlias("hp", "生命值");
            writer.addHeaderAlias("weapon1", "主武器");
            writer.addHeaderAlias("weapon2", "副武器");
            writer.setOnlyAlias(true);
            writer.write(battleStatisticsDO.getRed(), true);
            writer.renameSheet(1, "红队统计");
        }

        if (CollectionUtils.isNotEmpty(battleStatisticsDO.getBlue())) {
            writer.setSheet(2);
            writer.addHeaderAlias("num", "编号");
            writer.addHeaderAlias("name", "姓名");
            writer.addHeaderAlias("ammo1", "弹药量1");
            writer.addHeaderAlias("ammo2", "弹药量2");
            writer.addHeaderAlias("hp", "生命值");
            writer.addHeaderAlias("weapon1", "主武器");
            writer.addHeaderAlias("weapon2", "副武器");
            writer.setOnlyAlias(true);
            writer.write(battleStatisticsDO.getBlue(), true);
            writer.renameSheet(2, "蓝队统计");
        }

        if (CollectionUtils.isNotEmpty(battleStatisticsDO.getOrange())) {
            writer.setSheet(3);
            writer.addHeaderAlias("num", "编号");
            writer.addHeaderAlias("name", "姓名");
            writer.addHeaderAlias("hp", "生命值");
            writer.setOnlyAlias(true);
            writer.write(battleStatisticsDO.getOrange(), true);
            writer.renameSheet(3, "人质统计");
        }

        if (CollectionUtils.isNotEmpty(battleStatisticsDO.getYellow())) {
            writer.setSheet(4);
            writer.addHeaderAlias("num", "编号");
            writer.addHeaderAlias("name", "姓名");
            writer.addHeaderAlias("ammo1", "弹药量1");
            writer.addHeaderAlias("ammo2", "弹药量2");
            writer.addHeaderAlias("hp", "生命值");
            writer.addHeaderAlias("weapon1", "主武器");
            writer.addHeaderAlias("weapon2", "副武器");
            writer.setOnlyAlias(true);
            writer.write(battleStatisticsDO.getYellow(), true);
            writer.renameSheet(4, "劫匪统计");
        }

        List<BattleTeamStatisticsDO> teamStatisticsEntityList = new ArrayList<>(4);
        teamStatisticsEntityList.add(new BattleTeamStatisticsDO("红队", battleStatisticsDO.getRed()));
        teamStatisticsEntityList.add(new BattleTeamStatisticsDO("蓝队", battleStatisticsDO.getBlue()));
        teamStatisticsEntityList.add(new BattleTeamStatisticsDO("人质", battleStatisticsDO.getOrange()));
        teamStatisticsEntityList.add(new BattleTeamStatisticsDO("劫匪", battleStatisticsDO.getYellow()));
        writer.setSheet(5);
        writer.addHeaderAlias("teamName", "队伍");
        writer.addHeaderAlias("normal", "正常");
        writer.addHeaderAlias("slightWound", "轻伤");
        writer.addHeaderAlias("middleWound", "中伤");
        writer.addHeaderAlias("heavyWound", "重伤");
        writer.addHeaderAlias("die", "阵亡");
        writer.setOnlyAlias(true);
        writer.write(teamStatisticsEntityList, true);
        writer.renameSheet(5, "演习统计");

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("演习实况", "utf-8") + ".xlsx");
        try (OutputStream os = response.getOutputStream()) {
            writer.flush(os, true);
        } catch (Exception e) {
            log.error("导出演习实况时出现异常", e);
        }
    }
}
