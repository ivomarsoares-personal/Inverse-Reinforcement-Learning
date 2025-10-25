import org.apache.commons.math3.linear.RealMatrix;

/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public interface IRLAgent extends IRL {

	
	public IRLGrid getGrid();
	public void setGrid(IRLGrid aGrid);
	
	public IRLPolicy getPolicy();
	public void setPolicy(IRLPolicy aPolicy);
	
	public java.util.List<IRLState> getStateList();
	public IRLState retrieveState( final int aIndex );
	public IRLState retrieveState( final int aRowIndex, final int aColumnIndex );
	public IRLState getAbsorbingState();
	public void setAbsorbingState(IRLState aAbsorbingState);
	public Double getDiscountingFactor();
	public void setDiscountingFactor(Double aDiscountingFactor);
	public Double getCorrectActionProbability();
	public void setCorrectActionProbability(Double fCorrectActionProbability);
	public Double getActionNoiseProbability();
	public void setActionNoiseProbability(Double fActionNoiseProbability);
	public RealMatrix getTPMatrixNorth();
	public void setTPMatrixNorth(RealMatrix aTPMatrixNorth);
	public RealMatrix getTPMatrixEast();
	public void setTPMatrixEast(RealMatrix aTPMatrixEast);
	public RealMatrix getTPMatrixSouth();
	public void setTPMatrixSouth(RealMatrix aTPMatrixSouth);
	public RealMatrix getTPMatrixWest();
	public void setTPMatrixWest(RealMatrix aTPMatrixWest);
	public IRLState getInitialState();
	public void setInitialState(IRLState aInitialState);
	
}
