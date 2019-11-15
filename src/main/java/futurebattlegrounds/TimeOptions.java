package futurebattlegrounds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeOptions {
    private final double activationTime;
    private final double durationTime;

    @JsonCreator
    public TimeOptions(@JsonProperty("activationTime") double activationTime,
            @JsonProperty("durationTime") double durationTime) {
        this.activationTime = activationTime;
        this.durationTime = durationTime;
    }

    /**
     * @return the activationTime
     */
    public double getActivationTime() {
        return activationTime;
    }

    /**
     * @return the durationTime
     */
    public double getDurationTime() {
        return durationTime;
    }

    public boolean isActive(double timestampMin, double timestampMax) {
        return timestampMin >= activationTime && timestampMin <= activationTime + durationTime
                && timestampMax > activationTime;
    }

    public boolean wasActive(double timestamp) {
        return activationTime + durationTime < timestamp;
    }
}
