package com.example.tyler.trafficapp;

public class Camera {
    private String cameraName;
    private String cameraId;
    private String cameraLong;
    private String cameraLat;

    public Camera(String name, String id, String longitude, String lat) {

        this.cameraName = name;
        this.cameraId = id;
        this.cameraLat = lat;
        this.cameraLong = longitude;
    }

    public String getCameraName() {
        return cameraName;
    }

    public String getCameraId() {
        return cameraId;
    }

    public String getCameraLong() {
        return cameraLong;
    }

    public String getCameraLat() {
        return cameraLat;
    }

    public void setCameraName(String name) {
        this.cameraName = name;
    }

    public void setCameraId(String id) {
        this.cameraId = id;
    }

    public void setCameraLong(String longitude) {
        this.cameraLong = longitude;
    }

    public void setCameraLat(String lat) {
        this.cameraLat = lat;
    }

}