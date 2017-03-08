package io.riddles.linesofaction;

import io.riddles.javainterface.game.player.PlayerProvider;
import io.riddles.javainterface.io.IOHandler;
import io.riddles.linesofaction.engine.LoaEngine;
import io.riddles.linesofaction.game.state.LoaState;

/**
 * io.riddles.linesofaction.LinesOfAction - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LinesOfAction {

    public static void main(String[] args) throws Exception {
        LoaEngine engine = new LoaEngine(new PlayerProvider<>(), new IOHandler());

        LoaState firstState = engine.willRun();
        LoaState finalState = engine.run(firstState);

        engine.didRun(firstState, finalState);
    }
}
