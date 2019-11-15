package futurebattlegrounds;

import javax.inject.Singleton;
import futurebattlegroundsRPC.BattlegroundsGrpc;
import futurebattlegroundsRPC.StateReply;

@Singleton
public class BattlegroundsEndpoint extends BattlegroundsGrpc.BattlegroundsImplBase {

    private final Battleground battleground;

    public BattlegroundsEndpoint(Battleground battleground) {
        this.battleground = battleground;
    }

    @Override
    public void getState(futurebattlegroundsRPC.Empty request,
            io.grpc.stub.StreamObserver<futurebattlegroundsRPC.StateReply> responseObserver) {
        futurebattlegroundsRPC.Ship.Builder shipBuilder = futurebattlegroundsRPC.Ship.newBuilder();
        battleground.getShips()
                .forEach(
                        ship -> shipBuilder
                                .setPosition(futurebattlegroundsRPC.Position.newBuilder()
                                        .setX(ship.getPosition().getX()).setY(ship.getPosition().getY()).build())
                                .build());

        StateReply reply = StateReply.newBuilder().addShips(shipBuilder.build()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}