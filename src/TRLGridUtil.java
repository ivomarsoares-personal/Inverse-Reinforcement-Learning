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
		
		IRLGrid lSquareGrid = createGrid(aNumberOfRows, aNumberOfRows);
		return lSquareGrid;
		
	}
	
	
	public IRLGrid createGrid( final int aNumberOfRows, final int aNumberOfColums ){

		assert aNumberOfRows == aNumberOfColums;

		if( aNumberOfRows != aNumberOfColums ){
			return null;
		}

		int lIndex = 0;

		IRLGrid lRLSquareGrid = (IRLGrid) TRLFactory.createRLObject( IRL.sGRID );
		lRLSquareGrid.setNumberOfRows(aNumberOfRows);
		lRLSquareGrid.setNumberOfColumns(aNumberOfColums);

		for( int lRowIndex = 0; lRowIndex < aNumberOfRows; lRowIndex++ ){			
			for( int lColumnIndex = 0; lColumnIndex < aNumberOfColums; lColumnIndex++ ){

				final IRLCell lCell = (IRLCell) TRLFactory.createRLObject(IRLGridComponent.sCELL);

				lCell.setRowIndex(lRowIndex);
				lCell.setColumnIndex(lColumnIndex);
				lCell.setIndex(lIndex);
				lIndex++;

				lRLSquareGrid.getCellList().add(lCell);

				// Setting walls

				// North wall
				if( lRowIndex == 0 ){
					IRLWall lNorthWall = (IRLWall) TRLFactory.createRLObject(IRLGridComponent.sWALL);
					lCell.setNorthWall(lNorthWall);
				}

				// West wall
				if( lColumnIndex == 0 ){
					IRLWall lWestWall = (IRLWall) TRLFactory.createRLObject(IRLGridComponent.sWALL);
					lCell.setWestWall(lWestWall);
				}

				// South Wall
				if( lRowIndex == aNumberOfRows - 1 ){
					IRLWall lSouthWall = (IRLWall) TRLFactory.createRLObject(IRLGridComponent.sWALL);
					lCell.setSouthWall(lSouthWall);
				}

				// East Wall
				if( lColumnIndex == aNumberOfColums - 1 ){
					IRLWall lEastWall = (IRLWall) TRLFactory.createRLObject(IRLGridComponent.sWALL);
					lCell.setEastWall(lEastWall);
				}				
			}						
		}	
		
		List<IRLCell> lCellList = lRLSquareGrid.getCellList();
		
		for( int lCellIndex = 0; lCellIndex < lCellList.size(); lCellIndex++ ){
			final IRLCell lCell = lCellList.get(lCellIndex);
			
			// Setting neighbors				
			lCell.setNorthCell(retrieveCell(lCell.getRowIndex()-1, lCell.getColumnIndex(),   lRLSquareGrid));
			lCell.setEastCell(retrieveCell( lCell.getRowIndex(),   lCell.getColumnIndex()+1, lRLSquareGrid));
			lCell.setSouthCell(retrieveCell(lCell.getRowIndex()+1, lCell.getColumnIndex(),   lRLSquareGrid));
			lCell.setWestCell(retrieveCell( lCell.getRowIndex(),   lCell.getColumnIndex()-1, lRLSquareGrid));
		}
		

		return lRLSquareGrid;		
	}
}
