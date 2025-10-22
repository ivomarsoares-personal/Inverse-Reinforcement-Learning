import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Abstract helper that constructs a transition probability matrix for a
 * particular primitive action. Concrete subclasses implement the direction-
 * specific predicates; the shared method {@link #createTransitionProbabilitiesForAction}
 * implements the common probabilistic model (correct action vs noise).
 */
public abstract class ARLTransitionProbabilities implements IRLTransitionProbability {
	
	protected abstract boolean getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithWallAtDirectionOfAction( IRLCell aOriginCell );
	protected abstract boolean getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithoutWallAtDirectionOfAction(IRLCell aOriginCell);
	protected abstract boolean getAgentStaysAtTheSameCellWithWallAtDirectionOfAction(IRLCell aOriginCell);
	protected abstract boolean getDestinationCellIsNeighborInTheDirectionOfAction(IRLCell aOriginCell, IRLCell aDestinationCell);
	protected abstract boolean getDestinationCellIsNeighborNotInTheDirectionOfAction(IRLCell aOriginCell, IRLCell aDestinationCell);

	public RealMatrix createTransitionProbabilitiesForAction( 
			final double aCorrectActionProbability, 
			final double aActionNoiseProbability, 
			final IRLAgent aAgent ){
		assert Math.abs(aCorrectActionProbability + aActionNoiseProbability -1 ) < 0.00001; 

		IRLGrid lGrid = aAgent.getGrid();
		
		List<IRLCell> lCellList = lGrid.getCellList();
		int lCellListSize = lCellList.size();

		int lNumberOfRows    = lCellListSize;
		int lNumberOfColumns = lCellListSize;

		RealMatrix lTransitionProbabilitiesMatrix = MatrixUtils.createRealMatrix( lCellListSize, lCellListSize);

		double lProbability = -1;

		for( int lTPRowIndex = 0; lTPRowIndex < lNumberOfRows; lTPRowIndex++ ){

			final IRLCell lOriginCell = TRLGridUtil.getSharedInstance().retrieveCell(lTPRowIndex, lGrid);


			IRLWall lOriginNorthWall = lOriginCell.getNorthWall();
			IRLWall lOriginEastWall  = lOriginCell.getEastWall();
			IRLWall lOriginSouthWall = lOriginCell.getSouthWall();
			IRLWall lOriginWestWall  = lOriginCell.getWestWall();


			for( int lTPColumnIndex = 0; lTPColumnIndex < lNumberOfColumns; lTPColumnIndex++ ){

				final IRLCell lDestinationCell = TRLGridUtil.getSharedInstance().retrieveCell(lTPColumnIndex, lGrid);

//				// Absorbing state
				if( aAgent.getAbsorbingState().getCell() == lOriginCell  ){
					
					if( lOriginCell == lDestinationCell ){
						lProbability = 1; // Absorbing state
					}
					else {
						lProbability = 0;
					}
				}
				

				// Agent origin and destination cell are the same.
				else if( lOriginCell == lDestinationCell ){
					if( getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithWallAtDirectionOfAction(lOriginCell) ){
						lProbability = aCorrectActionProbability + aActionNoiseProbability * 0.5;
					}
					else if( getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithoutWallAtDirectionOfAction(lOriginCell)  ){
						lProbability = aActionNoiseProbability * 0.5;
					}
					
					else if( lOriginNorthWall == null && lOriginEastWall == null && lOriginSouthWall == null && lOriginWestWall == null ){
						lProbability = 0; // No walls surrounding, it never stays on the origin cell.
					}
					else if( getAgentStaysAtTheSameCellWithWallAtDirectionOfAction(lOriginCell) ) {
						lProbability = aCorrectActionProbability + aActionNoiseProbability * 0.25;	
					}
					else{
						lProbability = aActionNoiseProbability * 0.25;
					}
				}
				// Destination cell is not a neighbor of origin cell.
				else if( ! lOriginCell.isNeighboor(lDestinationCell) ){
					lProbability = 0;
				}

				// Destination cell is a neighbor.
				else {

					assert lOriginCell.isNeighboor(lDestinationCell);

					if( getDestinationCellIsNeighborInTheDirectionOfAction(lOriginCell, lDestinationCell)  ){
						lProbability = aCorrectActionProbability + aActionNoiseProbability * 0.25;
					}
					else if( getDestinationCellIsNeighborNotInTheDirectionOfAction(lOriginCell, lDestinationCell) ){
						lProbability = aActionNoiseProbability * 0.25;
					}
				}

				lTransitionProbabilitiesMatrix.setEntry(lTPRowIndex, lTPColumnIndex, lProbability);
			}
		}

		return lTransitionProbabilitiesMatrix;
	}
	
}
