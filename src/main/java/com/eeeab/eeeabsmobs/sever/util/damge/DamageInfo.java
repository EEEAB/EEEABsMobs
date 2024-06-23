package com.eeeab.eeeabsmobs.sever.util.damge;

public class DamageInfo {
    private long timestamp;
    private float adaptFactor;

    public DamageInfo(long timestamp, float adaptFactor) {
        this.timestamp = timestamp;
        this.adaptFactor = adaptFactor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getAdaptFactor() {
        return adaptFactor;
    }

    public void setAdaptFactor(float adaptFactor) {
        this.adaptFactor = adaptFactor;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DamageInfo{" +
                "timestamp=" + timestamp +
                ", adaptFactor=" + adaptFactor +
                '}';
    }
}
