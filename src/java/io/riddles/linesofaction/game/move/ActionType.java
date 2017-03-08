package io.riddles.linesofaction.game.move;

/**
 * io.riddles.linesofaction.game.move.ActionType - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public enum ActionType {
    MOVE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
