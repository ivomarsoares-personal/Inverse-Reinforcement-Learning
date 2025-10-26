/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public interface IRLCell extends IRLGridComponent {

	public  Integer getIndex();
	public  void setIndex(Integer aIndex);
	public boolean isNeighboor( IRLCell aCell );
	public Integer getWallCount();
	
	
	public  int getRowIndex();
	public  void setRowIndex(int aRowIndex);
	public  int getColumnIndex();
	public  void setColumnIndex(int aColumnIndex);
	public  IRLCell getNorthCell();
	public  void setNorthCell(IRLCell aNorthCell);
	public  IRLCell getEastCell();
	public  void setEastCell(IRLCell aEastCell);
	public  IRLCell getSouthCell();
	public  void setSouthCell(IRLCell aSouthCell);
	public  IRLCell getWestCell();
	public  void setWestCell(IRLCell aWestCell);
	public  IRLWall getNorthWall();
	public  void setNorthWall(IRLWall aNorthWall);
	public  IRLWall getEastWall();
	public  void setEastWall(IRLWall aEastWall);
	public  IRLWall getSouthWall();
	public  void setSouthWall(IRLWall aSouthWall);
	public  IRLWall getWestWall();
	public  void setWestWall(IRLWall aWestWall);
}