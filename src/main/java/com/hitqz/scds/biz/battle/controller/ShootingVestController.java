package com.hitqz.scds.biz.battle.controller;

import com.hitqz.scds.biz.battle.service.ShootingVestService;
import com.hitqz.scds.biz.license.service.LicenseService;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.common.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/vest")
public class ShootingVestController {

    @Autowired
    private LicenseService licenseService;
    @Autowired
    private ShootingVestService shootingVestService;

    @GetMapping
    public BaseResponse getAllVest() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(shootingVestService.getAllVest())
                .build();
    }

    @GetMapping(value = "/die/{vestNum}")
    public BaseResponse sendVestDie(@PathVariable("vestNum") int vestNum) throws InterruptedException {
        shootingVestService.sendDieData(vestNum, licenseService.getMachineName());
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("判死成功")
                .build();
    }

    @GetMapping(value = "/live/{vestNum}")
    public BaseResponse sendVestLive(@PathVariable("vestNum") int vestNum) throws InterruptedException {
        shootingVestService.sendLiveData(vestNum);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("判活成功")
                .build();
    }

    @GetMapping(value = "/injure/{vestNum}")
    public BaseResponse sendVestInjure(@PathVariable("vestNum") int vestNum) throws InterruptedException {
        shootingVestService.sendInjureData(vestNum);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("判伤成功")
                .build();
    }

    @GetMapping(value = "/reloadAll")
    public BaseResponse sendReloadAll(@RequestParam(value = "ammo") int ammo
            , @RequestParam(value = "weapon") int weapon) throws InterruptedException {
        if (weapon != (byte) 0x02 && weapon != (byte) 0x03) {
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_BAD_REQUEST)
                    .status(Constant.FAIL_STATUS)
                    .msg("武器代码必须是0x02或者0x03")
                    .build();
        }
        shootingVestService.reloadAllVest(ammo, weapon);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("全体充弹成功")
                .build();
    }

    @GetMapping(value = "/reload")
    public BaseResponse reload(@RequestParam(value = "vestNum") int vestNum, @RequestParam(value = "ammo") int ammo
            , @RequestParam(value = "weapon") int weapon) throws InterruptedException {
        if (weapon != (byte) 0x02 && weapon != (byte) 0x03) {
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_BAD_REQUEST)
                    .status(Constant.FAIL_STATUS)
                    .msg("武器代码必须是0x02或者0x03")
                    .build();
        }
        shootingVestService.reloadVestAmmo(vestNum, ammo, weapon);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg(vestNum + "号充弹成功")
                .build();
    }
}
