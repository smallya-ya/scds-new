package com.hitqz.scds.biz.map.controller;

import com.hitqz.scds.biz.map.domain.IndoorMapDO;
import com.hitqz.scds.biz.map.domain.MapEntity;
import com.hitqz.scds.biz.map.domain.MapQueryDO;
import com.hitqz.scds.biz.map.service.MapService;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.common.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @PostMapping("/indoor")
    public BaseResponse addMap(@RequestParam("file") MultipartFile file, IndoorMapDO indoorMapDO) throws IOException {
        mapService.addIndoorMap(file, indoorMapDO);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @PostMapping("")
    public BaseResponse addMap(@RequestBody MapEntity mapEntity) {
        mapService.addMap(mapEntity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("地图创建成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteMap(@PathVariable("id") long id) throws IOException {
        mapService.deleteMap(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @GetMapping("/{id}")
    public BaseResponse getMap(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(mapService.getMap(id))
                .build();
    }

    @PostMapping("/query")
    public BaseResponse query(@RequestBody MapQueryDO mapQueryDO){
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(mapService.listMapByQuery(mapQueryDO))
                .build();
    }

}
