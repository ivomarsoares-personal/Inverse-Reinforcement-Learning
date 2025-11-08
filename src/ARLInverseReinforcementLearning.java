public abstract class ARLInverseReinforcementLearning implements IRLInverseReinforcementLearning{
	private Double  fDiscountingFactor;
	private Double fCorrectActionProbability;
	private Double fActionNoiseProbability;
	private Double fStepLambda;
	private Double fMinLambda;
	private Double fMaxLambda;
	private Double fRMax;
	private Double fLambda;
	private double[] fRewardFunction;
	
	@Override
	public boolean execute(IRLAgent aAgent) {
		double lLambda = 0;
		boolean lSolutionFound;
		for( lLambda = fMinLambda; lLambda <= fMaxLambda; lLambda = lLambda + fStepLambda ){
			try{
				fRewardFunction = TRLInverseReinforcementLearningUtil.getSharedInstance().solveIRL( aAgent, fRMax, lLambda );
				fLambda = lLambda;
				lSolutionFound = TRLInverseReinforcementLearningUtil.getSharedInstance().iRLSolutionFound(aAgent, fRMax, fRewardFunction);
				if( lSolutionFound ){
					return true;
				}
			}

			catch(org.apache.commons.math3.optim.linear.UnboundedSolutionException aUnboundedSolutionException ){
				System.err.println("UnboundedSolutionException for Lambda " + lLambda );
				continue;
			}
		}		
		return false;
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
	
	@Override
	public Double getStepLambda() {
		return fStepLambda;
	}

	@Override
	public void setStepLambda(Double aStepLambda) {
		fStepLambda = aStepLambda;
	}

	@Override
	public Double getMinLambda() {
		return fMinLambda;
	}

	@Override
	public void setMinLambda(Double aMinLambda) {
		fMinLambda = aMinLambda;
	}

	@Override
	public Double getMaxLambda() {
		return fMaxLambda;
	}

	@Override
	public void setMaxLambda(Double aMaxLambda) {
		fMaxLambda = aMaxLambda;
	}
	
	@Override
	public Double getRMax() {
		return fRMax;
	}

	@Override
	public void setRMax(Double aRMax) {
		fRMax = aRMax;
	}
	
	@Override
    public Double getLambda() {
		return fLambda;
	}

	@Override
	public void setLambda(Double aLambda) {
		fLambda = aLambda;
	}
	
	@Override
	public double[] getRewardFunction() {
		return fRewardFunction;
	}

	@Override
	public void setRewardFunction(double[] aRewardFunction) {
		fRewardFunction = aRewardFunction;
	}
}