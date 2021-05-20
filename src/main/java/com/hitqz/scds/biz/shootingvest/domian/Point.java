package com.hitqz.scds.biz.shootingvest.domian;

import com.hitqz.scds.utils.GpsCoordinateUtils;

public class Point {

    private double lat;// WGS-84纬度
    private double lng;// WGS-84经度

    private double bdLat;// 百度纬度
    private double bdLng;// 百度经度

    public Point(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
        double[] bdPoint = GpsCoordinateUtils.calWGS84toBD09(this.lat, this.lng);
        this.bdLat = bdPoint[0];
        this.bdLng = bdPoint[1];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point bmapPoint = (Point) obj;
            return (bmapPoint.getLng() == lng && bmapPoint.getLat() == lat) ? true : false;
        } else {
            return false;
        }
    }

    public double getBdLat() {
        return bdLat;
    }

    public void setBdLat(double bdLat) {
        this.bdLat = bdLat;
    }

    public double getBdLng() {
        return bdLng;
    }

    public void setBdLng(double bdLng) {
        this.bdLng = bdLng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Point [lat=" + lat + ", lng=" + lng + "]";
    }
}
