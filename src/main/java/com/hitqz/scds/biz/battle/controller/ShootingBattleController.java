package com.hitqz.scds.biz.battle.controller;

import com.hitqz.scds.biz.battle.domain.BattleQueryDO;
import com.hitqz.scds.biz.battle.domain.BattleSettingDO;
import com.hitqz.scds.biz.battle.domain.BattleSettingQueryDO;
import com.hitqz.scds.biz.battle.domain.BattleStatisticsDO;
import com.hitqz.scds.biz.battle.service.BattleService;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.common.domain.BaseResponse;
import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequestMapping("/battle")
public class ShootingBattleController {

    @Autowired
    private BattleService battleService;

    @PostMapping(value = "/set")
    public BaseResponse createBattleSetting(@RequestBody BattleSettingDO battleSettingDO) {
        battleService.createBattleSetting(battleSettingDO);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("创建成功")
                .build();
    }

    @PostMapping(value = "/set/query")
    public BaseResponse queryBattleSetting(@RequestBody BattleSettingQueryDO battleSettingQueryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.listBattleSettingByQuery(battleSettingQueryDO))
                .build();
    }

    @DeleteMapping(value = "/set/{id}")
    public BaseResponse deleteBattleSetting(@PathVariable("id") Long id) {
        battleService.deleteBattleSetting(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @GetMapping(value = "/start/{id}")
    public BaseResponse startBattle(@PathVariable("id") Long id) throws InterruptedException, SerialPortException, IOException {
        battleService.startBattle(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("开始演习")
                .build();
    }

    @GetMapping(value = "/end")
    public BaseResponse endBattle() throws SerialPortException, InterruptedException {
        battleService.endBattle();
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("结束演习")
                .build();
    }

    @GetMapping(value = "/now")
    public BaseResponse battleNow(@RequestParam(value = "last", required = false) Date time) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.getNowBattleDetail(time))
                .build();
    }

    @PostMapping(value = "/query")
    public BaseResponse queryBattle(@RequestBody BattleQueryDO battleQueryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.listBattleByQuery(battleQueryDO))
                .build();
    }

    @GetMapping(value = "/record/{id}/{index}")
    public BaseResponse getBattleRecord(@PathVariable("id") Long battleId, @PathVariable("index") int index) throws IOException {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.getBattleRecord(battleId, index))
                .build();
    }

    @DeleteMapping(value = "/{id}")
    public BaseResponse deleteBattle(@PathVariable("id") Long id) {
        battleService.deleteBattle(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @PostMapping(value = "/export/excel/{id}")
    public void getBattleRecord(@PathVariable("id") Long id, @RequestBody BattleStatisticsDO battleStatisticsDO, HttpServletResponse response) throws UnsupportedEncodingException {
        battleService.exportExcel(id, battleStatisticsDO, response);
    }
}
