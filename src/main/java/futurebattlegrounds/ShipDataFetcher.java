package futurebattlegrounds;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Singleton
public class ShipDataFetcher implements DataFetcher<Ship> {

    @Inject
    private Battleground battleground;

    @Override
    public Ship get(DataFetchingEnvironment environment) throws Exception {
        String uuid = environment.getArgument("uuid");
        Optional<Ship> ship = this.battleground.getShip(uuid);
        if (ship.isPresent()) {
            return ship.get();
        }
        return null;
    }

}