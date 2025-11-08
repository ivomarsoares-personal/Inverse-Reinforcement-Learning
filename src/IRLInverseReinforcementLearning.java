public interface IRLInverseReinforcementLearning extends IRLAlgorithm{

	public Double getDiscountingFactor();
	public void setDiscountingFactor(Double aDiscountingFactor);
	
	public Double getCorrectActionProbability();
	public void setCorrectActionProbability(Double fCorrectActionProbability);
	
	public Double getActionNoiseProbability();
	public void setActionNoiseProbability(Double fActionNoiseProbability);	
	
	public Double getStepLambda();
	public void setStepLambda(Double aStepLambda);

	public Double getMinLambda();
	public void setMinLambda(Double aMinLambda);

	public Double getMaxLambda();
	public void setMaxLambda(Double aMaxLambda);

	public Double getRMax();
	public void setRMax(Double aRMax);
	
    public Double getLambda();
	public void setLambda(Double aLambda);
	
	public double[] getRewardFunction();
	public void setRewardFunction(double[] aRewardFunction);
	
}