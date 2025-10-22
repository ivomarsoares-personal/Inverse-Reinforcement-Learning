import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Concrete policy container used by the TRL implementation.
 * Holds the list of state/action pairs that represent the chosen action per
 * state along with auxiliary structures such as value function and Q-values.
 */
public class TRLPolicy implements IRLPolicy {

	private List<TRLStateActionPair> fStateActionPairList = new ArrayList<TRLStateActionPair>();
	private double[] fValueFunction;	
	private HashMap<IRLState, HashMap<IRLAction, Double>> fQValueHashMap;
	

	public HashMap<IRLState, HashMap<IRLAction, Double>> getQValueHashMap() {
		return fQValueHashMap;
	}


	public void setQValueHashMap(
			HashMap<IRLState, HashMap<IRLAction, Double>> aQValueHashMap) {
		fQValueHashMap = aQValueHashMap;
	}


	public double[] getValueFunction() {
		return fValueFunction;
	}


	public void setValueFunction(double[] aValueFunction) {
		fValueFunction = aValueFunction;
	}


	public List<TRLStateActionPair> getStateActionPairList() {
		return fStateActionPairList;
	}

	
	
}
