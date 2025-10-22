import org.apache.commons.math3.linear.RealMatrix;

/**
 * Helper / factory utilities for creating and wiring up {@link IRLAgent}
 * instances in the concrete TRL implementation.
 *
 * Responsibilities:
 * - Construct agent state/action structures for a given grid.
 * - Create and install transition probability matrices for each primitive action
 *   using the concrete TRL transition probability builders.
 */
public class TRLAgentUtil {

	private static final TRLAgentUtil sSharedInstance = new TRLAgentUtil();
	public static TRLAgentUtil getSharedInstance(){
		return sSharedInstance;
	}	
	private TRLAgentUtil(){}
	
	
	public IRLAgent createAgent( final IRLGrid aGrid, final Integer aInitialStateIndex, final Integer aAbsorbingStateIndex, final Double aCorrectActionProbability, final Double aActionNoiseProbability ){
		
		IRLAgent lAgent = (IRLAgent) TRLFactory.createRLObject(IRLObject.sAGENT);
		lAgent.setGrid(aGrid);
	
		
		for(int lCellIndex = 0; lCellIndex < aGrid.getCellList().size(); lCellIndex++ ){
		
			IRLCell lCell = aGrid.getCellList().get(lCellIndex);
			IRLState lState = (IRLState) TRLFactory.createRLObject(IRLObject.sSTATE);
			lState.setCell(lCell);
			lAgent.getStateList().add(lState);
			
			if( aInitialStateIndex == lCellIndex ){
				lState.setInitial(Boolean.TRUE);
				lAgent.setInitialState(lState);
			}
			
			if( aAbsorbingStateIndex == lCellIndex ){
				lState.setAbsorbing(Boolean.TRUE);
				lAgent.setAbsorbingState(lState);
			}
			
			IRLAction lActionNorth = (IRLAction) TRLFactory.createRLObject(IRLObject.sACTION_MOVE_NORTH);
			lActionNorth.setID(IRLObject.sACTION_MOVE_NORTH);
			
			IRLAction lActionEast  = (IRLAction) TRLFactory.createRLObject(IRLObject.sACTION_MOVE_EAST);
			lActionEast.setID(IRLObject.sACTION_MOVE_EAST);
			
			IRLAction lActionSouth = (IRLAction) TRLFactory.createRLObject(IRLObject.sACTION_MOVE_SOUTH);
			lActionSouth.setID(IRLObject.sACTION_MOVE_SOUTH);
			
			IRLAction lActionWest  = (IRLAction) TRLFactory.createRLObject(IRLObject.sACTION_MOVE_WEST);
			lActionWest.setID(IRLObject.sACTION_MOVE_WEST);
		
			lState.getActionList().add(lActionNorth);
			lState.getActionList().add(lActionEast);
			lState.getActionList().add(lActionSouth);
			lState.getActionList().add(lActionWest);
		}
		
		createTransitionProbabilities(lAgent, aCorrectActionProbability, aActionNoiseProbability);
		return lAgent;
	}
	
	public void createTransitionProbabilities( final IRLAgent aAgent, final Double aCorrectActionProbability, final Double aActionNoiseProbability ){
		
		TRLTransitionProbabilitiesActionMoveNorth lTPActionNorth = new TRLTransitionProbabilitiesActionMoveNorth();
		TRLTransitionProbabilitiesActionMoveEast  lTPActionEast  = new TRLTransitionProbabilitiesActionMoveEast();
		TRLTransitionProbabilitiesActionMoveSouth lTPActionSouth = new TRLTransitionProbabilitiesActionMoveSouth();
		TRLTransitionProbabilitiesActionMoveWest  lTPActionWest  = new TRLTransitionProbabilitiesActionMoveWest();

		RealMatrix lTransitionProbabilitiesActionNorthMatrix = lTPActionNorth.createTransitionProbabilitiesForAction(aCorrectActionProbability, aActionNoiseProbability, aAgent);
		RealMatrix lTransitionProbabilitiesActionEastMatrix  = lTPActionEast.createTransitionProbabilitiesForAction (aCorrectActionProbability, aActionNoiseProbability, aAgent);
		RealMatrix lTransitionProbabilitiesActionSouthMatrix = lTPActionSouth.createTransitionProbabilitiesForAction(aCorrectActionProbability, aActionNoiseProbability, aAgent);
		RealMatrix lTransitionProbabilitiesActionWestMatrix  = lTPActionWest.createTransitionProbabilitiesForAction( aCorrectActionProbability, aActionNoiseProbability, aAgent);
		
		aAgent.setTPMatrixNorth(lTransitionProbabilitiesActionNorthMatrix);
		aAgent.setTPMatrixEast(lTransitionProbabilitiesActionEastMatrix);
		aAgent.setTPMatrixSouth(lTransitionProbabilitiesActionSouthMatrix);
		aAgent.setTPMatrixWest(lTransitionProbabilitiesActionWestMatrix);
		
	}
	
	
	
	
}
