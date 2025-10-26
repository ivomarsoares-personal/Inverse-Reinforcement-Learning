
/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public abstract class ARLCell implements IRLCell {


	private Integer fIndex;
	private Integer fRowIndex;
	private Integer fColumnIndex;
	
	private IRLCell fNorthCell;	
	private IRLCell fEastCell;	
	private IRLCell fSouthCell;	
	private IRLCell fWestCell;
	
	private IRLWall fNorthWall;
	private IRLWall fEastWall;
	private IRLWall fSouthWall;
	private IRLWall fWestWall;

	public Integer getIndex() {
		return fIndex;
	}
	public void setIndex(Integer aIndex) {
		fIndex = aIndex;
	}
	@Override
	public int getRowIndex() {
		return fRowIndex;
	}
	@Override
	public void setRowIndex(int aRowIndex) {
		fRowIndex = aRowIndex;
	}
	@Override
	public int getColumnIndex() {
		return fColumnIndex;
	}
	@Override
	public void setColumnIndex(int aColumnIndex) {
		fColumnIndex = aColumnIndex;
	}
	@Override
	public IRLCell getNorthCell() {
		return fNorthCell;
	}
	@Override
	public void setNorthCell(IRLCell aNorthCell) {
		fNorthCell = aNorthCell;
	}
	
	@Override
	public IRLCell getEastCell() {
		return fEastCell;
	}
	
	@Override
	public void setEastCell(IRLCell aEastCell) {
		fEastCell = aEastCell;
	}
	
	@Override
	public IRLCell getSouthCell() {
		return fSouthCell;
	}
	
	@Override
	public void setSouthCell(IRLCell aSouthCell) {
		fSouthCell = aSouthCell;
	}
	
	@Override
	public IRLCell getWestCell() {
		return fWestCell;
	}
	
	@Override
	public void setWestCell(IRLCell aWestCell) {
		fWestCell = aWestCell;
	}
	
	@Override
	public IRLWall getNorthWall() {
		return fNorthWall;
	}
	
	@Override
	public void setNorthWall(IRLWall aNorthWall) {
		fNorthWall = aNorthWall;
	}
	
	@Override
	public IRLWall getEastWall() {
		return fEastWall;
	}
	
	@Override
	public void setEastWall(IRLWall aEastWall) {
		fEastWall = aEastWall;
	}
	
	@Override
	public IRLWall getSouthWall() {
		return fSouthWall;
	}

	@Override
	public void setSouthWall(IRLWall aSouthWall) {
		fSouthWall = aSouthWall;
	}

	@Override
	public IRLWall getWestWall() {
		return fWestWall;
	}

	@Override
	public void setWestWall(IRLWall aWestWall) {
		fWestWall = aWestWall;
	}
	
	public Integer getWallCount() {
		
		int lWallCount = 0;
		
		if( getNorthWall() != null ){
			lWallCount++;
		}

		if( getEastWall() != null ){
			lWallCount++;
		}
		
		if( getSouthWall() != null ){
			lWallCount++;
		}
		
		if( getWestWall() != null ){
			lWallCount++;
		}
		
		
		return lWallCount;
	}


	
}
