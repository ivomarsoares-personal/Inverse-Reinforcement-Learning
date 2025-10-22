/**
 * Concrete transition-probability builder for the "move north" primitive
 * action used by the TRL agent. Implements direction-specific checks used by
 * {@link ARLTransitionProbabilities}.
 */
public class TRLTransitionProbabilitiesActionMoveNorth extends ARLTransitionProbabilities {


	@Override
	protected boolean getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithWallAtDirectionOfAction(IRLCell aOriginCell) {
		return aOriginCell.getNorthWall() != null && ( aOriginCell.getWestWall() != null || aOriginCell.getEastWall() != null ) ;
	}

	@Override
	protected boolean getAgentStaysAtTheSameCellWithWallAtDirectionOfAction(IRLCell aOriginCell) {
		return aOriginCell.getNorthWall() != null;
	}
	
	@Override
	protected boolean getDestinationCellIsNeighborInTheDirectionOfAction(IRLCell aOriginCell, IRLCell aDestinationCell) {
		return aDestinationCell == aOriginCell.getNorthCell();
	}

	@Override
	protected boolean getDestinationCellIsNeighborNotInTheDirectionOfAction(IRLCell aOriginCell, IRLCell aDestinationCell) {
		return  aDestinationCell == aOriginCell.getEastCell() || aDestinationCell == aOriginCell.getSouthCell() || aDestinationCell == aOriginCell.getWestCell();
	}

	@Override
	protected boolean getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithoutWallAtDirectionOfAction(	IRLCell aOriginCell) {
		return aOriginCell.getNorthWall() == null && aOriginCell.getWallCount() == 2;
	}
}
