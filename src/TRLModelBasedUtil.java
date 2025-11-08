import org.apache.commons.math3.linear.RealMatrix;

public class TRLModelBasedUtil {

	private static final TRLModelBasedUtil sSharedInstance = new TRLModelBasedUtil();
	public static TRLModelBasedUtil getSharedInstance(){
		return sSharedInstance;
	}	
	private TRLModelBasedUtil(){}
	
	public IRLValueIteration createValueIteration(final double aDiscountingFactor, final double aCorrectActionProbability, final double aActionNoiseProbability){
		
		IRLValueIteration lValueIteration = (IRLValueIteration) TRLFactory.createRLObject(IRLObject.sMODEL_BASED_VALUE_ITERATION);
		lValueIteration.setDiscountingFactor(aDiscountingFactor);
		lValueIteration.setCorrectActionProbability(aCorrectActionProbability);
		lValueIteration.setActionNoiseProbability(aActionNoiseProbability);
		return lValueIteration;
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