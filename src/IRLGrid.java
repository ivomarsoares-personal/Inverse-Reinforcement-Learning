import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public interface IRLGrid extends IRLGridComponent{
	public List<IRLCell> getCellList();


	public int getNumberOfRows();
	public void setNumberOfRows(int aNumberOfRows);
	public int getNumberOfColumns();
	public void setNumberOfColumns(int aNumberOfColumns);
	
	public RealMatrix getTransitionProbabilityMatrixMoveNorth();
	public void setTransitionProbabilityMatrixMoveNorth(RealMatrix aTransitionProbabilityMatrixMoveNorth);

	public RealMatrix getTransitionProbabilityMatrixMoveEast();
	public void setTransitionProbabilityMatrixMoveEast(RealMatrix aTransitionProbabilityMatrixMoveEast);

	public RealMatrix getTransitionProbabilityMatrixMoveSouth();
	public void setTransitionProbabilityMatrixMoveSouth(RealMatrix aTransitionProbabilityMatrixMoveSouth);

	public RealMatrix getTransitionProbabilityMatrixMoveWest();
	public void setTransitionProbabilityMatrixMoveWest(	RealMatrix aTransitionProbabilityMatrixMoveWest);
	
	public IRLRewardFunction getRewardFunction();

	public void setRewardFunction(IRLRewardFunction aRewardFunction);
	public List<IRLWall> getWallList();

}
