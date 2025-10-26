import java.util.List;

/**
 * 
 * @author Ivomar Brito Soares
 *
 */
public interface IRLWall extends IRLGridComponent{
	
	public int getInitialVerticeXCoordinate();
	public void setInitialVerticeXCoordinate(int fInitialVerticeXCoordinate);
	public int getInitialVerticeYCoordinate();
	public void setInitialVerticeYCoordinate(int fInitialVerticeYCoordinate);
	public int getFinalVerticeXCoordinate();
	public void setFinalVerticeXCoordinate(int fFinalVerticeXCoordinate);
	public int getFinalVerticeYCoordinate();
	public void setFinalVerticeYCoordinate(int fFinalVerticeYCoordinate);
	public List<IRLCell> getNorthCellsList();
	public void setNorthCellsList(List<IRLCell> fNorthCellsList);
	public List<IRLCell> getEastCellsList();
	public void setEastCellsList(List<IRLCell> fEastCellsList);
	public List<IRLCell> getWestCellsList();
	public void setWestCellsList(List<IRLCell> fWestCellsList);
	public List<IRLCell> getSouthCellsList();
	public void setSouthCellsList(List<IRLCell> fSouthCellsList);
}
