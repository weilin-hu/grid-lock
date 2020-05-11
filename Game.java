/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    
    
    public void run() {
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("GRID LOCK");
        
        frame.setLocation(0, 0);

        // LOWER LEVEL PANEL
        final JPanel level_panel = new JPanel();
        frame.add(level_panel, BorderLayout.NORTH);
        
        
        final JLabel status = new JLabel("Welcome!");
        level_panel.add(status);
        
        
        
        // Main playing area
        Board board = new Board(status);
        
        
        
        /* --------------------------------
         *        Instruction Button
         * --------------------------------
         */
        final JPanel instructions_panel = new JPanel();
        
        instructions_panel.setLayout(new BoxLayout(instructions_panel, BoxLayout.Y_AXIS));
        
        final JLabel instruction = new JLabel("Instructions:");
        
        final JLabel instruction1 = new JLabel("Get the blue car to the other side by " + 
                                                "maneuvering the brown blocks with your mouse.");
            
        final JLabel instruction2 = new JLabel("*The blocks can only move in one direction!*");
        
        final JLabel instruction3 = new JLabel("Remember to save your progress when you're done,");
        final JLabel instruction4 = new JLabel("so you can come back to the game anytime" + 
                                                " you'd like or if you're stuck.");
        
        final JLabel instruction5 = new JLabel("Good luck!");
        
        instructions_panel.add(instruction);
        instructions_panel.add(instruction1);
        instructions_panel.add(instruction2);
        instructions_panel.add(instruction3);
        instructions_panel.add(instruction4);
        instructions_panel.add(instruction5);
        
        frame.add(instructions_panel, BorderLayout.SOUTH);
        
        /* --------------------------------
         *            Reset Button
         * --------------------------------
         */
        
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               board.reset();
            }
        });
        level_panel.add(reset);
        
        /* --------------------------------
         *        Save Button 
         * --------------------------------
         */
        final JButton save = new JButton("Save");
        level_panel.add(save);
        
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String currentFile = board.getCurrentFile();
                
                if (currentFile.equals("files/level1.txt")) {
                    board.writeToFile("files/level1_progress.txt");
                } else if (currentFile.equals("files/level2.txt")) {
                    board.writeToFile("files/level2_progress.txt");
                } else if (currentFile.equals("files/level3.txt")) {
                    board.writeToFile("files/level3_progress.txt");
                } else if (currentFile.equals("files/level4.txt")) {
                    board.writeToFile("files/level4_progress.txt");
                } else if (currentFile.equals("files/level5.txt")) {
                    board.writeToFile("files/level5_progress.txt");
                }
            }
        });
        
        
        
        final JLabel levelText = new JLabel("Level: ");
        level_panel.add(levelText, BorderLayout.SOUTH);
        
        // Level 1
        final JButton level1 = new JButton("1");
        level_panel.add(level1);
        level1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameState g = new GameState("files/level1_progress.txt");
                board.loadState(g);
            }
        });
        
        
        // Level 2
        final JButton level2 = new JButton("2");
        level_panel.add(level2);
        level2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameState g = new GameState("files/level2_progress.txt");
                board.loadState(g);
            }
        });
        
        // Level 3 
        final JButton level3 = new JButton("3");
        level_panel.add(level3);
        level3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameState g = new GameState("files/level3_progress.txt");
                board.loadState(g);
                
            }
        });
        
        
        // Level 4
        final JButton level4 = new JButton("4");
        level_panel.add(level4);
        level4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameState g = new GameState("files/level4_progress.txt");
                board.loadState(g);
            }
        });
        
        
        // Level 5
        final JButton level5 = new JButton("5");
        level_panel.add(level5);
        level5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameState g = new GameState("files/level5_progress.txt");
                board.loadState(g);
            }
        });
        
        frame.add(board, BorderLayout.CENTER);
        
        
        
        /* --------------------------------
         *        Frame
         * --------------------------------
         */
        
        // Put the frame on the screen
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        //court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
