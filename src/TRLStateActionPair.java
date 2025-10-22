
/**
 * Simple data holder pairing an initial {@link IRLState} with the
 * {@link IRLAction} that the policy prescribes for that state as well as
 * the state's value. Used by {@link TRLPolicy} to store computed policies.
 */
public class TRLStateActionPair {

	private IRLState fInitialState;
	private Double   fInitialStateValue;
	
	public Double getInitialStateValue() {
		return fInitialStateValue;
	}
	public void setInitialStateValue(Double aInitialStateValue) {
		fInitialStateValue = aInitialStateValue;
	}
	private IRLAction fAction;
	
	public IRLAction getAction() {
		return fAction;
	}
	public void setAction(IRLAction aAction) {
		fAction = aAction;
	}
	public IRLState getInitialState() {
		return fInitialState;
	}
	public void setInitialState(IRLState aInitialState) {
		fInitialState = aInitialState;
	}
	
	
}
