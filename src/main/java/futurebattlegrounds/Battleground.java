package futurebattlegrounds;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import io.micronaut.scheduling.annotation.Scheduled;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class Battleground {
    private ArrayList<Ship> ships = new ArrayList<>();
    private PublishSubject<ArrayList<Ship>> observableShips;
    private double timestamp = 0;

    @PostConstruct
    public void initialize() {
        observableShips = PublishSubject.create();

        Ship ship = new Ship(this, null);
        addShip(ship);
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public void setShips(ArrayList<Ship> ships) {
        this.ships = ships;
    }

    private void tick(double seconds) {
        getShips().forEach(ship -> ship.tick(seconds));
        timestamp = timestamp + seconds;
    }

    public Ship addShip() {
        Ship ship = new Ship(this, null);
        this.getShips().add(ship);
        return ship;
    }

    public Ship addShip(Ship ship) {
        this.getShips().add(ship);
        return ship;
    }

    public Optional<Ship> getShip(UUID uuid) {
        return getShips().stream().filter(ship -> ship.getUUID().equals(uuid)).findFirst();
    }

    public Optional<Ship> getShip(String uuid) {
        return getShips().stream().filter(ship -> ship.getUUID().toString().equals(uuid)).findFirst();
    }

    @Scheduled(fixedDelay = "20ms")
    void autoTick() {
        this.tick(0.1);
        observableShips.onNext(this.getShips());
    }

    public PublishSubject<ArrayList<Ship>> getObservableShips() {
        return observableShips;
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

}