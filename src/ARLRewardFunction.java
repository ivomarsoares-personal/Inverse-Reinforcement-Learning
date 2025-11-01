import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public abstract class ARLRewardFunction implements IRLRewardFunction {

	private HashMap<IRLState, Double> fStateRewardHashMap = new HashMap<IRLState, Double>();

	public double[] toArray(){
		double[] l_Array = new double[fStateRewardHashMap.size()];
		
		Set<IRLState> l_KeySet = fStateRewardHashMap.keySet();
		for (Iterator<IRLState> l_Iterator = l_KeySet.iterator(); l_Iterator.hasNext();) {
			IRLState lState = (IRLState) l_Iterator.next();
			l_Array[lState.getIndex()] = fStateRewardHashMap.get(lState);
		}
		
		return l_Array;
	}


	public HashMap<IRLState, Double> getStateRewardHashMap() {
		return fStateRewardHashMap;
	}

	public void setStateRewardHashMap(HashMap<IRLState, Double> aStateRewardHashMap) {
		fStateRewardHashMap = aStateRewardHashMap;
	}
	
	
}
