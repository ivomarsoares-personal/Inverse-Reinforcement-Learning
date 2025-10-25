import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public abstract class ARLGrid extends Observable implements IRLGrid {
	
	private int fNumberOfRows;
	private int fNumberOfColumns;	
	private List<IRLCell> fCellList = new ArrayList<IRLCell>();
	private List<IRLWall> fWallList = new ArrayList<IRLWall>();
	
	private IRLRewardFunction fRewardFunction;

	private RealMatrix fTransitionProbabilityMatrixMoveNorth;
	private RealMatrix fTransitionProbabilityMatrixMoveEast;
	private RealMatrix fTransitionProbabilityMatrixMoveSouth;
	private RealMatrix fTransitionProbabilityMatrixMoveWest;
	

	public int getNumberOfRows() {
		return fNumberOfRows;
	}

	public RealMatrix getTransitionProbabilityMatrixMoveNorth() {
		return fTransitionProbabilityMatrixMoveNorth;
	}

	public void setTransitionProbabilityMatrixMoveNorth(
			RealMatrix aTransitionProbabilityMatrixMoveNorth) {
		fTransitionProbabilityMatrixMoveNorth = aTransitionProbabilityMatrixMoveNorth;
	}

	public RealMatrix getTransitionProbabilityMatrixMoveEast() {
		return fTransitionProbabilityMatrixMoveEast;
	}

	public void setTransitionProbabilityMatrixMoveEast(
			RealMatrix aTransitionProbabilityMatrixMoveEast) {
		fTransitionProbabilityMatrixMoveEast = aTransitionProbabilityMatrixMoveEast;
	}

	public RealMatrix getTransitionProbabilityMatrixMoveSouth() {
		return fTransitionProbabilityMatrixMoveSouth;
	}

	public void setTransitionProbabilityMatrixMoveSouth(
			RealMatrix aTransitionProbabilityMatrixMoveSouth) {
		fTransitionProbabilityMatrixMoveSouth = aTransitionProbabilityMatrixMoveSouth;
	}

	public RealMatrix getTransitionProbabilityMatrixMoveWest() {
		return fTransitionProbabilityMatrixMoveWest;
	}

	public void setTransitionProbabilityMatrixMoveWest(
			RealMatrix aTransitionProbabilityMatrixMoveWest) {
		fTransitionProbabilityMatrixMoveWest = aTransitionProbabilityMatrixMoveWest;
	}

	public void setNumberOfRows(int aNumberOfRows) {
		fNumberOfRows = aNumberOfRows;
		setChanged();
		notifyObservers();
	}

	public int getNumberOfColumns() {
		return fNumberOfColumns;
	}

	public void setNumberOfColumns(int aNumberOfColumns) {
		fNumberOfColumns = aNumberOfColumns;
		setChanged();
		notifyObservers();
	}

	public List<IRLCell> getCellList() {
		return fCellList;
	}
	
	public List<IRLWall> getWallList() {
		return fWallList;
	}
	
	public IRLRewardFunction getRewardFunction() {
		return fRewardFunction;
	}

	public void setRewardFunction(IRLRewardFunction aRewardFunction) {
		fRewardFunction = aRewardFunction;
	}
	

}
