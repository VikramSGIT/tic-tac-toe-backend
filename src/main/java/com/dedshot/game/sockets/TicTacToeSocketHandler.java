package com.dedshot.game.sockets;

import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.controller.GameSocketController;
import com.dedshot.game.controller.GameSocketExceptionHandler;
import com.dedshot.game.entity.PlayerCommand;
import com.dedshot.game.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

@Component
@AllArgsConstructor
@Slf4j
public class TicTacToeSocketHandler extends TextWebSocketHandler {

    private GameSocketController gameSocketController;
    private GameSocketExceptionHandler exceptionHandler;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        try {
            gameSocketController.newPlayerConnected(session);
        } catch (Exception e) {
            exceptionHandler.handleException(e);
            if(session.isOpen()) {
                Map<String, Object> message = new HashMap<>();
                message.put("status_code", "500");
                message.put("message", "An unexpected error occured.");
                session.sendMessage(new TextMessage(ServiceUtils.toJSONString(message)));
                session.close();
            }
        }
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage textMessage) throws Exception {
        try{
            log.debug(textMessage.getPayload());

            PlayerCommand playerCommand = ServiceUtils.toObject(textMessage.getPayload(), PlayerCommand.class);
            if(playerCommand.getCommand().equals(CommonConstants.PLAYER_COMMAND_PUT)) {
                Integer x = Integer.parseInt(playerCommand.getValues()[0]);
                Integer y = Integer.parseInt(playerCommand.getValues()[1]);
                gameSocketController.playerMove(session, x, y);
            }
        } catch (Exception e) {
            log.error("Skipping due to error processing Error: {}", e.getMessage());
            e.printStackTrace();
        }
        super.handleTextMessage(session, textMessage);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,@NonNull CloseStatus status) throws Exception {
        try{
            gameSocketController.playerDisconnected(session);
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        super.afterConnectionClosed(session, status);
    }
}
