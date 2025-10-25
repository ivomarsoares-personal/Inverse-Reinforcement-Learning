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
}
