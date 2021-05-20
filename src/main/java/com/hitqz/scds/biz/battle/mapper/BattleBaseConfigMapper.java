package com.hitqz.scds.biz.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitqz.scds.biz.battle.domain.BattleBaseConfigEntity;
import com.hitqz.scds.biz.battle.domain.BattleSettingQueryDO;
import com.hitqz.scds.biz.battle.domain.BattleSettingVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BattleBaseConfigMapper extends BaseMapper<BattleBaseConfigEntity> {

    @SelectProvider(type = BattleConfigProvider.class, method = "listBattleSettingByQuery")
    @Results(id = "battleSettingVO", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "mode", property = "mode"),
            @Result(column = "map_id", property = "mapId"),
            @Result(column = "map_name", property = "mapName"),
            @Result(column = "map_type", property = "mapType"),
            @Result(column = "id", property = "teamData"
                    , many = @Many(select = "com.hitqz.scds.biz.battle.mapper.BattleTeamConfigMapper.queryByBaseConfigId"
                    , fetchType = FetchType.EAGER))
    })
    List<BattleSettingVO> listBattleSettingByQuery(BattleSettingQueryDO battleSettingQueryDO);
}
