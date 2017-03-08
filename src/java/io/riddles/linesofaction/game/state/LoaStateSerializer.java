package io.riddles.linesofaction.game.state;

import org.json.JSONArray;
import org.json.JSONObject;

import io.riddles.javainterface.game.state.AbstractStateSerializer;
import io.riddles.linesofaction.game.board.LoaBoard;
import io.riddles.linesofaction.game.move.LoaMoveSerializer;

/**
 * io.riddles.linesofaction.game.state.LinesOfActionStateSerializer - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaStateSerializer extends AbstractStateSerializer<LoaState> {

    @Override
    public String traverseToString(LoaState state) {
        return visitState(state).toString();
    }

    @Override
    public JSONObject traverseToJson(LoaState state) {
        return visitState(state);
    }

    private JSONObject visitState(LoaState state) {
        LoaMoveSerializer moveSerializer = new LoaMoveSerializer();

        JSONObject stateObj = new JSONObject();
        LoaBoard board = state.getBoard();

        JSONArray players = new JSONArray();
        for (LoaPlayerState playerState : state.getPlayerStates()) {
            JSONObject playerObj = new JSONObject();
            playerObj.put("id", playerState.getPlayerId());
            playerObj.put("score", playerState.getPieceCount());

            if (playerState.getMove() != null) {
                JSONObject moveObj = moveSerializer.traverseToJson(playerState.getMove());
                playerObj.put("move", moveObj);
            } else {
                playerObj.put("move", JSONObject.NULL);
            }

            players.put(playerObj);
        }

        stateObj.put("round", state.getRoundNumber());
        stateObj.put("board", board.toString());
        stateObj.put("players", players);

        return stateObj;
    }
}
