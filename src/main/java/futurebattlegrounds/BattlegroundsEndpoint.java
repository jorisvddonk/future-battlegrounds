package futurebattlegrounds;

import javax.inject.Singleton;
import futurebattlegroundsRPC.BattlegroundsGrpc;
import futurebattlegroundsRPC.Empty;
import io.grpc.stub.StreamObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@Singleton
public class BattlegroundsEndpoint extends BattlegroundsGrpc.BattlegroundsImplBase {

    private final futurebattlegrounds.Battleground battleground;

    public BattlegroundsEndpoint(futurebattlegrounds.Battleground battleground) {
        this.battleground = battleground;
    }

    private futurebattlegroundsRPC.Battleground getBattleground(futurebattlegrounds.Battleground battleground) {
        futurebattlegroundsRPC.Battleground.Builder reply = futurebattlegroundsRPC.Battleground.newBuilder();

        battleground.getShips()
                .forEach(ship -> reply.addShips(futurebattlegroundsRPC.Ship.newBuilder()
                        .setPosition(futurebattlegroundsRPC.Vector2d.newBuilder().setX(ship.getPosition().getX())
                                .setY(ship.getPosition().getY()).build())
                        .setMovementVector(futurebattlegroundsRPC.Vector2d.newBuilder()
                                .setX(ship.getMovementVector().getX()).setY(ship.getMovementVector().getY()).build())
                        .setRotationVector(futurebattlegroundsRPC.Vector2d.newBuilder()
                                .setX(ship.getRotationVector().getX()).setY(ship.getRotationVector().getY()).build())
                        .setBattery(ship.getBattery()).setHull(ship.getHull()).setIFF(ship.getIFF()).build()));

        battleground.getBullets().forEach(bullet -> reply.addBullets(futurebattlegroundsRPC.Bullet.newBuilder()
                .setPosition(futurebattlegroundsRPC.Vector2d.newBuilder().setX(bullet.getPosition().getX())
                        .setY(bullet.getPosition().getY()).build())
                .setMovementVector(futurebattlegroundsRPC.Vector2d.newBuilder().setX(bullet.getMovementVector().getX())
                        .setY(bullet.getMovementVector().getY()).build())
                .setRotationVector(futurebattlegroundsRPC.Vector2d.newBuilder().setX(bullet.getRotationVector().getX())
                        .setY(bullet.getRotationVector().getY()).build())
                .build()));

        reply.setTimestamp(battleground.getTimestamp());
        return reply.build();
    }

    @Override
    public void getBattleground(futurebattlegroundsRPC.Empty request,
            StreamObserver<futurebattlegroundsRPC.Battleground> responseObserver) {
        responseObserver.onNext(getBattleground(battleground));
        responseObserver.onCompleted();
    }

    @Override
    public void streamBattleground(Empty request,
            StreamObserver<futurebattlegroundsRPC.Battleground> responseObserver) {
        battleground.getObservable().subscribe(new Observer<futurebattlegrounds.Battleground>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                this.disposable = d;
            }

            @Override
            public void onNext(futurebattlegrounds.Battleground t) {
                try {
                    responseObserver.onNext(getBattleground(t));
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