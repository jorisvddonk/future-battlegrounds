package futurebattlegrounds;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.vecmath.Vector2d;

import io.micronaut.scheduling.annotation.Scheduled;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class Battleground {
    private ArrayList<Ship> ships = new ArrayList<>();
    private PublishSubject<ArrayList<Ship>> observableShips;

    @PostConstruct
    public void initialize() {
        observableShips = PublishSubject.create();

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

    @Scheduled(fixedDelay = "20ms")
    void autoTick() {
        this.tick(0.1);
        observableShips.onNext(this.getShips());
    }

    public PublishSubject<ArrayList<Ship>> getObservableShips() {
        return observableShips;
    }

}