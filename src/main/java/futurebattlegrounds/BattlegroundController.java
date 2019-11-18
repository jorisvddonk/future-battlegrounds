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

    @Get(value = "/ships/stream", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flowable<ArrayList<Ship>> shipsStream() {
        return battleground.getObservableShips().toFlowable(BackpressureStrategy.DROP);
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
}