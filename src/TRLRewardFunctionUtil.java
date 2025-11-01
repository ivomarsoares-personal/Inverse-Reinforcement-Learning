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
	
	public IRLRewardFunction createRewardFunction(final IRLAgent aAgent, final String aStateRewardList) {
		// Create reward function object
		IRLRewardFunction lRewardFunction = (IRLRewardFunction) TRLFactory.createRLObject(IRLObject.sREWARD_FUNCTION);

		// Parse input string into a temporary map from index -> reward
		HashMap<Integer, Double> lIndexToReward = new HashMap<Integer, Double>();
		String[] lEntries = aStateRewardList.split(",");
		int lMaxIndex = -1;
		for( String lEntry : lEntries ){
			if( lEntry == null ) continue;
			String lTrim = lEntry.trim();
			if( lTrim.isEmpty() ) continue;
			String[] lParts = lTrim.split(":");
			if( lParts.length < 2 ) continue; // malformed, skip
			try{
				int idx = Integer.parseInt(lParts[0].trim());
				double r = Double.parseDouble(lParts[1].trim());
				lIndexToReward.put(idx, r);
				if( idx > lMaxIndex ) lMaxIndex = idx;
			}
			catch(NumberFormatException e){
				// skip malformed numbers
				continue;
			}
		}

		// Build the HashMap<IRLState,Double> ensuring entries for indices 0..lMaxIndex
		HashMap<IRLState, Double> lStateRewardHashMap = new HashMap<IRLState, Double>();
		List<IRLState> lStateList = aAgent.getStateList();
		
		for (int lStateIndex = 0; lStateIndex < lStateList.size(); lStateIndex++) {
			IRLState lState = lStateList.get(lStateIndex);
			lStateRewardHashMap.put(lState, lIndexToReward.get(lStateIndex));		
		}

		// Set the hashmap to the reward function and return
		lRewardFunction.setStateRewardHashMap(lStateRewardHashMap);
		return lRewardFunction;
	}
	
	
	public IRLRewardFunction createRewardFunction(final HashMap<IRLState, Double> aStateRewardHashMap) {
		
		IRLRewardFunction lRewardFunction = (IRLRewardFunction) TRLFactory.createRLObject(IRLObject.sREWARD_FUNCTION);
		lRewardFunction.setStateRewardHashMap(aStateRewardHashMap);
		return lRewardFunction;
	}	
}