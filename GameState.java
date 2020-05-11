import java.util.*;
import java.awt.*;
import javax.swing.*;


    /*  Middleman between the File and the Board
     *  constructs the blocks given the file to be used to create the 2-D array state
     *  in the block method
     */

public class GameState {
    private Map<Character, Block> idToBlocks; // maps the id to the block
    
    private String filePath; // holds the file that was read
    /* 
     * Constructor 
     */
    public GameState (String filePath) {
        this.filePath = filePath;
        
        // column keeps track of which column we the file we are reading into
        idToBlocks = new TreeMap<Character, Block>();
        
        // create an instance of an iterator to iterator through the given file
        FileIterator iter = new FileIterator(filePath);
        
        int column = 0;
        int row = 0;
        
        
        
        // while we are not done iterating through the file...
        while (iter.hasNext()) {
            /* @next keeps track of the next character as an integer value
             * @nextChar keeps track of the next character as a character
             * @currLine keeps track of the current Line of iteration
             */
            int next = iter.next().intValue();
            char nextChar = (char) next;
            
            // throw exception if read past 6 lines
            if (row > 5) {
                throw new IllegalArgumentException("Board has invalid row dimensions!");
            }
            
            // if next has reached end of row, it starts at the 0th column again
            if (next == 10) {
                if (column < 6) {
                    throw new IllegalArgumentException("Board has invalid column dimensions!");
                }
                column = 0;
                row = row + 1;
                // else if next is a character 'a' - 'z'
            } else if (next >= 97 && next <= 122) {
                
                // if there is already a block of such id
                if (idToBlocks.containsKey(nextChar)) {
                    // obtain a reference to that block and the current length of it
                    Block currentBlock = idToBlocks.get(nextChar);
                    int currLength = currentBlock.getLength();
                    
                    // if we find the same id value in the same row
                    if (currentBlock.getRow() == row) {
                        if (currentBlock.getColumn() + currentBlock.getLength() == column) {
                            // then that means the direction of the block is Horizontal
                            currentBlock.updateDirection("H");
                        } else {
                            throw new IllegalArgumentException("Block has invalid column location");
                        }
                            
                    } else if (currentBlock.getColumn() == column) {
                        if (currentBlock.getRow() + currentBlock.getLength() != row) {
                            throw new IllegalArgumentException("Block has invalid row locations!");
                        }
                    } else {
                        throw new IllegalArgumentException("Block has invalid locations!");
                    }
                    
                    // if length + 1 is valid 
                    if (currLength + 1 < 4) {
                        // update +1 to the length
                        currentBlock.changeLength(currentBlock.getLength() + 1);
                        
                    } else {
                        // else we cannot create such a state so throw an exception
                        throw new IllegalArgumentException("Block has invalid parameters!");
                    }
                    // if the character is a new ID 
                } else {
                    Block b;
                    // create a new block of the current iteration row and column with size 1
                    // with vertical type and the character read as the id
                    if (next == 120) {
                        b = new Block(row, column, 1, "H", nextChar);
                    } else {
                        b = new Block(row, column, 1, "V", nextChar);
                    }
                    
                    // add it to the map
                    idToBlocks.put(nextChar, b);
                }
                // increment column
                column ++;
            } else if (next == 46) {
                // increment column
                column ++;
            } else {
                throw new IllegalArgumentException("Invalid character read!");
                
            }
            
        }
            
    }
    
    // gets the filePath of the state 
    public String getFilePath() {
        return filePath;
    }
    
    // gets the char[][] of the state of the game 
    public Block[][] getState() {
        Block[][] ret = new Block[6][6];
        Collection<Block> blocks = getBlocks();
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                for (Block block : blocks) {
                    if (block.pressed(new Point(c * 100 + 50, r * 100 + 50))) {
                        ret[r][c] = block;
                        break;
                    }
                }
                
            }
        }
        
        return ret;
    }
    
    // gets the set of block IDS currently in game
    public Set<Character> getIDS() {
        Set<Character> ids = idToBlocks.keySet();
        Set<Character> ret = new TreeSet<Character>();
        
        for (Character id: ids) {
            ret.add(id);
        }
        return ret;
    }
    
    // gets the blocks currently in game
    public Collection<Block> getBlocks() {
        Collection<Block> blocks = idToBlocks.values();
        Set<Block> ret = new TreeSet<Block>();
        
        for (Block block: blocks) {
            ret.add(block);
        }
        return ret;
    }
    
    // gets the Block associated with the id
    public Block getBlockFromID(char id) {
        Block b = null;
        
        Collection<Block> blocks = getBlocks();
        for (Block block : blocks) {
            if (block.getID() == id) {
                b = block;
            }
        }
        
        return b;
    }
    
    // gets driver row location 
    public int getDriverRow() {
        if (idToBlocks.containsKey('x')) {
            Block driver = idToBlocks.get('x');
            return driver.getRow();
        }
        return -1;
    }
    
    // gets driver column location 
    public int getDriverColumn() {
        if (idToBlocks.containsKey('x')) {
            Block driver = idToBlocks.get('x');
            return driver.getColumn();
        }
        return -1;
    }
}
    
