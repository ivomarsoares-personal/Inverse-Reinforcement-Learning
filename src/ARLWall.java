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

	@Override
	public int getId() {
		return fId;
	}
	
	@Override
	public void setId(int aId) {
		fId = aId;
	}	
	
	@Override
	public int getInitialVerticeXCoordinate() {
		return fInitialVerticeXCoordinate;
	}
	
	@Override
	public void setInitialVerticeXCoordinate(int fInitialVerticeXCoordinate) {
		this.fInitialVerticeXCoordinate = fInitialVerticeXCoordinate;
	}
	
	@Override
	public int getInitialVerticeYCoordinate() {
		return fInitialVerticeYCoordinate;
	}
	
	@Override
	public void setInitialVerticeYCoordinate(int fInitialVerticeYCoordinate) {
		this.fInitialVerticeYCoordinate = fInitialVerticeYCoordinate;
	}
	
	@Override
	public int getFinalVerticeXCoordinate() {
		return fFinalVerticeXCoordinate;
	}
	
	@Override
	public void setFinalVerticeXCoordinate(int fFinalVerticeXCoordinate) {
		this.fFinalVerticeXCoordinate = fFinalVerticeXCoordinate;
	}
	
	@Override
	public int getFinalVerticeYCoordinate() {
		return fFinalVerticeYCoordinate;
	}
	
	@Override
	public void setFinalVerticeYCoordinate(int fFinalVerticeYCoordinate) {
		this.fFinalVerticeYCoordinate = fFinalVerticeYCoordinate;
	}
	
	@Override
	public List<IRLCell> getNorthCellsList() {
		return fNorthCellsList;
	}
	
	@Override
	public void setNorthCellsList(List<IRLCell> fNorthCellsList) {
		this.fNorthCellsList = fNorthCellsList;
	}
	
	@Override
	public List<IRLCell> getEastCellsList() {
		return fEastCellsList;
	}
	
	@Override
	public void setEastCellsList(List<IRLCell> fEastCellsList) {
		this.fEastCellsList = fEastCellsList;
	}
	
	@Override
	public List<IRLCell> getWestCellsList() {
		return fWestCellsList;
	}
	
	@Override
	public void setWestCellsList(List<IRLCell> fWestCellsList) {
		this.fWestCellsList = fWestCellsList;
	}
	
	@Override
	public List<IRLCell> getSouthCellsList() {
		return fSouthCellsList;
	}
	
	@Override
	public void setSouthCellsList(List<IRLCell> fSouthCellsList) {
		this.fSouthCellsList = fSouthCellsList;
	}			
}
