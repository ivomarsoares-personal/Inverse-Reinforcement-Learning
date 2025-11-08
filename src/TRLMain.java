import javax.swing.UIManager;

/**
 * 
 * Thanks for your interest in this source code, you are welcome to send me any suggestion, feedback or criticism.
 * Ivomar Brito Soares, ivomarbsoares@gmail.com.

 * @author Ivomar Brito Soares
 *
 */
public class TRLMain {

	public static void main( String[] args ){
        try {
            // Use the OS-native look and feel (Windows, macOS, GTK, etc.)
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }


		new TRLMainFrame( );
	}

}

