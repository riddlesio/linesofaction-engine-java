package io.riddles.linesofaction.game.board;

import java.awt.Point;
import java.util.ArrayList;

/**
 * io.riddles.linesofaction.game.board.LoaPiece - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaPiece {

    private LoaPieceType type;
    private Point coordinate;
    private ArrayList<LoaPiece> neighbors;

    LoaPiece(LoaPieceType type, Point coordinate) {
        this.type = type;
        this.coordinate = coordinate;
        this.neighbors = new ArrayList<>();
    }

    LoaPiece(LoaPiece piece) {
        this.type = piece.getType();
        this.coordinate = new Point(piece.getCoordinate());
        this.neighbors = new ArrayList<>();
    }

    LoaPieceType getType() {
        return this.type;
    }

    void addNeighbor(LoaPiece neighbor) {
        if (neighbor == null) return;

        this.neighbors.add(neighbor);
        neighbor.getNeighbors().add(this);
    }

    ArrayList<LoaPiece> getNeighbors() {
        return this.neighbors;
    }

    void setNeighbors(ArrayList<LoaPiece> neighbors) {
        this.neighbors = neighbors;
    }

    void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    Point getCoordinate() {
        return this.coordinate;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
