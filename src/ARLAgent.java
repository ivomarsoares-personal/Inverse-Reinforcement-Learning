import java.util.ArrayList;
import java.util.Observable;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public abstract class ARLAgent extends Observable implements IRLAgent {
	
	protected int fId;
	
	private IRLGrid fGrid;
	private IRLPolicy                fPolicy;
	private java.util.List<IRLState> fStateList = new ArrayList<IRLState>();
	private IRLState		         fInitialState;


	private IRLState                 fAbsorbingState;
	private RealMatrix               fTPMatrixNorth;
	private RealMatrix               fTPMatrixEast;
	private RealMatrix               fTPMatrixSouth;
	private RealMatrix               fTPMatrixWest;
	
	private IRLModelBased            fModelBased;
	

	public IRLState retrieveState( final int aIndex ){
		
		for( int lStateIndex = 0; lStateIndex < fStateList.size(); lStateIndex++ ){
			
			final IRLState lState = fStateList.get(aIndex);
			if( lState.getCell().getIndex() == aIndex ){
				return lState;
			}
		}
		
		
		assert false;
		return null;
	}
	
	public IRLState retrieveState( final int aRowIndex, final int aColumnIndex ){
		
		for( int lStateIndex = 0; lStateIndex < fStateList.size(); lStateIndex++ ){
			
			final IRLState lState = fStateList.get(lStateIndex);
			final IRLCell lCell = lState.getCell();
			if( lCell.getRowIndex() == aRowIndex && lCell.getColumnIndex() == aColumnIndex ) {
				return lState;
			}
		}
		
		
		assert false;
		return null;
	}
	
	public java.util.List<IRLState> getStateList() {
		return fStateList;
	}
	
	public IRLModelBased getModelBased() {
		return fModelBased;
	}
	public void setModelBased(IRLModelBased aModelBased) {
		fModelBased = aModelBased;
	}
		
	
	public IRLPolicy getPolicy() {
		return fPolicy;
	}


	public void setPolicy(IRLPolicy aPolicy) {
		fPolicy = aPolicy;
		setChanged();
		notifyObservers();
	}
	
	
	public IRLGrid getGrid() {
		return fGrid;
	}
	
	public void setGrid(IRLGrid aGrid) {
		fGrid = aGrid;
	}

	public IRLState getAbsorbingState() {
		return fAbsorbingState;
	}

	public void setAbsorbingState(IRLState aAbsorbingState) {
		fAbsorbingState = aAbsorbingState;
		setChanged();
		notifyObservers();
	}
	

	
	public RealMatrix getTPMatrixNorth() {
		return fTPMatrixNorth;
	}

	public void setTPMatrixNorth(RealMatrix aTPMatrixNorth) {
		fTPMatrixNorth = aTPMatrixNorth;
	}

	public RealMatrix getTPMatrixEast() {
		return fTPMatrixEast;
	}

	public void setTPMatrixEast(RealMatrix aTPMatrixEast) {
		fTPMatrixEast = aTPMatrixEast;
	}

	public RealMatrix getTPMatrixSouth() {
		return fTPMatrixSouth;
	}

	public void setTPMatrixSouth(RealMatrix aTPMatrixSouth) {
		fTPMatrixSouth = aTPMatrixSouth;
	}

	public RealMatrix getTPMatrixWest() {
		return fTPMatrixWest;
	}

	public void setTPMatrixWest(RealMatrix aTPMatrixWest) {
		fTPMatrixWest = aTPMatrixWest;
	}
	public IRLState getInitialState() {
		return fInitialState;
	}

	public void setInitialState(IRLState aInitialState) {
		fInitialState = aInitialState;
	}
	
}
