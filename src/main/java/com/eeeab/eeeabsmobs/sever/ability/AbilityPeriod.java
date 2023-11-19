package com.eeeab.eeeabsmobs.sever.ability;

public abstract class AbilityPeriod {
    public enum AbilityPeriodType {
        STARTUP,
        ACTIVE,
        RECOVERY,
        MISC
    }
    public final AbilityPeriodType periodType;

    protected AbilityPeriod(AbilityPeriodType periodType) {
        this.periodType = periodType;
    }

    public static class AbilityPeriodInstant extends AbilityPeriod {
        public AbilityPeriodInstant(AbilityPeriodType sectionType) {
            super(sectionType);
        }
    }

    public static class AbilityPeriodDuration extends AbilityPeriod {
        public final int duration;
        public AbilityPeriodDuration(AbilityPeriodType sectionType, int duration) {
            super(sectionType);
            this.duration = duration;
        }
    }

    public static class AbilityPeriodInfinite extends AbilityPeriod {
        public AbilityPeriodInfinite(AbilityPeriodType sectionType) {
            super(sectionType);
        }
    }
}
