package futurebattlegrounds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipActionState {
    private float thrust;
    private float rotate;
    private boolean shooting;

    @JsonCreator
    public ShipActionState(@JsonProperty("thrust") float thrust, @JsonProperty("rotate") float rotate,
            @JsonProperty("shooting") boolean shooting) throws Exception {
        this.thrust = thrust;
        this.rotate = rotate;
        this.shooting = shooting;
        if (thrust > 1 || thrust < -1) {
            throw new Exception("thrust value is out of bounds [-1,1]");
        }
        if (rotate > 1 || rotate < -1) {
            throw new Exception("rotate value is out of bounds [-1,1]");
        }
    }

    /**
     * @return the thrust
     */
    public float getThrust() {
        return thrust;
    }

    /**
     * @return the rotate value
     */
    public float getRotate() {
        return rotate;
    }

    /**
     * @return the shooting value
     */
    public boolean getShooting() {
        return shooting;
    }
}