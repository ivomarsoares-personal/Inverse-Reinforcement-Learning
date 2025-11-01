/**
 * Concrete grid implementation used by the TRL classes. Extends
 * {@link ARLGrid} and is the runtime holder for cells, walls and reward
 * functions used by agents and the UI.
 */
public class TRLGrid extends ARLGrid{

	@Override
	public String toString() {
		return "ARLGrid [fNumberOfRows=" + getNumberOfRows() + ", fNumberOfColumns=" + getNumberOfColumns() + ", fCellList=" + getCellList() + "]";
	}

}
