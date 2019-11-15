package futurebattlegrounds;

import java.util.ArrayList;
import java.util.stream.Stream;

public class RequestManager<T extends TimedRequest> {
    private Battleground battleground;
    private final ArrayList<T> requests;

    RequestManager(Battleground battleground) {
        this.battleground = battleground;
        this.requests = new ArrayList<>();
    }

    private void removeOld() {
        this.requests.removeIf(request -> request.timeOptions.wasActive(battleground.getTimestamp()));
    }

    Stream<T> getActiveRequests() {
        this.removeOld();
        double timestampMin = battleground.getTimestamp();
        double timestampMax = battleground.getTimestampMax();
        return this.requests.stream().filter(request -> request.timeOptions.isActive(timestampMin, timestampMax));
    }

    public void addRequest(T request) {
        this.requests.add(request);
    }

}