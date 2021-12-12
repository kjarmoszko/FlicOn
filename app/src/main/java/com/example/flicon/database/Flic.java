package com.example.flicon.database;

public class Flic {
    private String mac;
    private String name;
    private long singleClick;
    private long doubleClick;
    private long hold;

    public Flic() {

    }
    public Flic(String mac) {
        this.mac = mac;
    }
    public Flic(String mac, String name) {
        this.mac = mac;
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSingleClick() {
        return singleClick;
    }

    public void setSingleClick(long singleClick) {
        this.singleClick = singleClick;
    }

    public long getDoubleClick() {
        return doubleClick;
    }

    public void setDoubleClick(long doubleClick) {
        this.doubleClick = doubleClick;
    }

    public long getHold() {
        return hold;
    }

    public void setHold(long hold) {
        this.hold = hold;
    }

    @Override
    public String toString() {
        return mac;
    }
}
