package com.hitqz.scds.biz.map.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hitqz.scds.common.domain.CommonEntity;

@TableName("t_map")
public class MapEntity extends CommonEntity {

    private String lng;

    private String lat;

    private String zoom;

    private String name;

    private String remark;

    /**
     * 地图类型
     * 0，室外地图，使用百度地图显示
     * 1，室内地图，使用上传图片显示
     */
    private int type = 0;

    /**
     * 当type为1时保存有效，保存地图路径
     */
    private String path;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static final class MapEntityBuilder {
        protected Long id;
        private String lng;
        private String lat;
        private String zoom;
        private String name;
        private String remark;
        private int type = 0;
        private String path;

        private MapEntityBuilder() {
        }

        public static MapEntityBuilder aMapEntity() {
            return new MapEntityBuilder();
        }

        public MapEntityBuilder lng(String lng) {
            this.lng = lng;
            return this;
        }

        public MapEntityBuilder lat(String lat) {
            this.lat = lat;
            return this;
        }

        public MapEntityBuilder zoom(String zoom) {
            this.zoom = zoom;
            return this;
        }

        public MapEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MapEntityBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public MapEntityBuilder type(int type) {
            this.type = type;
            return this;
        }

        public MapEntityBuilder path(String path) {
            this.path = path;
            return this;
        }

        public MapEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MapEntity build() {
            MapEntity mapEntity = new MapEntity();
            mapEntity.setLng(lng);
            mapEntity.setLat(lat);
            mapEntity.setZoom(zoom);
            mapEntity.setName(name);
            mapEntity.setRemark(remark);
            mapEntity.setType(type);
            mapEntity.setPath(path);
            mapEntity.setId(id);
            return mapEntity;
        }
    }
}
