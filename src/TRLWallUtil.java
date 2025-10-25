
public class TRLWallUtil {

	private static final TRLWallUtil sSharedInstance = new TRLWallUtil();
	public static TRLWallUtil getSharedInstance(){
		return sSharedInstance;
	}	
	private TRLWallUtil(){}

	public IRLWall createWall( final IRLGrid aGrid, final String aInitialVertice, final String aFinalVertice){
		
		int lInitialVerticeXCoordinate = getVerticeXCoordinate( aInitialVertice );
		int lInitialVerticeYCoordinate = getVerticeYCoordinate( aInitialVertice );
		int lFinalVerticeXCoordinate   = getVerticeXCoordinate( aFinalVertice );
		int lFinalVerticeYCoordinate   = getVerticeYCoordinate( aFinalVertice );
	
		IRLWall lWall = (IRLWall) TRLFactory.createRLObject(IRLGridComponent.sWALL);
		lWall.setInitialVerticeXCoordinate(lInitialVerticeXCoordinate);
		lWall.setInitialVerticeYCoordinate(lInitialVerticeYCoordinate);
		lWall.setFinalVerticeXCoordinate(lFinalVerticeXCoordinate);
		lWall.setFinalVerticeYCoordinate(lFinalVerticeYCoordinate);
		aGrid.getWallList().add(lWall);
	
		return lWall;
	}
	
	public int getVerticeXCoordinate( final String aVertice ){		
		String[] lComponents = aVertice.split(",");
		return Integer.parseInt( lComponents[0] );
	}
	
	public int getVerticeYCoordinate( final String aVertice ){		
		String[] lComponents = aVertice.split(",");
		return Integer.parseInt( lComponents[1] );
	}
	
	public boolean isHorizontalWall(final int aInitialVerticeXCoordinate, final int aFinalVerticeXCoordinate ) {
		return aInitialVerticeXCoordinate == aFinalVerticeXCoordinate;
	}
	
	public boolean isVerticalWall(final int aInitialVerticeYCoordinate, final int aFinalVerticeYCoordinate ) {
		return aInitialVerticeYCoordinate == aFinalVerticeYCoordinate;
	}
	
}
