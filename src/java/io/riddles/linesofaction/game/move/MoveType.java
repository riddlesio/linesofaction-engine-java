package io.riddles.linesofaction.game.move;

/**
 * io.riddles.linesofaction.game.move.MoveType - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public enum MoveType {
    MOVE,
    NONE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static MoveType fromString(String input) {
        for (MoveType moveType : MoveType.values()) {
            if (moveType.toString().equalsIgnoreCase(input)) {
                return moveType;
            }
        }

        return NONE;
    }
}
