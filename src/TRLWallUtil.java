
public class TRLWallUtil {

	private static final TRLWallUtil sSharedInstance = new TRLWallUtil();
	public static TRLWallUtil getSharedInstance(){
		return sSharedInstance;
	}	
	private TRLWallUtil(){}

	public IRLWall createWall( final String aInitialVertice, final String aFinalVertice){
		
		int lInitialVerticeXCoordinate = getVerticeXCoordinate( aInitialVertice );
		int lInitialVerticeYCoordinate = getVerticeYCoordinate( aInitialVertice );
		int lFinalVerticeXCoordinate   = getVerticeXCoordinate( aFinalVertice );
		int lFinalVerticeYCoordinate   = getVerticeYCoordinate( aFinalVertice );
	
	
		System.out.println( "Creating wall between (" + lInitialVerticeXCoordinate + "," + lInitialVerticeYCoordinate + ") and (" + lFinalVerticeXCoordinate + "," + lFinalVerticeYCoordinate + ")" );
		//		IRLWall lWall = (IRLWall) TRLFactory.createRLObject(IRLObject.sWALL);
		
		return null;
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
