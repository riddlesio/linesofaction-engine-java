package io.riddles.linesofaction.game.board;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import io.riddles.javainterface.exception.InvalidMoveException;
import io.riddles.javainterface.game.data.Board;
import io.riddles.linesofaction.game.move.LoaMove;

/**
 * io.riddles.linesofaction.game.board.LinesOfActionBoard - Created on 2-3-17
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class LoaBoard extends Board<LoaPiece> {

    private int playerCount;

    public LoaBoard(int width, int height, int playerCount) {
        super(width, height);
        this.playerCount = playerCount;
        this.fields = new LoaPiece[width][height];
        this.clear();
    }

    public LoaBoard(int width, int height, int playerCount, String fieldInput) {
        super(width, height);
        this.playerCount = playerCount;
        this.fields = new LoaPiece[width][height];
        this.setFieldsFromString(fieldInput);
    }

    public LoaBoard(LoaBoard board) {
        super(board.getWidth(), board.getHeight());
        this.playerCount = board.getPlayerCount();
        this.fields = board.getFieldsCopy();
    }

    @Override
    public void clear() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.fields[x][y] = null;
            }
        }
    }

    @Override
    public void setFieldsFromString(String input) {
        String[] split = input.split(",");
        int x = 0;
        int y = 0;

        for (String fieldString : split) {
            LoaPieceType pieceType = LoaPieceType.fromString(fieldString);
            LoaPiece piece = null;

            if (pieceType != null) {
                piece = new LoaPiece(pieceType, new Point(x, y));
                addNeighbors(piece);
            }

            this.fields[x][y] = piece;

            if (++x == this.width) {
                x = 0;
                y++;
            }
        }
    }

    /**
     * Creates comma separated String
     * @return String
     */
    public String toString() {
        String output = "";
        String connector = "";

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.fields[x][y] == null) {
                    output += connector + ".";
                } else {
                    output += connector + this.fields[x][y];
                }

                connector = ",";
            }
        }

        return output;
    }

    /**
     * Dumps visual representation of the board to the
     * output log
     */
    public void dump() {
        for (int y = 0; y < this.height; y++) {  // dump the board
            String line = "";
            for (int x = 0; x < this.width; x++) {
                String cell = ".";

                if (this.fields[x][y] != null) {
                    cell = this.fields[x][y] + "";
                }

                line += cell + " ";
            }
            System.err.println(line);
        }

        System.err.println();
    }

    public ArrayList<Integer> getConnectedPlayerIds() {
        ArrayList<Integer> connectedPlayerIds = new ArrayList<>();
        for (int id = 0; id < this.playerCount; id++) {
            if (isPlayerConnected(id)) {
                connectedPlayerIds.add(id);
            }
        }

        return connectedPlayerIds;
    }

    /**
     * Counts the amount of pieces each player has
     * @return Array with player id as index and values piece count
     */
    public int[] countPlayerPieces() {
        int[] playerPieces = new int[this.playerCount];
        for (int i = 0; i < this.playerCount; i++) {
            playerPieces[i] = 0;
        }

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                try {
                    LoaPiece piece = this.fields[x][y];
                    int playerIndex = piece.getType().toInt();
                    playerPieces[playerIndex]++;
                } catch (Exception ignored) {}
            }
        }

        return playerPieces;
    }

    public void processMove(LoaMove move, int playerId) {
        LoaPiece piece;

        if (move.getException() != null) {
            return;
        }

        try {
            piece = getPieceFromMove(move, playerId);
            validateMove(piece, move);
        } catch (InvalidMoveException ex) {
            move.setException(ex);
            return;
        }

        movePiece(move, piece);
    }

    private LoaPiece[][] getFieldsCopy() {
        LoaPiece[][] copy = new LoaPiece[this.width][this.height];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.fields[x][y] != null) {
                    copy[x][y] = new LoaPiece(this.fields[x][y]);
                    addNeighbors(copy[x][y]);
                }
            }
        }

        return copy;
    }

    private LoaPiece getPieceFromMove(LoaMove move, int playerId) throws InvalidMoveException {
        LoaPiece piece = getFieldAt(move.getFromCoordinate());

        if (piece == null) {
            throw new InvalidMoveException("There is no piece at selected coordinate");
        }

        if (piece.getType().toInt() != playerId) {
            throw new InvalidMoveException("Selected piece is not your piece");
        }

        return piece;
    }

    /**
     * Gets all valid moves for given piece and confirms that the given
     * move moves the piece to one of those valid points.
     * @param piece Piece being moved
     * @param move Move to perform
     * @throws InvalidMoveException Thrown if move is not valid
     */
    private void validateMove(LoaPiece piece, LoaMove move) throws InvalidMoveException {
        ArrayList<Point> validPointsForPiece = getValidPointsForPiece(piece);
        Point toCoordinate = move.getToCoordinate();

        for (Point validPoint : validPointsForPiece) {
            if (toCoordinate.equals(validPoint)) return;
        }

        throw new InvalidMoveException("Your piece can not move here");
    }

    public ArrayList<Point> getValidPointsForPiece(LoaPiece piece) {
        ArrayList<Point> validPointsForPiece = new ArrayList<>();
        int playerId = piece.getType().toInt();

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dy == 0 && dx == 0) continue;

                Point direction = new Point(dx, dy);
                Point base = piece.getCoordinate();
                int count = getPieceCountOnLine(base, direction);

                Point movablePoint = getValidPointInDirection(base, direction, count, playerId);

                if (movablePoint != null) {
                    validPointsForPiece.add(movablePoint);
                }
            }
        }

        return validPointsForPiece;
    }

    private int getPieceCountOnLine(Point base, Point direction) {
        if (direction.x == 0 && direction.y == 0) {
            throw new RuntimeException("Direction can't be 0,0");
        }

        int count = getPieceCountInDirection(base, direction);
        Point reversedDirection = new Point(direction.x * -1, direction.y * -1);

        // -1 because base is counted twice
        return count + getPieceCountInDirection(base, reversedDirection) - 1;
    }

    private int getPieceCountInDirection(Point base, Point direction) {
        int x = base.x;
        int y = base.y;
        int count = 0;

        while (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            if (this.fields[x][y] != null) {
                count++;
            }

            x += direction.x;
            y += direction.y;
        }

        return count;
    }

    private Point getValidPointInDirection(Point base, Point direction, int count, int playerId) {
        int x = base.x;
        int y = base.y;

        for (int n = 0; n < count; n++) {
            x += direction.x;
            y += direction.y;

            // outside of board
            if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
                return null;
            }

            LoaPiece piece = this.fields[x][y];

            // blocked by opponent piece
            if (n < count - 1 && piece != null && piece.getType().toInt() != playerId) {
                return null;
            }

            // final position contains own piece
            if (n == count - 1 && piece != null && piece.getType().toInt() == playerId) {
                return null;
            }
        }

        return new Point(x, y);
    }

    private void movePiece(LoaMove move, LoaPiece piece) {
        Point fromCoordinate = move.getFromCoordinate();
        Point toCoordinate = move.getToCoordinate();

        piece.setCoordinate(new Point(toCoordinate));
        this.fields[fromCoordinate.x][fromCoordinate.y] = null;
        this.fields[toCoordinate.x][toCoordinate.y] = piece;

        updateField();
    }

    private void updateField() {
        // Clear all neighbors
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                LoaPiece piece = this.fields[x][y];

                if (piece == null) continue;

                piece.setNeighbors(new ArrayList<>());
            }
        }

        // Set new neighbors
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                LoaPiece piece = this.fields[x][y];

                if (piece == null) continue;

                addNeighbors(piece);
            }
        }
    }

    private void addNeighbors(LoaPiece piece) {
        int x = piece.getCoordinate().x;
        int y = piece.getCoordinate().y;

        for (int dy = y - 1; dy <= y; dy++) {
            for (int dx = x - 1; dx <= x + 1; dx++) {
                if (dx < 0 || dy < 0 || dx >= this.width) continue;

                piece.addNeighbor(this.fields[dx][dy]);

                if (dy == y) return; // stop here
            }
        }
    }

    private boolean isPlayerConnected(int id) {
        int pieceCount = countPlayerPieces()[id];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                LoaPiece piece = this.fields[x][y];

                if (piece == null || piece.getType().toInt() != id) continue;

                int connected = traverseNeighbors(piece);
                return connected == pieceCount;
            }
        }

        return true; // shouldn't be reached, 0 pieces on board for player
    }

    private int traverseNeighbors(LoaPiece root) {
        LinkedList<LoaPiece> queue = new LinkedList<>();
        ArrayList<LoaPiece> visited = new ArrayList<>();
        queue.add(root);
        visited.add(root);

        while (!queue.isEmpty()) {
            LoaPiece piece = queue.pop();

            for (LoaPiece neighbor : piece.getNeighbors()) {
                if (neighbor.getType() == piece.getType() && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visited.size();
    }

    private int getPlayerCount() {
        return this.playerCount;
    }

    @Override
    public LoaPiece fieldFromString(String field) {
        return null;
    }
}
