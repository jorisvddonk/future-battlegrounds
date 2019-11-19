package futurebattlegrounds;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

import javax.vecmath.Vector2d;

public class Ship extends BaseMovable {
    private final String IFF;
    private final UUID UUID;
    private final Vector2d thrust = new Vector2d(0, 1);
    private ShipActionState actionState;
    private ShipState shipState;
    private Battleground battleground;

    @Deprecated
    private RequestManager<ThrustRequest> thrustRequestManager;

    public Ship(Battleground battleground, final String IFF) {
        super();
        this.battleground = battleground;
        this.thrustRequestManager = new RequestManager<>(battleground);
        if (IFF != null) {
            this.IFF = IFF;
        } else {
            final byte[] array = new byte[12];
            new Random().nextBytes(array);
            this.IFF = new String(array, Charset.forName("UTF-8"));
        }
        this.UUID = java.util.UUID.randomUUID();

        this.shipState = new ShipState(100, 60);
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

        super.tick(seconds);
    }

    public UUID getUUID() {
        return UUID;
    }

    public String getIFF() {
        return IFF;
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

    public boolean isShooting() {
        return this.actionState.getShooting();
    }

    public double getHull() {
        return this.shipState.getHull();
    }

    public double getBattery() {
        return this.shipState.getBattery();
    }

}