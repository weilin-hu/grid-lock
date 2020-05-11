import java.util.*;
import java.io.*;


/* FileIterator class 
 * Iterator through a file
 * 
 * 
 * @param next - the next character stored as an integer
 * @param reader - filereader that iterates and reads through the file
 * 
 */
public class FileIterator implements Iterator<Integer> {
    // this holds the next character in the iteration
    private Integer next;
    // this is the reader than reads through the file
    private FileReader reader;
    
    /* Creates a File Iterator that iterates over the given path
     * @param filePath is the path of the file to be read
     */
    public FileIterator(String filePath) {
        try { 
            reader = new FileReader(filePath);
            // finds integer value of first character in file
            next = reader.read();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Invalid File @notFoundException!"); 
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid File @nullException!");
        } catch (IOException e) {
            next = null;
        }
    }
    
    @Override
    public Integer next() {
        // throw exception if there are no more characters in the file to read
        if (next == null || next.intValue() == -1) {
            throw new NoSuchElementException("No more characters to read!");
        }
        Integer ret = next;
        try {
            next = reader.read();
        } catch (IOException e) {
            System.out.println("Can't read file!");
        }
        return ret;
    }
    
    @Override
    public boolean hasNext() {
        // if next is null of the next character read has value -1
        if (next == null || next.intValue() == -1) {
            if (reader != null) {
                try {
                    // close the reader
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error occurred when trying to close reader!");
                }
            }
            // no next element
            return false;
        }
        // else there is next character
        return true;
    }
    
}
