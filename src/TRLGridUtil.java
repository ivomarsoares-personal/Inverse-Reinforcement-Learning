import java.util.List;

/**
 * Utility class for constructing and querying TRL grid layouts. Provides
 * methods to create square grids, retrieve cells by (row,column) or index and
 * to wire up cell neighbors and border walls.
 */
public class TRLGridUtil {
	
	private static final TRLGridUtil sSharedInstance = new TRLGridUtil();
	public static TRLGridUtil getSharedInstance(){
		return sSharedInstance;
	}	
	private TRLGridUtil(){}
	
	
	public IRLCell retrieveCell( final int aRowIndex, final int aColumnIndex, final IRLGrid aRLGrid ){
		List<IRLCell> lCellList = aRLGrid.getCellList();
		for( int lCellIndex = 0; lCellIndex < lCellList.size(); lCellIndex++ ){
			final IRLCell lCell = lCellList.get(lCellIndex);
			if( lCell.getRowIndex() == aRowIndex && lCell.getColumnIndex() == aColumnIndex ){
				return lCell;
			}
			
		}
		return null;
	}
	
	public IRLCell retrieveCell( final int aCellIndex, final IRLGrid aRLGrid ){
		List<IRLCell> lCellList = aRLGrid.getCellList();
		for( int lCellIndex = 0; lCellIndex < lCellList.size(); lCellIndex++ ){
			final IRLCell lCell = lCellList.get(lCellIndex);
			if( lCell.getIndex() == aCellIndex ){
				return lCell;
			}
		}
		return null;
	}
	
	public IRLGrid createSquareGrid( int aNumberOfRows ) {		
		IRLGrid lSquareGrid = createGrid(aNumberOfRows, aNumberOfRows, true );
		return lSquareGrid;		
	}
	
	
	public IRLGrid createGrid( final int aNumberOfRows, final int aNumberOfColumns, final boolean createOuterWalls ) {

		int lIndex = 0;

		IRLGrid lGrid = (IRLGrid) TRLFactory.createRLObject( IRL.sGRID );
		lGrid.setNumberOfRows(aNumberOfRows);
		lGrid.setNumberOfColumns(aNumberOfColumns);
		

		IRLWall lNorthWall = null;
		IRLWall lWestWall = null;
		IRLWall lSouthWall = null;
		IRLWall lEastWall = null;
		
		// Creating walls
		if (createOuterWalls) {
		
			// North wall
			lNorthWall = TRLWallUtil.getSharedInstance().createWall(lGrid, -1, 0, 0, aNumberOfColumns, 0);
			
			// West wall
			lWestWall = TRLWallUtil.getSharedInstance().createWall(lGrid, -1, 0, 0, 0, aNumberOfRows);
	
			// South wall
			lSouthWall = TRLWallUtil.getSharedInstance().createWall(lGrid,-1,  0, aNumberOfRows, aNumberOfColumns, aNumberOfRows);
			
			// East wall
			lEastWall = TRLWallUtil.getSharedInstance().createWall(lGrid, -1,   aNumberOfColumns, 0, aNumberOfColumns, aNumberOfRows);
		}
		
		// Creating cells
		for( int lRowIndex = 0; lRowIndex < aNumberOfRows; lRowIndex++ ){			
			for( int lColumnIndex = 0; lColumnIndex < aNumberOfColumns; lColumnIndex++ ){

				final IRLCell lCell = (IRLCell) TRLFactory.createRLObject(IRLGridComponent.sCELL);

				lCell.setRowIndex(lRowIndex);
				lCell.setColumnIndex(lColumnIndex);
				lCell.setIndex(lIndex);				
				lIndex++;

				lGrid.getCellList().add(lCell);

				// Setting walls
				
				if( !createOuterWalls ){
					continue;
				}

				// North wall
				if( lRowIndex == 0 ){
					lCell.setNorthWall(lNorthWall);
					lNorthWall.getSouthCellsList().add(lCell);
				}

				// West wall
				if( lColumnIndex == 0 ){
					lCell.setWestWall(lWestWall);
					lWestWall.getEastCellsList().add(lCell);
				}

				// South Wall
				if( lRowIndex == aNumberOfRows - 1 ){
					lCell.setSouthWall(lSouthWall);
					lSouthWall.getNorthCellsList().add(lCell);
				}

				// East Wall
				if( lColumnIndex == aNumberOfColumns - 1 ){
					lCell.setEastWall(lEastWall);
					lEastWall.getWestCellsList().add(lCell);
				}				
			}						
		}	
		
		List<IRLCell> lCellList = lGrid.getCellList();
		
		for( int lCellIndex = 0; lCellIndex < lCellList.size(); lCellIndex++ ){
			final IRLCell lCell = lCellList.get(lCellIndex);
			
			// Setting neighbors				
			lCell.setNorthCell(retrieveCell(lCell.getRowIndex()-1, lCell.getColumnIndex(),   lGrid));
			lCell.setEastCell(retrieveCell( lCell.getRowIndex(),   lCell.getColumnIndex()+1, lGrid));
			lCell.setSouthCell(retrieveCell(lCell.getRowIndex()+1, lCell.getColumnIndex(),   lGrid));
			lCell.setWestCell(retrieveCell( lCell.getRowIndex(),   lCell.getColumnIndex()-1, lGrid));
		}
		

		return lGrid;		
	}
}
