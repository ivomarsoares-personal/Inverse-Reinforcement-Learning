import java.util.HashMap;

/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public interface IRLRewardFunction extends IRL{

	
	public HashMap<IRLState, Double> getStateRewardHashMap();
	public void setStateRewardHashMap(HashMap<IRLState, Double> aStateRewardHashMap);
	public double[] toArray();

}
