package com.dedshot.game.sockets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.dedshot.game.sockets.TicTacToeSocketHandler;
import com.dedshot.game.sockets.interceptors.TicTacToeSocketInterceptor;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private TicTacToeSocketHandler ticTacToeSocketHandler;
    private TicTacToeSocketInterceptor ticTacToeSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(ticTacToeSocketHandler, "/tictactoe")
                .addInterceptors(ticTacToeSocketInterceptor)
                .setAllowedOrigins("*");
    }
}
