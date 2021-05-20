package com.hitqz.scds.biz.battle.thread;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hitqz.scds.biz.battle.context.ShootingBattleContext;
import com.hitqz.scds.biz.battle.domain.SendDataMissionModel;
import com.hitqz.scds.biz.map.domain.MapEntity;
import com.hitqz.scds.biz.shootingvest.domian.Point;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import com.hitqz.scds.utils.DataFrameUtils;
import com.hitqz.scds.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class MapDataThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ShootingBattleContext context;
    private MapEntity mapEntity;
    private Collection<ShootingVestModel> vestModelList;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;
    private String originMapImgBase64;
    private String mapImgBase64;
    private ListMultimap<Integer, Point> pointMap;

    public MapDataThread(ShootingBattleContext context, MapEntity mapEntity
            , Collection<ShootingVestModel> vestModelList
            , BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) throws IOException {
        this.context = context;
        this.mapEntity = mapEntity;
        this.vestModelList = vestModelList;
        this.rollingMissionBlockingQueue = rollingMissionBlockingQueue;

        byte[] data = Files.readAllBytes(Paths.get(mapEntity.getPath()));
        this.originMapImgBase64 = new String(Base64.getEncoder().encode(data));
        pointMap = ArrayListMultimap.create();
    }

    @Override
    public void run() {
        double width = Double.parseDouble(mapEntity.getLng());
        double highth = Double.parseDouble(mapEntity.getLat());

        while (!this.context.isStop()) {
            Map<Integer, Point> vestPointMap = new HashMap<>();
            Map<Integer, Color> vestColorMap = new HashMap<>();

            for(ShootingVestModel vestEntity : vestModelList) {
                if (vestEntity.getLng().doubleValue() > 0 && vestEntity.getLng().doubleValue() <= width && vestEntity.getLat().doubleValue() > 0 && vestEntity.getLat().doubleValue() <= highth) {
                    log.info("[绘图线程]:{}号绘制于点{},{}", vestEntity.getNum(), vestEntity.getLng(), vestEntity.getLat());
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
                    if (vestEntity.getLat().compareTo(BigDecimal.ZERO) != 0 || vestEntity.getLng().compareTo(BigDecimal.ZERO) != 0) {
                        log.error("[绘图线程]:{}号当前坐标[{},{}]超出画图 下发超出范围预警", vestEntity.getNum(), vestEntity.getLng(), vestEntity.getLat());
                        vestEntity.setLat(BigDecimal.ZERO);
                        vestEntity.setLng(BigDecimal.ZERO);
                        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                                .aSendDataMissionModel()
                                .vestNum(0)
                                .isWait(false)
                                .data(DataFrameUtils.createOutOfRangeData(vestEntity.getNum()))
                                .build();
                        try {
                            rollingMissionBlockingQueue.put(data);
                        } catch (InterruptedException e) {
                            log.error("[绘图线程]:下发超出范围预警发生异常", e);
                            continue;
                        }
                    }
                }
            }
            StringBuilder sb = new StringBuilder("data:image/jpg;base64,");
            sb.append(ImageUtils.pressBatchBackGroudText(vestPointMap, pointMap, originMapImgBase64, "宋体", Font.BOLD, 40, Color.white, vestColorMap));
            mapImgBase64 = sb.toString();
        }
        log.info("[绘图线程]：结束");
    }

    public String getMapImgBase64() {
        return StrUtil.isNotBlank(mapImgBase64) ? mapImgBase64 : originMapImgBase64;
    }
}
