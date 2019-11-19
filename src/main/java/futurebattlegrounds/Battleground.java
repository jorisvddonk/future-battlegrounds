package futurebattlegrounds;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.vecmath.Vector2d;

import io.micronaut.scheduling.annotation.Scheduled;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class Battleground {
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private double timestamp = 0;
    private Random random = new Random();

    @PostConstruct
    public void initialize() {
        observableShips = PublishSubject.create();
        observableStage = PublishSubject.create();
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public void setShips(final ArrayList<Ship> ships) {
        this.ships = ships;
    }

    private void tick(final double seconds) {
        getShips().forEach(ship -> ship.tick(seconds));
        timestamp = timestamp + seconds;
    }

    public class Stage {
        public final ArrayList<Ship> ships;
        public final double timestamp;

        Stage(final ArrayList<Ship> ships, final double timestamp) {
            this.ships = ships;
            this.timestamp = timestamp;
        }

        /**
         * @return the ships
         */
        public ArrayList<Ship> getShips() {
            return ships;
        }

        /**
         * @return the timestamp
         */
        public double getTimestamp() {
            return timestamp;
        }
    }

    public Stage getStage() {
        // todo: improve perf by not returning an new object every time?
        return new Stage(this.getShips(), this.getTimestampMax());
    }

    public Ship addShip() {
        final Ship ship = new Ship(this, null);
        final int r = random.nextInt(360);
        final int d = 500;
        final Vector2d v = new Vector2d(Math.cos(r) * d, Math.sin(r) * d);
        ship.setPosition(v);
        v.scale(-1);
        ship.setRotation(v);
        this.getShips().add(ship);
        return ship;
    }

    public Ship addShip(final Ship ship) {
        this.getShips().add(ship);
        return ship;
    }

    public Optional<Ship> getShip(final UUID uuid) {
        return getShips().stream().filter(ship -> ship.getUUID().equals(uuid)).findFirst();
    }

    public Optional<Ship> getShip(final String uuid) {
        return getShips().stream().filter(ship -> ship.getUUID().toString().equals(uuid)).findFirst();
    }

    @Scheduled(fixedDelay = "20ms")
    void autoTick() {
        this.tick(0.1);
        observableShips.onNext(this.getShips());
        observableStage.onNext(this.getStage());
    }

    public PublishSubject<ArrayList<Ship>> getObservableShips() {
        return observableShips;
    }

    public PublishSubject<Stage> getObservableStage() {
        return observableStage;
    }

    /**
     * @return the current simulation timestamp
     */
    public double getTimestamp() {
        return timestamp;
    }

    /**
     * @return the timestamp plus the tick rate
     */
    public double getTimestampMax() {
        return timestamp + 0.2;
    }

    protected void shoot(Bullet b) {
        this.bullets.add(b);
    }

}