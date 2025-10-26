import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;


/**
 * Swing component responsible for rendering the grid, policy arrows, Q-values
 * and the initial/absorbing states. It observes the grid/agent model and
 * repaints itself when notified.
 */
public class TRLGridPanel extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IRLGrid fGrid;
	private IRLAgent fAgent;


	private final static int sGRID_MARGIN = 10;
	
	private final static Color sGRID_COLOR = Color.black;
	private final static Color sCELL_ID_COLOR = Color.black;
	private final static BasicStroke sWALL_STROKE =  new BasicStroke(4);
	
	private final static Color sPOLICY_ACTION_ARROW_COLOR =  Color.BLUE;
	private final static BasicStroke sPOLICY_ACTION_ARROW_STROKE =  new BasicStroke(3);
	
	private final static Font sCELL_ID_FONT     = new Font ("Monospaced", Font.BOLD, 16);
	private final static Font sVERTICES_COORDINATES_FONT     = new Font ("Monospaced", Font.PLAIN, 12);


	private int fGridXInPixels;
	private int fGridYInPixels;
	private double fCellWidthInPixels;
	private double fCellHeightInPixels;
	private int fGridLengthInPixels;

	
	private boolean fDisplayCellIds;
	private boolean fDisplayVerticesCoordinates;
	private boolean fDisplayPolicyActionArrows;
	private boolean fDisplayPolicyStateValues;
	private boolean fDisplayQValues;
	

	@Override
	public void update(Observable aObservable, Object aObject) {
		repaint();
	}

	@Override
	public void paint(Graphics aGraphics) {
		super.paint(aGraphics);

		if( getGrid() == null ){
			return;
		}
		
		this.setBackground(Color.WHITE);

		drawGrid(this, aGraphics);
		drawWalls(this, aGraphics);
		drawCellId(this, aGraphics);
		drawVerticesCoordinates(this, aGraphics);
		drawInitialStateAndGoalState( this, aGraphics);
		drawPolicyActionArrowAndStateValue(this, aGraphics);
		drawQValues( this, aGraphics );

	}

	public void drawGrid( final Component aCanvas, final Graphics aGraphics ){
		Graphics2D lGraphics2D = (Graphics2D)aGraphics;
		lGraphics2D.setColor(sGRID_COLOR);		

		int lCanvasWidth = aCanvas.getWidth();
		int lCanvasHeight = aCanvas.getHeight();

		fGridLengthInPixels = lCanvasWidth;
		boolean lCanvasHeightIsSmaller = false;
		if( lCanvasHeight < lCanvasWidth ){
			fGridLengthInPixels = lCanvasHeight;
			lCanvasHeightIsSmaller = true;
		}

		fGridLengthInPixels -= 2 * sGRID_MARGIN;


		if( lCanvasHeightIsSmaller ) {
			fGridXInPixels =  (int) (( lCanvasWidth - fGridLengthInPixels ) / 2.0);
			fGridYInPixels = sGRID_MARGIN;
		}
		else {
			fGridXInPixels =  sGRID_MARGIN;
			fGridYInPixels = (int) (( lCanvasHeight - fGridLengthInPixels ) / 2.0);
		}



		// Drawing cells lines.
		lGraphics2D.setStroke(new BasicStroke(1));
		int lNumberOfRows    = getGrid().getNumberOfRows();
		int lNumberOfColumns = getGrid().getNumberOfColumns();

		fCellHeightInPixels = (fGridLengthInPixels / (double) lNumberOfRows );
		fCellWidthInPixels  = (fGridLengthInPixels / (double) lNumberOfColumns );

		double lYInPixels = fGridYInPixels + fCellHeightInPixels; 
		for( int lRowIndex = 0; lRowIndex < lNumberOfRows - 1; lRowIndex++ ){
			lGraphics2D.drawLine(fGridXInPixels, (int) lYInPixels, fGridXInPixels+fGridLengthInPixels, (int)lYInPixels);
			lYInPixels += fCellHeightInPixels;
		}

		double lXInPixels = fGridXInPixels + fCellWidthInPixels;
		for( int lColumnIndex = 0; lColumnIndex < lNumberOfColumns - 1; lColumnIndex++ ){
			lGraphics2D.drawLine((int)lXInPixels, fGridYInPixels, (int)lXInPixels, fGridYInPixels + fGridLengthInPixels);
			lXInPixels += fCellWidthInPixels;
		}
	}
	
	public void drawWalls( final Component aCanvas, final Graphics aGraphics ){
		
		Graphics2D lGraphics2D = (Graphics2D)aGraphics;
		
		// Drawing walls.
		List<IRLWall> lWallList = fGrid.getWallList();
		for(int lWallIndex = 0; lWallIndex < lWallList.size(); lWallIndex++) {
			IRLWall lWall = lWallList.get(lWallIndex);
			int lInitialVerticeXCoordinate = lWall.getInitialVerticeXCoordinate();
			int lInitialVerticeYCoordinate = lWall.getInitialVerticeYCoordinate();
			int lFinalVerticeXCoordinate   = lWall.getFinalVerticeXCoordinate();
			int lFinalVerticeYCoordinate   = lWall.getFinalVerticeYCoordinate();

			// Map the wall vertex coordinates (in grid units) to pixel coordinates.
			// The top-left corner of the grid in pixels is (fGridXInPixels, fGridYInPixels).
			// Assume the wall vertex coordinates refer to grid lines between cells, so
			// multiply by the cell width/height to get offsets.
			double x1 = fGridXInPixels + lInitialVerticeXCoordinate * fCellWidthInPixels;
			double y1 = fGridYInPixels + lInitialVerticeYCoordinate * fCellHeightInPixels;
			double x2 = fGridXInPixels + lFinalVerticeXCoordinate * fCellWidthInPixels;
			double y2 = fGridYInPixels + lFinalVerticeYCoordinate * fCellHeightInPixels;

			// Draw wall line (walls are horizontal or vertical).
			lGraphics2D.setColor(sGRID_COLOR);
			lGraphics2D.setStroke(sWALL_STROKE);
			lGraphics2D.drawLine((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2));
		}
	}

	
	private void drawInitialStateAndGoalState( final Component aCanvas, final Graphics aGraphics ){
		
		if( fAgent == null ){
			return;
		}
		
		Graphics2D lGraphics2D = (Graphics2D)aGraphics;
		lGraphics2D.setColor(Color.blue);
		
		IRLState lInitialState = fAgent.getInitialState();
		IRLState lAbsorbingState = fAgent.getAbsorbingState();
		
		int lInitialStateRowIndex = lInitialState.getCell().getRowIndex();
		int lInitialStateColumnIndex = lInitialState.getCell().getColumnIndex();

		int lAbsorbingStateRowIndex = lAbsorbingState.getCell().getRowIndex();
		int lAbsorbingStateColumnIndex = lAbsorbingState.getCell().getColumnIndex();
		
		lGraphics2D.drawRect((int) ( fGridXInPixels + lInitialStateColumnIndex * fCellWidthInPixels + 2 ), (int) ( fGridYInPixels + lInitialStateRowIndex * fCellHeightInPixels + 2 ), (int) (fCellWidthInPixels - 4),(int) (fCellHeightInPixels - 4));
		lGraphics2D.drawOval((int) ( fGridXInPixels + lAbsorbingStateColumnIndex * fCellWidthInPixels + 2 ), (int) ( fGridYInPixels + lAbsorbingStateRowIndex * fCellHeightInPixels + 2 ), (int) (fCellWidthInPixels - 4),(int) (fCellHeightInPixels - 4));
	}
	
	
	private void drawCellId(  final Component aCanvas, final Graphics aGraphics ){
		
		if( !fDisplayCellIds ){
			return;
		}

		Graphics2D lGraphics2D = (Graphics2D)aGraphics;
		lGraphics2D.setColor(sCELL_ID_COLOR); 
		lGraphics2D.setFont (sCELL_ID_FONT);

		final List<IRLCell> lCellList = getGrid().getCellList();

		for( int lCellIndex = 0; lCellIndex < lCellList.size(); lCellIndex++ ){

			final IRLCell lCell            = lCellList.get(lCellIndex);
			final int     lCellRowIndex    = lCell.getRowIndex();
			final int     lCellColumnIndex = lCell.getColumnIndex();

			String lCellIdAsString = lCell.getIndex()+"";
			String lCellCoordAsString = "(" + lCellColumnIndex + "," + lCellRowIndex + ")";

			// Compute bounding sizes for id and coordinate using current font
			FontMetrics lFontMetrics = aGraphics.getFontMetrics();
			Rectangle2D lIdBounds = lFontMetrics.getStringBounds(lCellIdAsString, lGraphics2D);
			Rectangle2D lCoordBounds = lFontMetrics.getStringBounds(lCellCoordAsString, lGraphics2D);

			int lCellXInPixels = (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels );
			int lCellYInPixels = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels );
			int lCellWidthInPixels = (int) (fCellWidthInPixels);
			int lCellHeightInPixels = (int) (fCellHeightInPixels);

			// Horizontal centers for each string
			int lIdX = (int) Math.round(lCellXInPixels + (lCellWidthInPixels - lIdBounds.getWidth()) / 2.0);
			int lCoordX = (int) Math.round(lCellXInPixels + (lCellWidthInPixels - lCoordBounds.getWidth()) / 2.0);

			// Vertical centering for the two-line block
			double lTotalTextHeight = lIdBounds.getHeight() + lCoordBounds.getHeight();
			double lTopOfTextBlock = lCellYInPixels + (lCellHeightInPixels - lTotalTextHeight) / 2.0;

			int lIdBaseline = (int) Math.round(lTopOfTextBlock + lFontMetrics.getAscent());
			int lCoordBaseline = (int) Math.round(lTopOfTextBlock + lIdBounds.getHeight() + lFontMetrics.getAscent());

			// Draw both strings
			lGraphics2D.drawString(lCellIdAsString, lIdX, lIdBaseline);
			lGraphics2D.drawString(lCellCoordAsString, lCoordX, lCoordBaseline);

		}
	}
	
	private void drawVerticesCoordinates(  final Component aCanvas, final Graphics aGraphics ){ 
		if( !fDisplayVerticesCoordinates ){
			return;
		}
		
		Graphics2D lGraphics2D = (Graphics2D)aGraphics;
		lGraphics2D.setColor(sCELL_ID_COLOR);
		lGraphics2D.setFont (sVERTICES_COORDINATES_FONT);
		FontMetrics lFontMetrics = aGraphics.getFontMetrics();

		// The vertices are the intersections of the grid lines. There are
		// (columns + 1) vertices along X and (rows + 1) along Y. The top-left
		// vertex is (0,0). Coordinates increase to the right (x) and down (y).
		int lNumberOfRows    = getGrid().getNumberOfRows();
		int lNumberOfColumns = getGrid().getNumberOfColumns();

		for (int vy = 0; vy <= lNumberOfRows; vy++) {
			for (int vx = 0; vx <= lNumberOfColumns; vx++) {
				String lCoord = "(" + vx + "," + vy + ")";

				// Pixel position of the vertex
				double xPixel = fGridXInPixels + vx * fCellWidthInPixels;
				double yPixel = fGridYInPixels + vy * fCellHeightInPixels;

				// Measure text size
				Rectangle2D lBounds = lFontMetrics.getStringBounds(lCoord, lGraphics2D);
				double textWidth = lBounds.getWidth();
				int ascent = lFontMetrics.getAscent();
				int descent = lFontMetrics.getDescent();

				// Place the text so its right edge is 2px left of the vertex.
				int drawX = (int) Math.round(xPixel - 2.0 - textWidth);

				// Default: draw text below the vertex (top = vertexY + 2)
				int drawY;
				if (vy == lNumberOfRows) {
					// Bottom-most grid line: show the coordinates above the line.
					// We want the bottom of the text to be 2px above the vertex, so
					// baseline = (vertexY - 2) - descent
					drawY = (int) Math.round(yPixel - 2.0 - descent);
				} else {
					// Other vertices: show coordinates 2px below the vertex.
					drawY = (int) Math.round(yPixel + 2.0 + ascent);
				}

				// Safety clamp so text stays inside the canvas
				if (drawX < 0) drawX = 0;
				if (drawY < ascent) drawY = ascent;

				lGraphics2D.drawString(lCoord, drawX, drawY);
			}
		}
	}
	
	
	
	private void drawStringCentered( final String aString, final int aXInPixels, final int aYInPixels, final int aWidthInPixels, final int aHeightInPixels, final Graphics aGraphics  ){
		Graphics2D lGraphics2D = (Graphics2D)aGraphics;
		FontMetrics lFontMetrics = aGraphics.getFontMetrics();
		Rectangle2D lCellIdBoundingRectangle = lFontMetrics.getStringBounds(aString, lGraphics2D);

		int lCellIdXInPixels = (int) (aXInPixels + ( aWidthInPixels - lCellIdBoundingRectangle.getWidth() )  / 2.0 );
		int lCellIdYInPixels = (int) (aYInPixels + ( aHeightInPixels - lCellIdBoundingRectangle.getHeight() ) / 2.0 + lFontMetrics.getAscent() );
		lGraphics2D.drawString( aString, lCellIdXInPixels, lCellIdYInPixels );
	}

	
	private void drawPolicyActionArrowAndStateValue( final Component aCanvas, final Graphics aGraphics ){
		Graphics2D lGraphics2D = (Graphics2D)aGraphics;

		if( getAgent() == null ){
			return;
		}

		IRLPolicy lPolicy = getAgent().getPolicy();

		if( lPolicy == null ){
			return;
		}

		lGraphics2D.setFont (sCELL_ID_FONT);
		lGraphics2D.setStroke(sPOLICY_ACTION_ARROW_STROKE);
		lGraphics2D.setColor(sPOLICY_ACTION_ARROW_COLOR);

		List<TRLStateActionPair> lStateActionPairList = lPolicy.getStateActionPairList();

		for( int lStateActionPairIndex = 0; lStateActionPairIndex < lStateActionPairList.size(); lStateActionPairIndex++ ){
			TRLStateActionPair lStateActionPair = lStateActionPairList.get(lStateActionPairIndex);
			IRLAction lAction = lStateActionPair.getAction();
			Double lStateValue = lStateActionPair.getInitialStateValue();
			String lStateValueAsString = String.format("%.2f", lStateValue );


			final IRLCell lCell            = getGrid().getCellList().get(lStateActionPairIndex);
			final int     lCellRowIndex    = lCell.getRowIndex();
			final int     lCellColumnIndex = lCell.getColumnIndex();

			int lInitialX = 0;
			int lInitialY = 0;
			int lFinalX   = 0;
			int lFinalY   = 0;

			if( lAction instanceof TRLActionMoveNorth ){
				lInitialX = (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + fCellWidthInPixels / 2.0 );
				lFinalX = lInitialX;
				lInitialY = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels + fCellHeightInPixels  );
				lFinalY = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels  );
			} 
			else if( lAction instanceof TRLActionMoveEast ){
				lInitialX = (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels );
				lFinalX =  (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + fCellWidthInPixels );
				lInitialY = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels + fCellHeightInPixels / 2.0  );
				lFinalY = lInitialY;
			}
			else if( lAction instanceof TRLActionMoveSouth ){
				lInitialX = (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + fCellWidthInPixels / 2.0 );
				lFinalX = lInitialX;
				lInitialY = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels  );
				lFinalY = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels + fCellHeightInPixels  );
			}
			else if( lAction instanceof TRLActionMoveWest ){
				lInitialX =  (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + fCellWidthInPixels );
				lFinalX = (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels );
				lInitialY = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels + fCellHeightInPixels / 2.0  );
				lFinalY = lInitialY;
			}
			else{
				lInitialX =  (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels  );
				lFinalX = (int) ( fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + fCellWidthInPixels / 2.0);
				lInitialY = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels );
				lFinalY = (int) ( fGridYInPixels + lCellRowIndex * fCellHeightInPixels + fCellHeightInPixels / 2.0  );
			}

			if( fDisplayPolicyActionArrows && lAction != null ){
				drawArrow(aGraphics, lInitialX, lInitialY, lFinalX, lFinalY);
			}
			if( fDisplayPolicyStateValues ) {
				lGraphics2D.drawString(lStateValueAsString, (int)((lInitialX + lFinalX ) / 2.0 + 10.0), (int)((lInitialY + lFinalY)/2.0 -10.0));
			}
		}
	}
	
	private void drawArrow(Graphics aGraphics, int aX1, int aY1, int aX2, int aY2) {
        Graphics2D lGraphics2D = (Graphics2D) aGraphics.create();

        final int lArrowSize = (int) (fCellWidthInPixels*0.1);
        
        double dx = aX2 - aX1, dy = aY2 - aY1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(aX1, aY1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        lGraphics2D.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        lGraphics2D.drawLine(0, 0, len, 0);
        lGraphics2D.fillPolygon(new int[] {len, len-lArrowSize, len-lArrowSize, len},
                      new int[] {0, -lArrowSize, lArrowSize, 0}, 4);
    }
	
	
	private void drawQValues( final Component aCanvas, final Graphics aGraphics ){
		Graphics2D lGraphics2D = (Graphics2D)aGraphics;

		if( !fDisplayQValues || getAgent() == null ){
			return;
		}

		IRLPolicy lPolicy = getAgent().getPolicy();

		if( lPolicy == null ){
			return;
		}

		lGraphics2D.setColor(Color.red);
		lGraphics2D.setFont (sCELL_ID_FONT);


		HashMap<IRLState, HashMap<IRLAction, Double>> lQValueHashMap = lPolicy.getQValueHashMap();
		Set<IRLState> lStateKeySet = lQValueHashMap.keySet();

		for (Iterator<IRLState> StateIterator = lStateKeySet.iterator(); StateIterator.hasNext();) {
			IRLState lState = (IRLState) StateIterator.next();
			IRLCell lCell = lState.getCell();
			final int     lCellRowIndex    = lCell.getRowIndex();
			final int     lCellColumnIndex = lCell.getColumnIndex();

			HashMap<IRLAction, Double> lActionQValueHashMap = lQValueHashMap.get(lState);
			Set<IRLAction> lActionKeySet = lActionQValueHashMap.keySet();
			
			if( lState.getAbsorbing() ){
				continue;
			}

			for (Iterator<IRLAction> lActionIterator = lActionKeySet.iterator(); lActionIterator.hasNext();) {
				IRLAction lAction = (IRLAction) lActionIterator.next();
				Double lQValue = lActionQValueHashMap.get(lAction);

				
				int lX = 0;
				int lY = 0;
				
				if( lAction instanceof TRLActionMoveNorth ){
					lX = (int) (fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + fCellWidthInPixels / 2.0);
					lY = (int) (fGridYInPixels + lCellRowIndex * fCellHeightInPixels + 14 );
					
				} else if( lAction instanceof TRLActionMoveEast ){
					lX = (int) (fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + fCellWidthInPixels - 40);
					lY = (int) (fGridYInPixels + lCellRowIndex * fCellHeightInPixels + fCellHeightInPixels / 2.0 );					
				} else if( lAction instanceof TRLActionMoveSouth ){
					lX = (int) (fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + fCellWidthInPixels / 2.0);
					lY = (int) (fGridYInPixels + lCellRowIndex * fCellHeightInPixels + fCellHeightInPixels -  4 );
					
				} else if( lAction instanceof TRLActionMoveWest ){
					lX = (int) (fGridXInPixels + lCellColumnIndex * fCellWidthInPixels + 4);
					lY = (int) (fGridYInPixels + lCellRowIndex * fCellHeightInPixels + fCellHeightInPixels / 2.0 );
				}
				
				lGraphics2D.drawString(String.format("%.2f", lQValue ), lX, lY);

			}
		}
	}
	
	


	public IRLGrid getGrid() {
		return fGrid;
	}

	public void setGrid(IRLGrid aGrid) {
		fGrid = aGrid;
	}

	public IRLAgent getAgent() {
		return fAgent;
	}

	public void setAgent(IRLAgent aAgent) {
		fAgent = aAgent;
	}
	
	public boolean isDisplayCellIds() {
		return fDisplayCellIds;
	}

	public void setDisplayCellIds(boolean fDrawCellIds) {
		this.fDisplayCellIds = fDrawCellIds;
	}
	
	public boolean isDisplayVerticesCoordinates() {
		return fDisplayVerticesCoordinates;
	}

	public void setDisplayVerticesCoordinates(boolean fDisplayVerticesCoordinates) {
		this.fDisplayVerticesCoordinates = fDisplayVerticesCoordinates;
	}
	
	public boolean isDisplayPolicyActionArrows() {
		return fDisplayPolicyActionArrows;
	}

	public void setDisplayPolicyActionArrows(boolean fDisplayPolicyActionArrows) {
		this.fDisplayPolicyActionArrows = fDisplayPolicyActionArrows;
	}

	public boolean isDisplayPolicyStateValues() {
		return fDisplayPolicyStateValues;
	}

	public void setDisplayPolicyStateValues(boolean fDisplayPolicyStateValues) {
		this.fDisplayPolicyStateValues = fDisplayPolicyStateValues;
	}
	
	public boolean isDisplayQValues() {
		return fDisplayQValues;
	}

	public void setDisplayQValues(boolean fDisplayQValues) {
		this.fDisplayQValues = fDisplayQValues;
	}
}
