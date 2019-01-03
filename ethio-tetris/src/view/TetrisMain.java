package view;
import java.awt.EventQueue;


/**
* Main class that calls the game.
* 
* @author Fasil Ayenew
* @version 09 December 2016
*/
public class TetrisMain {
    /** calls the GUI class and runs the program.
     * 
     * @param theArgs arguments
     */
    public static void main(final String[] theArgs) {
        
        EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                new TetrisGUI().start();
            }
        });
    }
}