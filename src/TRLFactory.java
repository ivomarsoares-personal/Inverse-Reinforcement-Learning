
/**
 * Simple object factory for the TRL concrete classes.
 * This class centralizes construction of grid, cell, wall, agent, policy and
 * reward objects used across utilities and UI components.
 */
public class TRLFactory {

	public static IRLObject createRLObject(String aRLObjectType){
	      if(aRLObjectType == null){
	         return null;
	      }		
	      if(aRLObjectType.equalsIgnoreCase(IRLObject.sCELL)){
	         return new TRLCell();	         
	      } 
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sWALL)){
	         return new TRLWall();
	      } 
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sGRID)){
		     return new TRLGrid();
		  }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sAGENT)){
			     return new TRLAgent();
		 }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sSTATE)){
			     return new TRLState();
		 }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sACTION_MOVE_NORTH)){
			     return new TRLActionMoveNorth();
		 }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sACTION_MOVE_EAST)){
			     return new TRLActionMoveEast();
		 }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sACTION_MOVE_SOUTH)){
			     return new TRLActionMoveSouth();
		 }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sACTION_MOVE_WEST)){
			     return new TRLActionMoveWest();
		 }	      
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sPOLICY )){
	    	  return new TRLPolicy();
	      }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sREWARD_FUNCTION ) ){
	    	  return new TRLRewardFunction();
	      }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sMODEL_BASED_VALUE_ITERATION ) ){
	    	  return new TRLValueIteration();
	      }
	      else if(aRLObjectType.equalsIgnoreCase(IRLObject.sINVERSE_REINFORCEMENT_LEARNING ) ){
	    	  return new TRLInverseReinforcementLearning();
	      }
	      else {
	    	  assert false;	    	  
	      }
	      
	      return null;
	   }
	
}
