package com.hitqz.scds.biz.serialport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitqz.scds.biz.serialport.domain.SerialPortEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SerialPortMapper extends BaseMapper<SerialPortEntity> {

    @Select("select id, port_name, baud_rate, `status`, msg, `type` from t_com where `type` = 0 LIMIT 1 ")
    SerialPortEntity getComEntity();

}
