package futurebattlegrounds;

import java.util.ArrayList;

import javax.inject.Inject;

import futurebattlegrounds.Battleground;
import futurebattlegrounds.Ship;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/battleground")
public class BattlegroundController {

    @Inject
    private Battleground battleground;

    @Get(produces = MediaType.APPLICATION_JSON)
    public ArrayList<Ship> index() {
        return battleground.getShips();
    }
}