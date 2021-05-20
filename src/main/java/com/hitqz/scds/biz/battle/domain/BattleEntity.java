package com.hitqz.scds.biz.battle.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hitqz.scds.common.domain.CommonEntity;

import java.util.Date;

@TableName("t_battle")
public class BattleEntity extends CommonEntity {

    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long mapId;

    private int mapType;

    private Date beginTime;

    private Date endTime;

    private Integer status;

    private String remark;

    @TableField(exist = false)
    private int isLoadAmmo;

    @TableField(exist = false)
    private int isLoadAmmo2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsLoadAmmo() {
        return isLoadAmmo;
    }

    public void setIsLoadAmmo(int isLoadAmmo) {
        this.isLoadAmmo = isLoadAmmo;
    }

    public int getIsLoadAmmo2() {
        return isLoadAmmo2;
    }

    public void setIsLoadAmmo2(int isLoadAmmo2) {
        this.isLoadAmmo2 = isLoadAmmo2;
    }

    public static final class BattleEntityBuilder {
        private String name;
        private Long mapId;
        private int mapType;
        private Date beginTime;
        private Date endTime;
        private Integer status;
        private String remark;
        private int isLoadAmmo;
        private int isLoadAmmo2;

        private BattleEntityBuilder() {
        }

        public static BattleEntityBuilder aBattleEntity() {
            return new BattleEntityBuilder();
        }

        public BattleEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BattleEntityBuilder mapId(Long mapId) {
            this.mapId = mapId;
            return this;
        }

        public BattleEntityBuilder mapType(int mapType) {
            this.mapType = mapType;
            return this;
        }

        public BattleEntityBuilder beginTime(Date beginTime) {
            this.beginTime = beginTime;
            return this;
        }

        public BattleEntityBuilder endTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public BattleEntityBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public BattleEntityBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public BattleEntityBuilder isLoadAmmo(int isLoadAmmo) {
            this.isLoadAmmo = isLoadAmmo;
            return this;
        }

        public BattleEntityBuilder isLoadAmmo2(int isLoadAmmo2) {
            this.isLoadAmmo2 = isLoadAmmo2;
            return this;
        }

        public BattleEntity build() {
            BattleEntity battleEntity = new BattleEntity();
            battleEntity.setName(name);
            battleEntity.setMapId(mapId);
            battleEntity.setMapType(mapType);
            battleEntity.setBeginTime(beginTime);
            battleEntity.setEndTime(endTime);
            battleEntity.setStatus(status);
            battleEntity.setRemark(remark);
            battleEntity.setIsLoadAmmo(isLoadAmmo);
            battleEntity.setIsLoadAmmo2(isLoadAmmo2);
            return battleEntity;
        }
    }
}
