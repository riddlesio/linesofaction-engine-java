package io.riddles.linesofaction.game.board

import spock.lang.Specification

import java.awt.Point

/**
 * io.riddles.linesofaction.game.board.LoaBoardSpec - Created on 3-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
class LoaBoardSpec extends Specification {

    def "test board initialization"() {
        setup:
        String input = ".,0,0,0,0,0,0,.," +
                       "1,.,.,.,.,.,.,1," +
                       "1,.,.,.,.,.,.,1," +
                       "1,.,.,.,.,.,.,1," +
                       "1,.,.,.,.,.,.,1," +
                       "1,.,.,.,.,.,.,1," +
                       "1,.,.,.,.,.,.,1," +
                       ".,0,0,0,0,0,0,."
        LoaBoard board = new LoaBoard(8, 8, 2, input)

        expect:
        board.getFields()[1][0].getNeighbors().size() == 2
        board.getFields()[2][0].getNeighbors().size() == 2
        board.getFields()[0][1].getNeighbors().size() == 2
        board.getFields()[7][1].getNeighbors().size() == 2
        board.getFields()[7][6].getNeighbors().size() == 2
    }

    def "test board connection"() {
        when:
        String input1 = ".,.,.,.,.,.,.,.," +
                        ".,.,0,.,.,.,.,.," +
                        ".,.,.,.,.,.,.,.," +
                        ".,.,.,.,.,.,.,.," +
                        ".,.,.,.,.,1,.,.," +
                        ".,.,.,.,.,.,.,.," +
                        ".,.,1,.,.,.,.,.," +
                        ".,.,.,.,.,.,.,."
        LoaBoard board1 = new LoaBoard(8, 8, 2, input1)
        ArrayList<Integer> connectedPlayerIds1 = board1.getConnectedPlayerIds()
        String input2 = ".,.,.,.,.,.,.,.," +
                        ".,.,0,0,1,.,.,.," +
                        ".,0,.,1,0,.,.,.," +
                        ".,0,.,.,1,0,.,.," +
                        ".,0,0,0,0,1,.,.," +
                        ".,.,.,.,.,1,.,.," +
                        ".,.,.,1,1,1,.,.," +
                        ".,.,1,.,.,.,.,."
        LoaBoard board2 = new LoaBoard(8, 8, 2, input2)
        ArrayList<Integer> connectedPlayerIds2 = board2.getConnectedPlayerIds()

        then:
        connectedPlayerIds1.contains(0)
        !connectedPlayerIds1.contains(1)
        connectedPlayerIds2.contains(0)
        connectedPlayerIds2.contains(1)
    }

    def "test available moves"() {
        when:
        String input = ".,0,0,0,.,0,0,.," +
                       ".,.,.,.,.,.,.,.," +
                       "1,.,.,.,.,.,.,1," +
                       "1,.,1,1,.,1,.,.," +
                       "1,.,0,.,.,1,.,.," +
                       "1,.,.,.,0,0,.,1," +
                       "1,.,.,.,.,.,.,1," +
                       ".,0,.,0,0,.,.,."
        LoaBoard board = new LoaBoard(8, 8, 2, input)
        LoaPiece piece = board.getFieldAt(new Point(5, 5))
        ArrayList<Point> validPoints = board.getValidPointsForPiece(piece)

        then:
        validPoints.size() == 4
        validPoints.get(0) == new Point(3, 3)
        validPoints.get(1) == new Point(7, 3)
        validPoints.get(2) == new Point(1, 5)
        validPoints.get(3) == new Point(7, 7)
    }
}
