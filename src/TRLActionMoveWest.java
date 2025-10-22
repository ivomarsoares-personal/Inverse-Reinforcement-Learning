/**
 * Concrete action that represents moving the agent one cell to the west.
 * This class is a trivial concrete subclass of {@link ARLAction} and provides
 * an identifier (toString) used throughout the grid and policy utilities.
 */
public class TRLActionMoveWest extends ARLAction {

	@Override
	public String toString() {
		return IRL.sACTION_MOVE_WEST;
	}
	
}
