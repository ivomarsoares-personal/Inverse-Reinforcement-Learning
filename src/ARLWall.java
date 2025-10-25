
/**
 * Abstract base for wall objects used by grids. Concrete implementations may
 * add visualization or metadata, but a wall primarily marks a blocked edge
 * between adjacent cells.
 */
public abstract class ARLWall implements IRLWall{

	protected int fId;
	private int fInitialVerticeXCoordinate;
	private int fInitialVerticeYCoordinate;
	private int fFinalVerticeXCoordinate;
	private int fFinalVerticeYCoordinate;
	
	
	public int getInitialVerticeXCoordinate() {
		return fInitialVerticeXCoordinate;
	}
	public void setInitialVerticeXCoordinate(int fInitialVerticeXCoordinate) {
		this.fInitialVerticeXCoordinate = fInitialVerticeXCoordinate;
	}
	public int getInitialVerticeYCoordinate() {
		return fInitialVerticeYCoordinate;
	}
	public void setInitialVerticeYCoordinate(int fInitialVerticeYCoordinate) {
		this.fInitialVerticeYCoordinate = fInitialVerticeYCoordinate;
	}
	public int getFinalVerticeXCoordinate() {
		return fFinalVerticeXCoordinate;
	}
	public void setFinalVerticeXCoordinate(int fFinalVerticeXCoordinate) {
		this.fFinalVerticeXCoordinate = fFinalVerticeXCoordinate;
	}
	public int getFinalVerticeYCoordinate() {
		return fFinalVerticeYCoordinate;
	}
	public void setFinalVerticeYCoordinate(int fFinalVerticeYCoordinate) {
		this.fFinalVerticeYCoordinate = fFinalVerticeYCoordinate;
	}
		
	
}
