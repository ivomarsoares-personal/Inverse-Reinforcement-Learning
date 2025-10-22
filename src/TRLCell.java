
/**
 * Concrete cell implementation for the TRL grid. Extends {@link ARLCell}
 * and adds a helpful toString and neighbor check used by utilities and tests.
 */
public class TRLCell extends ARLCell{

	@Override
	public String toString() {
		return "ARLCell [fIndex=" + getIndex() + ", fRowIndex=" + getRowIndex() + ", fColumnIndex=" + getColumnIndex() + "]";
	}
	
	public boolean isNeighboor( IRLCell aCell ){
		return getNorthCell() == aCell || getEastCell() == aCell || getSouthCell() == aCell || getWestCell() == aCell;
	}
}
