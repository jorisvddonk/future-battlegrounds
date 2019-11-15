package futurebattlegrounds;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.vecmath.Vector2d;

import io.micronaut.scheduling.annotation.Scheduled;

@Singleton
public class Battleground {
    private ArrayList<Ship> ships = new ArrayList<>();

    @PostConstruct
    public void initialize() {
        Ship ship = new Ship();
        ship.setMovementVector(new Vector2d(1, 0));
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
    }

    public void addShip(Ship ship) {
        this.getShips().add(ship);
    }

    @Scheduled(fixedDelay = "10ms")
    void autoTick() {
        this.tick(0.1);
    }

}