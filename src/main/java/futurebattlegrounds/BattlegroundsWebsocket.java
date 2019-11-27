package futurebattlegrounds;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

@ServerWebSocket("/ws/battleground")
public class BattlegroundsWebsocket {
  private WebSocketBroadcaster broadcaster;
  @Inject
  private Battleground battleground;

  public BattlegroundsWebsocket(WebSocketBroadcaster broadcaster) {
    this.broadcaster = broadcaster;
  }

  @OnOpen
  public void onOpen(WebSocketSession session) {
    battleground.getObservable().subscribe(new Observer<Battleground>() {
      Disposable disposable;

      @Override
      public void onSubscribe(Disposable d) {
        this.disposable = d;
      }

      @Override
      public void onNext(Battleground t) {
        try {
          broadcaster.broadcastSync(t, s -> s == session);
        } catch (Exception e) {
          System.out.print(e);
          // was probably completed already; dispose the observable!
          if (this.disposable != null && this.disposable.isDisposed() == false) {
            this.disposable.dispose();
          }
        }
      }

      @Override
      public void onError(Throwable e) {
        System.out.print(e);
      }

      @Override
      public void onComplete() {
        // do nothing
      }
    });

  }

  @OnMessage
  public void onMessage(String message, WebSocketSession session) {
    // do nothing
  }

  @OnClose
  public void onClose(WebSocketSession session) {
    // do nothing
  }
}
