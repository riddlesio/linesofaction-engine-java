package io.riddles.linesofaction.game;

import org.json.JSONArray;
import org.json.JSONObject;

import io.riddles.javainterface.game.AbstractGameSerializer;
import io.riddles.linesofaction.game.processor.LoaProcessor;
import io.riddles.linesofaction.game.state.LoaState;
import io.riddles.linesofaction.game.state.LoaStateSerializer;

/**
 * io.riddles.linesofaction.game.LinesOfActionSerializer - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaSerializer extends AbstractGameSerializer<LoaProcessor, LoaState> {

    @Override
    public String traverseToString(LoaProcessor processor, LoaState initialState) {
        LoaStateSerializer stateSerializer = new LoaStateSerializer();
        JSONObject game = new JSONObject();

        game = addDefaultJSON(initialState, game, processor);

        JSONArray states = new JSONArray();
        states.put(stateSerializer.traverseToJson(initialState));

        LoaState state = initialState;
        while (state.hasNextState()) {
            state = (LoaState) state.getNextState();
            states.put(stateSerializer.traverseToJson(state));
        }

        game.put("states", states);

        return game.toString();
    }
}
