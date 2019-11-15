package futurebattlegrounds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThrustRequest extends TimedRequest {

    @JsonCreator
    public ThrustRequest(@JsonProperty("timeOptions") TimeOptions timeOptions) {
        super(timeOptions);
    }

}