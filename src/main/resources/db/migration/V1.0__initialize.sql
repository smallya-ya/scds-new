-- ----------------------------
-- Table structure for t_map
-- ----------------------------
CREATE TABLE `t_map`  (
  `id` bigint,
  `lng` varchar(100),
  `lat` varchar(100),
  `zoom` varchar(15),
  `create_time` datetime,
  `update_time` datetime,
  `remark` varchar(255),
  `name` varchar(20),
  `type` int(6) DEFAULT 0 COMMENT '0，室外地图；1，室内地图',
  `path` varchar(1024) COMMENT '当type为1时保存有效，保存地图路径',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_license
-- ----------------------------
CREATE TABLE `t_license`  (
    `id` varchar(32),
    `key` varchar(32),
    `machine_name` varchar(50),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_com
-- ----------------------------
CREATE TABLE `t_com`  (
  `id` bigint,
  `port_name` varchar(16),
  `baud_rate` int,
  `status` int,
  `msg` varchar(255),
  `type` int,
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_battle_base_config
-- ----------------------------
CREATE TABLE `t_battle_base_config`  (
  `id` bigint,
  `name` varchar(64),
  `map_id` bigint,
  `mode` int,
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_battle_team_config
-- ----------------------------
CREATE TABLE `t_battle_team_config`  (
  `id` bigint,
  `battle_base_config_id` bigint,
  `team` varchar(64),
  `start_num` int,
  `end_num` int,
  `primary_weapon` varchar(32),
  `secondary_weapon` varchar(32),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_battle
-- ----------------------------
CREATE TABLE `t_battle`  (
  `id` bigint,
  `name` varchar(64),
  `map_id` bigint,
  `map_type` int,
  `begin_time` datetime,
  `end_time` datetime,
  `status` int,
  `remark` varchar(256),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_battle_log
-- ----------------------------
CREATE TABLE `t_battle_log`  (
  `id` bigint,
  `battle_id` bigint,
  `type` int,
  `time` datetime,
  `log` varchar(256),
  `is_show` int,
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_battle_record
-- ----------------------------
CREATE TABLE `t_battle_record`  (
  `id` bigint,
  `battle_id` bigint,
  `index` int,
  `time` datetime,
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for t_battle_record_detail
-- ----------------------------
CREATE TABLE `t_battle_record_detail`  (
  `id` bigint,
  `record_id` bigint,
  `team` varchar(64),
  `num` int,
  `name` varchar(64),
  `weapon1` varchar(64),
  `ammo1` int,
  `weapon2` varchar(64),
  `ammo2` int,
  `hp` int,
  `status` int,
  `mode` int,
  `lat` decimal(10,6),
  `lng` decimal(10,6),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;
