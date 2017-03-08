package io.riddles.linesofaction.game.processor;

import java.util.ArrayList;

import io.riddles.javainterface.game.player.PlayerProvider;
import io.riddles.javainterface.game.processor.SimpleProcessor;
import io.riddles.linesofaction.engine.LoaEngine;
import io.riddles.linesofaction.game.move.ActionType;
import io.riddles.linesofaction.game.move.LoaMove;
import io.riddles.linesofaction.game.move.LoaMoveDeserializer;
import io.riddles.linesofaction.game.player.LoaPlayer;
import io.riddles.linesofaction.game.state.LoaPlayerState;
import io.riddles.linesofaction.game.state.LoaState;

/**
 * io.riddles.linesofaction.game.processor.LinesOfActionProcessor - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaProcessor extends SimpleProcessor<LoaState, LoaPlayer> {

    private LoaMoveDeserializer moveDeserializer;
    private int winnerByOpponentBreaking = -1;  // If a bot returns the wrong input, opponent wins

    public LoaProcessor(PlayerProvider<LoaPlayer> playerProvider) {
        super(playerProvider);
        this.moveDeserializer = new LoaMoveDeserializer();
    }

    @Override
    public boolean hasGameEnded(LoaState state) {
        int maxRounds = LoaEngine.configuration.getInt("maxRounds");
        ArrayList<Integer> connectedPlayerIds = state.getBoard().getConnectedPlayerIds();

        return  this.winnerByOpponentBreaking >= 0 ||
                state.getRoundNumber() >= maxRounds ||
                connectedPlayerIds.size() > 0;
    }

    @Override
    public Integer getWinnerId(LoaState state) {
        if (this.winnerByOpponentBreaking >= 0) {
            return this.winnerByOpponentBreaking;
        }

        ArrayList<Integer> connectedPlayerIds = state.getBoard().getConnectedPlayerIds();

        if (connectedPlayerIds.size() == 1) {
            return connectedPlayerIds.get(0);
        }

        return null;
    }

    @Override
    public double getScore(LoaState state) {
        return state.getRoundNumber();
    }

    @Override
    public LoaState createNextState(LoaState inputState, int roundNumber) {
        LoaState nextState = inputState;

        for (LoaPlayerState playerState : inputState.getPlayerStates()) {
            LoaPlayer player = getPlayer(playerState.getPlayerId());
            nextState = createNextStateForPlayer(nextState, player, roundNumber);
            nextState.getBoard().dump();

            if (hasGameEnded(nextState)) break;
        }

        return nextState;
    }

    private LoaState createNextStateForPlayer(LoaState inputState, LoaPlayer player, int roundNumber) {
        int playerId = player.getId();
        sendUpdatesToPlayer(inputState, player);

        LoaState movePerformedState = inputState.createNextState(roundNumber);

        String response = player.requestMove(ActionType.MOVE);
        LoaMove move = this.moveDeserializer.traverse(response);

        movePerformedState.getBoard().processMove(move, playerId);

        if (move.getException() != null) {
            this.winnerByOpponentBreaking = 2 - (playerId + 1);
        }

        LoaPlayerState playerState = movePerformedState.getPlayerStateById(playerId);
        playerState.setMove(move);
        movePerformedState.updatePlayerStates();

        return movePerformedState;
    }

    private void sendUpdatesToPlayer(LoaState state, LoaPlayer player) {
        player.sendUpdate("round", state.getRoundNumber());
        player.sendUpdate("board", state.getBoard().toString());

        for (LoaPlayerState targetPlayerState : state.getPlayerStates()) {
            LoaPlayer target = getPlayer(targetPlayerState.getPlayerId());
            player.sendUpdate("piece_count", target, targetPlayerState.getPieceCount());
        }
    }

    private LoaPlayer getPlayer(int id) {
        return this.playerProvider.getPlayerById(id);
    }
}
