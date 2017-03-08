package io.riddles.linesofaction.game.move;

import org.json.JSONObject;

import java.awt.Point;

import io.riddles.javainterface.serialize.Serializer;

/**
 * io.riddles.linesofaction.game.move.LoaMoveSerializer - Created on 7-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaMoveSerializer implements Serializer<LoaMove> {

    @Override
    public String traverseToString(LoaMove traversible) {
        return visitMove(traversible).toString();
    }

    @Override
    public JSONObject traverseToJson(LoaMove traversible) {
        return visitMove(traversible);
    }

    private JSONObject visitMove(LoaMove move) {
        JSONObject moveObj = new JSONObject();


        if (move.getException() != null) {
            moveObj.put("exception", move.getException().getMessage());
            moveObj.put("from", JSONObject.NULL);
            moveObj.put("to", JSONObject.NULL);
        } else {
            moveObj.put("exception", JSONObject.NULL);
            moveObj.put("from", visitPoint(move.getFromCoordinate()));
            moveObj.put("to", visitPoint(move.getToCoordinate()));
        }

        return moveObj;
    }

    private JSONObject visitPoint(Point point) {
        JSONObject pointObj = new JSONObject();

        pointObj.put("x", point.x);
        pointObj.put("y", point.y);

        return pointObj;
    }
}
