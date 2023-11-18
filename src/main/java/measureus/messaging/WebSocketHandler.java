package measureus.messaging;

import lombok.Getter;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> userIdSessionMap = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established;" + session.toString());
        String userId = Objects.requireNonNull(session
                        .getHandshakeHeaders()
                        .get("userId"))
                .get(0);
        userIdSessionMap.put(userId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed" + session.toString() + status.toString());
        String userId = Objects.requireNonNull(session
                        .getHandshakeHeaders()
                        .get("userId"))
                .get(0);
        userIdSessionMap.remove(userId);
    }

    public void sendMessageToSession(WebSocketSession session, String message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}