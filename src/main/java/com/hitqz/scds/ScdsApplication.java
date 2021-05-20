package com.hitqz.scds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author hongjiasen
 */
@SpringBootApplication(exclude = {
        FlywayAutoConfiguration.class
})
@MapperScan("com.hitqz.scds.**.mapper")
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class ScdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScdsApplication.class, args);
    }

}
