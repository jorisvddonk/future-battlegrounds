package futurebattlegrounds;

import javax.vecmath.Vector2d;

public class BaseMovable implements Movable, HasLifetime {
  protected final Vector2d position;
  protected final Vector2d movementVector;
  protected final Vector2d rotationVector;
  protected double lifetime;

  public BaseMovable() {
    this.position = new Vector2d(0, 0);
    this.movementVector = new Vector2d(0, 0);
    this.rotationVector = new Vector2d(0, 1);
    this.keepAlive();
  }

  @Override
  public void tick(final double seconds) {
    final Vector2d cloned = (Vector2d) this.movementVector.clone();
    cloned.scale(seconds);
    this.position.add(cloned);

    this.lifetime -= seconds;
  }

  @Override
  public void setMovementVector(final Vector2d vector) {
    this.movementVector.set(vector);
  }

  @Override
  public Vector2d getMovementVector() {
    return this.movementVector;
  }

  public Vector2d getRotationVector() {
    return this.rotationVector;
  }

  @Override
  public Vector2d getPosition() {
    return this.position;
  }

  protected void setPosition(Vector2d position) {
    this.position.set(position);
  }

  protected void setRotation(Vector2d rotation) {
    this.rotationVector.set(rotation);
    this.rotationVector.normalize();
  }

  @Override
  public double getRemainingLifetime() {
    return this.lifetime;
  }

  @Override
  public void keepAlive() {
    this.lifetime = Constants.KEEPALIVETIME;
  }

}