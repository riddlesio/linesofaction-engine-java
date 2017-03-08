package io.riddles.linesofaction.game.state;

import java.util.ArrayList;

import io.riddles.javainterface.game.state.AbstractState;
import io.riddles.linesofaction.game.board.LoaBoard;

/**
 * io.riddles.linesofaction.game.state.LinesOfActionState - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaState extends AbstractState<LoaPlayerState> {

    private LoaBoard board;

    // for initial state only
    public LoaState(ArrayList<LoaPlayerState> playerStates, LoaBoard board) {
        super(null, playerStates, 0);
        this.board = board;
    }

    public LoaState(LoaState previousState, ArrayList<LoaPlayerState> playerStates, int roundNumber) {
        super(previousState, playerStates, roundNumber);
        this.board = new LoaBoard(previousState.getBoard());
    }

    public LoaState createNextState(int roundNumber) {
        // Create new player states from current player states
        ArrayList<LoaPlayerState> playerStates = new ArrayList<>();
        for (LoaPlayerState playerState : this.getPlayerStates()) {
            playerStates.add(new LoaPlayerState(playerState.getPlayerId()));
        }

        // Create new state from current state
        return new LoaState(this, playerStates, roundNumber);
    }

    public void updatePlayerStates() {
        int[] playerPieceCount = this.board.countPlayerPieces();

        for (LoaPlayerState playerState : this.getPlayerStates()) {
            int pieceCount = playerPieceCount[playerState.getPlayerId()];
            playerState.setPieceCount(pieceCount);
        }
    }

    public LoaBoard getBoard() {
        return this.board;
    }
}
