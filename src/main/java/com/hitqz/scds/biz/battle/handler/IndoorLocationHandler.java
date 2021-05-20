package com.hitqz.scds.biz.battle.handler;

import com.hitqz.scds.biz.battle.service.BattleService;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

public class IndoorLocationHandler extends CommonDataHandler {

    private Pattern pattern = Pattern.compile("^(FF06)\\w{24}(AFFA)$");

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
        int width = Integer.parseInt(str.substring(10, 18), 16);
        int height = Integer.parseInt(str.substring(18, 26), 16);
        log.info("[指令处理]：收到{}号上报 室内坐标系坐标{},{}", vestNum, width, height);
        vestEntity.setLastReportTime(new Date());
        vestEntity.setLng(BigDecimal.valueOf(width));
        vestEntity.setLat(BigDecimal.valueOf(height));
    }
}
