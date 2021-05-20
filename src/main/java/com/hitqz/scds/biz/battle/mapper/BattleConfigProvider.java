package com.hitqz.scds.biz.battle.mapper;

import cn.hutool.core.util.StrUtil;
import com.hitqz.scds.biz.battle.domain.BattleSettingQueryDO;
import org.apache.ibatis.jdbc.SQL;

public class BattleConfigProvider {

    public String listBattleSettingByQuery(BattleSettingQueryDO battleSettingQueryDO) {
        SQL sql = new SQL();
        sql.SELECT("t1.id", "t1.`name`", "t1.`mode`", "t1.map_id", "t2.`name` map_name", "t2.type map_type")
                .FROM("t_battle_base_config t1", "t_map t2")
                .WHERE("t1.map_id = t2.id");
        if (StrUtil.isNotBlank(battleSettingQueryDO.getName())) {
            sql.WHERE("t1.`name` like concat('%',#{name},'%')");
        }
        if (StrUtil.isNotBlank(battleSettingQueryDO.getMapName())) {
            sql.WHERE("t2.`name` like concat('%',#{mapName},'%')");
        }
        sql.ORDER_BY("t1.create_time desc");
        return sql.toString();
    }
}
