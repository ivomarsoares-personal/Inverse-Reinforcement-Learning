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

		return createWall(aGrid, lInitialVerticeXCoordinate, lInitialVerticeYCoordinate, lFinalVerticeXCoordinate, lFinalVerticeYCoordinate);
	}
	
	public IRLWall createWall(IRLGrid aGrid, int aInitialVerticeXCoordinate, int aInitialVerticeYCoordinate, int aFinalVerticeXCoordinate, int  aFinalVerticeYCoordinate) {
		IRLWall lWall = (IRLWall) TRLFactory.createRLObject(IRLGridComponent.sWALL);
		lWall.setInitialVerticeXCoordinate(aInitialVerticeXCoordinate);
		lWall.setInitialVerticeYCoordinate(aInitialVerticeYCoordinate);
		lWall.setFinalVerticeXCoordinate(aFinalVerticeXCoordinate);
		lWall.setFinalVerticeYCoordinate(aFinalVerticeYCoordinate);
		aGrid.getWallList().add(lWall);

		// Determine orientation and populate adjacent cells lists
		int lMinX = Math.min(aInitialVerticeXCoordinate, aFinalVerticeXCoordinate);
		int lMaxX = Math.max(aInitialVerticeXCoordinate, aFinalVerticeXCoordinate);
		int lMinY = Math.min(aInitialVerticeYCoordinate, aFinalVerticeYCoordinate);
		int lMaxY = Math.max(aInitialVerticeYCoordinate, aFinalVerticeYCoordinate);

		int lRows = aGrid.getNumberOfRows();
		int lCols = aGrid.getNumberOfColumns();

		// Horizontal wall: same Y coordinate
		if( aInitialVerticeYCoordinate == aFinalVerticeYCoordinate ){
			int lY = aInitialVerticeYCoordinate;
			// columns covered by the wall are lMinX .. lMaxX-1 (cells sit between integer vertex coordinates)
			for( int c = lMinX; c < lMaxX; c++ ){
				// South cell is the cell below the wall (row = Y)
				int lSouthRow = lY;
				if( lSouthRow >= 0 && lSouthRow < lRows && c >= 0 && c < lCols ){
					IRLCell lCell = TRLGridUtil.getSharedInstance().retrieveCell(lSouthRow, c, aGrid);
					if( lCell != null ){
						// The wall is the north wall of the south cell
						lCell.setNorthWall(lWall);
						lWall.getSouthCellsList().add(lCell);
					}
				}

				// North cell is the cell above the wall (row = Y - 1)
				int lNorthRow = lY - 1;
				if( lNorthRow >= 0 && lNorthRow < lRows && c >= 0 && c < lCols ){
					IRLCell lCell = TRLGridUtil.getSharedInstance().retrieveCell(lNorthRow, c, aGrid);
					if( lCell != null ){
						// The wall is the south wall of the north cell
						lCell.setSouthWall(lWall);
						lWall.getNorthCellsList().add(lCell);
					}
				}
			}
		}
		// Vertical wall: same X coordinate
		else if( aInitialVerticeXCoordinate == aFinalVerticeXCoordinate ){
			int lX = aInitialVerticeXCoordinate;
			// rows covered by the wall are lMinY .. lMaxY-1
			for( int r = lMinY; r < lMaxY; r++ ){
				// East cell is the cell to the right of the wall (column = X)
				int lEastCol = lX;
				if( r >= 0 && r < lRows && lEastCol >= 0 && lEastCol < lCols ){
					IRLCell lCell = TRLGridUtil.getSharedInstance().retrieveCell(r, lEastCol, aGrid);
					if( lCell != null ){
						// The wall is the west wall of the east cell
						lCell.setWestWall(lWall);
						lWall.getEastCellsList().add(lCell);
					}
				}

				// West cell is the cell to the left of the wall (column = X - 1)
				int lWestCol = lX - 1;
				if( r >= 0 && r < lRows && lWestCol >= 0 && lWestCol < lCols ){
					IRLCell lCell = TRLGridUtil.getSharedInstance().retrieveCell(r, lWestCol, aGrid);
					if( lCell != null ){
						// The wall is the east wall of the west cell
						lCell.setEastWall(lWall);
						lWall.getWestCellsList().add(lCell);
					}
				}
			}
		}

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
	
	public boolean isHorizontalWall(final int aInitialVerticeYCoordinate, final int aFinalVerticeYCoordinate ) {
		return aInitialVerticeYCoordinate == aFinalVerticeYCoordinate;
	}
	
	public boolean isVerticalWall(final int aInitialVerticeXCoordinate, final int aFinalVerticeXCoordinate ) {
		return aInitialVerticeXCoordinate == aFinalVerticeXCoordinate;
	}
	
}