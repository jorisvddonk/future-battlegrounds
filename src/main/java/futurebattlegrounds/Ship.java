package futurebattlegrounds;

import java.beans.JavaBean;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

import javax.vecmath.Vector2d;

@JavaBean
public class Ship implements Movable, HasLifetime {
    private final Vector2d position;
    private Vector2d movementVector;
    private final Vector2d rotationVector;
    private final String IFF;
    private final UUID UUID;
    private double lifetime;
    private final Vector2d thrust = new Vector2d(0, 1);

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
    }

    @Override
    public void tick(final double seconds) {
        if (this.isThrusting()) {
            final Vector2d v = ((Vector2d) this.rotationVector.clone()); // TODO: directional thrust support?
            v.scale(this.thrust.length() * seconds);
            this.movementVector.add(v);
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

    public boolean isThrusting() {
        return this.thrustRequestManager.getActiveRequests().count() > 0;
    }

}