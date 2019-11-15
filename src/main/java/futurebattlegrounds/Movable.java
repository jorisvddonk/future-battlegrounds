package futurebattlegrounds;

import javax.vecmath.Vector2d;

public interface Movable extends Tickable {
    Vector2d getPosition();

    void setMovementVector(Vector2d vector);

    Vector2d getMovementVector();
}