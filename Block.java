import javax.swing.*;
import java.awt.*;

/* This class holds the constructor for a type of block which the player will slide around
 * to achieve motive and win the game. 
 * 
 * Fields: 
 * @row, column - integer value holds the position of the block
 * @length - integer holds the size of the block. A block can only be of size 2 or 3.
 * @direction - String either "V" or "H" representing vertical for blocks that move up and down,
 * horizontal for blocks that move side to side. 
 * @id - character representing id of the block - helps identify blocks and determine which one is the driver
 * @color - the color the block is drawn
*/ 

public class Block implements Comparable<Block>  {
    
    private int row;
    
    private int column;
    
    private int length;
    
    private String direction; 
    
    private char id;
    
    private final Color color;
    
    public Block(int row, int column, int length, String direction, char id) {
        
        // if id is 'x', then that block is a driver
        if (id == 'x') {
            color = new Color(160, 200, 240);
            if (direction.equals("V")) { 
                throw new IllegalArgumentException("Driver cannot be vertical");
            }
        } else {
            color = new Color(230, 200, 160);
        }
        
        // block cannot be out of bounds, must have length 2 or 3
        if ( row < 0 || row > 5 || column < 0 || column > 5 || length < 1 || length > 3) {
            if (row < 0) {
                throw new IllegalArgumentException("row < 0");
            }
            if (row > 5) {
                throw new IllegalArgumentException("row > 5");
            }
            if (column < 0) {
                throw new IllegalArgumentException("column < 0");
            }
            if ( column > 5) {
                throw new IllegalArgumentException(" column > 5");
            }
            if (length < 1) {
                throw new IllegalArgumentException("length < 1");
            }
            throw new IllegalArgumentException("Cannot create a block of this type");
        }
        
        // throw an error if the car cannot be in 6x6 bounds
        if (direction.equals("V")) {
            if (row + length > 6) {
                throw new IllegalArgumentException("Cannot create a block out of bounds");
            }
        } else if (direction.equals("H")) {
            if (column + length > 6) {
                throw new IllegalArgumentException("Cannot create a block out of bounds");
            }
        } else {
            // throw an error if car does not have direction "H" or "V"
            throw new IllegalArgumentException("Cannot create car with this direction");
        }
        
        this.row = row;
        this.column = column;
        this.length = length;
        this.direction = direction;    
        this.id = id;
        
    }
    
    /* -------------------------------------------
     *                GETTER METHODS
     * --------------------------------------------*/
    
    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }
    
    // this methods gets the width of the car in the board
    public int getWidth() {
        int width = 80;
        if (direction.equals("H")) {
            width = (length - 1) * 100 + 80;
        }
        return width;
    }
    
    // this method gets the height of the car in the board
    public int getHeight() {
        int height = 80;
        if (direction.equals("V")) {
            height = (length - 1) * 100 + 80;
        }
        return height;
    }
    
    public int getLength() {
        return length;
    }
    
    public boolean getIsDriver() {
        return id == 'x';
    }
    
    public String getDirection() {
        return direction;
    }
    
    public char getID() {
        return id;
    }
    
    public Color getColor() {
        return color; 
    }
    
    /* -------------------------------------------
     *                  METHODS
     * --------------------------------------------*/
    
    // used for GameState constructor
    public void changeLength(int newLength) {
        length = newLength;
    }
    
    // used for GameState constructor
    public void updateDirection(String direction) {
        if (id != 'x') {
            this.direction = direction; 
        } else if (!direction.equals("H")) {
            throw new IllegalArgumentException("Driver can't be vertical!");
        }
            
    }
    
    // slides to block from the current locations to the one given
    public void slideTo(int row, int column) {
        // if block is horizontal
        if (direction.equals("H")) {
            // column cannot be greater than 6 - length
            int newColumn = Math.min(column, 6 - length);
            
            // column cannot be less than 0
            newColumn = Math.max(0, newColumn);
            
            // row is ignored since it doesn't change
            this.column = newColumn;
            
        } else {
            // row cannot be greater than 6 - length
            int newRow = Math.min(row, 6 - length);
            
            // row cannot be less than 0
            newRow = Math.max(0, newRow);
            
            // column is ignored since it doesn't change
            this.row = newRow;
        }
    }
    
    // gets an int[][] of the cells the block occupies
    public int[][] getCoorOccupied() {
        int[][] coorOccupied = new int[length][2];
        
        if (direction.equals("H")) {
            // iterate through the length of the block
            for (int l = 0; l < length; l++) {
                int[] coorH = {column + l, row};
                coorOccupied[l] = coorH;
            }
            
        } else if (direction.equals("V")) {
            for (int e = 0; e < length; e++) {
                int[] coorV = {row, column + e};
                coorOccupied[e] = coorV;
            }
        }
        return coorOccupied;
    }
    
    // returns a boolean if the block intersects that block
    public boolean intersects(Block that) {
        int[][] thisOccupied = this.getCoorOccupied();
        int[][] thatOccupied = that.getCoorOccupied();
        
        for (int i = 0; i < thisOccupied.length; i++) {
            for (int j = 0; j < thatOccupied.length; j++) {
                // if the coordinates occupied are the same 
                if (thisOccupied[i][0] == thatOccupied[j][0] && 
                    thisOccupied[i][1] == thatOccupied[j][1]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /* -------------------------------------------
     *                  METHODS
     * --------------------------------------------*/
    
    public boolean pressed(Point p) {
        boolean pressed = false;
        
        int xCoor = (int) p.getX();
        int yCoor = (int) p.getY();
        
        // gets the left and top edge of the block
        int leftBorder = column * 100 + 10;
        int topBorder = row * 100 + 10;
        
        // if the coordinate is in the bounds of the area of the block
        if (xCoor >= leftBorder && xCoor <= leftBorder + getWidth() && 
            yCoor >= topBorder && yCoor <= topBorder + getHeight()) {
            pressed = true;
        }
        
        return pressed; 
    }
    
    public void draw(Graphics g) {
        // set color to the color of the block
        g.setColor(color);
        
        // offset the area by 10 to make space for tiles
        int x = column * 100 + 10;
        int y = row * 100 + 10;
        
        g.fillRoundRect(x, y, getWidth(), getHeight(), 20, 20);
    }
    
    /* -------------------------------------------
     *                Comparable METHODS
     * --------------------------------------------*/
    
    
    // compares Block based on the character
    
    public int compareTo(Block otherBlock) {
        // if all fields have same value
        if (row == otherBlock.getRow() && column == otherBlock.getColumn() && 
           length == otherBlock.getLength() && id == otherBlock.getID() && 
            direction.equals(otherBlock.getDirection())) {
            return 0;
        } else if (id == otherBlock.getID()) {
            // if they have same ID but are different
            return -1;
        }
        
        // otherwise compare using the ID
        Character thisID = id;
        Character otherId = otherBlock.getID();
        return thisID.compareTo(otherId);
    }
    
    
    public String toString() {
        return "Block " + id + ": [Row = " + row + "; Column = " + column + 
            "; Length = " + length + "; Direction = " + direction + "]"; 
    }
    
    @Override
    public boolean equals(Object otherBlock) {
        if (this != null && otherBlock != null) {
            return this.toString().equals(otherBlock.toString());
        } else {
            return (otherBlock == null && this == null);
        }
        
    }
    
    
    
}
