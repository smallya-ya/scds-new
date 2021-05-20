package com.hitqz.scds.biz.battle.handler;

import com.hitqz.scds.biz.battle.service.BattleService;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import com.hitqz.scds.common.exception.DataHandleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.util.Objects;

public class CommonDataHandler implements AbstractDataHandler {

    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 指令类型校验，每种数据类型的校验，由具体实现类实现
     * @param data 数据帧16进制字符串
     * @return
     */
    protected boolean dataTypeVerification(String data) {
        return false;
    }

    @Override
    public void handle(byte[] data) {
        String str = DatatypeConverter.printHexBinary(data);
        log.info("[指令处理]：开始处理数据帧[{}]", str);
        if (!dataTypeVerification(str)) {
            throw new DataHandleException("[指令处理]：数据格式校验不通过，丢弃该数据帧" + str);
        }
        if(!dataVerification(data)) {
            throw new DataHandleException("[指令处理]：数据帧检验和验证不通过，丢弃该数据帧" + str);
        }
        int vestNum = Integer.parseInt(str.substring(6, 10), 16);
        ShootingVestModel vestEntity = BattleService.context.getVestEntityMap().get(vestNum);
        if (Objects.isNull(vestEntity)) {
            throw new DataHandleException(String.format("[指令处理]：收到%s号上报 未找到%s号数据，丢弃该数据帧%s", vestNum, vestNum, str));
        }
    }

}
