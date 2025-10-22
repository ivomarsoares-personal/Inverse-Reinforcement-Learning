
public class TRLWallUtil {

	private static final TRLWallUtil sSharedInstance = new TRLWallUtil();
	public static TRLWallUtil getSharedInstance(){
		return sSharedInstance;
	}	
	private TRLWallUtil(){}

	public IRLWall createWall( final String aInitialVertice, final String aFinalVertice){
		
		IRLWall lWall = (IRLWall) TRLFactory.createRLObject(IRLObject.sWALL);
		return lWall;
	}
	
	private int getVerticeXCoordinate( final String aVertice ){
		
		String[] lComponents = aVertice.split(",");
		return Integer.parseInt( lComponents[0] );
	}
	
	private int getVerticeYCoordinate( final String aVertice ){
		
		String[] lComponents = aVertice.split(",");
		return Integer.parseInt( lComponents[1] );
	}
	
	
}
