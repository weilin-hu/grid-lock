import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.io.*;


/* Contains the state of the game and information about how to draw.
 * The element that users interract act with to manipulate the state.
 * 
 * 
 */
@SuppressWarnings("serial")
public class Board extends JComponent {
    
    // Game constants
    private static final int BOARD_WIDTH = 600;
    private static final int BOARD_HEIGHT = 600;
    
    private String currentLevel; // current Level the boar displays
    
    private JLabel status; // Current status text
    
    private boolean playing = false; // if board has a game playing
    
    private boolean pressed = false; // if mouse is pressed
    
    private Block[][] stateToArray; // holds the state of the game 
    
    private GameState state; // we use this to update the state Array
    
    private Point pressPoint; // coordinates the point that was pressed
    
    private Point dragPoint; // coordinates of the current mouse position
    
    private Block pressedBlock; // block that is moving 
    
    private Point oldLocation; // original coordinates of the block
    
    private Collection<Block> blocks; // collection of all the blocks 
                                        //(originally intended for intersection implementaion)
    
    /* -----------------------------------------
     *          CONSTRUCTOR
     * ---------------------------------------
     */
    
    public Board(JLabel status) {
        // if board is created everything is essentially empty
        state = null;
        stateToArray = null;
        currentLevel = null;
        pressedBlock = null;
        pressPoint = new Point();
        dragPoint = new Point();
        oldLocation = new Point();
        
        // add a mouseListener
        addMouseListener(new MouseAdapter() {
            
            public void mousePressed(MouseEvent e) {
                pressed = true;
                pressPoint = e.getPoint();
                
                // if game is open
                if (playing) {
                    blocks = state.getBlocks();
                    for (Block block : blocks) {
                        // if this block contains the coordinates
                        if (block.pressed(pressPoint)) {
                            // this block is to be moved
                            pressedBlock = block;
                            // record the current location of the block
                            oldLocation = new Point(block.getColumn() * 100,
                                                        block.getRow() * 100);
                            // leave the loop
                            break;
                        }
                    }
                }
            }
            
            public void mouseReleased(MouseEvent e) {
                // if mouse is released, set everything back to normal
                oldLocation = new Point();
                pressPoint = new Point();
                pressedBlock = null;
                pressed = false;
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            
            public void mouseDragged(MouseEvent e) {
                
                dragPoint = e.getPoint();
                // if mouse is dragged while pressed
                if (pressed) {
                    // if a block was pressed
                    if (pressedBlock != null) {
                        // get the point the block should be moved
                        Point newIndex = getRealEndIndex(pressedBlock, oldLocation, 
                                                             pressPoint, dragPoint);
                        // slide the block to that point
                        slideCar(pressedBlock, newIndex);
                    }
                }
            }
        });
        
        this.status = status;
    }
    
    // this methods loads a game to the board when the button is pressed from Game
    public void loadState(GameState state) {
        // change status to playing 
        status.setText("Playing...");
        blocks = state.getBlocks();
        this.state = state;
        stateToArray = state.getState();
        playing = true;
        
        // get the file
        String fileName = state.getFilePath();
        
        // retrieves the current level
        if (fileName.equals("files/level1.txt") || fileName.equals("files/level1_progress.txt")) {
            currentLevel = "files/level1.txt";
        } else if (fileName.equals("files/level2.txt") || 
                   fileName.equals("files/level2_progress.txt")) {
            currentLevel = "files/level2.txt";
        } else if (fileName.equals("files/level3.txt") || 
                   fileName.equals("files/level3_progress.txt")) {
            currentLevel = "files/level3.txt";
        } else if (fileName.equals("files/level4.txt") || 
                   fileName.equals("files/level4_progress.txt")) {
            currentLevel = "files/level4.txt";
        } else if (fileName.equals("files/level5.txt") || 
                   fileName.equals("files/level5_progress.txt")) {
            currentLevel = "files/level5.txt";
        }
        
        
    }
    
    // slides the block to the location
    public void slideCar(Block block, Point location) {
        double xCoor = location.getX();
        double yCoor = location.getY();
        
        // snap the location to the nearest index
        int xIndex = (int) Math.round(xCoor / 100); 
        int yIndex = (int) Math.round(yCoor / 100);
        
        // change the state of the block
        block.slideTo(yIndex, xIndex);
        stateToArray = state.getState();
        repaint();
    }
    
    // get the index the block should be moved relative to the difference/distance mouse is dragged
    public Point getRealEndIndex(Block block, Point oldIndex, Point pressPoint, Point dragPoint) {
        final String direction = block.getDirection();
        // get the block's current location in the state
        final int blockRow = (int) oldIndex.getY();
        final int blockCol = (int) oldIndex.getX();
        
        // get the coordinates the mouse start and end
        double startX = pressPoint.getX();
        double startY = pressPoint.getY();
        double dragX = dragPoint.getX();
        double dragY = dragPoint.getY();
        
        double endColIndex = 0;
        double endRowIndex = 0;
        
        // if horizontal
        if (direction.equals("H")) {
            // row is the same
            endRowIndex = blockRow;
            // column is the sum of the current column and the distance mouse is dragged
            endColIndex = blockCol + dragX - startX;
            
            // if vertical
        } else if (direction.equals("V")) {
            // column stays the same 
            endColIndex = blockCol;
            // row is the sum of current row and distance mouse is dragged
            endRowIndex = blockRow + dragY - startY;
        }
        
        return new Point((int) endColIndex, (int) endRowIndex);
    }
    
    /* -----------------------------------------
     *          METHODS
     * ---------------------------------------
     */
    
    // the method resets the state of the board to the default level 
    public void reset() {
        // when game is reset (after victory) change state of Playing
        if (playing) {
            status.setText("Playing...");
        }
        
        // load the original state of the level again
        if (currentLevel != null) {
            loadState(new GameState(currentLevel));
            stateToArray = state.getState();
        }
    }
    
    /* -----------------------------------------
     *          JPANEL COMPONENT
     * ---------------------------------------
     */
    
     
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        int sqDim = 98;
        int tileX = 0;
        int tileY = 0;
        
        // fill the background grey
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        
        g.setColor(Color.WHITE);
        // for each tile draw a white square
        for (int i = 0; i < 6; i++) {
            tileX = i * 100;
            for (int j = 0; j < 6; j++) {
                tileY = j * 100;  
                g.fillRect(tileX, tileY, sqDim, sqDim);
            }
        }
        
        // if there is a game, get the cars from the cells
        if (stateToArray != null) {
            for (int r = 0; r < stateToArray.length; r++) {
                for (int c = 0; c < stateToArray[0].length; c++) {
                    if (stateToArray[r][c] != null) {
                        // if there is a car in the cell, draw it
                        stateToArray[r][c].draw(g);
                    }
                }
            }
        }
        
        // if the driver reaches the last index 
        if (playing) {
            if (state.getDriverColumn() == 4) {
                // set status to victory
                status.setText("You Passed!");
            }
        }
        
       
    }
    
     
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
    
    
    /* -----------------------------------------
     *          SAVE COMPONENT
     * ---------------------------------------
     */
    
    
    /* This method uses the current state of blocks on the board 
     * and writes them into a progress file that can be opened upon 
     * subsequent visits to the level.
     * 
     * Used for Save Progress button in Game class.
     * @param filePath: String with filePath location to be written
     * 
     */
    
    public void writeToFile(String filePath) {
        // instantiate a filewriter
        FileWriter w = null;
        
		try {
            // Override the current contents
            w = new FileWriter(filePath, false);
            
            // iterate through the Block[][] blocks
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    // current block in the cell
                    Block blockToWrite = stateToArray[i][j];
                    
                    // if there is no block is that cell
                    if (blockToWrite == null) {
                        // write '.'
                        w.write((int) '.');
                    } else {
                        // write the ID of the block in the cell
                        w.write((int) blockToWrite.getID());
                    }
                }
                if (i < 5) {
                    // next line when about to exceed 6 columns
                    w.write(10);
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error occurred while writing data");
        } catch (NullPointerException e) {
            System.out.println("There is no state to write");
        } finally {
            try {
                w.flush();
                w.close();
            } catch (IOException e) {
                System.out.println("Error when flushing or closing BufferedWriter");
            }
        }
        
    }
    
    // returns a String with the current level the player is working on
    public String getCurrentFile() {
        return currentLevel;
    }   
    
}
    
