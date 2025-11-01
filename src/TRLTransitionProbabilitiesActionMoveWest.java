
/**
 * Concrete transition-probability builder for the "move west" primitive
 * action. Implements predicate methods consulted by the generic
 * {@link ARLTransitionProbabilities#createTransitionProbabilitiesForAction}.
 */
public class TRLTransitionProbabilitiesActionMoveWest extends ARLTransitionProbabilities {

	@Override
	protected boolean getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithWallAtDirectionOfAction(IRLCell aOriginCell) {
		return aOriginCell.getWestWall() != null && ( aOriginCell.getNorthWall() != null || aOriginCell.getSouthWall() != null ) ;
	}

	@Override
	protected boolean getAgentStaysAtTheSameCellWithWallAtDirectionOfAction(IRLCell aOriginCell) {
		return aOriginCell.getWestWall() != null;
	}

	@Override
	protected boolean getDestinationCellIsNeighborInTheDirectionOfAction(IRLCell aOriginCell, IRLCell aDestinationCell) {
		return aDestinationCell == aOriginCell.getWestCell();
	}

	@Override
	protected boolean getDestinationCellIsNeighborNotInTheDirectionOfAction(IRLCell aOriginCell, IRLCell aDestinationCell) {
		return  aDestinationCell == aOriginCell.getNorthCell() || aDestinationCell == aOriginCell.getSouthCell() || aDestinationCell == aOriginCell.getEastCell();
	}
	
	@Override
	protected boolean getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithoutWallAtDirectionOfAction(	IRLCell aOriginCell) {
		return aOriginCell.getWestWall() == null && aOriginCell.getWallCount() == 2;
	}
	
	@Override
	protected boolean isThereAWallBetweenCells(IRLCell aOriginCell, IRLCell aDestinationCell) {
		return aOriginCell.getWestWall() != null && aDestinationCell == aOriginCell.getWestCell();
	}

}
