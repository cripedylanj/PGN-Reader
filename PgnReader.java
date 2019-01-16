import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PgnReader {
    /**
     * Find the tagName tag pair in a PGN game and return its value.
     *
     * @see http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm
     *
     * @param tagName the name of the tag whose value you want
     * @param game a `String` containing the PGN text of a chess game
     * @return the value in the named tag pair
     */
    public static String tagValue(String tagName, String game) {
        char[] charGame = game.toCharArray();
        String s = "";


        if (game.contains(tagName)) {
            int i = game.lastIndexOf(tagName);

            for ( ; i < charGame.length; i++) {
                if (charGame[i] == '"') {
                    while (charGame[i + 1] != '"') {
                        s += charGame[i + 1];
                        i++;
                    }
                    break;
                }
            }

            return s;
        }

        return "NOT GIVEN";
    }

    /**
     * Play out the moves in game and return a String with the game's
     * final position in Forsyth-Edwards Notation (FEN).
     *
     * @see http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1
     *
     * @param game a `String` containing a PGN-formatted chess game or opening
     * @return the game's final position in FEN.
     */
    public static String finalPosition(String game) {
        char[] charGame = game.toCharArray();
        String[][] grid = new String[8][8];
        char a = 'a';
        String temp = "";


        // Initialize board with piece locations i.e. d4
        for (int i = 0; i < grid.length; i++) {
            a = 'a';
            for (int j = 0; j < grid[0].length; j++) {
                temp = "";
                temp = Integer.toString(i);
                temp += a++;
                grid[i][j] = temp;
            }
        }

        //Reading tag values from game string
        int numOfMoves = 0;
        String s = "";
        String s2 = "";
        String s3 = "";
        int k = 1;
        int counter = 0;

        while (game.contains(Integer.toString(k) + '.')) {
            numOfMoves = k;
            k++;
        }

        String[] moves = new String[numOfMoves];
        int moveLength = 0;
        char[][] movesChar = new char[numOfMoves][];

        for (int i = 1; i <= numOfMoves; i++) {
            moveLength = 0;
            s = Integer.toString(i) + '.';
            s2 = Integer.toString(i + 1) + '.';
            moves[i - 1] = "";
            counter = 0;
            for (int j = game.indexOf(s) + 3; j < game.length(); j++) {
                if (j < game.length() - 2 && i < 9) {
                    s3 = "";
                    s3 += charGame[j + 1];
                    s3 += charGame[j + 2];
                }
                if (j < game.length() - 3 && i >= 9) {
                    s3 = "";
                    s3 += charGame[j + 1];
                    s3 += charGame[j + 2];
                    s3 += charGame[j + 3];
                }
                if (i > 9 && counter < 1) {
                    j += 1;
                    counter++;
                }
                if (!s3.equals(s2)) {
                    if (charGame[j] == '\n') {
                        break;
                    } else {
                        moves[i - 1] += charGame[j];
                    }
                    moveLength++;
                } else {
                    break;
                }
            }
            movesChar[i - 1] = moves[i - 1].toCharArray();
        }

        // Add starting pieces to the board
        char[][] boardPieces = {{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                                {'-', '-', '-', '-', '-', '-', '-', '-'},
                                {'-', '-', '-', '-', '-', '-', '-', '-'},
                                {'-', '-', '-', '-', '-', '-', '-', '-'},
                                {'-', '-', '-', '-', '-', '-', '-', '-'},
                                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};

        // Moving pieces
        char moveKnight = 'N';
        char moveBishop = 'B';
        char moveQueen = 'Q';
        char castling = '0';
        char checkmate = '#';
        char letter = 'a';
        int tempIn = 0;
        int moveNum = 1;
        int move = 0;
        boolean iIsOdd = true;
        boolean iIsEven = false;
        boolean jIsOdd = true;
        boolean jIsEven = false;
        boolean isStartingSpace = false;
        boolean isStartingSpaceln = false;
        boolean isStartingSpacern = false;
        boolean isStartingSpacelN = false;
        boolean isStartingSpacerN = false;
        boolean isCaptureMove = false;
        boolean jIsNum = false;

        for (int i = 0; i < numOfMoves; i++) {
            for (int j = 0; j < movesChar[i].length; j++) {
                letter = 'a';
                if (movesChar[i][j] == moveKnight) {
                    j++;

                    if (movesChar[i][j] == 'x') {
                        j++;
                        isCaptureMove = true;
                    } else {
                        isCaptureMove = false;
                    }
                    if (boardPieces[0][1] == 'n') {
                        isStartingSpaceln = true;
                    } else if (boardPieces[0][6] == 'n') {
                        isStartingSpacern = true;
                    } else if (boardPieces[7][1] == 'N') {
                        isStartingSpacelN = true;
                    } else if (boardPieces[7][6] == 'N') {
                        isStartingSpacerN = true;
                    } else {
                        isStartingSpace = false;
                    }
                    for (k = 0; k < 8; k++) {
                        tempIn = Character.getNumericValue(movesChar[i][j + 1]);
                        if (movesChar[i][j] == letter) {
                            if (k > 3) {
                                if (moveNum % 2 == 0 && tempIn == 6)  {
                                    if (boardPieces[0][6] == 'n') {
                                        boardPieces[8 - tempIn][k] = 'n';
                                        boardPieces[0][6] = '-';
                                        moveNum++;
                                        break;
                                    }
                                }
                                if (moveNum % 2 == 1 && tempIn == 3) {
                                    if (boardPieces[7][6] == 'N') {
                                        boardPieces[8 - tempIn][k] = 'N';
                                        boardPieces[7][6] = '-';
                                        moveNum++;
                                        break;
                                    }
                                }
                            }
                            if (k <= 4) {
                                if (moveNum % 2 == 0 && tempIn == 6) {
                                    if (boardPieces[0][1] == 'n') {
                                        boardPieces[8 - tempIn][k] = 'n';
                                        boardPieces[0][1] = '-';
                                        moveNum++;
                                        break;
                                    }
                                }
                                if (moveNum % 2 == 1 && tempIn == 3) {
                                    if (boardPieces[7][1] == 'N') {
                                        boardPieces[8 - tempIn][k] = 'N';
                                        boardPieces[7][1] = '-';
                                        moveNum++;
                                        break;
                                    }
                                }
                            }
                            if (k >= 3 && isCaptureMove) {
                                if (moveNum % 2 == 1) {
                                    boardPieces[1][5] = 'N';
                                    boardPieces[2][5] = '-';
                                    boardPieces[3][3] = 'n';
                                    boardPieces[4][4] = '-';
                                    boardPieces[5][5] = '-';
                                    boardPieces[1][3] = '-';

                                    moveNum++;
                                    break;
                                }
                            }
                        }
                        letter++;
                    }
                }
                if (movesChar[i][j] == moveBishop) {
                    j++;
                    iIsOdd = i % 2 == 1 ? true : false;
                    iIsEven = i % 2 == 0 ? true : false;
                    jIsOdd = j % 2 == 1 ? true : false;
                    jIsEven = j % 2 == 0 ? true : false;

                    if (boardPieces[0][2] == 'b' || boardPieces[0][5] == 'b') {
                        isStartingSpace = true;
                    } else if (boardPieces[7][2] == 'B') {
                        isStartingSpace = true;
                    } else if (boardPieces[7][5] == 'B') {
                        isStartingSpace = true;
                    } else {
                        isStartingSpace = false;
                    }

                    for (k = 0; k < 8; k++) {
                        tempIn = Character.getNumericValue(movesChar[i][j + 1]);
                        if (movesChar[i][j] == letter) {
                            if (iIsOdd && jIsOdd || iIsEven && jIsEven) {
                                if (moveNum % 2 == 0 && isStartingSpace) {
                                    move = jIsOdd ? 5 : 2;
                                    boardPieces[8 - tempIn][k] = 'b';
                                    boardPieces[0][k] = '-';
                                    moveNum++;
                                    break;
                                }
                                if (moveNum % 2 == 1 && isStartingSpace) {
                                    move = jIsOdd ? 5 : 2;
                                    boardPieces[8 - tempIn][k] = 'B';
                                    boardPieces[7][move] = '-';
                                    moveNum++;
                                    break;
                                }
                            }
                            if (iIsOdd && jIsEven || iIsEven && jIsOdd) {
                                if (moveNum % 2 == 0 && isStartingSpace) {
                                    move = jIsOdd ? 5 : 2;
                                    boardPieces[8 - tempIn][k] = 'b';
                                    boardPieces[0][move] = '-';
                                    moveNum++;
                                    break;
                                }
                                if (moveNum % 2 == 1 && isStartingSpace) {
                                    move = jIsOdd ? 5 : 2;
                                    boardPieces[8 - tempIn][k] = 'B';
                                    boardPieces[7][move] = '-';
                                    moveNum++;
                                    break;
                                }
                            }
                        }
                        letter++;
                    }
                }
                if (movesChar[i][j] == moveQueen) {
                    j++;

                    if (movesChar[i][j] == 'x') {
                        j++;
                        isCaptureMove = true;
                    } else {
                        isCaptureMove = false;
                    }
                    if (boardPieces[0][3] == 'q') {
                        isStartingSpace = true;
                    } else if (boardPieces[7][3] == 'Q') {
                        isStartingSpace = true;
                    } else {
                        isStartingSpace = false;
                    }
                    for (k = 0; k < 8; k++) {
                        tempIn = Character.getNumericValue(movesChar[i][j + 1]);
                        if (movesChar[i][j] ==  letter) {
                            if (moveNum % 2 == 1 && isCaptureMove) {
                                for (int m = 0; m < 8; m++) {
                                    for (int n = 0; n < 8; n++) {
                                        if (boardPieces[m][n] == 'Q') {
                                            boardPieces[m][n] = '-';
                                        }
                                    }
                                }
                                boardPieces[8 - tempIn][k] = 'Q';
                                moveNum++;
                                break;
                            }
                            if (moveNum % 2 == 0 && isCaptureMove) {
                                for (int m = 0; m < 8; m++) {
                                    for (int n = 0; n < 8; n++) {
                                        if (boardPieces[m][n] == 'q') {
                                            boardPieces[m][n] = '-';
                                        }
                                    }
                                }
                                boardPieces[8 - tempIn][k] = 'q';
                                moveNum++;
                                break;
                            }
                            if (moveNum % 2 == 0 && isStartingSpace) {
                                System.out.println(k);
                                boardPieces[8 - tempIn][k] = 'q';
                                boardPieces[0][3] = '-';
                                moveNum++;
                                break;
                            }
                            if (moveNum % 2 == 1 && isStartingSpace) {
                                boardPieces[8 - tempIn][k] = 'Q';
                                boardPieces[7][3] = '-';
                                moveNum++;
                                break;
                            }
                        }
                        letter++;
                    }
                }
                for (k = 0; k < 8; k++) {
                    if (movesChar[i][j] == 'e' && movesChar[i][j + 1] == 'x') {
                        moveNum++;
                        break;
                    }
                    if (j > 1) {
                        if (movesChar[i][j - 1] == moveKnight) {
                            break;
                        }
                        if (movesChar[i][j - 1] == moveBishop) {
                            break;
                        }
                        if (movesChar[i][j - 1] == moveQueen) {
                            break;
                        }
                        if (movesChar[i][j] == 'x') {
                            break;
                        }
                    }
                    if (j == 0 || j == 3 || j == 4) {
                        jIsNum = true;
                    } else {
                        jIsNum = false;
                    }
                    if (movesChar[i][j] == letter && jIsNum) {
                        tempIn = Character.getNumericValue(movesChar[i][j + 1]);
                        if (tempIn == 4 || tempIn == 5) {
                            move = 2;
                        } else {
                            move = 1;
                        }
                        if (moveNum % 2 == 0) {
                            boardPieces[8 - tempIn][k] = 'p';
                            boardPieces[8 - tempIn - move][k] = '-';
                            moveNum++;
                            break;
                        }
                        if (moveNum % 2 == 1) {
                            boardPieces[8 - tempIn][k] = 'P';
                            boardPieces[8 - tempIn + move][k] = '-';
                            moveNum++;
                            break;
                        }
                    }
                    letter++;
                }
                if (movesChar[i][j] == 'O' && movesChar[i][j + 1] == '-') {
                    if (movesChar[i][j + 2] == 'O') {
                        if (moveNum % 2 == 0) {
                            boardPieces[0][6] = 'k';
                            boardPieces[0][5] = 'r';
                            boardPieces[0][4] = '-';
                            boardPieces[0][7] = '-';
                        }
                        if (moveNum % 2 == 1) {
                            boardPieces[7][6] = 'K';
                            boardPieces[7][5] = 'R';
                            boardPieces[7][4] = '-';
                            boardPieces[7][7] = '-';
                        }
                    }
                }
            }
        }

        // Printing final positions
        int emptySpaces = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (boardPieces[i][j] == '-') {
                    emptySpaces++;
                    if (j == 7) {
                        System.out.print(emptySpaces);
                        emptySpaces = 0;
                    }
                } else {
                    if (emptySpaces != 0) {
                        System.out.print(emptySpaces);
                        emptySpaces = 0;
                    }
                    System.out.print(boardPieces[i][j]);
                }
            }
            if (i != 7) {
                System.out.printf("/");
            }
        }
        return "";
    }

    /**
     * Reads the file named by path and returns its content as a String.
     *
     * @param path the relative or abolute path of the file to read
     * @return a String containing the content of the file
     */
    public static String fileContent(String path) {
        Path file = Paths.get(path);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Add the \n that's removed by readline()
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.exit(1);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String game = fileContent(args[0]);

        //System.out.println(game);

        System.out.format("Event: %s%n", tagValue("Event", game));
        System.out.format("Site: %s%n", tagValue("Site", game));
        System.out.format("Date: %s%n", tagValue("Date", game));
        System.out.format("Round: %s%n", tagValue("Round", game));
        System.out.format("White: %s%n", tagValue("White", game));
        System.out.format("Black: %s%n", tagValue("Black", game));
        System.out.format("Result: %s%n", tagValue("Result", game));
        System.out.println("Final Position:");
        System.out.println(finalPosition(game));

    }
}
