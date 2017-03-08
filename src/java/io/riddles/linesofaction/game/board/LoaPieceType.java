package io.riddles.linesofaction.game.board;

/**
 * io.riddles.linesofaction.game.board.LoaPieceType - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public enum LoaPieceType {
    PLAYER0(0),
    PLAYER1(1);

    private final int id;

    LoaPieceType(int id) {
        this.id = id;
    }

    public String toString() {
        return this.id + "";
    }

    public int toInt() {
        return this.id;
    }

    public static LoaPieceType fromString(String name) {
        if (name.equals(PLAYER0.id + "")) {
            return PLAYER0;
        }
        if (name.equals(PLAYER1.id + "")) {
            return PLAYER1;
        }
        return null;
    }
}
