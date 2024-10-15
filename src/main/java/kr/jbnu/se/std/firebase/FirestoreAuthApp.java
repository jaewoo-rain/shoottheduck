//package kr.jbnu.se.std.firebase;
//
//import kr.jbnu.se.std.Framework;
//
//import javax.swing.JFrame;
//import javax.swing.SwingUtilities;
//
//
//public class FirestoreAuthApp extends JFrame{
//
//    FirestoreAuthApp()
//    {
//        // Sets the title for this frame.
//        this.setTitle("Shoot the duck");
//
//        // Sets size of the frame.
//        if(false) // Full screen mode
//        {
//            // Disables decorations for this frame.
//            this.setUndecorated(true);
//            // Puts the frame to full screen.
//            this.setExtendedState(this.MAXIMIZED_BOTH);
//        }
//        else // kr.jbnu.se.std.Window mode
//        {
//            // Size of the frame.
//            this.setSize(800, 600);
//            // Puts frame to center of the screen.
//            this.setLocationRelativeTo(null);
//            // So that frame cannot be resizable by the user.
//            this.setResizable(false);
//        }
//
//        // Exit the application when user close frame.
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        this.setContentPane(new Framework());
//
//        this.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//
//        SwingUtilities.invokeLater(() -> {
//            MainFrame mainFrame = new MainFrame();
//            mainFrame.setVisible(true);
//        });
//
//
//    }
//}
