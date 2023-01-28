package game;

import game.Exceptions.*;

/**
 * Contains functions which are useful for validating user inputs
 */
public class Utils {

	
    public static char convertIntToLetter(int i) {
        return (char) (i + 64);
    }

    public static int convertCharIntToInt(char c, int size) throws InvalidMoveException {
        int i = c - '0' - 1;
        if (i < 0 || i > size - 1) throw new InvalidMoveException("Enter a coordinate like 'A1'");
        return i;
    }

    public static int convertCharToInt(char c, int size) throws InvalidMoveException {
        int i = c - 'A';
        if (i < 0 || i > size - 1) throw new InvalidMoveException("Enter a coordinate like 'A1'");
        return i;
    }

    public static Coordinate parseUserMove(String move, int size) throws InvalidMoveException {
        if (move.length() != 2) throw new InvalidMoveException("Enter a coordinate like 'A1'");
        int row = convertCharIntToInt(move.charAt(1), size);
        int col = convertCharToInt(move.charAt(0), size);

        return new Coordinate(row, col);
    }
}
