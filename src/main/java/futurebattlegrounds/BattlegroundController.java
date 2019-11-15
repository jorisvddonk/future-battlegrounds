package futurebattlegrounds;

import java.util.ArrayList;

import javax.inject.Inject;

import futurebattlegrounds.Battleground;
import futurebattlegrounds.Ship;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

@Controller("/battleground")
public class BattlegroundController {

    @Inject
    private Battleground battleground;

    @Get(produces = MediaType.APPLICATION_JSON)
    public ArrayList<Ship> index() {
        return battleground.getShips();
    }

    @Get(value = "/stream", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flowable<ArrayList<Ship>> stream() {
        return battleground.getObservableShips().toFlowable(BackpressureStrategy.DROP);
    }
}