package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import javax.swing.JSlider;
import javax.swing.JSplitPane;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;

import model.Block;
import model.Board;
import model.TetrisPiece;
import sound.SoundPlayer;


/**
* GUI class.
* 
* @author Fasil Ayenew
* @version 09 December 2016
*/
public class TetrisGUI {
    /** accesses observer and key listener objects.*/
    public class View implements Observer, KeyListener {
        /** helps set the frame of the pop up.*/
        private JFrame myFrame;
        /** game board class to construct board.*/
        private TetrisBoard myBoard;
        /** game panel that tells the next incoming piece.*/
        private TetrisPanel myPiecePanel;
        /** contains all elements in panel.*/
        private JPanel myContainer;
        /** spit panel to show next piece.*/
        private JSplitPane mySplitPane;
        /** displays message.*/
        private JLabel myLabel;
        /** board class.*/
        private Board myBoardModel;
        /** keeps track time in game.*/
        private Timer myGameTimer;
        /** menu bar for buttons.*/
        private JMenuBar myMenuBar;
        /** help button.*/
        private JMenu myHelp;
        /** instructions on how to play.*/
        private JMenuItem myInstructions;
        /** allows user to make custom controls.*/
        //private JMenuItem myCustom;
        /** extra panel to be added onto the main one.*/
        private JPanel myFillPanel1;
        /** extra panel to be added onto the main one.*/
        private JPanel myFillPanel2;
        /** dimension of frame.*/
        private final int myX = 600;
        /** dimension of frame.*/
        private final int myY = 750;
        /** divider location/ window dimension.*/
        private final double myZ = 0.25;
        /** keeps track of number of lines until next level.*/
        private final int myLines = 5;
        /** multiplier to keep track of level score.*/
        private final int myMultiplier = 100;
        /** initial score added when piece freezes in place.*/
        private final int myIntScore = 4;
        /** initial score added for line 1.*/
        private final int myIntScore2 = 40;
        /** initial score added for line 2.*/
        private final int myIntScore3 = 100;
        /** initial score added for line 3.*/
        private final int myIntScore4 = 300;
        /** initial score added for line 4.*/
        private final int myIntScore5 = 1200;
        /** the game's speed.*/
        private final int myIntGameSpeed = 1000;
        /** initial game speed.*/
        private int myGameSpeed = myIntGameSpeed;
        /** checks if game is currently running.*/
        private boolean myGameRun;
        /** checks if game is over.*/
        private boolean myGameOver;
        /** keeps track of the games level.*/
        private int myGameLevel;
        /** stores new/end game options.*/
        private JMenu myFile;
        /** starts a new game.*/
        private JMenuItem myNewGame;
        /** ends the current game.*/
        private JMenuItem myEndGame;
        /** keeps track of rows cleared.*/
        private int myRowsCleared;
        /** keeps track of the high score.*/
        private int myHighScore;
        /** keeps track of current score.*/
        private int myScore;
        /** displays score in frame.*/
        private JLabel myScoreLabel;
        /** displays level in frame.*/
        private JLabel myLevelLabel;
        /** displays game speed.*/
        private JLabel mySpeedLabel;
        /** displays lines cleared in frame.*/
        private JLabel myLinesCleared;
        /** displays number of lines until next level in frame.*/
        private JLabel myLinesToGo;
        /** displays high score in frame.*/
        private JLabel myHighScoreLabel;
        /** displays when game is over.*/
        private JLabel myGameStatus;
        /** helps keep track of score.*/
        private boolean mySkipFirst;
        /** music!.*/
        private SoundPlayer myTunes = new SoundPlayer();
        /** progression of the game's levels.*/
        private final int myLevelSpeed = 5;
        
        
        /**
         * Constructor that sets up all the buttons with filters
         * and assigns specific tasks for each button.
         */
        public View() {
            
            //Create board model
            myBoardModel = new Board();
            myGameTimer = null;
            
            myBoardModel.addObserver(this);
            
            //Create window
            myFrame = new JFrame();
            myFrame.setPreferredSize(new Dimension(myX, myY));
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myFrame.addKeyListener(this);
            myFrame.pack();
            
            final ImageIcon image = new ImageIcon("images/tetris_icon.jpg");
            myFrame.setIconImage(image.getImage());
            
            

            //Add listener to handle dynamic resizing of window
            myFrame.addComponentListener(new ComponentAdapter() {
                public void componentResized(final ComponentEvent theSize) {
                    
                    try {
                        
                        myFrame.setPreferredSize(calcFrame());
                        mySplitPane.setDividerLocation(myZ);
                        myBoard.setPreferredSize(calcBoardPanel());
                        myPiecePanel.setPreferredSize(calcTetrisPanel());
                        myFillPanel1.setPreferredSize(calcFillPanel1());
                        myFillPanel2.setPreferredSize(calcFillPanel2());
                    } catch (final Exception error) {
                        
                        myFrame.setPreferredSize(new 
                                                 Dimension(myX, myY));                        
                    }
                }
            });
            
            
            myFrame.setLayout(new BorderLayout());
            
            // create game board 
            myBoard = new TetrisBoard();            
            myBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            myBoard.setPreferredSize(calcBoardPanel());
            
            // container for preview stuff
            myContainer = new JPanel();
            myContainer.setLayout(new BoxLayout(myContainer, BoxLayout.PAGE_AXIS));
            
            // create panel to show piece preview
            myPiecePanel = new TetrisPanel();
            myPiecePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));            

            
            // create fill panel1
            myFillPanel1 = new JPanel();
            myContainer.add(myFillPanel1);
            
            // preview Label & add things to container
            myLabel = new JLabel("Next");            
            myContainer.add(myLabel);
            myContainer.add(myPiecePanel);
            
            // create second fill panel & add
            myFillPanel2 = new JPanel();
            myFillPanel2.setLayout(new BoxLayout(myFillPanel2, BoxLayout.PAGE_AXIS));
            myScoreLabel = new JLabel();
            myHighScoreLabel = new JLabel();
            myLevelLabel = new JLabel();
            mySpeedLabel = new JLabel();
            myGameStatus = new JLabel();
            myLinesCleared = new JLabel();
            myLinesToGo = new JLabel();
            
            myFillPanel2.add(myHighScoreLabel);
            myFillPanel2.add(myLevelLabel);
            myFillPanel2.add(mySpeedLabel);
            myFillPanel2.add(myScoreLabel);
            myFillPanel2.add(myLinesCleared);
            myFillPanel2.add(myLinesToGo);
            
            myFillPanel2.add(myGameStatus);
            
            myContainer.add(myFillPanel2);
            
            
            // create split pane to divide window into 2 sections (preview & board)
            mySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myContainer, myBoard);
            
            mySplitPane.setDividerLocation(myZ);

            myFrame.add(mySplitPane, BorderLayout.CENTER);
            
            // set sizes
            myPiecePanel.setPreferredSize(calcTetrisPanel());
            myFillPanel1.setPreferredSize(calcFillPanel1());            
            myFillPanel2.setPreferredSize(calcFillPanel2());
            
            myMenuBar = new JMenuBar();
            
            myFile = new JMenu("File");
            myNewGame = new JMenuItem("New Game");
            myNewGame.setEnabled(false);            
            myNewGame.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    // TODO Auto-generated method stub                    
                    myEndGame.setEnabled(true);
                    resetGame();
                    newGame();
                    myTunes.loop("music/Megalovania.wav");
                }                
            });
            
            myEndGame = new JMenuItem("End Game");
            myEndGame.setEnabled(true);
            myEndGame.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    // TODO Auto-generated method stub
                    endGame();
                    myTunes.stopAll();
                }                
            });
            
            myFile.add(myNewGame);
            myFile.add(myEndGame);
            myMenuBar.add(myFile);
  
            myHelp = new JMenu("Help");  
            
            myInstructions = new JMenuItem("Controls");
            myHelp.add(myInstructions);
            myInstructions.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent theEvent) {
                    
                    String helpMsg = "Key                  Action\n";
                    helpMsg += "\u2190       -          Move Piece Left\n";
                    helpMsg += "\u2192       -          Move Piece Right\n";
                    helpMsg += "\u2193        -           Move Piece Down\n";
                    helpMsg += "z         -          Rotate Piece CLockwise\n";
                    helpMsg += "x         -          Rotate Piece Counter CLockwise\n";
                    helpMsg += "space    -      Drop Piece \n";
                    helpMsg += "p    -              Pause/Unpause game \n";
                    JOptionPane.showMessageDialog(myFrame, helpMsg, "Controls", 
                                                  JOptionPane.PLAIN_MESSAGE);
                }
            });
            
            myMenuBar.add(myHelp);
            myFrame.setJMenuBar(myMenuBar);
            
            mySplitPane.setEnabled(false);
            myFrame.setVisible(true);
            myFrame.pack();
            
            getHighScore();
            
            resetGame();
        }
        /** Helper functions to calculate window dimensions.
         * 
         * @return dimension of frame
         */
        private Dimension calcFrame() {
            
            final int height = myFrame.getHeight();
            final int width = (int) Math.round(height * 0.8);
            
            return new Dimension(width, height);
        }
        
        /** Helper functions to calculate game panel dimensions.
         * 
         * @return dimension of the game panel
         */
        private Dimension calcTetrisPanel() {
            final int width = (int) Math.round(myZ * myFrame.getWidth());
            final int height = width;       
            
            return new Dimension(width, height);
        }
        /** Helper functions to calculate board dimensions.
         * 
         * @return dimension of board
         */
        private Dimension calcBoardPanel() {
            
            final int width = myFrame.getHeight();
            final int height = myFrame.getHeight();
            
            return new Dimension(width, height);
        }
        /** Helper functions to calculate extra panel dimensions.
         * 
         * @return dimension of the extra panel
         */
        private Dimension calcFillPanel1() {
            
            final int width = (int) Math.round(myZ * myFrame.getWidth());
            int height = myFrame.getHeight();
            
            height = height / 2 - myPiecePanel.getHeight() / 2 - myLabel.getHeight();
            
            return new Dimension(width, height);
        }
                
        /** Helper functions to calculate extra panel dimensions.
         * 
         * @return dimension of the extra panel
         */
        private Dimension calcFillPanel2() {
            
            final int width = (int) Math.round(myZ * myFrame.getWidth());
            int height = myFrame.getHeight();
            
            height = height / 2 - myPiecePanel.getHeight() / 2;
            
            return new Dimension(width, height);
        }
        
        /**
         * Updates the program.
         */
        public void update() {
            updateLabels();
            myFrame.pack();
        }
        /** updates the games information.*/
        private void updateLabels() {
            
            myHighScoreLabel.setText("High Score: " + myHighScore);
            myScoreLabel.setText("Score: " + myScore);
            myLevelLabel.setText("Level: " + myGameLevel);
            mySpeedLabel.setText("Game speed(ms): " + myGameSpeed);
            myLinesCleared.setText("Lines Cleared: " + myRowsCleared);
            myLinesToGo.setText("Lines Until Next Level: " + (myLines 
                            - myRowsCleared % myLines));
            if (!myGameOver) {
                myGameStatus.setText("");
            } else {
                //myGameSpeed = myIntGameSpeed;
                myGameStatus.setText("Game Over");
                myTunes.stopAll();
            }
        }
        
        //Updates frame based on placements
        @Override
        public void update(final Observable theObserver, final Object theArg) {
            
            if (theArg instanceof List<?>) {
                
                if (((List<?>) theArg).get(0) instanceof Block[]) {
                    
                    //System.out.println("Updating GUI Board");
                    myBoard.updateBoard((List<Block[]>) theArg);
                    myBoard.repaint();
                    
                }
            } else if (theArg instanceof TetrisPiece) {
                if (!mySkipFirst) {
                    myScore += myIntScore;
                    myTunes.play("music/bottle-pop.wav");
                    
                } else {
                    mySkipFirst = false;
                }
                myPiecePanel.updatePiece((TetrisPiece) theArg);
                myPiecePanel.repaint();
            } else if (theArg instanceof Boolean) {
                
                if ((Boolean) theArg) {
                    endGame();
                }
            } else if (theArg instanceof Integer[]) {
                
                myRowsCleared += ((Integer[]) theArg).length;
                addScore(((Integer[]) theArg).length);
                //System.out.println("line cleared!");
                myGameLevel = myRowsCleared / myLevelSpeed + 1;
                myGameSpeed = myIntGameSpeed - myGameLevel * myMultiplier;
                //System.out.println("increasing game speed" + myGameSpeed);
                if (myGameSpeed < myIntScore4) {
                    myGameSpeed = myIntScore4;
                }
               
            }
            this.update();
        }
        
        @Override
        public void keyPressed(final KeyEvent arg0) {
            
            if (!myGameOver) {
                
                if (!myGameRun)  {
                    
                    if (arg0.getKeyCode() == KeyEvent.VK_P) {
                        myTunes.loop("music/Megalovania.wav");
                        myTunes.stop("music/Elevator-Music.wav");
                        startTimer();
                    }
                } else {
                
                    if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
                        
                        myBoardModel.left();
                        myTunes.play("music/turn.wav");
                    } else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
                        
                        myBoardModel.right();
                        myTunes.play("music/turn.wav");
                    } else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
                        
                        myBoardModel.down();
                        myTunes.play("music/turn.wav");
                    } else if (arg0.getKeyCode() == KeyEvent.VK_Z) {
                        
                        myBoardModel.rotateCW();
                        myTunes.play("music/turn.wav");
                    } else if (arg0.getKeyCode() == KeyEvent.VK_X) {
                        
                        myBoardModel.rotateCCW();
                        myTunes.play("music/turn.wav");
                    } else if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
                        
                        myBoardModel.drop();
                    } else if (arg0.getKeyCode() == KeyEvent.VK_P) {
                        
                        myTunes.stop("music/Megalovania.wav");
                        myTunes.loop("music/Elevator-Music.wav");
                        stopTimer();
                    }
                }
            }
            
        }
        /** adds scores based on levels.
         * 
         * @param theLines levels.
         */
        public void addScore(final int theLines) {
            
            switch (theLines) {
                
                case 0: myScore += myIntScore;
                break;
                case 1: myScore += myIntScore2 * myGameLevel;
                break;
                case 2: myScore += myIntScore3 * myGameLevel;
                break;
                case 3: myScore += myIntScore4 * myGameLevel;
                break;
                default:myScore += myIntScore5 * myGameLevel;
            }
        }

        @Override
        public void keyReleased(final KeyEvent theEvent) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyTyped(final KeyEvent theEvent) {
            // TODO Auto-generated method stub
            
        }
        
        /** timer to increment piece every 1s.*/
        private void startTimer() {
            
            myGameTimer = new Timer();
            myGameTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    
                    myBoardModel.step();
                }
            }, 0, myGameSpeed);
            //System.out.println("current game speed: " + myGameSpeed);
            myGameRun = true;
            this.update();
        }
        /** stop the timer.*/
        private void stopTimer() {
            
            myGameTimer.cancel();
            myGameTimer.purge();
            myGameRun = false;
            this.update();
        }
        /** starts new game.*/
        private void newGame() {
            
            myNewGame.setEnabled(false);
            myEndGame.setEnabled(true);
            myBoardModel.newGame();
            myGameOver = false;
            startTimer();
            this.update();
        }
        /** resets game.*/
        private void resetGame() {
            
            myNewGame.setEnabled(true);
            myEndGame.setEnabled(false);
            myGameLevel = 1;         
            myGameSpeed = myIntGameSpeed;
            //System.out.println("game speed at the start: " + myGameSpeed);
            myRowsCleared = 0;
            myScore = 0;
            
            myGameOver = true;
            mySkipFirst = true;
        }
        /** ends game.*/
        private void endGame() {
            
            myNewGame.setEnabled(true);
            myEndGame.setEnabled(false);
            stopTimer();
            myGameOver = true;
            myGameRun = false;
            
            
            if (myScore > myHighScore) {
                
                myHighScore = myScore;
                writeHighScore();
            }
            this.update();
        }
        
        /** gets the high score of the current game.*/
        private void getHighScore() {
            
            final File scoreFile = new File("Score.dat");
            try {
                
                final FileReader fr = new FileReader(scoreFile);
                final BufferedReader scoreReader = new BufferedReader(fr);
                myHighScore = Integer.parseInt(scoreReader.readLine());
                scoreReader.close();
            } catch (final FileNotFoundException e) {
                
                myHighScore = 0;
                try {
                    scoreFile.createNewFile();
                } catch (final IOException e1) {
                    
                    System.out.println("Cannot Access High Score Records");
                }

            } catch (final NumberFormatException e) {
                
                myHighScore = 0;
            } catch (final IOException e) {
                
                myHighScore = 0;
            }
        }
        /** keeps track of high score.*/
        private void writeHighScore() {
            
            final File scoreFile = new File("Score.dat");
            final FileWriter fr;
            try {
                scoreFile.createNewFile();
                fr = new FileWriter(scoreFile);
                fr.write(Integer.toString(myHighScore));
                fr.close();
            } catch (final IOException e) {
                System.out.println("Error saving high score!");
            }
        }
    }
    
    /**
     * Updates methods in class. 
     */
    public void start() {
        
        final View imageView = new View();
        imageView.update();      
    } 
}