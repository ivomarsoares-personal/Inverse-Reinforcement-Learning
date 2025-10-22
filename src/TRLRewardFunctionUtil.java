import java.util.HashMap;
import java.util.List;

/**
 * Utilities to create reward functions for TRL agents. Provides helpers to
 * build per-state reward mappings used by policy evaluation and IRL routines.
 */
public class TRLRewardFunctionUtil {

	private static final TRLRewardFunctionUtil sSharedInstance = new TRLRewardFunctionUtil();
	public static TRLRewardFunctionUtil getSharedInstance(){
		return sSharedInstance;
	}	
	private TRLRewardFunctionUtil(){}
	
	
	public IRLRewardFunction createRewardFunction( final IRLAgent aAgent, final Double aRewardOutsideAbsorbingState, final Double aRewardAtAbosrbingState ){

		final List<IRLState> lStateList = aAgent.getStateList();

		IRLRewardFunction lRewardFunction = (IRLRewardFunction) TRLFactory.createRLObject(IRLObject.sREWARD_FUNCTION);
		HashMap<IRLState, Double> lStateRewardHashMap = lRewardFunction.getStateRewardHashMap();

		for( int lStateIndex = 0; lStateIndex < lStateList.size(); lStateIndex++ ){
			IRLState lState = lStateList.get(lStateIndex);

			if( lState.getAbsorbing() ){
				lStateRewardHashMap.put(lState, aRewardAtAbosrbingState);
			}
			else {
				lStateRewardHashMap.put(lState, aRewardOutsideAbsorbingState);
			}
		}

		return lRewardFunction;
	}
	
	
}
