package com.hitqz.scds.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGenerator implements IdentifierGenerator {

    @Autowired
    private SnowflakeConfig snowFlake;

    @Override
    public Long nextId(Object entity) {
        return snowFlake.snowflakeId();
    }
}
