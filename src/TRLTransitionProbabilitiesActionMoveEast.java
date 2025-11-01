/**
 * Concrete transition-probability builder for the "move east" primitive
 * action. Implements the direction-specific checks used by the generic
 * {@link ARLTransitionProbabilities#createTransitionProbabilitiesForAction} code.
 */
public class TRLTransitionProbabilitiesActionMoveEast extends ARLTransitionProbabilities {
	
	@Override
	protected boolean getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithWallAtDirectionOfAction(IRLCell aOriginCell) {
		return aOriginCell.getEastWall() != null && ( aOriginCell.getNorthWall() != null || aOriginCell.getSouthWall() != null ) ;
	}

	@Override
	protected boolean getAgentStaysAtTheSameCellWithWallAtDirectionOfAction(IRLCell aOriginCell) {
		return aOriginCell.getEastWall() != null;
	}

	@Override
	protected boolean getDestinationCellIsNeighborInTheDirectionOfAction(IRLCell aOriginCell, IRLCell aDestinationCell) {
		return aDestinationCell == aOriginCell.getEastCell();
	}
	

	@Override
	protected boolean getDestinationCellIsNeighborNotInTheDirectionOfAction(IRLCell aOriginCell, IRLCell aDestinationCell) {
		return  aDestinationCell == aOriginCell.getNorthCell() || aDestinationCell == aOriginCell.getSouthCell() || aDestinationCell == aOriginCell.getWestCell();
	}


	@Override
	protected boolean getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithoutWallAtDirectionOfAction(	IRLCell aOriginCell) {
		return aOriginCell.getEastWall() == null && aOriginCell.getWallCount() == 2;
	}
	
	@Override
	protected boolean isThereAWallBetweenCells(IRLCell aOriginCell, IRLCell aDestinationCell) {
		return aOriginCell.getEastWall() != null && aDestinationCell == aOriginCell.getEastCell();
	}
	
}
