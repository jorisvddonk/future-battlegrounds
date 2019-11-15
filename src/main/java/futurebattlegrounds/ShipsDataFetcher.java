package futurebattlegrounds;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Singleton
public class ShipsDataFetcher implements DataFetcher<List<Ship>> {

    @Inject
    private Battleground battleground;

    @Override
    public List<Ship> get(DataFetchingEnvironment environment) throws Exception {
        return battleground.getShips();
    }

}