package view;

import javax.swing.JPanel;
/**
* Abstract class for the game board.
* 
* @author Fasil Ayenew
* @version 09 December 2016
*/
public abstract class AbstractBoard extends JPanel {
    /**
     * serial id.
     */
    private static final long serialVersionUID = 1L;
    /** integer to help with the size of the grid.*/
    protected int myNumX;
    /** integer to help with the size of the grid.*/
    protected int myNumY;
    /** calls J panel super class.*/
    public AbstractBoard() {
        super();
    }
    /** calculates the x dimension of the grid.
     * 
     * @return array list of the grid
     */
    protected double[] calcGridX() {
        
        final double[] grid = new double[myNumX];
        
        double loc = 0;
        final double div = (double) this.getWidth() / myNumX;
        
        for (int i = 0; i < myNumX; i++) {
            grid[i] = loc;
            loc = loc + div;
        }
        return grid;
    }
    /** calculates the y dimension of the grid.
     * 
     * @return array list of the grid
     */
    protected double[] calcGridY() {
        
        final double[] grid = new double[myNumY];
        double loc = 0;
        final double div = (double) this.getHeight() / myNumY;
        
        for (int i = myNumY - 1; i >= 0; i--) {
            grid[i] = loc;
            loc = loc + div;
        }
        return grid;
    }
}