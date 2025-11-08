public interface IRLValueIteration extends IRLModelBased{
	public Double getDiscountingFactor();
	public void setDiscountingFactor(Double aDiscountingFactor);
	public Double getLearningRate();
	public void setLearningRate(Double aLearningRate);
	public Double getCorrectActionProbability();
	public void setCorrectActionProbability(Double fCorrectActionProbability);
	public Double getActionNoiseProbability();
	public void setActionNoiseProbability(Double fActionNoiseProbability);
}