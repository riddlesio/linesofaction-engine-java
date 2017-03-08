package io.riddles.linesofaction.game.state;

import io.riddles.javainterface.game.state.AbstractPlayerState;
import io.riddles.linesofaction.game.move.LoaMove;

/**
 * io.riddles.linesofaction.game.state.LinesOfActionPlayerState - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaPlayerState extends AbstractPlayerState<LoaMove> {

    private int pieceCount;

    public LoaPlayerState(int playerId) {
        super(playerId);
        this.pieceCount = -1;
    }

    public int getPieceCount() {
        return this.pieceCount;
    }

    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }
}
