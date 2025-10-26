import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TRLFileUtil {

	private static final TRLFileUtil sSharedInstance = new TRLFileUtil();
	public static TRLFileUtil getSharedInstance(){
		return sSharedInstance;
	}
	private TRLFileUtil(){}

	// State kept between the parameterless loadXXX calls
	private File fLoadedFolder = null;

	public int saveToFolder( final IRLGrid aGrid ) throws Exception {
		if( aGrid == null ){
			throw new IllegalArgumentException("aGrid cannot be null");
		}

		// Ask user to select parent directory where the grid folder will be created
		JFileChooser lFileChooser = new JFileChooser();
		lFileChooser.setDialogTitle("Select folder to save grid world");
		lFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int lResult = lFileChooser.showSaveDialog(null);
		if( lResult != JFileChooser.APPROVE_OPTION ){
			return lResult; // user cancelled
		}

		File lSelectedParent = lFileChooser.getSelectedFile();
		if( lSelectedParent == null ) return lResult;

		String lFolderName = aGrid.getName();
		if( lFolderName == null || lFolderName.trim().isEmpty() ){
			lFolderName = JOptionPane.showInputDialog(null, "Enter name for the grid folder:", "GridWorld");
			if( lFolderName == null || lFolderName.trim().isEmpty() ){
				return lResult; // user cancelled or empty
			}
		}

		// Create target folder inside selectedParent
		File lTargetFolder = new File(lSelectedParent, lFolderName);
		if( lTargetFolder.exists() ){
			if( !lTargetFolder.isDirectory() ){
				throw new IOException("Target path exists and is not a directory: " + lTargetFolder.getAbsolutePath());
			}
		} else {
			if( !lTargetFolder.mkdirs() ){
				throw new IOException("Could not create target folder: " + lTargetFolder.getAbsolutePath());
			}
		}

		// Write CSV files
		saveGridToFile(aGrid, lTargetFolder);
		saveWallsToFile(aGrid, lTargetFolder);
		saveAgentsToFile(aGrid, lTargetFolder);
		
		return lResult;
	}

	private void saveGridToFile(final IRLGrid aGrid, final File aDirectory) throws IOException {
		File lOut = new File(aDirectory, "grid.csv");
		try ( PrintWriter lPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(lOut))) ){
			// Header
			lPrintWriter.println("Name,NumberOfRows,NumberOfColumns");
			String lName = aGrid.getName() == null ? "" : escapeCsv(aGrid.getName());
			lPrintWriter.println(String.format("%s,%d,%d", lName, aGrid.getNumberOfRows(), aGrid.getNumberOfColumns()));
		}
	}

	private void saveWallsToFile(final IRLGrid aGrid, final File aDirectory) throws IOException {
		File lOut = new File(aDirectory, "walls.csv");
		try ( PrintWriter lPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(lOut))) ){
			// Header
			lPrintWriter.println("Id,InitialVerticeXCoordinate,InitialVerticeYCoordinate,FinalVerticeXCoordinate,FinalVerticeYCoordinate");
			List<IRLWall> lWalls = aGrid.getWallList();
			if( lWalls != null ){
				for( int i = 0; i < lWalls.size(); i++ ){
					IRLWall lW = lWalls.get(i);
					int lId = i; // use index as id (no public id getter available)
					lPrintWriter.println(String.format("%d,%d,%d,%d,%d", lId, lW.getInitialVerticeXCoordinate(), lW.getInitialVerticeYCoordinate(), lW.getFinalVerticeXCoordinate(), lW.getFinalVerticeYCoordinate()));
				}
			}
		}
	}

	private void saveAgentsToFile(final IRLGrid aGrid, final File aDirectory) throws IOException {
		File lOut = new File(aDirectory, "agents.csv");
		try ( PrintWriter lPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(lOut))) ){
			// Header
			lPrintWriter.println("Id,InitialStateCellIndex,AbsorbingStateCellIndex,DiscountingFactor,CorrectActionProbability,ActionNoiseProbability");
			List<IRLAgent> lAgents = aGrid.getAgentList();
			if( lAgents != null ){
				for( int i = 0; i < lAgents.size(); i++ ){
					IRLAgent lAgent = lAgents.get(i);
					int lId = i;
					int lInit = -1;
					int lAbs = -1;
					if( lAgent.getInitialState() != null && lAgent.getInitialState().getIndex() != null ) lInit = lAgent.getInitialState().getIndex();
					if( lAgent.getAbsorbingState() != null && lAgent.getAbsorbingState().getIndex() != null ) lAbs = lAgent.getAbsorbingState().getIndex();
					double lDiscountingFactor = lAgent.getDiscountingFactor();
					double lCorrectActionProbability = lAgent.getCorrectActionProbability();
					double lActionNoiseProbability = lAgent.getActionNoiseProbability();
					
					lPrintWriter.println(String.format("%d,%d,%d,%f,%f,%f", lId, lInit, lAbs, lDiscountingFactor,lCorrectActionProbability, lActionNoiseProbability) );
				}
			}
		}
	}

	private String escapeCsv(final String aString){
		if( aString == null ) return "";
		boolean lNeed = aString.contains(",") || aString.contains("\"") || aString.contains("\n") || aString.contains("\r");
		String lEscaped = aString.replace("\"", "\"\"");
		if( lNeed ) return "\"" + lEscaped + "\"";
		return lEscaped;
	}

	/**
	 * Let the user pick the folder containing the saved gridworld and load it.
	 * Returns the loaded IRLGrid or null on cancel/error.
	 */
	public IRLGrid loadGridWorld(){
		// Let the user select the folder that contains the gridworld files
		JFileChooser lFileChooser = new JFileChooser();
		lFileChooser.setDialogTitle("Select GridWorld folder to load");
		lFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int lResult = lFileChooser.showOpenDialog(null);
		if( lResult != JFileChooser.APPROVE_OPTION ){
			return null; // user cancelled
		}

		File lSelectedFolder = lFileChooser.getSelectedFile();
		if( lSelectedFolder == null ) return null;

		// Check required files
		List<String> lMissing = new ArrayList<String>();
		File lGridFile = new File(lSelectedFolder, "grid.csv");
		File lWallsFile = new File(lSelectedFolder, "walls.csv");
		File lAgentsFile = new File(lSelectedFolder, "agents.csv");
		if( !lGridFile.exists() ) lMissing.add("grid.csv");
		if( !lWallsFile.exists() ) lMissing.add("walls.csv");
		if( !lAgentsFile.exists() ) lMissing.add("agents.csv");
		if( !lMissing.isEmpty() ){
			StringBuilder lMsg = new StringBuilder();
			lMsg.append("Missing files: ");
			for( int i = 0; i < lMissing.size(); i++ ){
				if( i > 0 ) lMsg.append(", ");
				lMsg.append(lMissing.get(i));
			}
			JOptionPane.showMessageDialog(null, lMsg.toString(), "Missing files", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		// Save selected folder for subsequent calls
		fLoadedFolder = lSelectedFolder;
		try{
			IRLGrid lGrid = loadGrid();
			if( lGrid == null ) return null;
			loadWalls(lGrid);
			loadAgents(lGrid);
			return lGrid;
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error loading gridworld: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Parse grid.csv and build an IRLGrid with cells and neighbors. Returns the grid or null on error.
	 */
	public IRLGrid loadGrid(){

		File lGridFile = new File(fLoadedFolder, "grid.csv");

		try ( BufferedReader lReader = new BufferedReader(new FileReader(lGridFile)) ){
			String lHeader = lReader.readLine(); // header
			String lLine = null;
			while( (lLine = lReader.readLine()) != null ){
				if( lLine.trim().isEmpty() ) continue;
				List<String> lTokens = parseCsvLine(lLine);
				String lName = lTokens.size() > 0 ? lTokens.get(0) : "";
				int lRows = lTokens.size() > 1 ? Integer.parseInt(lTokens.get(1)) : 0;
				int lCols = lTokens.size() > 2 ? Integer.parseInt(lTokens.get(2)) : lRows;
				
				IRLGrid lGrid = TRLGridUtil.getSharedInstance().createGrid(lRows, lCols);
				lGrid.setName(lName);
				
				return lGrid;
			}
		}			
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error reading grid.csv: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read walls.csv and create IRLWall objects. Attach walls to grid cells as appropriate.
	 */
	private void loadWalls(IRLGrid aGrid){

		File lWallsFile = new File(fLoadedFolder, "walls.csv");

		try ( BufferedReader lReader = new BufferedReader(new FileReader(lWallsFile)) ){
			String lHeader = lReader.readLine();
			String lLine = null;
			while( (lLine = lReader.readLine()) != null ){
				if( lLine.trim().isEmpty() ) continue;
				String[] lParts = lLine.split(",");
				if( lParts.length < 5 ) continue;
				int lId = Integer.parseInt(lParts[0]);
				int lIx = Integer.parseInt(lParts[1]);
				int lIy = Integer.parseInt(lParts[2]);
				int lFx = Integer.parseInt(lParts[3]);
				int lFy = Integer.parseInt(lParts[4]);
				
				TRLWallUtil.getSharedInstance().createWall(aGrid, lIx, lIy, lFx, lFy);
			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error reading walls.csv: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Read agents.csv and create IRLAgent objects (using TRLAgentUtil helper).
	 */
	private void loadAgents(IRLGrid aGrid){
		
		File lAgentsFile = new File(fLoadedFolder, "agents.csv");
		
		try ( BufferedReader lReader = new BufferedReader(new FileReader(lAgentsFile)) ){
			String lHeader = lReader.readLine();
			String lLine = null;
			while( (lLine = lReader.readLine()) != null ){
				if( lLine.trim().isEmpty() ) continue;
				String[] lParts = lLine.split(",");
				if( lParts.length < 3 ) continue;
				int lId = Integer.parseInt(lParts[0]);
				int lInit = Integer.parseInt(lParts[1]);
				int lAbs = Integer.parseInt(lParts[2]);
				double lDiscountingFactor = Double.parseDouble(lParts[3]);
				double lCorrectActionProbability = Double.parseDouble(lParts[4]);
				double lActionNoiseProbability = Double.parseDouble(lParts[5]);
				// create agent using default probabilities (0.7, 0.3)
				TRLAgentUtil.getSharedInstance().createAgent(aGrid, lInit, lAbs, lDiscountingFactor, lCorrectActionProbability, lActionNoiseProbability);

			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error reading agents.csv: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Very small CSV parser that handles quoted values (double quotes doubled inside) and commas.
	 */
	private List<String> parseCsvLine(final String aLine){
		List<String> lTokens = new ArrayList<String>();
		StringBuilder lToken = new StringBuilder();
		boolean lInQuotes = false;
		for( int i = 0; i < aLine.length(); i++ ){
			char c = aLine.charAt(i);
			if( lInQuotes ){
				if( c == '"' ){
					// check for escaped quote
					if( i + 1 < aLine.length() && aLine.charAt(i+1) == '"' ){
						lToken.append('"');
						i++; // skip next
					} else {
						lInQuotes = false;
					}
				}
				else {
					lToken.append(c);
				}
			}
			else {
				if( c == ',' ){
					lTokens.add(lToken.toString());
					lToken.setLength(0);
				}
				else if( c == '"' ){
					lInQuotes = true;
				}
				else {
					lToken.append(c);
				}
			}
		}
		// add last token
		lTokens.add(lToken.toString());
		return lTokens;
	}

}