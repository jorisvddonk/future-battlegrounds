package futurebattlegrounds;

import javax.vecmath.Vector2d;

public class Ship implements Movable {
    private Vector2d position;
    private Vector2d movementVector;
    private Vector2d rotationVector;

    public Ship() {
        this.position = new Vector2d(0, 0);
        this.movementVector = new Vector2d(0, 0);
        this.rotationVector = new Vector2d(0, 0);
    }

    @Override
    public void tick(double seconds) {
        final Vector2d cloned = (Vector2d) this.movementVector.clone();
        cloned.scale(seconds);
        this.position.add(cloned);
    }

    @Override
    public void setMovementVector(Vector2d vector) {
        this.movementVector = vector;
    }

    @Override
    public Vector2d getMovementVector() {
        return this.movementVector;
    }

    /**
     * @return the rotationVector
     */
    public Vector2d getRotationVector() {
        return rotationVector;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

}