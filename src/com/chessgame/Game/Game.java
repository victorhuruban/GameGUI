package com.chessgame.Game;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Board.Loc;
import com.Main;
import com.chessgame.Pieces.*;
import com.chessgame.Player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private final Color MOVE_DARK_COLOR = new Color(193, 176, 28);
    private final Color MOVE_LIGHT_COLOR = new Color(246, 242, 110);
    private final Color CAPTURE_DARK_COLOR = new Color(193, 49, 28);
    private final Color CAPTURE_LIGHT_COLOR = new Color(246, 114, 110);
    private final Color MY_LOCATION_DARK_COLOR = new Color(65, 49, 155);
    private final Color MY_LOCATION_LIGHT_COLOR = new Color(119, 114, 238);
    private final Color CASTLING_DARK = new Color(65, 176, 28);
    private final Color CASTLING_LIGHT = new Color(119, 242, 101);
    private final Color DARK_COLOR = new Color(130, 97, 55);
    private final Color LIGHT_COLOR = new Color(237,228,202);
    private JLabel myName, opponentName;
    private Piece testPiece;
    public JTextArea logTA;
    public JFrame frame;
    public JPanel chessboard;
    private boolean gameover, canMove, turn, endPawn;
    volatile boolean movedPiece;
    private ChessBoard clone;
    public Player white, black;
    private ArrayList<Rook> castling;
    private ChessBoard gameChessboard;
    private StringBuilder log;
    private int[] logArr;

    public Game(int num) throws IOException {
        log = new StringBuilder();
        logArr = new int[4];
        logTA = new JTextArea(19, 30);
        logTA.setEditable(false);
        castling = new ArrayList<>();
        testPiece = null;
        turn = true; gameover = false;
        GameInitialization gi = new GameInitialization(num);
        gameChessboard = gi.getCb();
        white = new Player("white", getChessBoard());
        black = new Player("black", getChessBoard());
        clone = new ChessBoard();
        chessboard = new JPanel();
        movedPiece = false;
        canMove = true;
        endPawn = false;
        updateChessBoardUI(getChessBoard(), chessboard);
    }

    public JFrame createJFrameCB() throws IOException {
        Dimension dim = new Dimension(800, 800);
        JLabel[] piece = new JLabel[1];

        JFrame jFrame = new JFrame("Test ChessBoard");
        jFrame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(Main.class.getResource("/com/chessgame/Game/res/frame_background.jpg")))));
        jFrame.setSize(1250, 847);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new GridBagLayout());
        jFrame.setResizable(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 4;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,5,5,5);

        JLayeredPane jLayeredPane = new JLayeredPane();
        jLayeredPane.setPreferredSize(dim);
        jFrame.add(jLayeredPane, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.LINE_START;

        JPanel chatPane = new JPanel();
        JScrollPane scroll = new JScrollPane(logTA);
        scroll.setHorizontalScrollBar(null);
        chatPane.add(scroll);
        chatPane.setBackground(Color.DARK_GRAY);
        chatPane.setPreferredSize(new Dimension(300, 318));
        jFrame.add(chatPane, c);

        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        JPanel namesPanel = new JPanel(new GridLayout(2,0));
        namesPanel.setBackground(Color.LIGHT_GRAY);
        namesPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        myName = new JLabel();
        myName.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        opponentName = new JLabel();
        opponentName.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        namesPanel.add(myName);
        namesPanel.add(opponentName);
        jFrame.add(namesPanel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LAST_LINE_END;

        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(e -> {
            System.exit(0); // TODO: IMPROVE STUFF
        });
        jFrame.add(exitButton, c);

        jLayeredPane.add(chessboard, JLayeredPane.DEFAULT_LAYER);
        chessboard.setLayout(new GridLayout(8,8));
        chessboard.setPreferredSize(jFrame.getSize());
        chessboard.setBounds(0, 0, (int) dim.getWidth(), (int) dim.getHeight());

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (canMove) {
                    int column = e.getX() / 100;
                    int row = e.getY() / 100;
                    Component c = chessboard.findComponentAt(e.getX(), e.getY());
                    if (c instanceof JPanel && piece[0] == null) {
                        System.out.println("c instanceof jpanel return");
                        return;
                    }
                    King temp;
                    if (c instanceof JLabel && piece[0] == null) {
                        testPiece = getChessBoard().getLocation(row, column).getPiece();
                        if ((turn && testPiece.getColor().equals("white")) || (!turn && testPiece.getColor().equals("black"))) {
                            addLogArr(0, row);
                            addLogArr(1, column);
                            boolean[][] check = checkMoveToChangeBackground(getChessBoard(), row, column, 1);
                            piece[0] = (JLabel) c;
                            changeJPanelBackground(check, 1, testPiece.getRow(), testPiece.getColumn());
                            check = checkMoveToChangeBackground(getChessBoard(), row, column, 2);
                            changeJPanelBackground(check, 2, testPiece.getRow(), testPiece.getColumn());
                            if (testPiece.toString().equals("King")) {
                                castling = checkCastling((King) testPiece);
                                if (castling.size() == 0) {
                                    System.out.println("No castling available");
                                } else {
                                    for (Rook r: castling) {
                                        Loc t = castlingLocation(r);
                                        check[t.row][t.column] = true;
                                        changeJPanelBackground(check, 3, testPiece.getRow(), testPiece.getColumn());
                                    }
                                }
                            }
                            chessboard.updateUI();
                        } else {
                            System.out.println("Not your piece");
                        }
                    }
                    if (c instanceof JLabel && piece[0] != null) {
                        if (testPiece.isValidCapture(getChessBoard(), row, column)) {
                            if (checkIfChecked(getPlayer(testPiece.getColor()).getKing(), getChessBoard())) {
                                clone = cloneBoard();
                                clone.getLocation(testPiece.getRow(), testPiece.getColumn()).getPiece().capture(clone, row, column);
                                temp = getKing(clone, testPiece);
                                if (checkIfChecked(temp, clone)) {
                                    clearLogArr();
                                    System.out.println("still checked, try again");
                                    clone = new ChessBoard();
                                } else {
                                    testPiece.capture(getChessBoard(), row, column);
                                    if (testPiece.toString().equals("Pawn") && testPiece.getRow() == 0) {
                                        endPawn = true;
                                    }
                                    addLogArr(2, row);
                                    addLogArr(3, column);
                                    try {
                                        updateChessBoardUI(getChessBoard(), chessboard);
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                    StringBuilder tempSB = getSB();
                                    tempSB.append(testPiece.getColor()).append(" : ").append(testPiece.toString()).append(" from ")
                                            .append(getPos(testPiece.getColor(), getLogArr()[0], getLogArr()[1])).append(" to ")
                                            .append(getPos(testPiece.getColor(), getLogArr()[2], getLogArr()[3])).append(". (capture)\n");
                                    getLogTA().append(String.valueOf(tempSB));
                                    clearLogArr();
                                    changeMovedPiece();
                                    changeTurn();
                                    System.out.println("good capture, next player");
                                    piece[0] = null;
                                    testPiece = null;
                                    chessboard.updateUI();
                                }
                            } else {
                                testPiece.capture(getChessBoard(), row, column);
                                if (testPiece.toString().equals("Pawn") && testPiece.getRow() == 0) {
                                    endPawn = true;
                                }
                                addLogArr(2, row);
                                addLogArr(3, column);
                                try {
                                    updateChessBoardUI(getChessBoard(), chessboard);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                if (!castling.isEmpty()) {
                                    castling.clear();
                                }
                                StringBuilder tempSB = getSB();
                                tempSB.append(testPiece.getColor()).append(" : ").append(testPiece.toString()).append(" from ")
                                        .append(getPos(testPiece.getColor(), getLogArr()[0], getLogArr()[1])).append(" to ")
                                        .append(getPos(testPiece.getColor(), getLogArr()[2], getLogArr()[3])).append(". (capture)\n");
                                getLogTA().append(String.valueOf(tempSB));
                                clearLogArr();
                                changeMovedPiece();
                                changeTurn();
                                System.out.println("good capture, next player");
                                piece[0] = null;
                                testPiece = null;
                                chessboard.updateUI();
                            }
                        }
                    }
                    if (c instanceof JPanel) {
                        if (testPiece.isValidMove(getChessBoard(), row, column)) {
                            if (checkIfChecked(getPlayer(testPiece.getColor()).getKing(), getChessBoard())) {
                                clone = cloneBoard();
                                clone.getLocation(testPiece.getRow(), testPiece.getColumn()).getPiece().move(clone, row, column);

                                temp = getKing(clone, testPiece);
                                if (checkIfChecked(temp, clone)) {
                                    clearLogArr();
                                    System.out.println("still checked, try again");
                                    clearLogArr();
                                } else {
                                    testPiece.move(getChessBoard(), row, column);
                                    if (testPiece.toString().equals("Pawn") && testPiece.getRow() == 0) {
                                        endPawn = true;
                                    }
                                    addLogArr(2, row);
                                    addLogArr(3, column);
                                    try {
                                        updateChessBoardUI(getChessBoard(), chessboard);
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                    StringBuilder tempSB = getSB();
                                    tempSB.append(testPiece.getColor()).append(" : ").append(testPiece.toString()).append(" from ")
                                            .append(getPos(testPiece.getColor(), getLogArr()[0], getLogArr()[1])).append(" to ")
                                            .append(getPos(testPiece.getColor(), getLogArr()[2], getLogArr()[3])).append(". (move)\n");
                                    getLogTA().append(String.valueOf(tempSB));
                                    clearLogArr();
                                    changeMovedPiece();
                                    System.out.println("good, change player");
                                    changeTurn();
                                    piece[0] = null;
                                    testPiece = null;
                                    chessboard.updateUI();
                                }
                                clone = new ChessBoard();
                            } else {
                                testPiece.move(getChessBoard(), row, column);
                                if (testPiece.toString().equals("Pawn") && testPiece.getRow() == 0) {
                                    endPawn = true;
                                }
                                addLogArr(2, row);
                                addLogArr(3, column);
                                try {
                                    updateChessBoardUI(getChessBoard(), chessboard);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                if (!castling.isEmpty()) {
                                    castling.clear();
                                }
                                StringBuilder tempSB = getSB();
                                tempSB.append(testPiece.getColor()).append(" : ").append(testPiece.toString()).append(" from ")
                                        .append(getPos(testPiece.getColor(), getLogArr()[0], getLogArr()[1])).append(" to ")
                                        .append(getPos(testPiece.getColor(), getLogArr()[2], getLogArr()[3])).append(". (move)\n");
                                getLogTA().append(String.valueOf(tempSB));
                                clearLogArr();
                                changeMovedPiece();
                                System.out.println("good, change player");
                                changeTurn();
                                piece[0] = null;
                                testPiece = null;
                                chessboard.updateUI();
                            }
                        } else if (!castling.isEmpty() && goThroughCastling(row, column)) {
                            for (Rook r: castling) {
                                Loc l = castlingLocation(r);
                                if (l.row == row && l.column == column) {
                                    doCastling(r, (King) getKing(r.getColor()));
                                }
                            }
                            try {
                                updateChessBoardUI(getChessBoard(), chessboard);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            changeMovedPiece();
                            System.out.println("good, change player");
                            changeTurn();
                            chessboard.updateUI();
                            piece[0] = null;
                            testPiece = null;
                        } else {
                            try {
                                updateChessBoardUI(getChessBoard(), chessboard);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            chessboard.updateUI();
                            piece[0] = null;
                            testPiece = null;
                            System.out.println("Gresit borther");
                        }
                    }
                } else {
                    System.out.println("Not your turn");
                }

            }
        };
        chessboard.addMouseListener(ma);
        return jFrame;
    }

    public JTextArea getLogTA() {
        return logTA;
    }

    public String getPos(String color, int row, int column) {
        StringBuilder sb = new StringBuilder();
        if (row == 0 && color.equalsIgnoreCase("white")) {
            sb.append("8");
        } else if (row == 0 && color.equalsIgnoreCase("black")) {
            sb.append("1");
        }
        if (row == 1 && color.equalsIgnoreCase("white")) {
            sb.append("7");
        } else if (row == 1 && color.equalsIgnoreCase("black")) {
            sb.append("2");
        }
        if (row == 2 && color.equalsIgnoreCase("white")) {
            sb.append("6");
        } else if (row == 2 && color.equalsIgnoreCase("black")) {
            sb.append("3");
        }
        if (row == 3 && color.equalsIgnoreCase("white")) {
            sb.append("5");
        } else if (row == 3 && color.equalsIgnoreCase("black")) {
            sb.append("4");
        }
        if (row == 4 && color.equalsIgnoreCase("white")) {
            sb.append("4");
        } else if (row == 4 && color.equalsIgnoreCase("black")) {
            sb.append("5");
        }
        if (row == 5 && color.equalsIgnoreCase("white")) {
            sb.append("3");
        } else if (row == 5 && color.equalsIgnoreCase("black")) {
            sb.append("6");
        }
        if (row == 6 && color.equalsIgnoreCase("white")) {
            sb.append("2");
        } else if (row == 6 && color.equalsIgnoreCase("black")) {
            sb.append("7");
        }
        if (row == 7 && color.equalsIgnoreCase("white")) {
            sb.append("1");
        } else if (row == 7 && color.equalsIgnoreCase("black")) {
            sb.append("8");
        }

        if (column == 0 && color.equalsIgnoreCase("white")) {
            sb.append("A");
        } else if (column == 0 && color.equalsIgnoreCase("black")) {
            sb.append("H");
        }
        if (column == 1 && color.equalsIgnoreCase("white")) {
            sb.append("B");
        } else if (column == 1 && color.equalsIgnoreCase("black")) {
            sb.append("G");
        }
        if (column == 2 && color.equalsIgnoreCase("white")) {
            sb.append("C");
        } else if (column == 2 && color.equalsIgnoreCase("black")) {
            sb.append("F");
        }
        if (column == 3 && color.equalsIgnoreCase("white")) {
            sb.append("D");
        } else if (column == 3 && color.equalsIgnoreCase("black")) {
            sb.append("E");
        }
        if (column == 4 && color.equalsIgnoreCase("white")) {
            sb.append("E");
        } else if (column == 4 && color.equalsIgnoreCase("black")) {
            sb.append("D");
        }
        if (column == 5 && color.equalsIgnoreCase("white")) {
            sb.append("F");
        } else if (column == 5 && color.equalsIgnoreCase("black")) {
            sb.append("C");
        }
        if (column == 6 && color.equalsIgnoreCase("white")) {
            sb.append("G");
        } else if (column == 6 && color.equalsIgnoreCase("black")) {
            sb.append("B");
        }
        if (column == 7 && color.equalsIgnoreCase("white")) {
            sb.append("H");
        } else if (column == 7 && color.equalsIgnoreCase("black")) {
            sb.append("A");
        }

        return sb.toString();
    }

    public StringBuilder getSB() {
        return log;
    }

    public int[] getLogArr() {
        return logArr;
    }

    public void addLogArr(int index, int loc) {
        logArr[index] = loc;
    }

    public void clearLogArr() {
        for (int i = 0; i < 4; i++) {
            logArr[i] = 0;
        }
    }

    public void setCanMove() {
        canMove = !canMove;
    }

    public boolean[][] checkMoveToChangeBackground(ChessBoard cb, int row, int column, int choice) {
        boolean[][] temp = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (choice == 1) {
                    temp[i][j] = cb.getLocation(row, column).getPiece().isValidMove(cb, i, j);
                } else {
                    temp[i][j] = cb.getLocation(row, column).getPiece().isValidCapture(cb, i, j);
                }
            }
        }
        return temp;
    }

    public Player getPlayer(String color) {
        if (color.equals("white")) {
            return white;
        } else return black;
    }

    public void changeJPanelBackground(boolean[][] array, int choice, int roww, int column) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int row = (getPosition(i, j) / 8) % 2; // TODO: IMPROVE METHOD
                JPanel square = (JPanel) chessboard.getComponent(getPosition(i, j));
                if (!array[i][j]) {
                    if (square.getBackground() != MOVE_DARK_COLOR && square.getBackground() != MOVE_LIGHT_COLOR) {
                        if (row == 0) {
                            square.setBackground(getPosition(i, j) % 2 == 0 ? DARK_COLOR : LIGHT_COLOR);
                        } else square.setBackground(getPosition(i, j) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                    }
                } else {
                    if (choice == 1) {
                        if (row == 0) {
                            square.setBackground(getPosition(i, j) % 2 == 0 ? MOVE_DARK_COLOR : MOVE_LIGHT_COLOR);
                        } else square.setBackground(getPosition(i, j) % 2 == 0 ? MOVE_LIGHT_COLOR : MOVE_DARK_COLOR);
                    } else if (choice == 2) {
                        if (row == 0) {
                            square.setBackground(getPosition(i, j) % 2 == 0 ? CAPTURE_DARK_COLOR : CAPTURE_LIGHT_COLOR);
                        } else square.setBackground(getPosition(i, j) % 2 == 0 ? CAPTURE_LIGHT_COLOR : CAPTURE_DARK_COLOR);
                    } else {
                        if (row == 0) {
                            square.setBackground(getPosition(i, j) % 2 == 0 ? MOVE_DARK_COLOR : MOVE_LIGHT_COLOR);
                        } else square.setBackground(getPosition(i, j) % 2 == 0 ? MOVE_LIGHT_COLOR : MOVE_DARK_COLOR);
                    }
                }
                if (roww == i && column == j) {
                    if (row == 0) {
                        square.setBackground(getPosition(i, j) % 2 == 0 ? MOVE_DARK_COLOR : MOVE_LIGHT_COLOR);
                    } else square.setBackground(getPosition(i, j) % 2 == 0 ? MOVE_LIGHT_COLOR : MOVE_DARK_COLOR);
                }
            }
        }
    }

    public ArrayList<Rook> checkCastling(King king) {
        if (king.getFMoved()) {
            return new ArrayList<>();
        }
        ArrayList<Rook> rooks = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getChessBoard().getLocation(i, j).isOccupied() && getChessBoard().getLocation(i, j).getPiece().getColor().equals(king.getColor()) &&
                        getChessBoard().getLocation(i, j).getPiece().toString().equals("Rook")) {
                    if (getChessBoard().getLocation(i, j).getPiece().toString().equals("Rook")) {
                        Rook temp = (Rook) getChessBoard().getLocation(i, j).getPiece();
                        if (temp.getMoved()) {
                            System.out.println("Rook at (" + temp.getRow() + ", " + temp.getColumn() + ") is moved");
                        } else {
                            if (checkPiecesForCastlingFromRook(temp)) {
                                System.out.println("Merge pentru rookul :" + temp.getRow() + " " + temp.getColumn());
                                rooks.add(temp);
                            }
                        }
                    }
                }
            }
        }
        return rooks;
    }

    public boolean checkPiecesForCastlingFromRook(Rook rook) {
        if (rook.getColor().equals("white")) {
            if (rook.getColumn() == 0) {
                return getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn()).isOccupied() && getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn()).getPiece().getColor().equals(rook.getColor()) &&
                        getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn() + 1).isOccupied() && getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn() + 1).getPiece().getColor().equals(rook.getColor()) &&
                        getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn() + 2).isOccupied() && getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn() + 2).getPiece().getColor().equals(rook.getColor()) &&
                        !getChessBoard().getLocation(rook.getRow(), rook.getColumn() + 1).isOccupied() && !getChessBoard().getLocation(rook.getRow(), rook.getColumn() + 2).isOccupied() &&
                        !getChessBoard().getLocation(rook.getRow(), rook.getColumn() + 3).isOccupied();
            } else if (rook.getColumn() == 7) {
                return getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn()).isOccupied() && getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn()).getPiece().getColor().equals(rook.getColor()) &&
                        getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn() - 1).isOccupied() && getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn() - 1).getPiece().getColor().equals(rook.getColor()) &&
                        getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn() - 2).isOccupied() && getChessBoard().getLocation(rook.getRow() - 1, rook.getColumn() - 2).getPiece().getColor().equals(rook.getColor()) &&
                        !getChessBoard().getLocation(rook.getRow(), rook.getColumn() - 1).isOccupied() && !getChessBoard().getLocation(rook.getRow(), rook.getColumn() - 2).isOccupied();
            }
        } else {
            if (rook.getColumn() == 0) {
                return getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn()).isOccupied() && getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn()).getPiece().getColor().equals(rook.getColor()) &&
                        getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn() + 1).isOccupied() && getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn() + 1).getPiece().getColor().equals(rook.getColor()) &&
                        getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn() + 2).isOccupied() && getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn() + 2).getPiece().getColor().equals(rook.getColor()) &&
                        !getChessBoard().getLocation(rook.getRow(), rook.getColumn() + 1).isOccupied() && !getChessBoard().getLocation(rook.getRow(), rook.getColumn() + 1).isOccupied() &&
                        !getChessBoard().getLocation(rook.getRow(), rook.getColumn() + 2).isOccupied();
            } else if (rook.getColumn() == 7) {
                return getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn()).isOccupied() && getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn()).getPiece().getColor().equals(rook.getColor()) &&
                        getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn() - 1).isOccupied() && getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn() - 1).getPiece().getColor().equals(rook.getColor()) &&
                        getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn() - 2).isOccupied() && getChessBoard().getLocation(rook.getRow() + 1, rook.getColumn() - 2).getPiece().getColor().equals(rook.getColor()) &&
                        !getChessBoard().getLocation(rook.getRow(), rook.getColumn() - 1).isOccupied() && !getChessBoard().getLocation(rook.getRow(), rook.getColumn() - 2).isOccupied();
            }
        }
        return false;
    }

    public Loc castlingLocation(Rook rook) {
        if (rook.getColumn() == 0) {
            return getChessBoard().getLocation(rook.getRow(), rook.getColumn() + 2);
        } else return getChessBoard().getLocation(rook.getRow(), rook.getColumn() - 1);
    }

    public void doCastling(Rook rook, King king) {

        Loc t = castlingLocation(rook);
        Piece k = getChessBoard().getLocation(king.getRow(), king.getColumn()).getPiece();
        Piece r = getChessBoard().getLocation(rook.getRow(), rook.getColumn()).getPiece();
        getChessBoard().getLocation(king.getRow(), king.getColumn()).removePiece();
        getChessBoard().getLocation(t.row, t.column).setPiece(k);

        getChessBoard().getLocation(rook.getRow(), rook.getColumn()).removePiece();


        if (rook.getColumn() < king.getColumn()) {
            getChessBoard().getLocation(t.row, t.column + 1).setPiece(r);
        } else getChessBoard().getLocation(t.row, t.column - 1).setPiece(r);
    }

    public boolean goThroughCastling(int row, int column) {
        for (Rook r: castling) {
            Loc temp = castlingLocation(r);
            if (temp.row == row && temp.column == column) {
                return true;
            }
        }
        return false;
    }

    public boolean getMovedPiece() {
        return this.movedPiece;
    }

    public synchronized void changeMovedPiece() {
        this.movedPiece = !this.movedPiece;
    }

    public void updateChessBoardUI(ChessBoard cb, JPanel chessboard) throws IOException {
        chessboard.removeAll();
        setChessBoard(cb);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = new JPanel(new BorderLayout());
                int row = (getPosition(i, j) / 8) % 2;
                if (row == 0) {
                    square.setBackground(getPosition(i, j) % 2 == 0 ? DARK_COLOR : LIGHT_COLOR);
                } else square.setBackground(getPosition(i, j) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);

                if (!getChessBoard().getLocation(i, j).toString().equals("null")) {
                    String piece = getChessBoard().getLocation(i,j).getPiece().toString().toLowerCase();
                    String color = getChessBoard().getLocation(i,j).getPiece().getColor().substring(0,1);
                    BufferedImage icon = ImageIO.read(Main.class.getResource("/com/chessgame/Game/res/" + color + piece + ".png"));
                    JLabel iconpiece = new JLabel(new ImageIcon(icon));
                    square.add(iconpiece);
                }
                chessboard.add(square, getPosition(i, j));
            }
        }
    }

    private int getPosition(int row, int column) {
        return row * 8 + column;
    }

    public ChessBoard getChessBoard() {
        return gameChessboard;
    }

    public void setChessBoard(ChessBoard cb) {
        gameChessboard = cb;
    }

    public Piece getKing(String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getChessBoard().getLocation(i, j).getPiece() != null && getChessBoard().getLocation(i, j).getPiece().toString().equals("King") &&
                        getChessBoard().getLocation(i, j).getPiece().getColor().equals(color)) {
                    return getChessBoard().getLocation(i,j).getPiece();
                }
            }
        }
        return null;
    }

    private King getKing(ChessBoard cb, Piece piece) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cb.getLocation(i, j).toString().equals("King") && clone.getLocation(i, j).getPiece().getColor().equals(piece.getColor())) {
                    return new King(i, j, piece.getColor());
                }
            }
        }
        return null;
    }

    private boolean checkIfChecked(Piece king, ChessBoard cb) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cb.getLocation(i, j).getPiece() != null && !cb.getLocation(i, j).getPiece().getColor().equals(king.getColor()) &&
                        cb.getLocation(i, j).getPiece().isValidCapture(cb, king.getRow(), king.getColumn())) {
                    return true;
                }

            }
        }
        return false;
    }

    public boolean isCheckMate(Piece king) {
        String color = king.getColor();
        int row = king.getRow();
        int column = king.getColumn();
        Piece cloneKing = new King(row, column, color);
        clone = cloneBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == cloneKing.getRow() && j == cloneKing.getColumn()) {
                    continue;
                }
                if (cloneKing.isValidMove(clone, i, j)) {
                    cloneKing.move(clone, i, j);
                    cloneKing.setRow(i);
                    cloneKing.setColumn(j);

                    if (checkIfChecked(cloneKing, clone)) {
                        clone = cloneBoard();
                        cloneKing = new King(row, column, color);
                    } else {
                        return false;
                    }
                }
                if (cloneKing.isValidCapture(clone, i, j)) {
                    cloneKing.capture(clone, i, j);
                    clone.getLocation(i, j).removePiece();
                    clone.getLocation(i, j).setPiece(cloneKing);

                    if (checkIfChecked(cloneKing, clone)) {
                        cloneKing = new King(row, column, color);
                        clone = cloneBoard();
                    } else {
                        return false;
                    }
                }
                if (clone.getLocation(i, j).isOccupied() && clone.getLocation(i, j).getPiece().getColor().equals(color)) {
                    Piece rPiece = clone.getLocation(i, j).getPiece();
                    for (int x = 0; x < 8; x++) {
                        for (int z = 0; z < 8; z++) {
                            if (x == rPiece.getRow() && z == rPiece.getColumn()) {
                                continue;
                            }
                            if (rPiece.isValidMove(clone, x, z)) {
                                rPiece.move(clone, x, z);
                                if (checkIfChecked(cloneKing, clone)) {
                                    clone = cloneBoard();
                                } else {
                                    return false;
                                }
                            }
                            if (rPiece.isValidCapture(clone, x, z)) {
                                rPiece.capture(clone, x, z);
                                if (checkIfChecked(cloneKing, clone)) {
                                    clone = cloneBoard();
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public ChessBoard cloneBoard() {
        ChessBoard ret = new ChessBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getChessBoard().getLocation(i, j).isOccupied()) {
                    switch (getChessBoard().getLocation(i, j).getPiece().toString()) {
                        case "Pawn":
                            ret.getLocation(i, j).setPiece(new Pawn(i, j, getChessBoard().getLocation(i, j).getPiece().getColor()));
                            break;
                        case "Rook":
                            ret.getLocation(i, j).setPiece(new Rook(i, j, getChessBoard().getLocation(i, j).getPiece().getColor()));
                            break;
                        case "Knight":
                            ret.getLocation(i, j).setPiece(new Knight(i, j, getChessBoard().getLocation(i, j).getPiece().getColor()));
                            break;
                        case "Bishop":
                            ret.getLocation(i, j).setPiece(new Bishop(i, j, getChessBoard().getLocation(i, j).getPiece().getColor()));
                            break;
                        case "Queen":
                            ret.getLocation(i, j).setPiece(new Queen(i, j, getChessBoard().getLocation(i, j).getPiece().getColor()));
                            break;
                        case "King":
                            ret.getLocation(i, j).setPiece(new King(i, j, getChessBoard().getLocation(i, j).getPiece().getColor()));
                            break;
                        default:
                    }
                }
            }
        }
        return ret;
    }

    public boolean getEndPawn() {
        return endPawn;
    }

    public void changeEndPawn() {
        endPawn = !endPawn;
    }

    public void changeTurn() {
        turn = !turn;
    }

    public void youLost() {
        this.gameover = true;
    }

    public boolean getGameover() {
        return gameover;
    }

    public JLabel getMyNameL() {
        return myName;
    }

    public JLabel getOpponentsNameL() {
        return opponentName;
    }
}
