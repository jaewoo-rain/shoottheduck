package kr.jbnu.se.std;


import kr.jbnu.se.std.firebase.MainFrame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Window extends JFrame{

    public Window(String id)
    {
        // Sets the title for this frame.
        this.setTitle("Shoot the duck");

        new User(id);


        // Size of the frame.
        this.setSize(800, 600);
        // Puts frame to center of the screen.
        this.setLocationRelativeTo(null);
        // So that frame cannot be resizable by the user.
        this.setResizable(false);


        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(new Framework());

        this.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });

    }
}
