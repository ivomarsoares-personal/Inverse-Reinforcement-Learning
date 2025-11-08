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
	private IRLInverseReinforcementLearning fInverseReinforcementLearning;
	
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
	
	@Override
	public java.util.List<IRLState> getStateList() {
		return fStateList;
	}
	
	@Override
	public IRLModelBased getModelBased() {
		return fModelBased;
	}
	
	@Override
	public void setModelBased(IRLModelBased aModelBased) {
		fModelBased = aModelBased;
	}
	
	@Override
	public IRLPolicy getPolicy() {
		return fPolicy;
	}

	@Override
	public void setPolicy(IRLPolicy aPolicy) {
		fPolicy = aPolicy;
		setChanged();
		notifyObservers();
	}
		
	@Override
	public IRLGrid getGrid() {
		return fGrid;
	}
	
	@Override
	public void setGrid(IRLGrid aGrid) {
		fGrid = aGrid;
	}

	@Override
	public IRLState getAbsorbingState() {
		return fAbsorbingState;
	}

	@Override
	public void setAbsorbingState(IRLState aAbsorbingState) {
		fAbsorbingState = aAbsorbingState;
		setChanged();
		notifyObservers();
	}

	@Override
	public RealMatrix getTPMatrixNorth() {
		return fTPMatrixNorth;
	}

	@Override
	public void setTPMatrixNorth(RealMatrix aTPMatrixNorth) {
		fTPMatrixNorth = aTPMatrixNorth;
	}

	@Override
	public RealMatrix getTPMatrixEast() {
		return fTPMatrixEast;
	}

	@Override
	public void setTPMatrixEast(RealMatrix aTPMatrixEast) {
		fTPMatrixEast = aTPMatrixEast;
	}

	@Override
	public RealMatrix getTPMatrixSouth() {
		return fTPMatrixSouth;
	}

	@Override
	public void setTPMatrixSouth(RealMatrix aTPMatrixSouth) {
		fTPMatrixSouth = aTPMatrixSouth;
	}

	@Override
	public RealMatrix getTPMatrixWest() {
		return fTPMatrixWest;
	}

	@Override
	public void setTPMatrixWest(RealMatrix aTPMatrixWest) {
		fTPMatrixWest = aTPMatrixWest;
	}
	
	@Override
	public IRLState getInitialState() {
		return fInitialState;
	}

	@Override
	public void setInitialState(IRLState aInitialState) {
		fInitialState = aInitialState;
	}
	
	@Override
	public IRLInverseReinforcementLearning getInverseReinforcementLearning() {
		return fInverseReinforcementLearning;
	}

	@Override
	public void setInverseReinforcementLearning(IRLInverseReinforcementLearning aInverseReinforcementLearning) {
		fInverseReinforcementLearning = aInverseReinforcementLearning;
	}

	
}
