package com.hitqz.scds.utils;

/**
 * 这个祖传代码工具类不是我写的，是我拷过来的，我也不知道写的什么玩意，也不知道写的对不对
 * 等有空在重写吧
 * @author hongjiasen
 */
public class GpsCoordinateUtils {

    private static final double PI = 3.1415926535897932384626433832795;
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;

    /**
     * 地球坐标系 WGS-84 to 火星坐标系 GCJ-02
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calWGS84toGCJ02(double latitude, double longitude) {
        Point dev = calDev(latitude, longitude);
        double retLat = latitude + dev.getLatitude();
        double retLon = longitude + dev.getLongitude();
        return new double[]{retLat, retLon};
    }

    /**
     * 地球坐标系 WGS-84 to 百度坐标系 BD-09
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calWGS84toBD09(double latitude, double longitude) {
        Point dev = calDev(latitude, longitude);
        double retLat = latitude + dev.getLatitude();
        double retLon = longitude + dev.getLongitude();
        return calGCJ02toBD09(retLat, retLon);
    }

    /**
     * 火星坐标系 GCJ-02 to 地球坐标系 WGS-84
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calGCJ02toWGS84(double latitude, double longitude) {
        Point dev = calDev(latitude, longitude);
        double retLat = latitude - dev.getLatitude();
        double retLon = longitude - dev.getLongitude();
        dev = calDev(retLat, retLon);
        retLat = latitude - dev.getLatitude();
        retLon = longitude - dev.getLongitude();
        return new double[]{retLat, retLon};
    }

    /**
     * 百度坐标系 BD-09 to 地球坐标系 WGS-84
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calBD09toWGS84(double latitude, double longitude) {
        double[] gcj = calBD09toGCJ02(latitude, longitude);
        return calGCJ02toWGS84(gcj[0], gcj[1]);
    }

    private static Point calDev(double latitude, double longitude) {
        double dLat = calLat(longitude - 105.0, latitude - 35.0);
        double dLon = calLon(longitude - 105.0, latitude - 35.0);
        double radLat = latitude / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        return new Point(dLat, dLon);
    }

    private static double calLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double calLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 火星坐标系 GCJ-02 to 百度坐标系 BD-09
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calGCJ02toBD09(double latitude, double longitude) {
        double x = longitude, y = latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        double retLat = z * Math.sin(theta) + 0.006;
        double retLon = z * Math.cos(theta) + 0.0065;
        return new double[]{retLat, retLon};
    }

    /**
     * 百度坐标系 BD-09 to 火星坐标系 GCJ-02
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calBD09toGCJ02(double latitude, double longitude) {
        double x = longitude - 0.0065, y = latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        double retLat = z * Math.sin(theta);
        double retLon = z * Math.cos(theta);
        return new double[]{retLat, retLon};
    }

    static class Point {
        private double longitude;
        private double latitude;

        Point(double latitude, double longitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        @Override
        public String toString() {
            return longitude + "," + latitude;
        }
    }
}
