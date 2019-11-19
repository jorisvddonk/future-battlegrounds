package futurebattlegrounds;

import java.util.ArrayList;
import java.util.Optional;

import javax.inject.Inject;

import futurebattlegrounds.Battleground;
import futurebattlegrounds.Ship;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

@Controller("/battleground")
public class BattlegroundController {

    @Inject
    private Battleground battleground;

    @Get(value = "/ships", produces = MediaType.APPLICATION_JSON)
    public ArrayList<Ship> ships() {
        return battleground.getShips();
    }

    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    public Battleground battleground() {
        return battleground;
    }

    @Get(value = "/stream", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flowable<Battleground> battlegroundStream() {
        return battleground.getObservable().toFlowable(BackpressureStrategy.DROP);
    }

    @Post(value = "/ships", produces = MediaType.APPLICATION_JSON)
    public Ship addShip() {
        return battleground.addShip();
    }

    @Post(value = "/ships/{uuid}/thrust", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public Optional<Ship> thrust(@PathVariable String uuid, @Body ThrustRequest request) {
        Optional<Ship> ship = battleground.getShip(uuid);
        if (ship.isPresent()) {
            ship.get().thrustRequest(request);
        }
        return ship;
    }

    @Post(value = "/ships/{uuid}/actionstate", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public Optional<Ship> actionstate(@PathVariable String uuid, @Body ShipActionState actionState) {
        Optional<Ship> ship = battleground.getShip(uuid);
        if (ship.isPresent()) {
            ship.get().setActionState(actionState);
        }
        return ship;
    }
}