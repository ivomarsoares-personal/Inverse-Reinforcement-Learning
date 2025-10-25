/**
 * Concrete agent implementation for the TRL environment. Inherits behavior
 * and storage from {@link ARLAgent} and does not add behavior beyond
 * providing the concrete type expected by factories and utilities.
 */
public class TRLAgent extends ARLAgent {

	public static int COUNT = 0;
	
	public void TRWall() {
		fId = COUNT;
		COUNT++;		
	}
	
}
