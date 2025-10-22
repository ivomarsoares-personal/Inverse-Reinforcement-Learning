import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base implementation of {@link IRLState} shared by concrete state
 * classes. Provides common storage for the backing {@link IRLCell}, the list
 * of available {@link IRLAction}s, and convenience getters/setters for
 * initial/absorbing flags.
 */
public abstract class ARLState implements IRLState{
	
	private IRLCell fCell;
	private List<IRLAction> fActionList = new ArrayList<IRLAction>();
	private Boolean fAbsorbing = Boolean.FALSE;
	private Boolean fInitial   = Boolean.FALSE;
	
	public IRLAction retrieveAction( String aActionID ){
		
		for( int lActionIndex = 0; lActionIndex < fActionList.size(); lActionIndex++ ){
			IRLAction lAction = fActionList.get(lActionIndex);
			if( lAction.getID().equals(aActionID) ){
				return lAction;
			}
		}
		
		return null;
	}
	
	
	public List<IRLAction> getActionList() {
		return fActionList;
	}

	public void setActionList(List<IRLAction> aActionList) {
		fActionList = aActionList;
	}

	public Integer getIndex(){
		return fCell.getIndex();
	}

	public IRLCell getCell() {
		return fCell;
	}

	public void setCell(IRLCell aCell) {
		fCell = aCell;
	}
	public Boolean getAbsorbing() {
		return fAbsorbing;
	}
	public void setAbsorbing(Boolean aAbsorbing) {
		fAbsorbing = aAbsorbing;
	}
	public Boolean getInitial() {
		return fInitial;
	}
	public void setInitial(Boolean aInitial) {
		fInitial = aInitial;
	}
}
