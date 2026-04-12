package com.eeeab.animate.server.animation.release.cooldown;

import com.eeeab.animate.server.animation.Animation;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    private final Map<Animation, Long> cds = new HashMap<>();
    private long currentTime = 0;

    public void tick() {
        currentTime++;
    }

    public void setCD(Animation key, int duration) {
        if (duration > 0) {
            cds.put(key, currentTime + duration);
        }
    }

    public boolean isReady(Animation key) {
        Long readyTime = cds.get(key);
        return readyTime == null || currentTime >= readyTime;
    }

    public long getRemainingCD(Animation key) {
        Long readyTime = cds.get(key);
        if (readyTime == null) {
            return 0;
        }
        long remaining = readyTime - currentTime;
        return Math.max(0, remaining);
    }

    public void resetCD(Animation key) {
        cds.remove(key);
    }

    public void clear() {
        cds.clear();
    }
}