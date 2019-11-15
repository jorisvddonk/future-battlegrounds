package futurebattlegrounds;

import java.util.ArrayList;

import javax.inject.Singleton;
import futurebattlegroundsRPC.BattlegroundsGrpc;
import futurebattlegroundsRPC.Empty;
import futurebattlegroundsRPC.StateReply;
import io.grpc.stub.StreamObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@Singleton
public class BattlegroundsEndpoint extends BattlegroundsGrpc.BattlegroundsImplBase {

    private final Battleground battleground;

    public BattlegroundsEndpoint(Battleground battleground) {
        this.battleground = battleground;
    }

    private StateReply getStateReply(ArrayList<Ship> ships) {
        futurebattlegroundsRPC.Ship.Builder shipBuilder = futurebattlegroundsRPC.Ship.newBuilder();
        ships.forEach(ship -> shipBuilder.setPosition(futurebattlegroundsRPC.Position.newBuilder()
                .setX(ship.getPosition().getX()).setY(ship.getPosition().getY()).build()).build());

        StateReply reply = StateReply.newBuilder().addShips(shipBuilder.build()).build();
        return reply;
    }

    @Override
    public void getState(futurebattlegroundsRPC.Empty request, StreamObserver<StateReply> responseObserver) {
        responseObserver.onNext(getStateReply(battleground.getShips()));
        responseObserver.onCompleted();
    }

    @Override
    public void streamState(Empty request, StreamObserver<StateReply> responseObserver) {
        battleground.getObservableShips().subscribe(new Observer<ArrayList<Ship>>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                this.disposable = d;
            }

            @Override
            public void onNext(ArrayList<Ship> t) {
                try {
                    responseObserver.onNext(getStateReply(t));
                } catch (Exception e) {
                    // was probably completed already; dispose the observable!
                    if (this.disposable != null && this.disposable.isDisposed() == false) {
                        this.disposable.dispose();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                responseObserver.onError(e);
            }

            @Override
            public void onComplete() {
                responseObserver.onCompleted();
            }
        });
    }
}