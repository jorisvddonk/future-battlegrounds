package futurebattlegrounds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimedRequest {
    public final TimeOptions timeOptions;

    @JsonCreator
    public TimedRequest(@JsonProperty("timeOptions") TimeOptions timeOptions) {
        this.timeOptions = timeOptions;
    }

    /**
     * @return the timeOptions
     */
    public TimeOptions getTimeOptions() {
        return timeOptions;
    }
}
