package futurebattlegrounds;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.vecmath.Vector2d;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.scheduling.annotation.Scheduled;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class Battleground {
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private PublishSubject<Battleground> observable;
    private double timestamp = 0;
    private Random random = new Random();

    @PostConstruct
    public void initialize() {
        observable = PublishSubject.create();
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setShips(final ArrayList<Ship> ships) {
        this.ships = ships;
    }

    private void tick(final double seconds) {
        ships.forEach(ship -> ship.tick(seconds));
        bullets.forEach(bullet -> bullet.tick(seconds));

        timestamp = timestamp + seconds;

        ships.removeIf(ship -> ship.lifetime <= 0 || ship.getHull() <= 0);
        bullets.removeIf(bullet -> bullet.lifetime <= 0);
    }

    public Ship createShip() {
        final Ship ship = new Ship(this, null);
        final int r = random.nextInt(360);
        final int d = 500;
        final Vector2d v = new Vector2d(Math.cos(r) * d, Math.sin(r) * d);
        ship.setPosition(v);
        v.scale(-1);
        ship.setRotation(v);
        return ship;
    }

    public Ship addShip() {
        Ship s = this.createShip();
        this.getShips().add(s);
        return s;
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
        this.tick(0.02);
        observable.onNext(this);
    }

    @JsonIgnore
    public PublishSubject<Battleground> getObservable() {
        return observable;
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
    @JsonIgnore
    public double getTimestampMax() {
        return timestamp + 0.2;
    }

    protected void shoot(Bullet b) {
        this.bullets.add(b);
    }

}