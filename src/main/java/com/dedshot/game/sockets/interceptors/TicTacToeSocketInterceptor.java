package com.dedshot.game.sockets.interceptors;

import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.errors.InvalidSessionException;
import com.dedshot.game.errors.SessionNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class TicTacToeSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) throws Exception {

        HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpSession session = httpServletRequest.getSession(false);

        if(session != null) {
            Integer playerId = ((Integer) session.getAttribute(CommonConstants.PLAYER_ID));
            if(playerId == null) {
                throw new InvalidSessionException();
            }
            attributes.put(CommonConstants.PLAYER_ID, playerId);
            log.debug("New connection socket made for Player: {}", playerId);
        } else {
            log.error("Session not found!");
            throw new SessionNotFoundException();
        }
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler, @Nullable Exception exception) {}
}
