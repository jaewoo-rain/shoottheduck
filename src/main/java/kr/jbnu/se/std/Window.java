package kr.jbnu.se.std;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties.
<<<<<<< HEAD
 * 
=======
 *
>>>>>>> a369ebdd66c2b3e8ce0c24290fcdb907423b67dd
 * @author www.gametutorial.net
 */

public class Window extends JFrame{
<<<<<<< HEAD
        
=======

>>>>>>> a369ebdd66c2b3e8ce0c24290fcdb907423b67dd
    private Window()
    {
        // Sets the title for this frame.
        this.setTitle("Shoot the duck");
<<<<<<< HEAD
        
=======

>>>>>>> a369ebdd66c2b3e8ce0c24290fcdb907423b67dd
        // Sets size of the frame.
        if(false) // Full screen mode
        {
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(this.MAXIMIZED_BOTH);
        }
        else // kr.jbnu.se.std.Window mode
        {
            // Size of the frame.
            this.setSize(800, 600);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }
<<<<<<< HEAD
        
        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setContentPane(new Framework());
        
=======

        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(new Framework());

>>>>>>> a369ebdd66c2b3e8ce0c24290fcdb907423b67dd
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> a369ebdd66c2b3e8ce0c24290fcdb907423b67dd
