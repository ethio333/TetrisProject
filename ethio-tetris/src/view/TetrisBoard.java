package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import model.Block;

/**
* constructs the board of the game.
* 
* @author Fasil Ayenew
* @version 08 December 2016
*/
public class TetrisBoard extends AbstractBoard {
 
    /** serial ID.*/
    private static final long serialVersionUID = 1L;
    /** keeps track of when the board is empty and when to drop the next piece.*/
    private List<Block[]> myBoard;
    /** x coordinate size.*/
    private final int myX = 10;
    /** y coordinate size.*/
    private final int myY = 20;
    /** the number of rows.*/
    private final int myRowSet = 5;
    /** calls abstract board class to set new coordinates and set the initial board to null.*/
    
    public TetrisBoard() {
        
        super();
        myNumX = myX;
        myNumY = myY;
        myBoard = null;
    }
    /**
     * prints the board along with the pieces.
     * @param theGraphics graphics class
     */
    public void paintComponent(final Graphics theGraphics) {
        
        super.paintComponent(theGraphics);
        this.setBackground(Color.LIGHT_GRAY);
        theGraphics.setColor(Color.BLUE);
        final double [] gridX = this.calcGridX();
        final double [] gridY = this.calcGridY();
        
        final int divX = (int) Math.ceil((double) this.getWidth() / myNumX);
        final int divY = (int) Math.ceil((double) this.getHeight() / myNumY);
        final int rowOffset = myRowSet;
        
        if (myBoard != null) {

            for (int i = myBoard.size() - rowOffset; i >= 0; i--) {
                
                final Block[] row = myBoard.get(i);
                for (int j = 0; j < row.length; j++) {
                    
                    if (row[j] != null) {  
                        
                        theGraphics.fillRect((int) Math.ceil(gridX[j]), 
                                             (int) Math.ceil(gridY[i]), 
                                             divX, divY);                                   
                    }
                }                        
            }
        }  
    }
    /**
     * updates the current board.
     * @param theBoard game board
     */
    public void updateBoard(final List<Block[]> theBoard) {
        this.myBoard = theBoard;
    }
}