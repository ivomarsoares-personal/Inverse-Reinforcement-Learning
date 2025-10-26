import java.util.ArrayList;
import java.util.List;

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
	
	private List<IRLCell> fNorthCellsList = new ArrayList<IRLCell>();
	private List<IRLCell> fEastCellsList = new ArrayList<IRLCell>();
	private List<IRLCell> fWestCellsList = new ArrayList<IRLCell>();
	private List<IRLCell> fSouthCellsList = new ArrayList<IRLCell>();
	
	
	
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
	public List<IRLCell> getNorthCellsList() {
		return fNorthCellsList;
	}
	public void setNorthCellsList(List<IRLCell> fNorthCellsList) {
		this.fNorthCellsList = fNorthCellsList;
	}
	public List<IRLCell> getEastCellsList() {
		return fEastCellsList;
	}
	public void setEastCellsList(List<IRLCell> fEastCellsList) {
		this.fEastCellsList = fEastCellsList;
	}
	public List<IRLCell> getWestCellsList() {
		return fWestCellsList;
	}
	public void setWestCellsList(List<IRLCell> fWestCellsList) {
		this.fWestCellsList = fWestCellsList;
	}
	public List<IRLCell> getSouthCellsList() {
		return fSouthCellsList;
	}
	public void setSouthCellsList(List<IRLCell> fSouthCellsList) {
		this.fSouthCellsList = fSouthCellsList;
	}		
	
}
