package io.riddles.linesofaction.game.move;

import java.awt.Point;

import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.javainterface.game.move.AbstractMove;

/**
 * io.riddles.linesofaction.game.move.LinesOfActionMove - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaMove extends AbstractMove {

    private Point fromCoordinate;
    private Point toCoordinate;

    public LoaMove(Point fromCoordinate, Point toCoordinate) {
        super();
        this.fromCoordinate = fromCoordinate;
        this.toCoordinate = toCoordinate;
    }

    public LoaMove(InvalidInputException exception) {
        super(exception);
    }

    public Point getFromCoordinate() {
        return this.fromCoordinate;
    }

    public Point getToCoordinate() {
        return this.toCoordinate;
    }
}
