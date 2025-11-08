import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Policy-related utilities: contains implementations for value iteration,
 * policy extraction from value functions, and solving Bellman equations for
 * state and action value functions.
 */
public class TRLPolicyUtil {

	private static final TRLPolicyUtil sSharedInstance = new TRLPolicyUtil();
	public static TRLPolicyUtil getSharedInstance(){
		return sSharedInstance;
	}    
	private TRLPolicyUtil(){}
       
	public IRLPolicy createPolicyForGivenOptimalValueFunction( final IRLAgent aAgent, final double[] aOptimalValueFunction ){

		IRLPolicy lPolicy = (IRLPolicy) TRLFactory.createRLObject(IRLObject.sPOLICY);

		RealMatrix lTPNorthMatrix = aAgent.getTPMatrixNorth();
		RealMatrix lTPEastMatrix  = aAgent.getTPMatrixEast();
		RealMatrix lTPSouthMatrix = aAgent.getTPMatrixSouth();
		RealMatrix lTPWestMatrix  = aAgent.getTPMatrixWest();

		List<IRLState> lStateList = aAgent.getStateList();

		for( int lInitialTPStateIndex = 0; lInitialTPStateIndex < lStateList.size(); lInitialTPStateIndex++ ){
			IRLState lInitialTPState = lStateList.get(lInitialTPStateIndex);

			List<IRLAction> lActionList = lInitialTPState.getActionList();

			double lMaxValue = - Double.MAX_VALUE;
			IRLAction lMaxAction = null;

			for( int lActionIndex = 0; lActionIndex < lActionList.size(); lActionIndex++ ){
				IRLAction lInitialTPStateAction = lActionList.get(lActionIndex);
				RealMatrix lTPMatrix = retrieveTPMatrix(lInitialTPStateAction, lTPNorthMatrix, lTPEastMatrix, lTPSouthMatrix, lTPWestMatrix);

				double lValue = 0;


				for( int lDestinationTPStateIndex = 0; lDestinationTPStateIndex < lStateList.size(); lDestinationTPStateIndex++ ){
					lValue += lTPMatrix.getEntry(lInitialTPStateIndex, lDestinationTPStateIndex) * aOptimalValueFunction[lDestinationTPStateIndex];
				}

				if( lValue > lMaxValue ){
					lMaxValue = lValue;
					lMaxAction = lInitialTPStateAction;
				}
			}


			TRLStateActionPair lStateActionPair = new TRLStateActionPair();
			lStateActionPair.setInitialState(lInitialTPState);
			lStateActionPair.setInitialStateValue(aOptimalValueFunction[lInitialTPStateIndex]);
			if( ! lInitialTPState.getAbsorbing() ){
				lStateActionPair.setAction(lMaxAction);
			}

			lPolicy.getStateActionPairList().add(lStateActionPair);
		}

		return lPolicy;
	}
    
	public RealMatrix retrieveTPMatrix(    IRLAction  aAction,    RealMatrix aTPActionNorthMatrix, RealMatrix aTPActionEastMatrix, RealMatrix aTPActionSouthMatrix,    RealMatrix aTPActionWestMatrix){
		
		if( aAction instanceof TRLActionMoveNorth ){
			return aTPActionNorthMatrix;
		}
		else if( aAction instanceof TRLActionMoveEast ){
			return aTPActionEastMatrix;
		}
		else if( aAction instanceof TRLActionMoveSouth ){
			return aTPActionSouthMatrix;
		}
		else if( aAction instanceof TRLActionMoveWest ){
			return aTPActionWestMatrix;
		}
		
		assert false;
		return null;
	}
    
	public double[] solveValueIterationAssynchronously( final IRLAgent aAgent ) {
		
		
		int lNumberOfStates = aAgent.getGrid().getCellList().size();
		
		double[] lValueFunction = new double[ lNumberOfStates];
		double[] lRewardFunctionAsArray = aAgent.getGrid().getRewardFunction().toArray();
		

		RealMatrix lTPNorthMatrix = aAgent.getTPMatrixNorth();
		RealMatrix lTPEastMatrix  = aAgent.getTPMatrixEast();
		RealMatrix lTPSouthMatrix = aAgent.getTPMatrixSouth();
		RealMatrix lTPWestMatrix  = aAgent.getTPMatrixWest();
		
		
		int lIndex = 0;
		
		List<IRLState> lStateList = aAgent.getStateList();

		while( lIndex < 10000){

			for( int lInitialTPStateIndex = 0; lInitialTPStateIndex < lNumberOfStates; lInitialTPStateIndex++ ){

				IRLState lInitialTPState = lStateList.get(lInitialTPStateIndex);
				List<IRLAction> lActionList = lInitialTPState.getActionList();		

				double lMaxIterativeValue = Double.MIN_VALUE;

				if( lInitialTPState.getAbsorbing() ){
					lMaxIterativeValue = 0;
				}else {


					for( int lActionIndex = 0; lActionIndex < lActionList.size(); lActionIndex++ ){
						IRLAction lActionOnInitialTPState = lActionList.get(lActionIndex);
						RealMatrix lTransitionProbabilitiesMatrix = retrieveTPMatrix(lActionOnInitialTPState, lTPNorthMatrix, lTPEastMatrix, lTPSouthMatrix, lTPWestMatrix);					
						double lIterativeValue = 0;

						for( int lDestinationTPStateIndex = 0; lDestinationTPStateIndex < lNumberOfStates; lDestinationTPStateIndex++ ){
							lIterativeValue += ((IRLValueIteration)aAgent.getModelBased()).getDiscountingFactor() * lTransitionProbabilitiesMatrix.getEntry(lInitialTPStateIndex, lDestinationTPStateIndex) * lValueFunction[lDestinationTPStateIndex];
						}

						if( lIterativeValue > lMaxIterativeValue ){
							lMaxIterativeValue = lIterativeValue;
						}
					}
				}

				double lReward = lRewardFunctionAsArray[lInitialTPStateIndex];
				double lNewValueFunction = lReward + lMaxIterativeValue;
				lValueFunction[lInitialTPStateIndex] = lNewValueFunction;
			}
			
			lIndex++;
		}
				
		return lValueFunction;
	}
    
    
	public double[] solveBellmansEquationsForStateValueFunction( final IRLAgent aAgent){

		IRLGrid lGrid = aAgent.getGrid();
		int lNumberOfStates = lGrid.getCellList().size();

		List<TRLStateActionPair> lPolicyStateActionPairList = aAgent.getPolicy().getStateActionPairList();
		
		RealMatrix lTPNorthMatrix = aAgent.getTPMatrixNorth();
		RealMatrix lTPEastMatrix  = aAgent.getTPMatrixEast();
		RealMatrix lTPSouthMatrix = aAgent.getTPMatrixSouth();
		RealMatrix lTPWestMatrix  = aAgent.getTPMatrixWest();

		double[][] lLinearEquationCoefficients = new double[lNumberOfStates][lNumberOfStates];

		for( int lStateActionPairIndex = 0; lStateActionPairIndex < lPolicyStateActionPairList.size(); lStateActionPairIndex++ ){

			TRLStateActionPair lStateActionPair = lPolicyStateActionPairList.get(lStateActionPairIndex);
			IRLState           lInitialState    = lStateActionPair.getInitialState();
			IRLAction          lAction          = lStateActionPair.getAction();

			RealMatrix lTransitionProbabilitiesMatrix = retrieveTPMatrix(lAction, lTPNorthMatrix, lTPEastMatrix, lTPSouthMatrix, lTPWestMatrix);


			// Building the coefficients for the linear system of equations.			
			for( int lCoefficientIndex = 0; lCoefficientIndex < lNumberOfStates; lCoefficientIndex++ ){
				
				if( ! lInitialState.getAbsorbing() ){

					int lStateIndex = lInitialState.getCell().getIndex();
					double lStateTransitionProbability = lTransitionProbabilitiesMatrix.getEntry(lStateIndex, lCoefficientIndex);
					if( lStateActionPairIndex == lCoefficientIndex ){
						lLinearEquationCoefficients[lStateActionPairIndex][lCoefficientIndex] = 1 - ((IRLValueIteration)aAgent.getModelBased()).getDiscountingFactor() * lStateTransitionProbability;
					}
					else {
						lLinearEquationCoefficients[lStateActionPairIndex][lCoefficientIndex] = - ((IRLValueIteration)aAgent.getModelBased()).getDiscountingFactor() * lStateTransitionProbability;
					}
				}
				else if( lInitialState.getCell().getIndex() == lCoefficientIndex ) {
					// Absorbing state.
					assert lInitialState.getAbsorbing();
					lLinearEquationCoefficients[lStateActionPairIndex][lCoefficientIndex] = 1;								
				}                
			}
		}


		RealMatrix lCoefficients =  new Array2DRowRealMatrix(lLinearEquationCoefficients, false);
		DecompositionSolver lSolver = new LUDecomposition(lCoefficients).getSolver();


		RealVector lConstants = new ArrayRealVector(aAgent.getGrid().getRewardFunction().toArray(), false);
		RealVector lSolution = lSolver.solve(lConstants);


		return lSolution.toArray();
	}
	
	public HashMap<IRLState, HashMap<IRLAction, Double>> solveBellmansEquationsForActionValueFunction(     final IRLAgent aAgent,    final double[] aValueFunction ){

		RealMatrix lTPNorthMatrix = aAgent.getTPMatrixNorth();
		RealMatrix lTPEastMatrix  = aAgent.getTPMatrixEast();
		RealMatrix lTPSouthMatrix = aAgent.getTPMatrixSouth();
		RealMatrix lTPWestMatrix  = aAgent.getTPMatrixWest();

		final double[] lRewardFunctionAsArray = aAgent.getGrid().getRewardFunction().toArray();
		final HashMap<IRLState, HashMap<IRLAction, Double>> lStateActionValueFunctionHashMap = new HashMap<IRLState, HashMap<IRLAction,Double>>();

		List<IRLState> lStateList = aAgent.getStateList();

		for( int lStateIndex = 0; lStateIndex < lStateList.size(); lStateIndex++ ){

			final IRLState lInitialState = lStateList.get(lStateIndex);
			
			HashMap<IRLAction, Double> lActionValueFunctionHashMap = new HashMap<IRLAction, Double>();

			lStateActionValueFunctionHashMap.put(lInitialState, lActionValueFunctionHashMap);

			final List<IRLAction> lActionList = lInitialState.getActionList();
			for( int lActionIndex = 0; lActionIndex < lActionList.size(); lActionIndex++ ){
				IRLAction lAction = lActionList.get(lActionIndex);

				Double lQvalue = lRewardFunctionAsArray[lStateIndex];

				RealMatrix lTransitionProbabilitiesMatrix = retrieveTPMatrix(lAction, lTPNorthMatrix, lTPEastMatrix, lTPSouthMatrix, lTPWestMatrix);
				for( int lCoefficientIndex = 0; lCoefficientIndex < lStateList.size(); lCoefficientIndex++ ){

					double lStateTransitionProbability = lTransitionProbabilitiesMatrix.getEntry(lStateIndex, lCoefficientIndex);
					lQvalue += ((IRLValueIteration)aAgent.getModelBased()).getDiscountingFactor() * lStateTransitionProbability * aValueFunction[lCoefficientIndex];
				}

				lActionValueFunctionHashMap.put(lAction, lQvalue);
			}
		}


		return lStateActionValueFunctionHashMap;
	}
    
}