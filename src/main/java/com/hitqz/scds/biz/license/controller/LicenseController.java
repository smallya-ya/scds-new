package com.hitqz.scds.biz.license.controller;

import com.hitqz.scds.biz.license.domain.RegisterRequestDO;
import com.hitqz.scds.biz.license.service.LicenseService;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.common.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @GetMapping("/register")
    public BaseResponse isRegistered() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(licenseService.isRegistered() ? HttpServletResponse.SC_OK : HttpServletResponse.SC_FORBIDDEN)
                .status(Constant.SUCCESS_STATUS)
                .msg("")
                .data(licenseService.isRegistered())
                .build();

    }

    @GetMapping("/name")
    public BaseResponse getMachineName() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("")
                .data(licenseService.getMachineName())
                .build();

    }

    @PostMapping("/register")
    public BaseResponse register(@RequestBody RegisterRequestDO request){
        return licenseService.register(request);
    }

}
