
/**
 * Concrete representation of a wall used by the TRL grid implementation.
 * Walls are used to mark cell boundaries and affect transition probabilities
 * when the agent attempts actions that would cross a wall.
 */
public class TRLWall extends ARLWall {

	public static int sCOUNT = 0;
	
	public void TRWall() {
		fId = sCOUNT;
		sCOUNT++;		
	}
	
}
