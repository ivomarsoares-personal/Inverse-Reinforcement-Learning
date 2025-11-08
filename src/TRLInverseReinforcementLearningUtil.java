import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

/**
 * Utilities for solving the Inverse Reinforcement Learning (IRL) problem
 * for the TRL agent and grid instances. Contains code that sets up and
 * solves the linear programming problem (Ng & Russell style) using Apache
 * Commons Math linear and optimization utilities.
 */
public class TRLInverseReinforcementLearningUtil {

	private static final TRLInverseReinforcementLearningUtil sSharedInstance = new TRLInverseReinforcementLearningUtil();
	public static TRLInverseReinforcementLearningUtil getSharedInstance(){
		return sSharedInstance;
	}	
	private TRLInverseReinforcementLearningUtil(){}

	public IRLInverseReinforcementLearning createIRL( final IRLAgent aAgent, final double aDiscountingFactor, final double aCorrectActionProbability, final double aActionNoiseProbability, final double aStepLambda, final double aMinLambda, final double aMaxLambda, final double aRMax ){	
		
		IRLInverseReinforcementLearning lIRL = (IRLInverseReinforcementLearning) TRLFactory.createRLObject(IRLObject.sINVERSE_REINFORCEMENT_LEARNING);
		lIRL.setDiscountingFactor(aDiscountingFactor);
		lIRL.setCorrectActionProbability(aCorrectActionProbability);
		lIRL.setActionNoiseProbability(aActionNoiseProbability);
		lIRL.setStepLambda(aStepLambda);
		lIRL.setMinLambda(aMinLambda);
		lIRL.setMaxLambda(aMaxLambda);
		lIRL.setRMax(aRMax);
	
		return lIRL;
	}
	

	public double[] solveIRL( final IRLAgent aAgent, final double aRMax, final double aRegularization ) {


		RealMatrix lTPNorthMatrix = aAgent.getTPMatrixNorth();
		RealMatrix lTPEastMatrix  = aAgent.getTPMatrixEast();
		RealMatrix lTPSouthMatrix = aAgent.getTPMatrixSouth();
		RealMatrix lTPWestMatrix  = aAgent.getTPMatrixWest();
		Double lDiscountingFactor = aAgent.getInverseReinforcementLearning().getDiscountingFactor();


		HashMap<IRLState, HashMap<IRLAction, Double>> lActionValueFunctionHashMap = aAgent.getPolicy().getQValueHashMap();


		int lNumberOfStates = aAgent.getGrid().getCellList().size();

		/*
		 * Creating elements of optimization problem: Section 3.2, equation (7).
		 */

		RealMatrix lIdentityMatrix                 = MatrixUtils.createRealIdentityMatrix(lNumberOfStates);

		RealMatrix lGammaTimesNorth                = lTPNorthMatrix.scalarMultiply(lDiscountingFactor);
		RealMatrix lIdentityMinusGammaNorth        = lIdentityMatrix.subtract(lGammaTimesNorth);
		RealMatrix lIdentityMinusGammaNorthInverse = new LUDecomposition(lIdentityMinusGammaNorth).getSolver().getInverse();

		RealMatrix lGammaTimesEast                = lTPEastMatrix.scalarMultiply(lDiscountingFactor);
		RealMatrix lIdentityMinusGammaEast        = lIdentityMatrix.subtract(lGammaTimesEast);
		RealMatrix lIdentityMinusGammaEastInverse = new LUDecomposition(lIdentityMinusGammaEast).getSolver().getInverse();

		RealMatrix lGammaTimesSouth                = lTPSouthMatrix.scalarMultiply(lDiscountingFactor);
		RealMatrix lIdentityMinusGammaSouth        = lIdentityMatrix.subtract(lGammaTimesSouth);
		RealMatrix lIdentityMinusGammaSouthInverse = new LUDecomposition(lIdentityMinusGammaSouth).getSolver().getInverse();

		RealMatrix lGammaTimesWest                = lTPWestMatrix.scalarMultiply(lDiscountingFactor);
		RealMatrix lIdentityMinusGammaWest        = lIdentityMatrix.subtract(lGammaTimesWest);
		RealMatrix lIdentityMinusGammaWestInverse = new LUDecomposition(lIdentityMinusGammaWest).getSolver().getInverse();
		

		/*
		 * Solving Optimization problem: http://google-opensource.blogspot.com.br/2009/06/introducing-apache-commons-math.html
		 */

		double[] lObjectiveVector = new double[lNumberOfStates];
		Collection<LinearConstraint> lConstraints = new ArrayList<LinearConstraint>();


		List<TRLStateActionPair> lStateActionPairList = aAgent.getPolicy().getStateActionPairList();
		for( int lStateActionIndex = 0; lStateActionIndex < lStateActionPairList.size(); lStateActionIndex++ ){

			final TRLStateActionPair lStateActionPair    = lStateActionPairList.get(lStateActionIndex);
			IRLAction                lPolicyAction       = lStateActionPair.getAction();
			IRLState                 lPolicyInitialState = lStateActionPair.getInitialState();

			if( lPolicyInitialState.getAbsorbing() ){
				continue;
			}

			RealMatrix lPolicyTPMatrix                 = retrieveTransitionProbabilityMatrix(lPolicyAction, lTPNorthMatrix, lTPEastMatrix, lTPSouthMatrix, lTPWestMatrix);
			IRLAction lActionWithLowestQValue          = retrieveActionWithLowestQValueDifferentFromPolicyAction(lStateActionPair, lActionValueFunctionHashMap);
			RealMatrix lActionWithLowestQValueTPMatrix = retrieveTransitionProbabilityMatrix(lActionWithLowestQValue, lTPNorthMatrix, lTPEastMatrix, lTPSouthMatrix, lTPWestMatrix);

			RealMatrix lPolicyMinusLowestQValueTPMatrix = lPolicyTPMatrix.subtract(lActionWithLowestQValueTPMatrix);

			RealMatrix lObjectiveComponentMatrix = null;

			if( lPolicyAction instanceof TRLActionMoveNorth ){
				lObjectiveComponentMatrix = lPolicyMinusLowestQValueTPMatrix.multiply(lIdentityMinusGammaNorthInverse);
			}
			else if( lPolicyAction instanceof TRLActionMoveEast ){
				lObjectiveComponentMatrix = lPolicyMinusLowestQValueTPMatrix.multiply(lIdentityMinusGammaEastInverse);
			}
			else if( lPolicyAction instanceof TRLActionMoveSouth ){
				lObjectiveComponentMatrix = lPolicyMinusLowestQValueTPMatrix.multiply(lIdentityMinusGammaSouthInverse);
			}
			else if( lPolicyAction instanceof TRLActionMoveWest ){
				lObjectiveComponentMatrix = lPolicyMinusLowestQValueTPMatrix.multiply(lIdentityMinusGammaWestInverse);
			}		

			for( int lRowIndex = 0; lRowIndex < lNumberOfStates; lRowIndex++ ){
				double[] lConstraint = lObjectiveComponentMatrix.getRow(lPolicyInitialState.getIndex());
				lObjectiveVector[lRowIndex] += lObjectiveVector[lRowIndex] + lConstraint[lRowIndex] - aRegularization;
			}			
		}


		// Optimization problem.
		LinearObjectiveFunction lLinearObjectiveFunction = new LinearObjectiveFunction(lObjectiveVector, 0.0 );


		lConstraints.add(new LinearConstraint(new double[]{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}, Relationship.LEQ, aRMax));
		lConstraints.add(new LinearConstraint(new double[]{0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}, Relationship.LEQ, aRMax));
		lConstraints.add(new LinearConstraint(new double[]{0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0}, Relationship.LEQ, aRMax));
		lConstraints.add(new LinearConstraint(new double[]{0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0}, Relationship.LEQ, aRMax));
		lConstraints.add(new LinearConstraint(new double[]{0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0}, Relationship.LEQ, aRMax));
		lConstraints.add(new LinearConstraint(new double[]{0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0}, Relationship.LEQ, aRMax));
		lConstraints.add(new LinearConstraint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0}, Relationship.LEQ, aRMax));
		lConstraints.add(new LinearConstraint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0}, Relationship.LEQ, aRMax));
		lConstraints.add(new LinearConstraint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0}, Relationship.LEQ, aRMax));

		LinearConstraintSet lLinearConstraintSet = new LinearConstraintSet(lConstraints);

		// Creating and running the solver.
		PointValuePair lOptimizationSolution = new SimplexSolver().optimize( lLinearObjectiveFunction, lLinearConstraintSet,GoalType.MAXIMIZE, new	NonNegativeConstraint(true) );


		return lOptimizationSolution.getPoint();

	}

	
	public boolean iRLSolutionFound( final IRLAgent aAgent, final double aRMax, final double[] aRewardFunction ){
		
		final List<IRLState> lStateList = aAgent.getStateList();
		
		for( int lStateIndex = 0; lStateIndex < lStateList.size(); lStateIndex++ ){
			
			final IRLState lState = lStateList.get(lStateIndex);
			
			if( lState.getAbsorbing() ){
				if( Math.abs( aRMax - aRewardFunction[lStateIndex ] ) > 0.001 ){
					return false;
				}				
			}
			else {
				if( aRewardFunction[lStateIndex] > aRMax * 0.2 ){
					return false;
				}
			}
		}
		
		return true;
	}



	private RealMatrix retrieveTransitionProbabilityMatrix( IRLAction aRLAction, RealMatrix aTPActionNorthMatrix, RealMatrix aTPActionEastMatrix, RealMatrix aTPActionSouthMatrix,	RealMatrix aTPActionWestMatrix	){

		if( aRLAction instanceof TRLActionMoveNorth ){
			return aTPActionNorthMatrix;
		}
		else if( aRLAction instanceof TRLActionMoveEast ){
			return aTPActionEastMatrix;
		}
		else if( aRLAction instanceof TRLActionMoveSouth ){
			return aTPActionSouthMatrix;
		}
		else if( aRLAction instanceof TRLActionMoveWest ){
			return aTPActionWestMatrix;
		}				

		assert false;
		return null;

	}


	private IRLAction retrieveActionWithLowestQValueDifferentFromPolicyAction( final TRLStateActionPair aRLStateActionPair, final HashMap<IRLState, HashMap<IRLAction, Double>> aActionValueFunctionHashMap ){

		IRLState lPolicyInitialState = aRLStateActionPair.getInitialState();
		IRLAction lPolicyAction = aRLStateActionPair.getAction();

		double lMinQValue = Double.MAX_VALUE;
		IRLAction lActionWithLowestQValue = null;
		HashMap<IRLAction, Double> lActionValueHashMap = aActionValueFunctionHashMap.get(lPolicyInitialState);

		Set<IRLAction> lActionsKeySet = lActionValueHashMap.keySet();
		for (Iterator<IRLAction> lActionIterator = lActionsKeySet.iterator(); lActionIterator.hasNext();) {
			IRLAction lAction = (IRLAction) lActionIterator.next();
			if( lAction == lPolicyAction ){
				continue;
			}
			Double lActionQValue = lActionValueHashMap.get(lAction);
			if( lActionQValue < lMinQValue ){
				lMinQValue = lActionQValue;
				lActionWithLowestQValue = lAction;
			}

		}

		return lActionWithLowestQValue;

	}

}
