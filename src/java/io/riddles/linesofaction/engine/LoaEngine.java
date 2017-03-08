package io.riddles.linesofaction.engine;

import java.util.ArrayList;

import io.riddles.javainterface.configuration.Configuration;
import io.riddles.javainterface.engine.AbstractEngine;
import io.riddles.javainterface.engine.GameLoopInterface;
import io.riddles.javainterface.engine.SimpleGameLoop;
import io.riddles.javainterface.exception.TerminalException;
import io.riddles.javainterface.game.player.PlayerProvider;
import io.riddles.javainterface.io.IOInterface;
import io.riddles.linesofaction.game.LoaSerializer;
import io.riddles.linesofaction.game.board.LoaBoard;
import io.riddles.linesofaction.game.player.LoaPlayer;
import io.riddles.linesofaction.game.processor.LoaProcessor;
import io.riddles.linesofaction.game.state.LoaPlayerState;
import io.riddles.linesofaction.game.state.LoaState;

/**
 * io.riddles.linesofaction.engine.LinesOfActionEngine - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaEngine extends AbstractEngine<LoaProcessor, LoaPlayer, LoaState> {

    public LoaEngine(PlayerProvider<LoaPlayer> playerProvider, IOInterface ioHandler) throws TerminalException {
        super(playerProvider, ioHandler);
    }

    @Override
    protected Configuration getDefaultConfiguration() {
        Configuration configuration = new Configuration();

        configuration.put("boardWidth", 8);
        configuration.put("boardHeight", 8);
        configuration.put("maxRounds", 100);

        return configuration;
    }

    @Override
    protected LoaProcessor createProcessor() {
        return new LoaProcessor(this.playerProvider);
    }

    @Override
    protected GameLoopInterface createGameLoop() {
        return new SimpleGameLoop();
    }

    @Override
    protected LoaPlayer createPlayer(int id) {
        return new LoaPlayer(id);
    }

    @Override
    protected void sendSettingsToPlayer(LoaPlayer player) {
        player.sendSetting("your_botid", player.getId());
        player.sendSetting("board_width", configuration.getInt("boardWidth"));
        player.sendSetting("board_height", configuration.getInt("boardHeight"));
        player.sendSetting("max_rounds", configuration.getInt("maxRounds"));
    }

    @Override
    protected LoaState getInitialState() {
        int width = configuration.getInt("boardWidth");
        int height = configuration.getInt("boardHeight");
        int playerCount = this.playerProvider.getPlayers().size();

        if (playerCount != 2) {
            throw new RuntimeException("This game requires exactly 2 players");
        }

        // Create initial board
        LoaBoard board = new LoaBoard(width, height, playerCount, getStartingBoardString());

        // Create initial player states
        ArrayList<LoaPlayerState> playerStates = new ArrayList<>();
        for (LoaPlayer player : this.playerProvider.getPlayers()) {
            LoaPlayerState playerState = new LoaPlayerState(player.getId());
            playerStates.add(playerState);
        }

        // Create initial state
        LoaState state = new LoaState(playerStates, board);

        // Update initial player states
        state.updatePlayerStates();

        return state;
    }

    @Override
    protected String getPlayedGame(LoaState initialState) {
        LoaSerializer serializer = new LoaSerializer();
        return serializer.traverseToString(this.processor, initialState);
    }

    private String getStartingBoardString() {
        return ".,0,0,0,0,0,0,.," +
               "1,.,.,.,.,.,.,1," +
               "1,.,.,.,.,.,.,1," +
               "1,.,.,.,.,.,.,1," +
               "1,.,.,.,.,.,.,1," +
               "1,.,.,.,.,.,.,1," +
               "1,.,.,.,.,.,.,1," +
               ".,0,0,0,0,0,0,.";
    }
}
