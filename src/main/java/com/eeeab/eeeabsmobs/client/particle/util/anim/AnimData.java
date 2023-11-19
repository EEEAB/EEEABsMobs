package com.eeeab.eeeabsmobs.client.particle.util.anim;

public abstract class AnimData {
    public float evaluate(float t) {
        return 0;
    }

    public static class KeyTrack extends AnimData {
        float[] values;
        float[] times;

        public KeyTrack(float[] values, float[] times) {
            this.values = values;
            this.times = times;
            if (values.length != times.length)
                System.out.println("Malformed key track. Must have same number of keys and values or key track will evaluate to 0.");
        }

        @Override
        public float evaluate(float t) {
            if (values.length != times.length) return 0;
            for (int i = 0; i < times.length; i++) {
                float time = times[i];
                if (t == time) return values[i];
                else if (t < time) {
                    if (i == 0) return values[0];
                    float a = (t - times[i - 1]) / (time - times[i - 1]);
                    return values[i - 1] * (1 - a) + values[i] * a;
                } else {
                    if (i == values.length - 1) return values[i];
                }
            }
            return 0;
        }
    }

    public static class Oscillator extends AnimData {
        float value1, value2;
        float frequency;
        float phaseShift;

        public Oscillator(float value1, float value2, float frequency, float phaseShift) {
            this.value1 = value1;
            this.value2 = value2;
            this.frequency = frequency;
            this.phaseShift = phaseShift;
        }

        @Override
        public float evaluate(float t) {
            float a = (value2 - value1) / 2f;
            return (float) (value1 + a + a * Math.cos(t * frequency + phaseShift));
        }
    }

    public static class Constant extends AnimData {
        float value;

        public Constant(float value) {
            this.value = value;
        }

        @Override
        public float evaluate(float t) {
            return value;
        }
    }

    public static KeyTrack oscillate(float value1, float value2, int frequency) {
        if (frequency <= 1) new KeyTrack(new float[]{value1, value2}, new float[]{0, 1});
        float step = 1.0f / frequency;
        float[] times = new float[frequency + 1];
        float[] values = new float[frequency + 1];
        for (int i = 0; i < frequency + 1; i++) {
            float value = i % 2 == 0 ? value1 : value2;
            times[i] = step * i;
            values[i] = value;
        }
        return new KeyTrack(values, times);
    }

    public static KeyTrack startAndEnd(float startValue, float endValue) {
        return new KeyTrack(new float[]{startValue, endValue}, new float[]{0, 1});
    }

    public static Constant constant(float value) {
        return new Constant(value);
    }
}


