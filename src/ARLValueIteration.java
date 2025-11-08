import java.util.HashMap;

public abstract class ARLValueIteration implements IRLValueIteration{
	
	private Double  fDiscountingFactor;
	private Double fLearningRate;
	private Double fCorrectActionProbability;
	private Double fActionNoiseProbability;
	
	
	@Override
	public void execute(IRLAgent aAgent) {
		double[] lOptimalValueFunction = TRLPolicyUtil.getSharedInstance().solveValueIterationAssynchronously(aAgent);
		IRLPolicy lOptimalPolicy = TRLPolicyUtil.getSharedInstance().createPolicyForGivenOptimalValueFunction(aAgent, lOptimalValueFunction);
		lOptimalPolicy.setValueFunction(lOptimalValueFunction);
		
		HashMap<IRLState, HashMap<IRLAction, Double>> lActionValueFunctionHashMap = TRLPolicyUtil.getSharedInstance().solveBellmansEquationsForActionValueFunction(aAgent, lOptimalPolicy.getValueFunction());
		lOptimalPolicy.setQValueHashMap(lActionValueFunctionHashMap);
		
		aAgent.setPolicy(lOptimalPolicy);			
	}
	
	@Override
	public Double getDiscountingFactor() {
		return fDiscountingFactor;
	}

	@Override
	public void setDiscountingFactor(Double aDiscountingFactor) {
		fDiscountingFactor = aDiscountingFactor;
	}
	
	@Override
	public Double getLearningRate() {
		return fLearningRate;
	}

	@Override
	public void setLearningRate(Double aLearningRate) {
		fLearningRate = aLearningRate;
	}
	
	@Override
	public Double getCorrectActionProbability() {
		return fCorrectActionProbability;
	}

	@Override
	public void setCorrectActionProbability(Double fCorrectActionProbability) {
		this.fCorrectActionProbability = fCorrectActionProbability;
	}

	@Override
	public Double getActionNoiseProbability() {
		return fActionNoiseProbability;
	}

	@Override
	public void setActionNoiseProbability(Double fActionNoiseProbability) {
		this.fActionNoiseProbability = fActionNoiseProbability;
	}
}