package com.chessgame.Game;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Main;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game implements Serializable {
    private static final long serialVersionUID = 6156930883005779968L;
    private Piece testPiece;
    public JFrame frame;
    public JPanel chessboard;
    private boolean gameover, checked;
    volatile boolean movedPiece;
    private ChessBoard clone, clone2;
    private GameInitialization gi;
    public Player white, black;
    private boolean turn;

    public Game() throws IOException {
        testPiece = null;
        turn = true; gameover = false;
        gi = new GameInitialization();
        white = new Player("white", gi.getCb());
        black = new Player("black", gi.getCb());
        clone = new ChessBoard();
        clone2 = new ChessBoard();
        chessboard = new JPanel();
        movedPiece = false;
        updateChessBoardUI(gi.getCb(), chessboard);
    }

    public JFrame createJFrameCB() throws IOException {
        Dimension dim = new Dimension(800, 800);
        JLabel[] piece = new JLabel[1];

        JFrame jFrame = new JFrame("Test ChessBoard");
        jFrame.setSize(816, 840);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane jLayeredPane = new JLayeredPane();
        jFrame.add(jLayeredPane);
        jLayeredPane.setPreferredSize(dim);

        jLayeredPane.add(chessboard, JLayeredPane.DEFAULT_LAYER);
        chessboard.setLayout(new GridLayout(8,8));
        chessboard.setPreferredSize(jFrame.getSize());
        chessboard.setBounds(0, 0, (int) dim.getWidth(), (int) dim.getHeight());

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = e.getX() / 100;
                int row = e.getY() / 100;
                Component c = chessboard.findComponentAt(e.getX(), e.getY());
                if (c instanceof JPanel && piece[0] == null) {
                    System.out.println("c instanceof jpanel return");
                    return;
                }
                if (c instanceof JLabel && piece[0] == null) {

                    testPiece = getChessBoard().getLocation(row, column).getPiece();
                    if (turn && testPiece.getColor().equals("white")) {
                        piece[0] = (JLabel) c;
                        boolean[][] check = checkMoveToChangeBackground(gi.getCb(), row, column, 1);
                        changeJPanelBackground(check, 1, testPiece.getRow(), testPiece.getColumn());
                        check = checkMoveToChangeBackground(gi.getCb(), row, column, 2);
                        changeJPanelBackground(check, 2, testPiece.getRow(), testPiece.getColumn());
                        chessboard.updateUI();
                    } else if (!turn && testPiece.getColor().equals("black")){
                        if (isCheckMate(getKing("black"))) {
                            System.out.println("Black lost");
                        }
                        piece[0] = (JLabel) c;
                        boolean[][] check = checkMoveToChangeBackground(gi.getCb(), row, column, 1);
                        changeJPanelBackground(check, 1, testPiece.getRow(), testPiece.getColumn());
                        check = checkMoveToChangeBackground(gi.getCb(), row, column, 2);
                        changeJPanelBackground(check, 2, testPiece.getRow(), testPiece.getColumn());
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
                            King temp = null;
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    if (clone.getLocation(i, j).toString().equals("King") && clone.getLocation(i, j).getPiece().getColor().equals(testPiece.getColor())) {
                                        temp = new King(i, j, testPiece.getColor());
                                    }
                                }
                            }
                            if (checkIfChecked(temp, clone)) {
                                System.out.println("still checked, try again");
                                clone = new ChessBoard();
                            } else {
                                testPiece.capture(getChessBoard(), row, column);
                                try {
                                    updateChessBoardUI(getChessBoard(), chessboard);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                changeMovedPiece();
                                changeTurn();
                                System.out.println("good capture, next player");
                                piece[0] = null;
                                testPiece = null;
                                chessboard.updateUI();
                            }
                        } else {
                            testPiece.capture(getChessBoard(), row, column);
                            try {
                                updateChessBoardUI(getChessBoard(), chessboard);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            changeMovedPiece();
                            changeTurn();
                            System.out.println("good capture, next player");
                            piece[0] = null;
                            testPiece = null;
                            chessboard.updateUI();
                        }
                    }
                }
                if (c instanceof JPanel && piece[0] != null) {
                    if (testPiece.isValidMove(getChessBoard(), row, column)) {
                        if (checkIfChecked(getPlayer(testPiece.getColor()).getKing(), getChessBoard())) {
                            clone = cloneBoard();
                            clone.getLocation(testPiece.getRow(), testPiece.getColumn()).getPiece().move(clone, row, column);
                            King temp = null;
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    if (clone.getLocation(i, j).toString().equals("King") && clone.getLocation(i, j).getPiece().getColor().equals(testPiece.getColor())) {
                                        temp = new King(i, j, testPiece.getColor());
                                    }
                                }
                            }
                            if (checkIfChecked(temp, clone)) {
                                System.out.println("still checked, try again");
                            } else {
                                testPiece.move(getChessBoard(), row, column);
                                try {
                                    updateChessBoardUI(getChessBoard(), chessboard);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
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
                            try {
                                updateChessBoardUI(getChessBoard(), chessboard);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            changeMovedPiece();
                            System.out.println("good, change player");
                            changeTurn();
                            piece[0] = null;
                            testPiece = null;
                            chessboard.updateUI();
                        }
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
            }
        };
        chessboard.addMouseListener(ma);

        return jFrame;
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

                JPanel square = (JPanel) chessboard.getComponent(getPosition(i, j));
                if (!array[i][j]) {
                    int row = (getPosition(i, j) / 8) % 2;
                    if (square.getBackground() != Color.ORANGE) {
                        if (row == 0) {
                            square.setBackground(getPosition(i, j) % 2 == 0 ? Color.DARK_GRAY : Color.WHITE);
                        } else square.setBackground(getPosition(i, j) % 2 == 0 ? Color.WHITE : Color.DARK_GRAY);
                    }
                } else {
                    if (choice == 1) {
                        square.setBackground(Color.ORANGE);
                    } else {
                        square.setBackground(Color.RED);
                    }
                }
                if (roww == i && column == j) {
                    square.setBackground(Color.BLUE);
                }
            }
        }
    }

    public boolean getMovedPiece() {
        return this.movedPiece;
    }

    public synchronized void changeMovedPiece() {
        this.movedPiece = !this.movedPiece;
    }

    public void updateChessBoardUI(ChessBoard cb, JPanel chessboard) throws IOException {
        chessboard.removeAll();
        gi.setCb(cb);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = new JPanel(new BorderLayout());
                int row = (getPosition(i, j) / 8) % 2;
                if (row == 0) {
                    square.setBackground(getPosition(i, j) % 2 == 0 ? Color.DARK_GRAY : Color.WHITE);
                } else square.setBackground(getPosition(i, j) % 2 == 0 ? Color.WHITE : Color.DARK_GRAY);

                if (!cb.getLocation(i, j).toString().equals("null")) {
                    int compNum = getPosition(i, j);
                    String piece = cb.getLocation(i,j).getPiece().toString().toLowerCase();
                    String color = cb.getLocation(i,j).getPiece().getColor().substring(0,1);
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
        return gi.getCb();
    }

    public void setChessBoard(ChessBoard cb) {
        this.gi.setCb(cb);
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
        clone2 = cloneBoard();
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
                            System.out.println(i + " " + j + " : " + x + " " + z);
                            getChessBoard().listCB();
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

    private ChessBoard cloneBoard() {
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

    public boolean turn() {
        return turn;
    }

    public void changeTurn() {
        turn = !turn;
    }
}
