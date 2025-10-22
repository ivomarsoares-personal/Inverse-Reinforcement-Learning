/**
 * Concrete TRL implementation of {@link ARLState}. Represents a state in the
 * gridworld and delegates storage of its coordinates and neighbor links to
 * the underlying {@link IRLCell}.
 */
public class TRLState extends ARLState {
	
	@Override
	public String toString() {
		return "S" + getIndex();
	}
	
}
