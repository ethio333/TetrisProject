package view;

import java.awt.Color;
import java.awt.Graphics;

import model.Point;
import model.TetrisPiece;
/**
* Panel that will display the next incoming piece.
* 
* @author Fasil Ayenew
* @version 09 December 2016
*/
public class TetrisPanel extends AbstractBoard {
    /**
     * id.
     */
    private static final long serialVersionUID = 1L;
    /** a shape piece to called.*/
    private TetrisPiece myPiece;
    /** x coordinate size.*/
    private final int myX = 4;
    /** y coordinate size.*/
    private final int myY = 3;
    
    /** calls the abstract board class to get dimensions and sets initial shape to null.*/
    public TetrisPanel() {
        super();
        myPiece = null;        
        
        myNumX = myX;
        myNumY = myY;
    }
    /** displays piece that will come next.
     * @param theGraphics graphics class
     */
    public void paintComponent(final Graphics theGraphics) {
        
        super.paintComponent(theGraphics);
        this.setBackground(Color.WHITE);
        theGraphics.setColor(Color.BLUE);
        if (myPiece != null) {
            final double [] gridX = this.calcGridX();
            final double [] gridY = this.calcGridY();
            
            final Point [] p = myPiece.getPoints();
            final int divX = (int) Math.ceil((double) this.getWidth() / myNumX);
            final int divY = (int) Math.ceil((double) this.getHeight() / myNumY);
            
            final double xOffset = (this.getWidth() - myPiece.getWidth() * divX) / 2;
            final double yOffset = (this.getHeight() - myPiece.getHeight() * divY) / 2;
            
            for (int i = 0; i < p.length; i++) {
                
                theGraphics.fillRect((int) Math.ceil(gridX[p[i].x()] + xOffset), 
                                     (int) Math.ceil(gridY[p[i].y()] + yOffset), divX, divY);
            }
        }
    }
    /**
     * updates to the next game piece.
     * @param thePiece a game piece
     */
    public void updatePiece(final TetrisPiece thePiece) {
        
        this.myPiece = thePiece;
    } 
}