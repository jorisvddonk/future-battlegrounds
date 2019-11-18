package futurebattlegrounds;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

import javax.vecmath.Vector2d;

public class Ship implements Movable, HasLifetime {
    private final Vector2d position;
    private Vector2d movementVector;
    private final Vector2d rotationVector;
    private final String IFF;
    private final UUID UUID;
    private double lifetime;
    private final Vector2d thrust = new Vector2d(0, 1);
    private ShipActionState actionState;

    @Deprecated
    private RequestManager<ThrustRequest> thrustRequestManager;

    public Ship(Battleground battleground, final String IFF) {
        this.thrustRequestManager = new RequestManager<>(battleground);
        this.position = new Vector2d(0, 0);
        this.movementVector = new Vector2d(0, 0);
        this.rotationVector = new Vector2d(0, 1);
        if (IFF != null) {
            this.IFF = IFF;
        } else {
            final byte[] array = new byte[12];
            new Random().nextBytes(array);
            this.IFF = new String(array, Charset.forName("UTF-8"));
        }
        this.UUID = java.util.UUID.randomUUID();

        try {
            this.actionState = new ShipActionState(0, 0, false);
        } catch (Exception e) {
            // not gonna happen
            e.printStackTrace();
        }
    }

    @Override
    public void tick(final double seconds) {
        if (this.isThrusting()) {
            final Vector2d v = ((Vector2d) this.rotationVector.clone()); // TODO: directional thrust support?
            v.scale(this.thrust.length() * seconds * 2.0);
            if (this.actionState.getThrust() > 0.01 || this.actionState.getThrust() < 0.01) {
                v.scale(this.actionState.getThrust());
            }
            this.movementVector.add(v);
        }

        if (this.isRotating()) {
            float deg_delta_r = this.actionState.getRotate() * (float) seconds * 20.0f * (float) (Math.PI / 180.0);
            Vector2d r = new Vector2d(
                    (this.rotationVector.getX() * Math.cos(deg_delta_r))
                            - (this.rotationVector.getY() * Math.sin(deg_delta_r)),
                    (this.rotationVector.getX() * Math.sin(deg_delta_r))
                            + (this.rotationVector.getY() * Math.cos(deg_delta_r)));
            r.normalize();
            this.rotationVector.set(r);
        }

        final Vector2d cloned = (Vector2d) this.movementVector.clone();
        cloned.scale(seconds);
        this.position.add(cloned);

        this.lifetime -= seconds;
    }

    @Override
    public void setMovementVector(final Vector2d vector) {
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

    protected void setPosition(Vector2d position) {
        this.position.set(position);
    }

    protected void setRotation(Vector2d rotation) {
        this.rotationVector.set(rotation);
        this.rotationVector.normalize();
    }

    public UUID getUUID() {
        return UUID;
    }

    public String getIFF() {
        return IFF;
    }

    @Override
    public double getRemainingLifetime() {
        return this.lifetime;
    }

    @Override
    public void keepAlive() {
        this.lifetime = Constants.KEEPALIVETIME;
    }

    public void thrustRequest(ThrustRequest request) {
        this.thrustRequestManager.addRequest(request);
    }

    public void setActionState(ShipActionState actionState) {
        this.actionState = actionState;
    }

    public boolean isThrusting() {
        return this.actionState.getThrust() > 0.01 || this.actionState.getThrust() < 0.01
                || this.thrustRequestManager.getActiveRequests().count() > 0;
    }

    public boolean isRotating() {
        return this.actionState.getRotate() > 0.01 || this.actionState.getRotate() < 0.01;
    }

}