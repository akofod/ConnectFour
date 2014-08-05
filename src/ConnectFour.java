import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class ConnectFour extends JFrame implements MouseListener, ActionListener {

    private static final long serialVersionUID = 1L;
    
    //constants
    private final char BLANK = ' ';
    private final char BLACK = 'B';
    private final char RED = 'R';
    private final int ROWS = 6;
    private final int COLS = 7;
    private final String instructions = "Welcome to Connect Four! \n\n"
            + "Each player is assigned a color: Red or Black\n"
            + "Player 1 is Black and Player 2 is Red\n"
            + "Click in a column to drop a game piece.\n"
            + "Your piece will always drop to the lowest available space.\n"
            + "A player wins when they have four of their pieces in a line in any direction.";
    
    private JPanel topPanel;
    private JButton newGameButton;
    private JPanel bottomPanel;
    private JLabel blackPlayerName, redPlayerName, playerOne, playerTwo, winCounter;
    private JMenuBar menu;
    private JMenu file, options, help;
    private JMenuItem mi;
    
    private char[][] gameBoard = new char[ROWS][COLS];
    private char activePlayer;
    private boolean gameOver;
    private String blackName, redName;
    private int redWins, blackWins;
    private int startX, startY, endX, endY;
    
    public ConnectFour() {
        setTitle("Connect Four");
        setBounds(0, 0, 900, 800);
        setLayout(new BorderLayout());
        gameOver = false;
        activePlayer = BLACK;
        addMouseListener(this);
        initializeBoard();
        
        // Add panel components
        topPanel = new JPanel();
        playerOne = new JLabel("Player 1 (Black): ");
        playerTwo = new JLabel("Player 2 (Red): ");
        blackPlayerName = new JLabel();
        redPlayerName = new JLabel();
        winCounter = new JLabel(blackWins + " - " + redWins);
        topPanel.add(playerOne);
        topPanel.add(blackPlayerName);
        topPanel.add(new JLabel("                                                       "));
        topPanel.add(winCounter);
        topPanel.add(new JLabel("                                                       "));
        topPanel.add(playerTwo); 
        topPanel.add(redPlayerName);
        add(topPanel, BorderLayout.NORTH);
        bottomPanel = new JPanel();
        newGameButton = new JButton("Start New Game");
        newGameButton.addActionListener(this);
        bottomPanel.add(newGameButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Add menu bar
        setJMenuBar(menu = new JMenuBar());
        menu.add(file = new JMenu("File"));
        menu.add(options = new JMenu("Options"));
        menu.add(help = new JMenu("Help"));
        file.add(mi = new JMenuItem("New Game"));
        mi.addActionListener(this);
        file.add(mi = new JMenuItem("Exit"));
        mi.addActionListener(this);
        options.add(mi = new JMenuItem("Reset Win Count"));
        mi.addActionListener(this);
        options.add(mi = new JMenuItem("Change Player Names"));
        mi.addActionListener(this);
        help.add(mi = new JMenuItem("Instructions"));
        mi.addActionListener(this);
        
        setVisible(true);
        promptForPlayerNames();
              
    }
    
    public void promptForPlayerNames() {
        String name = setPlayerName();
        if (name != null && !name.equals("")) {
            blackName = name;
        }
        else {
            blackName = "Anonymous";
        }
        name = setPlayerName();
        if (name != null && !name.equals("")) {
            redName = name;
        }
        else {
            redName = "Anonymous";
        }
        blackPlayerName.setText(blackName);
        redPlayerName.setText(redName);
    }
    
    public String setPlayerName() {
        String name = JOptionPane.showInputDialog(this, "Please Enter Player's Name", null);
        return name;
    }
    
    public void initializeBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                gameBoard[r][c] = BLANK;
            }
        }
    }
    
    public void playerMove(int c) {
        for (int r = 0; r < ROWS - 1; r++) {            
            
            if (gameBoard[ROWS - 1][c] == BLANK) {
                gameBoard[ROWS - 1][c] = activePlayer;
                break;
            }
            else if (gameBoard[r + 1][c] != BLANK && gameBoard[r][c] == BLANK) {
                gameBoard[r][c] = activePlayer;
            }
        }
        repaint();
        if (checkForWin()) {
            gameOver = true;
            String winnerName;
            if (activePlayer == RED) {
                winnerName = redName;
                redWins++;
            }
            else {
                winnerName = blackName;
                blackWins++;
            }
            JOptionPane.showMessageDialog(this, winnerName + " Wins!!");
            updateWinCounter();
            repaint();
        }
        if (!gameOver) {
            if (activePlayer == BLACK) {
            activePlayer = RED;
            }
            else {
            activePlayer = BLACK;
            }
        }
    }
    
    public boolean isValidClick(Point p) {
        if (p.x > 100 && p.x < 100 + 100 * COLS && p.y > 100 && p.y < 100 + 100 * ROWS) {
            System.out.println("Valid click");
            return true;
        }
        else {
            System.out.println("Invalid click");
            return false;
        }        
    }
    
    public int findColumn(Point p) {
        if (p.x > 100 && p.x < 200) {
            System.out.println("Click in column 0");
            return 0;
        }
        else if (p.x > 200 && p.x < 300) {
            System.out.println("Click in column 1");
            return 1;
        }
        else if (p.x > 300 && p.x < 400) {
            System.out.println("Click in column 2");
            return 2;
        }
        else if (p.x > 400 && p.x < 500) {
            System.out.println("Click in column 3");
            return 3;
        }
        else if (p.x >= 500 && p.x < 600) {
            System.out.println("Click in column 4");
            return 4;
        }
        else if (p.x >= 600 && p.x < 700) {
            System.out.println("Click in column 5");
            return 5;
        }
        else if (p.x >= 700 && p.x < 800) {
            System.out.println("Click in column 6");
            return 6;
        }
        else {
            return -1;
        }
    }
    
    public boolean checkForWin() {
        //horizontal win
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                char check = gameBoard[r][c];
                if (check == activePlayer && check == gameBoard[r][c + 1] && 
                        check == gameBoard[r][c + 2] && check == gameBoard[r][c + 3]) {
                    startX = 100 + 100 * c + 5;
                    startY = 100 + 100 * (r + 1) - 50;
                    endX = 100 + 100 * c + 400 - 5;
                    endY = 100 + 100 * (r + 1) - 50;
                    return true;
                }
            }
        }
        // vertical win
        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r < ROWS - 3; r++) {
                char check = gameBoard[r][c];
                if (check == activePlayer && check == gameBoard[r + 1][c] && 
                       check == gameBoard[r + 2][c] && check == gameBoard[r + 3][c]) {
                    startX = 100 + 100 * (c + 1) - 50;
                    startY = 100 + 100 * r + 5;
                    endX = 100 + 100 * (c + 1) - 50;
                    endY = 100 + 100 * r + 400 - 5;
                    return true;
                }
            }
        }
        // diagonal from top left
        for (int r = 0; r < ROWS - 3; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                char check = gameBoard[r][c];
                if (check == activePlayer && check == gameBoard[r + 1][c + 1] && 
                        check == gameBoard[r + 2][c + 2] && check == gameBoard[r + 3][c + 3]) {
                    startX = 100 + c * 100 + 5;
                    startY = 100 + r * 100 + 5;
                    endX = 100 + (c + 4) * 100 - 5;
                    endY = 100 + (r + 4) * 100 - 5;
                    return true;
                }
            }
        }
        
        // diagonal from bottom left
        for (int r = ROWS - 1; r >= 3; r--) {
            for (int c = 0; c < COLS - 3; c++) {
                char check = gameBoard[r][c];
                if (check == activePlayer && check == gameBoard[r - 1][c + 1] && 
                       check == gameBoard[r - 2][c + 2] && check == gameBoard[r - 3][c + 3]) {
                    startX = 100 + c * 100 + 5;
                    startY = 100 + r * 100 + 100 - 5;
                    endX = 100 + (c + 4) * 100 - 5;
                    endY = 100 + (r - 3) * 100 + 5;
                    return true;
                }
            }
        }
        return false;
    }
    
    public void updateWinCounter() {
        winCounter.setText(blackWins + " - " + redWins);
        repaint();
    }
    
    public void resetWinCounter() {
        blackWins = 0;
        redWins = 0;
        updateWinCounter();
    }
    
    public void startNewGame() {
        gameOver = false;
        activePlayer = BLACK;
        initializeBoard();
        repaint();
    }
    
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        //Find location of click
        if (!gameOver) {
            if (isValidClick(e.getPoint())) {
                playerMove(findColumn(e.getPoint()));
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
    @Override
    public void mousePressed(MouseEvent arg0) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button Pushed: " + e.getActionCommand());
        if (e.getSource().equals(newGameButton)) {
            startNewGame();
        }
        
        String cmd = e.getActionCommand();
        if (cmd.equals("New Game")) {
            startNewGame();
        }
        if (cmd.equals("Exit")) {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit", 0);
            if (choice == 0) {
                setVisible(false);
                dispose();
                System.exit(0);
            }
        }
        if (cmd.equals("Reset Win Count")) {
            resetWinCounter();
        }
        if (cmd.equals("Change Player Names")) {
            promptForPlayerNames();
        }
        if (cmd.equals("Instructions")) {
            JOptionPane.showMessageDialog(this, instructions, "Conect Four Instructions", 1);
        }
    }
    
    public void markWinner(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(5));
        g.drawLine(startX, startY, endX, endY);
    }
    
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        this.paintComponents(g2d);
        
        //Draw the game board
        g2d.setColor(Color.YELLOW);
        g2d.fill3DRect(100, 100, 100 * COLS, 100 * ROWS, true);

        g2d.setColor(Color.BLACK);
        int line = 200;
        for (int r = 1; r <= ROWS - 1; r++) {
            g2d.drawLine(100, line, 799, line);
            line += 100;
        }
        line = 200;
        for (int c = 1; c <= COLS - 1; c++) { 
            g2d.drawLine(line, 100, line, 699);
            line += 100;
        }
        
        // Fill board positions
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (gameBoard[r][c] == BLANK) {
                    g2d.setColor(Color.WHITE);
                }
                if (gameBoard[r][c] == BLACK) {
                    g2d.setColor(Color.BLACK);
                }
                if (gameBoard[r][c] == RED) {
                    g2d.setColor(Color.RED);
                }
                g2d.fillOval(105 + 100 * c, 105 + 100 * r, 90, 90);
             }
        }
        
        if (gameOver) {
            markWinner(g2d);
        }
        
      }

    public static void main(String[] args) {
        ConnectFour game = new ConnectFour();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setVisible(true);
    }

}
