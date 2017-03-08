package io.riddles.linesofaction.game.move;

import java.awt.Point;

import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.javainterface.serialize.Deserializer;

/**
 * io.riddles.linesofaction.game.move.LoaMoveDeserializer - Created on 6-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaMoveDeserializer implements Deserializer<LoaMove> {

    @Override
    public LoaMove traverse(String string) {
        try {
            return visitMove(string);
        } catch (InvalidInputException ex) {
            return new LoaMove(ex);
        } catch (Exception ex) {
            return new LoaMove(new InvalidInputException("Failed to parse move"));
        }
    }

    private LoaMove visitMove(String input) throws InvalidInputException {
        String[] split = input.split(" ");
        MoveType moveType = MoveType.fromString(split[0]);

        if (moveType != MoveType.MOVE) {
            throw new InvalidInputException(String.format("Move type %s not recognized", split[0]));
        }

        if (split.length != 3) {
            throw new InvalidInputException("Move doesn't split into 3 parts");
        }

        Point fromCoordinate = visitCoordinate(split[1]);
        Point toCoordinate = visitCoordinate(split[2]);

        return new LoaMove(fromCoordinate, toCoordinate);
    }

    private Point visitCoordinate(String coordinate) throws InvalidInputException {
        try {
            String[] split = coordinate.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);

            return new Point(x, y);
        } catch (Exception ex) {
            throw new InvalidInputException(
                    String.format("Failed to parse coordinate %s", coordinate));
        }
    }
}
