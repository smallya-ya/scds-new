package com.hitqz.scds.biz.map.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hitqz.scds.biz.battle.domain.BattleBaseConfigEntity;
import com.hitqz.scds.biz.battle.domain.BattleEntity;
import com.hitqz.scds.biz.battle.mapper.BattleBaseConfigMapper;
import com.hitqz.scds.biz.battle.mapper.BattleMapper;
import com.hitqz.scds.biz.map.domain.IndoorMapDO;
import com.hitqz.scds.biz.map.domain.MapEntity;
import com.hitqz.scds.biz.map.domain.MapQueryDO;
import com.hitqz.scds.biz.map.mapper.MapEntityMapper;
import com.hitqz.scds.config.SnowflakeConfig;
import com.hitqz.scds.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MapService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MapEntityMapper mapEntityMapper;
    @Autowired
    private BattleMapper battleMapper;
    @Autowired
    private BattleBaseConfigMapper battleBaseConfigMapper;
    @Autowired
    private SnowflakeConfig snowFlake;

    @Transactional
    public void addIndoorMap(MultipartFile file, IndoorMapDO indoorMapDO) throws IOException {
        long id = snowFlake.snowflakeId();
        log.info("开始创建地图文件[{}]", indoorMapDO.getName());
        long start = System.currentTimeMillis();
        String mapPath = "mapImg/" + id + "." + FileUtil.getSuffix(file.getOriginalFilename());
        File imgFile = new File(mapPath);
        if (!imgFile.getParentFile().exists()){
            imgFile.getParentFile().mkdirs();
        }
        if (ImageUtils.isRgbOrCmyk(file.getInputStream())) {
            IoUtil.copy(file.getInputStream(), new FileOutputStream(imgFile));
        } else {
            BufferedImage bufferedImage = ImageUtils.readImage(file.getInputStream());
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "jpg", os);
                InputStream input = new ByteArrayInputStream(os.toByteArray());
                IoUtil.copy(input, new FileOutputStream(imgFile));
            }
        }
        long end = System.currentTimeMillis();
        log.info("结束创建地图文件[{}]，用时[{}]ms", indoorMapDO.getName(), end - start);

        MapEntity map = MapEntity.MapEntityBuilder.aMapEntity()
                .id(id)
                .lat(indoorMapDO.getHeight())
                .lng(indoorMapDO.getWidth())
                .zoom("0")
                .name(indoorMapDO.getName())
                .remark(indoorMapDO.getRemark())
                .type(1)
                .path(mapPath)
                .build();
        mapEntityMapper.insert(map);
        long end2 = System.currentTimeMillis();
        log.info("创建地图记录[{}]，用时[{}]ms", indoorMapDO.getName(), end2 - end);
    }

    @Transactional
    public void deleteMap(long id) throws IOException {
        MapEntity mapEntity = mapEntityMapper.selectById(id);
        log.info("开始删除地图文件[{}]", mapEntity.getName());
        long start = System.currentTimeMillis();
        if (1 == mapEntity.getType()) {
            Path mapFile = Paths.get(mapEntity.getPath());
            Files.deleteIfExists(mapFile);
        }
        long end = System.currentTimeMillis();
        log.info("结束删除地图文件[{}]，用时[{}]ms", mapEntity.getName(), end - start);
        mapEntityMapper.deleteById(id);
        long end2 = System.currentTimeMillis();
        log.info("删除地图记录[{}]，用时[{}]ms", mapEntity.getName(), end2 - end);

        LambdaQueryWrapper<BattleEntity> battleEntityLambdaQueryWrapper = Wrappers.lambdaQuery();
        battleEntityLambdaQueryWrapper.eq(BattleEntity::getMapId, id);
        battleMapper.delete(battleEntityLambdaQueryWrapper);

        LambdaQueryWrapper<BattleBaseConfigEntity> battleBaseConfigMapperLambdaQueryWrapper = Wrappers.lambdaQuery();
        battleBaseConfigMapperLambdaQueryWrapper.eq(BattleBaseConfigEntity::getMapId, id);
        battleBaseConfigMapper.delete(battleBaseConfigMapperLambdaQueryWrapper);
        long end3 = System.currentTimeMillis();
        log.info("删除演习回放记录和演习配置[{}]，用时[{}]ms", mapEntity.getName(), end3 - end2);
    }

    @Transactional
    public void addMap(MapEntity mapEntity) {
        mapEntity.setType(0);
        mapEntityMapper.insert(mapEntity);
    }

    @Transactional(readOnly = true)
    public PageInfo<MapEntity> listMapByQuery(MapQueryDO mapQueryDO){
        LambdaQueryWrapper<MapEntity> query = Wrappers.lambdaQuery();
        query.orderByDesc(MapEntity::getCreateTime);
        if (StrUtil.isNotBlank(mapQueryDO.getName())) {
            query.like(MapEntity::getName, mapQueryDO.getName());
        }
        PageHelper.startPage(mapQueryDO.getPage(), mapQueryDO.getPageSize());
        return new PageInfo(mapEntityMapper.selectList(query));
    }

    @Transactional(readOnly = true)
    public MapEntity getMap(long id){
        return mapEntityMapper.selectById(id);
    }
}
