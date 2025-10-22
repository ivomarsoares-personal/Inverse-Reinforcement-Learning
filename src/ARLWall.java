
/**
 * Abstract base for wall objects used by grids. Concrete implementations may
 * add visualization or metadata, but a wall primarily marks a blocked edge
 * between adjacent cells.
 */
public abstract class ARLWall implements IRLWall{

	private int fInitialVerticeXCoordinate;
	private int fInitialVerticeYCoordinate;
	private int fFinalVerticeXCoordinate;
	private int fFinalVerticeYCoordinate;
	
	
	public int getfInitialVerticeXCoordinate() {
		return fInitialVerticeXCoordinate;
	}
	public void setfInitialVerticeXCoordinate(int fInitialVerticeXCoordinate) {
		this.fInitialVerticeXCoordinate = fInitialVerticeXCoordinate;
	}
	public int getfInitialVerticeYCoordinate() {
		return fInitialVerticeYCoordinate;
	}
	public void setfInitialVerticeYCoordinate(int fInitialVerticeYCoordinate) {
		this.fInitialVerticeYCoordinate = fInitialVerticeYCoordinate;
	}
	public int getfFinalVerticeXCoordinate() {
		return fFinalVerticeXCoordinate;
	}
	public void setfFinalVerticeXCoordinate(int fFinalVerticeXCoordinate) {
		this.fFinalVerticeXCoordinate = fFinalVerticeXCoordinate;
	}
	public int getfFinalVerticeYCoordinate() {
		return fFinalVerticeYCoordinate;
	}
	public void setfFinalVerticeYCoordinate(int fFinalVerticeYCoordinate) {
		this.fFinalVerticeYCoordinate = fFinalVerticeYCoordinate;
	}
		
	
}
