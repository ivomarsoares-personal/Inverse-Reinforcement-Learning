import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


/**
 * Main Swing frame that composes the GridWorld UI. Provides menus to create
 * the grid/agent/reward, run value iteration and IRL, and to display results
 * using the embedded panels.
 */
public class TRLMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TRLGridPanel      fGridPanel;
	private IRLGrid           fGrid;
	private IRLAgent          fAgent;
	private TRLIRLRewardPanel fIRLRewardPanel;
	private JTabbedPane       fTabbedPane;
	private int fNumberOfRows = 5;
	private int fNumberOfColumns = 5;
	private boolean fInverseReinforcementLearningDisplayAllGraphsIsSelected;
	private boolean fValueIterationParametersSet = false;
	private boolean fInverseReinforcementLearningParametersSet = false;
	

	public TRLMainFrame( ){

		//make sure the program exits when the frame closes
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Reinforcement Learning Gridworld");

		setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );

		//This will center the JFrame in the middle of the screen
		setLocationRelativeTo(null);

		JMenuBar lMenuBar = new JMenuBar();
		
		JMenu lFileMenu = new JMenu("File");
		JMenuItem lNewMenuItem = new JMenuItem("New");
		JMenuItem lSaveMenuItem = new JMenuItem("Save");
		JMenuItem lOpenMenuItem = new JMenuItem("Open...");
		JMenuItem lExitMenuItem = new JMenuItem("Exit Gridworld");
		
		lNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
		));
		
		lSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
		));
		
		lOpenMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
        ));
		
		lExitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_Q,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
		));
		
		
		lFileMenu.add(lNewMenuItem);
		lFileMenu.add(lOpenMenuItem);
		lFileMenu.add(lSaveMenuItem);
		lFileMenu.addSeparator();
     	lFileMenu.add(lExitMenuItem);

		JMenu lGridMenu = new JMenu("Create");
		JMenu lCreateWallMenu = new JMenu("Wall");
		JMenuItem lAddWallMenuItem = new JMenuItem("Add");
		JMenuItem lRemoveWallMenuItem = new JMenuItem("Remove");
		lCreateWallMenu.add(lAddWallMenuItem);
		lCreateWallMenu.add(lRemoveWallMenuItem);
		
		JMenuItem lCreateAgentMenuItem = new JMenuItem("Agent");		
		JMenuItem lCreateRewardFunctionMenuItem = new JMenuItem("Reward Function");
		lGridMenu.add(lCreateWallMenu);
		lCreateWallMenu.setEnabled(true);
		
		lCreateAgentMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
		));
		
		lCreateRewardFunctionMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_R,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
		));
		
		lGridMenu.add(lCreateAgentMenuItem);
		lGridMenu.add(lCreateRewardFunctionMenuItem);
		
		JMenu lRLMenu = new JMenu("Reinforcement Learning");
		JMenu lRLExplorationStrategiesMenu = new JMenu("Action Selection");
		JMenu lRLModelBasedMenu = new JMenu("Model Based");
		JMenu lRLModelFreeMenu = new JMenu("Model Free");
		JMenu lRLOtherAlgorithmsMenu = new JMenu("Other Algorithms");
		JMenuItem lRLRunMenuItem    = new JMenuItem("Run");
		
		JMenuItem lEpsilonGreedyMenuItem  = new JMenuItem("Epsilon-Greedy");
		JMenuItem lValueIterationMenuItem = new JMenuItem("Value Iteration");
		JMenuItem lIRLMenuItem            = new JMenuItem("Inverse Reinforcement Learning");
		JMenuItem lQlearningMenuItem      = new JMenuItem("Q-Learning");
				
		lRLMenu.add(lRLExplorationStrategiesMenu);
		lRLMenu.add(lRLModelBasedMenu);
		lRLMenu.add(lRLModelFreeMenu);
		lRLMenu.add(lRLOtherAlgorithmsMenu);
		lRLMenu.addSeparator();
		lRLMenu.add(lRLRunMenuItem);
		lRLExplorationStrategiesMenu.add(lEpsilonGreedyMenuItem);
		lRLModelBasedMenu.add(lValueIterationMenuItem);
		lRLModelFreeMenu.add(lQlearningMenuItem);
		lRLOtherAlgorithmsMenu.add(lIRLMenuItem);
		
		
		JMenu lDisplayMenu = new JMenu("Display");
		JCheckBoxMenuItem lCellIdCheckBoxMenuItem = new JCheckBoxMenuItem("Cell Id");
		JCheckBoxMenuItem lWallIdCheckBoxMenuItem = new JCheckBoxMenuItem("Wall Id");
		
		
		JCheckBoxMenuItem lVerticesCoordinatesCheckBoxMenuItem = new JCheckBoxMenuItem("Vertices Coordinages");		
		JMenu lPolicyMenu = new JMenu("Policy");
		JCheckBoxMenuItem lPolicyArrowsCheckBoxMenuItem = new JCheckBoxMenuItem("Arrows");
		JCheckBoxMenuItem lPolicyStateValuesBoxMenuItem = new JCheckBoxMenuItem("State Values");
		lPolicyMenu.add(lPolicyArrowsCheckBoxMenuItem);
		lPolicyMenu.add(lPolicyStateValuesBoxMenuItem);
		JCheckBoxMenuItem lQValuesCheckBoxMenuItem = new JCheckBoxMenuItem("QValues");
		
		lCellIdCheckBoxMenuItem.setSelected(true);
		lVerticesCoordinatesCheckBoxMenuItem.setSelected(false);
		lWallIdCheckBoxMenuItem.setSelected(false);
		lPolicyArrowsCheckBoxMenuItem.setSelected(true);
		lPolicyStateValuesBoxMenuItem.setSelected(true);
		lQValuesCheckBoxMenuItem.setSelected(true);
		lDisplayMenu.add(lCellIdCheckBoxMenuItem);
		lDisplayMenu.add(lWallIdCheckBoxMenuItem);
		lDisplayMenu.add(lVerticesCoordinatesCheckBoxMenuItem);
		lDisplayMenu.add(lPolicyMenu);
		lDisplayMenu.add(lQValuesCheckBoxMenuItem);
		
		JMenu lHelpMenu = new JMenu("Help");
		JMenuItem lAboutMenuItem = new JMenuItem("About GridWorld");
		lHelpMenu.add(lAboutMenuItem);

		JMenu lDebugMenu = new JMenu("Debug");
		JMenuItem lPrintWallsInfoMenuItem = new JMenuItem("Print Walls Info");
		JMenuItem lPrintCellsInfoMenuItem = new JMenuItem("Print Cells Info");
		lDebugMenu.add(lPrintWallsInfoMenuItem);
		lDebugMenu.add(lPrintCellsInfoMenuItem);
		
		
		lMenuBar.add(lFileMenu);
		lMenuBar.add(lGridMenu);
		lMenuBar.add(lRLMenu);
		lMenuBar.add(lDisplayMenu);
		lMenuBar.add(lDebugMenu);
		lMenuBar.add(lHelpMenu);
		
		fGridPanel = new TRLGridPanel();
		
		fTabbedPane = new JTabbedPane();
		fTabbedPane.addTab("Grid", fGridPanel);


		add(lMenuBar, BorderLayout.NORTH);
		add( fTabbedPane, BorderLayout.CENTER);

		fGridPanel.setDisplayCellIds( lCellIdCheckBoxMenuItem.isSelected() );
		fGridPanel.setDisplayWallIds(lWallIdCheckBoxMenuItem.isSelected());
		fGridPanel.setDisplayPolicyActionArrows( lPolicyArrowsCheckBoxMenuItem.isSelected() );
		fGridPanel.setDisplayPolicyStateValues( lPolicyStateValuesBoxMenuItem.isSelected() );
		fGridPanel.setDisplayQValues(lQValuesCheckBoxMenuItem.isSelected());

		//make sure the JFrame is visible
		setVisible(true);
		
		lNewMenuItem.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				fGrid = null;
				fGridPanel.setGrid(null);
				fGridPanel.setAgent(null);
				fTabbedPane.removeAll();
				fTabbedPane.addTab("Grid",fGridPanel);
				
				JTextField lGridWorldNameTextField = new JTextField(25); 
				lGridWorldNameTextField.setText("untitled");
				JTextField lNumberOfRowsTextField = new JTextField(5); 
				lNumberOfRowsTextField.setText("");
				JTextField lNumberOfColumnsTextField = new JTextField(5); 
				lNumberOfColumnsTextField.setText("");

				
				JPanel lCreateGridWorldPanel = new JPanel();
							
				lCreateGridWorldPanel.add(new JLabel("GridWorld Name:"));
				lCreateGridWorldPanel.add(lGridWorldNameTextField);
				
				lCreateGridWorldPanel.add(new JLabel("Number of Rows:"));
				lCreateGridWorldPanel.add(lNumberOfRowsTextField);
				lNumberOfRowsTextField.setText(fNumberOfRows + "");
				lCreateGridWorldPanel.add(new JLabel("Number of Columns:"));
				lCreateGridWorldPanel.add(lNumberOfColumnsTextField);
				lNumberOfColumnsTextField.setText(fNumberOfRows + "");
				
				int lResult = JOptionPane.showConfirmDialog(null, lCreateGridWorldPanel, "GridWorld information", JOptionPane.OK_CANCEL_OPTION);
				if (lResult != JOptionPane.OK_OPTION) {
					return;
				}
				
				if( lGridWorldNameTextField.getText().trim().isEmpty() ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "GridWorld name cannot be empty.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}

				try{
					fNumberOfRows = Integer.parseInt(lNumberOfRowsTextField.getText());
					fNumberOfColumns = Integer.parseInt(lNumberOfColumnsTextField.getText());
				}
				catch( NumberFormatException aNumberFormatException ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Not a number.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Create the grid first, then set its name and wire it to the UI.
				fGrid = TRLGridUtil.getSharedInstance().createGrid(fNumberOfRows, fNumberOfColumns, true);				
				fGrid.setName(lGridWorldNameTextField.getText());
				setTitle("GridWorld - " + fGrid.getName() + " - Reinforcement Learning");
				fGridPanel.setGrid(fGrid);
				((ARLGrid)fGrid).addObserver(fGridPanel);
				fGridPanel.repaint();
			}
		});
		
		lSaveMenuItem.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				if( fGrid == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create grid first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( fAgent == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create agent first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( fGrid.getRewardFunction() == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create reward function first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try{
					int lResult = TRLFileUtil.getSharedInstance().saveToFolder(fGrid);
					if ( lResult == JFileChooser.APPROVE_OPTION ){
						JOptionPane.showMessageDialog(TRLMainFrame.this, "Saved successfully.", "Save" ,JOptionPane.INFORMATION_MESSAGE);
					}
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Error saving file: " + e.getMessage(), "Error" ,JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
		
		lOpenMenuItem.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				IRLGrid lLoadedGrid = TRLFileUtil.getSharedInstance().loadGridWorld();
				if( lLoadedGrid == null ){
					return; // user cancelled or load failed
				}
				// Wire loaded grid into UI
				fGrid = lLoadedGrid;
				fGridPanel.setGrid(fGrid);
				((ARLGrid)fGrid).addObserver(fGridPanel);
				// if there are agents, select first and wire it
				if( fGrid.getAgentList() != null && !fGrid.getAgentList().isEmpty() ){
					fAgent = fGrid.getAgentList().get(0);
					fGridPanel.setAgent(fAgent);
					((ARLAgent)fAgent).addObserver(fGridPanel);
				}
				// refresh tabs and title
				fTabbedPane.removeAll();
				fTabbedPane.addTab("Grid", fGridPanel);
				setTitle("GridWorld - " + fGrid.getName() + " - Reinforcement Learning");
				fGridPanel.repaint();
			}
		});
		
		lExitMenuItem.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				System.exit(0);
			}
		});

		lCellIdCheckBoxMenuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {				
				fGridPanel.setDisplayCellIds( lCellIdCheckBoxMenuItem.isSelected() );
				fGridPanel.repaint();
			}			
		});
		
		lWallIdCheckBoxMenuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {				
				fGridPanel.setDisplayWallIds( lWallIdCheckBoxMenuItem.isSelected() );
				fGridPanel.repaint();
			}			
		});
		
		lVerticesCoordinatesCheckBoxMenuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {				
				fGridPanel.setDisplayVerticesCoordinates( lVerticesCoordinatesCheckBoxMenuItem.isSelected() );
				fGridPanel.repaint();
			}			
		});
		
		
		lPolicyArrowsCheckBoxMenuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {				
				fGridPanel.setDisplayPolicyActionArrows(lPolicyArrowsCheckBoxMenuItem.isSelected());
				fGridPanel.repaint();
			}			
		});
		
		lPolicyStateValuesBoxMenuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {				
				fGridPanel.setDisplayPolicyStateValues(lPolicyStateValuesBoxMenuItem.isSelected());
				fGridPanel.repaint();
			}			
		});
		
		lQValuesCheckBoxMenuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {				
				fGridPanel.setDisplayQValues( lQValuesCheckBoxMenuItem.isSelected() );
				fGridPanel.repaint();
			}			
		});
		
		
		lCreateAgentMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fGrid == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create grid first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int lNumberOfRows = fGrid.getNumberOfRows();

				JTextField lInitialStateTextField             = new JTextField(5); 
				lInitialStateTextField.setText( (lNumberOfRows * (lNumberOfRows - 1))+"");
				JTextField lFinalStateTextField               = new JTextField(5); 
				lFinalStateTextField.setText((lNumberOfRows - 1)+"");				
				
				JPanel lCreateAgentPanel = new JPanel();
							
				lCreateAgentPanel.add(new JLabel("Initial State Index:"));
				lCreateAgentPanel.add(lInitialStateTextField);
				lCreateAgentPanel.add(new JLabel("Final State Index:"));
				lCreateAgentPanel.add(lFinalStateTextField);

				int lResult = JOptionPane.showConfirmDialog(null, lCreateAgentPanel, "Agent information", JOptionPane.OK_CANCEL_OPTION);
				if (lResult != JOptionPane.OK_OPTION) {
					return;
				}
				
				int lInitialStateIndex = 0;
				int lFinalStateIndex = 0;
				
				try{
					lInitialStateIndex = Integer.parseInt(lInitialStateTextField.getText());
					lFinalStateIndex = Integer.parseInt(lFinalStateTextField.getText());
				}
				catch(NumberFormatException aNumberFormatException ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Not a number.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
			
				
				int lNumberOfStates = fGrid.getCellList().size();
				if( lInitialStateIndex < 0 || lInitialStateIndex >= lNumberOfStates ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Initial State Index must be greater or equal than 0 and lower than " +  lNumberOfStates + ".", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lFinalStateIndex < 0 || lFinalStateIndex >= lNumberOfStates ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Final State Index must be greater or equal than 0 and lower than " +  lNumberOfStates + ".", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
								
			

				fAgent = TRLAgentUtil.getSharedInstance().createAgent(
						fGrid, 
						lInitialStateIndex, 
						lFinalStateIndex);
				fGridPanel.setAgent(fAgent);
				((ARLAgent)fAgent).addObserver(fGridPanel);
				fGridPanel.repaint();

			}
		});
		
		
		lAddWallMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fGrid == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create grid first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JTextField lInitialVerticeTextField = new JTextField(5);
				JTextField lFinalVerticeTextField = new JTextField(5);
				
				if (!wallInputValidations("add", lInitialVerticeTextField, lFinalVerticeTextField) ) {
					return;
				}
				
				int lInitialVerticeXCoordinate = TRLWallUtil.getSharedInstance().getVerticeXCoordinate( lInitialVerticeTextField.getText() );
				int lInitialVerticeYCoordinate = TRLWallUtil.getSharedInstance().getVerticeYCoordinate( lInitialVerticeTextField.getText() );
				int lFinalVerticeXCoordinate   = TRLWallUtil.getSharedInstance().getVerticeXCoordinate( lFinalVerticeTextField.getText() );
				int lFinalVerticeYCoordinate   = TRLWallUtil.getSharedInstance().getVerticeYCoordinate( lFinalVerticeTextField.getText() );
				
				if( TRLWallUtil.getSharedInstance().wallExists(fGrid, lInitialVerticeXCoordinate, lInitialVerticeYCoordinate, lFinalVerticeXCoordinate, lFinalVerticeYCoordinate)) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Wall with these coordinates already exists.", "Error" ,JOptionPane.ERROR_MESSAGE);
				}
				
				
			
				TRLWallUtil.getSharedInstance().createWall(fGrid, lInitialVerticeTextField.getText(), lFinalVerticeTextField.getText());
				fGridPanel.repaint();
			}			
		});
		
		
		lRemoveWallMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fGrid == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create grid first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( fGrid.getWallList().size() == 4 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "No inner walls to remove.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
			
				
				JTextField lInitialVerticeTextField = new JTextField(5);
				JTextField lFinalVerticeTextField = new JTextField(5);
								
				if (!wallInputValidations("remove", lInitialVerticeTextField, lFinalVerticeTextField) ) {
					return;
				}
				
				int lInitialVerticeXCoordinate = TRLWallUtil.getSharedInstance().getVerticeXCoordinate( lInitialVerticeTextField.getText() );
				int lInitialVerticeYCoordinate = TRLWallUtil.getSharedInstance().getVerticeYCoordinate( lInitialVerticeTextField.getText() );
				int lFinalVerticeXCoordinate   = TRLWallUtil.getSharedInstance().getVerticeXCoordinate( lFinalVerticeTextField.getText() );
				int lFinalVerticeYCoordinate   = TRLWallUtil.getSharedInstance().getVerticeYCoordinate( lFinalVerticeTextField.getText() );
				
				if (lInitialVerticeXCoordinate == 0 && lInitialVerticeYCoordinate == 0 &&
					lFinalVerticeXCoordinate == fGrid.getNumberOfColumns() && lFinalVerticeYCoordinate == 0 ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Cannot remove outer north wall.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (lInitialVerticeXCoordinate == 0 && lInitialVerticeYCoordinate == 0 &&
					lFinalVerticeXCoordinate == 0 && lFinalVerticeYCoordinate == fGrid.getNumberOfRows() ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Cannot remove outer west wall.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (lInitialVerticeXCoordinate == fGrid.getNumberOfColumns() && lInitialVerticeYCoordinate == 0 &&
					lFinalVerticeXCoordinate == fGrid.getNumberOfColumns() && lFinalVerticeYCoordinate == fGrid.getNumberOfRows() ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Cannot remove outer east wall.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (lInitialVerticeXCoordinate == 0 && lInitialVerticeYCoordinate == fGrid.getNumberOfRows() &&
					lFinalVerticeXCoordinate == fGrid.getNumberOfColumns() && lFinalVerticeYCoordinate == fGrid.getNumberOfRows() ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Cannot remove outer south wall.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(!TRLWallUtil.getSharedInstance().wallExists(fGrid, lInitialVerticeXCoordinate, lInitialVerticeYCoordinate, lFinalVerticeXCoordinate, lFinalVerticeYCoordinate)) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Inner wall not found.", "Error" ,JOptionPane.ERROR_MESSAGE);
				}
				
				
				
				TRLWallUtil.getSharedInstance().removeWall(fGrid, lInitialVerticeXCoordinate, lInitialVerticeYCoordinate, lFinalVerticeXCoordinate, lFinalVerticeYCoordinate);
				fGridPanel.repaint();
			}			
		});

		
		
		
		lCreateRewardFunctionMenuItem.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fAgent == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create agent first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}		

				JTextField lRewardAtNonAbsorbingStateTextField = new JTextField(5);
				lRewardAtNonAbsorbingStateTextField.setText(0 + "");
				JTextField lRewardAtAbsorbingStateTextField = new JTextField(5);
				lRewardAtAbsorbingStateTextField.setText(1 + "");
				JTextField lStateRewardListTextField = new JTextField(25);
				

				JPanel lCreateAgentPanel = new JPanel();
				lCreateAgentPanel.add(new JLabel("Reward at non absorbing states:"));
				lCreateAgentPanel.add(lRewardAtNonAbsorbingStateTextField);
				//lCreateAgentPanel.add(Box.createHorizontalStrut(15)); // a spacer
				lCreateAgentPanel.add(new JLabel("Reward at absorbing state:"));
				lCreateAgentPanel.add(lRewardAtAbsorbingStateTextField);
				lCreateAgentPanel.add(new JLabel("State Reward List:"));
				lCreateAgentPanel.add(lStateRewardListTextField); // a spacer
				

				int lResult = JOptionPane.showConfirmDialog(null, lCreateAgentPanel, "Reward Function", JOptionPane.OK_CANCEL_OPTION);
				if (lResult != JOptionPane.OK_OPTION) {
					return;
				}
				double lRewardAtNonAbsorbingStates = 0;
				double lRewardAtAbsorbingState = 0;
				try{
					lRewardAtNonAbsorbingStates = Double.parseDouble(lRewardAtNonAbsorbingStateTextField.getText());
					lRewardAtAbsorbingState = Double.parseDouble(lRewardAtAbsorbingStateTextField.getText());
				}
				catch(NumberFormatException aNumberFormatException ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Not a number.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String lStateRewardListString = lStateRewardListTextField.getText().trim();
				
				IRLRewardFunction lRewardFunction = null;
				
				if(!lStateRewardListString.isEmpty() ){
					String lRegex = "^\\d+:(?:\\d+(?:\\.\\d+)?)(?:,\\d+:(?:\\d+(?:\\.\\d+)?))*$";
					Pattern lPattern = Pattern.compile(lRegex);
					
					if ( !lPattern.matcher(lStateRewardListString).matches()) {						
						JOptionPane.showMessageDialog(TRLMainFrame.this, "State Reward list is not valid. Use S0:R0,S1,R1:S2:R2,... format. Where S0:R0 is the reward at state 0 and so on.", "Error" ,JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String[] lStateRewardListSplit = lStateRewardListString.split(",");
					if (lStateRewardListSplit.length != fGrid.getCellList().size()) {
						JOptionPane.showMessageDialog(TRLMainFrame.this, "State Reward list must have exactly " + fGrid.getCellList().size() + " entries.", "Error" ,JOptionPane.ERROR_MESSAGE);
						return;
					}		
					
					int[] lStateIndices = new int[lStateRewardListSplit.length];
					int lIndex = 0;
					for (String lStateRewardEntry : lStateRewardListSplit) {
						String[] lParts = lStateRewardEntry.split(":");
						int lStateIndex = Integer.parseInt(lParts[0]);
						lStateIndices[lIndex++] = lStateIndex;
						if( lStateIndex < 0 || lStateIndex >= fGrid.getCellList().size() ) {
							JOptionPane.showMessageDialog(TRLMainFrame.this, "State index " + lStateIndex + " is out of bounds. It must be greater or equal than 0 and lower than " + fGrid.getCellList().size() + ".", "Error" ,JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					List<IRLState> lStateList = fAgent.getStateList();
					for (int i = 0; i < lStateIndices.length; i++) {
						IRLState lState = lStateList.get(i);
						if( lState.getIndex() != lStateIndices[i] ) {
							JOptionPane.showMessageDialog(TRLMainFrame.this, "State indices in the State Reward list must be in order and without repetitions.", "Error" ,JOptionPane.ERROR_MESSAGE);
							return;
						}
					}					
					
					lRewardFunction = TRLRewardFunctionUtil.getSharedInstance().createRewardFunction(fAgent, lStateRewardListString);			
				}
				else {
					lRewardFunction = TRLRewardFunctionUtil.getSharedInstance().createRewardFunction(fAgent, lRewardAtNonAbsorbingStates, lRewardAtAbsorbingState);
				}
				fGrid.setRewardFunction(lRewardFunction);
				
			}
		});


		
		lValueIterationMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fAgent == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create agent first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( fGrid.getRewardFunction() == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create reward function first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if ( fGrid.getWallList().size() > 4 ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Cant perform value iteration in a gridworld with inner walls.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				JPanel lValueIterationPanel = new JPanel();
				
				JTextField lDiscountingFactorTextField        = new JTextField(5);
				lDiscountingFactorTextField.setText(0.9+"");
				JTextField lCorrectActionProbabilityTextField = new JTextField(5);
				lCorrectActionProbabilityTextField.setText(0.7+"");
				JTextField lActionNoiseProbabilityTextField   = new JTextField(5);
				lActionNoiseProbabilityTextField.setText(0.3+"");

				
				lValueIterationPanel.add(new JLabel("Discounting Factor:"));
				lValueIterationPanel.add(lDiscountingFactorTextField);
				lValueIterationPanel.add(new JLabel("Correct Action Probability:"));
				lValueIterationPanel.add(lCorrectActionProbabilityTextField);
				lValueIterationPanel.add(new JLabel("Action Noise Probability:"));
				lValueIterationPanel.add(lActionNoiseProbabilityTextField);

				
				int lResult = JOptionPane.showConfirmDialog(null, lValueIterationPanel, "Value Iteration", JOptionPane.OK_CANCEL_OPTION);
				if (lResult != JOptionPane.OK_OPTION) {
					return;
				}

				double lDiscountingFactor = 0;
				double lCorrectActionProbability = 0;
				double lActionNoiseProbability = 0;
				
				try {
					lDiscountingFactor = Double.parseDouble(lDiscountingFactorTextField.getText());
					lCorrectActionProbability = Double.parseDouble(lCorrectActionProbabilityTextField.getText());
					lActionNoiseProbability = Double.parseDouble(lActionNoiseProbabilityTextField.getText());	
				}
				catch(NumberFormatException aNumberFormatException ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Not a number.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lCorrectActionProbability < 0 || lCorrectActionProbability > 1 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Correct Action Probability must be greater or equal than 0 and lower or equal than 1.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lActionNoiseProbability < 0 || lActionNoiseProbability > 1 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Action Noise Probability must be greater or equal than 0 and lower or equal than 1.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( Math.abs(lCorrectActionProbability + lActionNoiseProbability - 1) > 0.00001 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Correct Action Probability and Action Noise Probability must add up to 1.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lDiscountingFactor < 0 || lDiscountingFactor >= 1 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Discounting Factor must be greater or equal than 0 and lower than 1.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				IRLValueIteration lValueIteration = TRLModelBasedUtil.getSharedInstance().createValueIteration(lDiscountingFactor, lCorrectActionProbability, lActionNoiseProbability);
				fAgent.setModelBased(lValueIteration);				
				TRLModelBasedUtil.getSharedInstance().createTransitionProbabilities(fAgent, lCorrectActionProbability, lActionNoiseProbability);	
				fValueIterationParametersSet = true;
			}
		});
		
		
		
		lIRLMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {

				if( fAgent == null ){
					if( fAgent == null ){
						JOptionPane.showMessageDialog(TRLMainFrame.this, "Create agent first.", "Error" ,JOptionPane.ERROR_MESSAGE);
						return;
					}
					return;
				}
				
			
				if ( fGrid.getWallList().size() > 4 ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Cant perform IRL in a gridworld with inner walls.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				

				JTextField lDiscountingFactorTextField        = new JTextField(5);
				lDiscountingFactorTextField.setText(0.9+"");
				JTextField lCorrectActionProbabilityTextField = new JTextField(5);
				lCorrectActionProbabilityTextField.setText(0.7+"");
				JTextField lActionNoiseProbabilityTextField   = new JTextField(5);
				lActionNoiseProbabilityTextField.setText(0.3+"");
				JTextField lRMaxTextField = new JTextField(5);
				lRMaxTextField.setText(1 + "");
				JTextField lMinRegularizationTextField = new JTextField(5);
				lMinRegularizationTextField.setText(0 + "");
				JTextField lMaxRegularizationTextField = new JTextField(5);
				lMaxRegularizationTextField.setText(30 + "");
				JTextField lRegularizationStepTextField = new JTextField(5);
				lRegularizationStepTextField.setText(0.1 + "");
				JCheckBox lDisplayAllGraphsCheckBox = new JCheckBox("DisplayAllGraphs");
				lDisplayAllGraphsCheckBox.setSelected(false);
				

				JPanel lIRLPanel = new JPanel();
				lIRLPanel.add(new JLabel("Discounting Factor:"));
				lIRLPanel.add(lDiscountingFactorTextField);
				lIRLPanel.add(new JLabel("Correct Action Probability:"));
				lIRLPanel.add(lCorrectActionProbabilityTextField);
				lIRLPanel.add(new JLabel("Action Noise Probability:"));
				lIRLPanel.add(lActionNoiseProbabilityTextField);
				lIRLPanel.add(new JLabel("RMax:"));
				lIRLPanel.add(lRMaxTextField);
				lIRLPanel.add(new JLabel("Min Lambda:"));
				lIRLPanel.add(lMinRegularizationTextField);
				lIRLPanel.add(new JLabel("Max Lambda:"));
				lIRLPanel.add(lMaxRegularizationTextField);
				lIRLPanel.add(new JLabel("Step Lambda:"));
				lIRLPanel.add(lRegularizationStepTextField);
				lIRLPanel.add(lDisplayAllGraphsCheckBox);

				int lResult = JOptionPane.showConfirmDialog(null, lIRLPanel, "Inverse Reinforcement Learning", JOptionPane.OK_CANCEL_OPTION);
				if (lResult != JOptionPane.OK_OPTION) {
					return;
				}
				
				// Running now IRL
				double lMinLambda = 0;
				double lMaxLambda = 0;
				double lStepLambda = 0;
				double lDiscountingFactor = 0;
				double lCorrectActionProbability = 0;
				double lActionNoiseProbability = 0;
				double lRMax = 0;
				try{
					lDiscountingFactor = Double.parseDouble(lDiscountingFactorTextField.getText());
					lCorrectActionProbability = Double.parseDouble(lCorrectActionProbabilityTextField.getText());
					lActionNoiseProbability = Double.parseDouble(lActionNoiseProbabilityTextField.getText());

					lMinLambda = Double.parseDouble(lMinRegularizationTextField.getText());
					lMaxLambda = Double.parseDouble(lMaxRegularizationTextField.getText());
					lStepLambda = Double.parseDouble(lRegularizationStepTextField.getText() );
					
					lRMax = Double.parseDouble(lRMaxTextField.getText());
				}
				catch(NumberFormatException aNumberFormatException ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Not a number.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lCorrectActionProbability < 0 || lCorrectActionProbability > 1 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Correct Action Probability must be greater or equal than 0 and lower or equal than 1.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lActionNoiseProbability < 0 || lActionNoiseProbability > 1 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Action Noise Probability must be greater or equal than 0 and lower or equal than 1.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( Math.abs(lCorrectActionProbability + lActionNoiseProbability - 1) > 0.00001 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Correct Action Probability and Action Noise Probability must add up to 1.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lDiscountingFactor < 0 || lDiscountingFactor >= 1 ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Discounting Factor must be greater or equal than 0 and lower than 1.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				IRLValueIteration lValueIteration = TRLModelBasedUtil.getSharedInstance().createValueIteration(lDiscountingFactor, lCorrectActionProbability, lActionNoiseProbability);
				fAgent.setModelBased(lValueIteration);				
				TRLModelBasedUtil.getSharedInstance().createTransitionProbabilities(fAgent, lCorrectActionProbability, lActionNoiseProbability);
				
				IRLInverseReinforcementLearning lIRL = TRLInverseReinforcementLearningUtil.getSharedInstance().createIRL(fAgent, lDiscountingFactor, lCorrectActionProbability, lActionNoiseProbability, lStepLambda, lMinLambda, lMaxLambda, lRMax);
				fAgent.setInverseReinforcementLearning(lIRL);
				fInverseReinforcementLearningDisplayAllGraphsIsSelected = lDisplayAllGraphsCheckBox.isSelected();
				fInverseReinforcementLearningParametersSet = true;
				
			}
		});
		
		lRLRunMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				// Create the dialog (modal so it blocks until closed)
			    JDialog lDialog = new JDialog(TRLMainFrame.this, "Run Settings", true);

			    // Main panel with vertical layout
			    JPanel lMainPanel = new JPanel();
			    lMainPanel.setLayout(new BorderLayout(10, 10));
			    lMainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			    // Panel for form rows (labels + text fields vertically)
			    JPanel lFormPanel = new JPanel();
			    lFormPanel.setLayout(new GridBagLayout());
			    GridBagConstraints lGridBagConstraints = new GridBagConstraints();
			    lGridBagConstraints.insets = new Insets(5, 5, 5, 5);
			    lGridBagConstraints.anchor = GridBagConstraints.WEST;
			    lGridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

			    // Create combo boxes			    
			    String[] lActionSelectionMethods = { "Epsilon-Greedy", "Softmax" };
			    JComboBox<String> lActionSelectionMethodComboBox = new JComboBox<>(lActionSelectionMethods);
			    lActionSelectionMethodComboBox.setSelectedIndex(0);
			    
			    String[] lSolutionMethods = { "Model Based", "Model Free", "Inverse Reinforcement Learning" };
			    JComboBox<String> lSolutionMethodComboBox = new JComboBox<>(lSolutionMethods);
			    lSolutionMethodComboBox.setSelectedIndex(1);
			    
			    String[] lModelBasedMethods = { "Value Iteration" };
			    JComboBox<String> lModelBasedMethodsComboBox = new JComboBox<>(lModelBasedMethods);

			    String[] lModelFreeMethods = { "Q-Learning" };
			    JComboBox<String> lModelFreeMethodsComboBox = new JComboBox<>(lModelFreeMethods);
			    
			    int lRow = 0;

			    // Helper function-like pattern: add one row (label + field)
			    
			    lGridBagConstraints.gridy = lRow;
			    lGridBagConstraints.gridx = 0;
			    lFormPanel.add(new JLabel("Action Selection:"), lGridBagConstraints);
			    lGridBagConstraints.gridx = 1;
			    lFormPanel.add(lActionSelectionMethodComboBox, lGridBagConstraints);

			    
			    lRow++;
			    lGridBagConstraints.gridy = lRow;
			    lGridBagConstraints.gridx = 0;
			    lFormPanel.add(new JLabel("Solution Method:"), lGridBagConstraints);
			    lGridBagConstraints.gridx = 1;
			    lFormPanel.add(lSolutionMethodComboBox, lGridBagConstraints);

			    lRow++;
			    lGridBagConstraints.gridy = lRow;
			    lGridBagConstraints.gridx = 0;
			    lFormPanel.add(new JLabel("Model Based:"), lGridBagConstraints);
			    lGridBagConstraints.gridx = 1;
			    lFormPanel.add(lModelBasedMethodsComboBox, lGridBagConstraints);

			    lRow++;
			    lGridBagConstraints.gridy = lRow;
			    lGridBagConstraints.gridx = 0;
			    lFormPanel.add(new JLabel("Model Free:"), lGridBagConstraints);
			    lGridBagConstraints.gridx = 1;
			    lFormPanel.add(lModelFreeMethodsComboBox, lGridBagConstraints);

			    // Add form panel to center of main panel
			    lMainPanel.add(lFormPanel, BorderLayout.CENTER);

			    // Buttons panel (OK and Cancel at the bottom)
			    JPanel lButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

			    JButton lRunButton = new JButton("Run");
			    JButton lCancelButton = new JButton("Cancel");

			    lButtonsPanel.add(lRunButton);
			    lButtonsPanel.add(lCancelButton);

			    // Add buttons panel to bottom of main panel
			    lMainPanel.add(lButtonsPanel, BorderLayout.SOUTH);

			    // Add main panel to dialog
			    lDialog.getContentPane().add(lMainPanel);

			    // Dialog size and position
			    lDialog.pack();
			    lDialog.setLocationRelativeTo(TRLMainFrame.this);

			    // Behavior for buttons
			    lRunButton.addActionListener(new ActionListener() {
			        @Override
			        public void actionPerformed(ActionEvent aActionEvent) {
			            // Here you read the text fields and do something
			        	
			        	String lSolutionMethod =  (String) lSolutionMethodComboBox.getSelectedItem();			        	
			        	if(lSolutionMethod.equals("Model Based") ) {			        		
			        		String lModelBased = (String) lModelBasedMethodsComboBox.getSelectedItem();			        		
			        		if (lModelBased.equals("Value Iteration")){
			        			if( ! fValueIterationParametersSet ){
			        				JOptionPane.showMessageDialog(TRLMainFrame.this, "Set Value Iteration parameters first.", "Error" ,JOptionPane.ERROR_MESSAGE);
			        				return;
			        			}
			        			fAgent.getModelBased().execute(fAgent);
			        		}			        		
			        	}			        	
			        	else if (lSolutionMethod.equals("Inverse Reinforcement Learning") ) {
			        		if( ! fInverseReinforcementLearningParametersSet ){
			        				JOptionPane.showMessageDialog(TRLMainFrame.this, "Set Inverse Reinforcement Learning parameters first.", "Error" ,JOptionPane.ERROR_MESSAGE);
			        				return;
			        		}
			        		fAgent.getModelBased().execute(fAgent);
			        		boolean lSolutionFound = fAgent.getInverseReinforcementLearning().execute(fAgent);
			        		if( fInverseReinforcementLearningDisplayAllGraphsIsSelected || lSolutionFound){
								fIRLRewardPanel = new TRLIRLRewardPanel();
								fIRLRewardPanel.setRewardFunction(fAgent.getInverseReinforcementLearning().getRewardFunction());
								fIRLRewardPanel.setRMax(fAgent.getInverseReinforcementLearning().getRMax());
								fIRLRewardPanel.setRegularization(fAgent.getInverseReinforcementLearning().getLambda());
								fIRLRewardPanel.generateChart();
								fTabbedPane.add("Reward Function (" + fAgent.getInverseReinforcementLearning().getRMax() + "," + String.format("%.2f", fAgent.getInverseReinforcementLearning().getLambda()) + ")" , fIRLRewardPanel);
								if( ! fInverseReinforcementLearningDisplayAllGraphsIsSelected ){
						            lDialog.dispose();  // close dialog
									return;
								}
							}
			        	}

			            lDialog.dispose();  // close dialog
			        }
			    });

			    lCancelButton.addActionListener(e -> {
			        // Just close the dialog without using the values
			        lDialog.dispose();
			    });

			    // Show dialog (modal)
			    lDialog.setVisible(true);

			}
		});
		
		lPrintWallsInfoMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fGrid == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create grid first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				System.out.println("Number of walls: " + fGrid.getWallList().size() + "\n\n");
				
				List<IRLWall> lWallsList = fGrid.getWallList();
				for (int lWallIndex = 0; lWallIndex < lWallsList.size(); lWallIndex++ ) {
					IRLWall lWall = lWallsList.get(lWallIndex);
					System.out.println( String.format("Wall %d: Initial Vertice (%d,%d), Final Vertice (%d,%d)", 
							lWall.getId(),
							lWall.getInitialVerticeXCoordinate(),
							lWall.getInitialVerticeYCoordinate(),
							lWall.getFinalVerticeXCoordinate(),
							lWall.getFinalVerticeYCoordinate()));
					
					List<IRLCell> lNorthCellsList = lWall.getNorthCellsList();
					if (! lNorthCellsList.isEmpty()) {
						System.out.print("  North Cells: ");
						for (IRLCell lCell : lNorthCellsList) {
							System.out.print( lCell.getIndex() + " " );
						}
					}
					
					List<IRLCell> lSouthCellsList = lWall.getSouthCellsList();
					if (! lSouthCellsList.isEmpty()) {
						System.out.print("  South Cells: ");
						for (IRLCell lCell : lSouthCellsList) {
							System.out.print( lCell.getIndex() + " " );
						}
					}
					List<IRLCell> lEastCellsList  = lWall.getEastCellsList();
					if (! lEastCellsList.isEmpty()) {
						System.out.print("  East Cells: ");
						for (IRLCell lCell : lEastCellsList) {
							System.out.print( lCell.getIndex() + " " );
						}
					}
					
					List<IRLCell> lWestCellsList  = lWall.getWestCellsList();
					if (! lWestCellsList.isEmpty()) {
						System.out.print("  West Cells: ");
						for (IRLCell lCell : lWestCellsList) {
							System.out.print( lCell.getIndex() + " " );
						}
					}
					
					System.out.println("\n\n");
				}				
			}			
		});
		
		lPrintCellsInfoMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fGrid == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create grid first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				List<IRLCell> lCellsList = fGrid.getCellList();
				for (int lCellIndex = 0; lCellIndex < lCellsList.size(); lCellIndex++ ) {
					IRLCell lCell = lCellsList.get(lCellIndex);
					System.out.println( String.format("Cell %d: Coordinates (%d,%d)", 
							lCellIndex,
							lCell.getColumnIndex(),
							lCell.getRowIndex()));
					
					IRLCell lNorthCell = lCell.getNorthCell();
					if (lNorthCell != null)	System.out.println("  North Cell: " + lNorthCell.getIndex() );
					
					IRLCell lSouthCell = lCell.getSouthCell();
					if (lSouthCell != null) System.out.println("  South Cell: " + lSouthCell.getIndex() );
					
					IRLCell lEastCell  = lCell.getEastCell();
					if (lEastCell != null) System.out.println("  East Cell: " + lEastCell.getIndex() );
					
					IRLCell lWestCell  = lCell.getWestCell();
					if (lWestCell != null) System.out.println("  West Cell: " + lWestCell.getIndex() );
					
					IRLWall lNorthWall = lCell.getNorthWall();
					if (lNorthWall != null) System.out.println(String.format("  North Wall: (%d,%d), (%d,%d)", lNorthWall.getInitialVerticeXCoordinate(), lNorthWall.getInitialVerticeYCoordinate(),lNorthWall.getFinalVerticeXCoordinate(), lNorthWall.getFinalVerticeYCoordinate() ));
					
					IRLWall lSouthWall = lCell.getSouthWall();
					if (lSouthWall != null) System.out.println(String.format("  South Wall: (%d,%d), (%d,%d)", lSouthWall.getInitialVerticeXCoordinate(), lSouthWall.getInitialVerticeYCoordinate(),lSouthWall.getFinalVerticeXCoordinate(), lSouthWall.getFinalVerticeYCoordinate() ));
					
					IRLWall lEastWall  = lCell.getEastWall();
					if (lEastWall != null) System.out.println(String.format("  East Wall: (%d,%d), (%d,%d)", lEastWall.getInitialVerticeXCoordinate(), lEastWall.getInitialVerticeYCoordinate(),lEastWall.getFinalVerticeXCoordinate(), lEastWall.getFinalVerticeYCoordinate() ));
					
					IRLWall lWestWall  = lCell.getWestWall();
					if (lWestWall != null) System.out.println(String.format("  West Wall: (%d,%d), (%d,%d)", lWestWall.getInitialVerticeXCoordinate(), lWestWall.getInitialVerticeYCoordinate(),lWestWall.getFinalVerticeXCoordinate(), lWestWall.getFinalVerticeYCoordinate() ));
					
					System.out.println("\n\n");
				}
			}
		});
		
		lAboutMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				JOptionPane.showMessageDialog(TRLMainFrame.this, "Ivomar Brito Soares, ivomarbsoares@gmail.com", "Gridworld" ,JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	/**
	 * Perform some validations to verify if the wall input data is valid.
	 * 
	 * @param action
	 * @param aInitialVerticeTextField
	 * @param aFinalVerticeTextField
	 * @return
	 */
	private boolean wallInputValidations(final String action, final JTextField aInitialVerticeTextField, final JTextField aFinalVerticeTextField) {
		JPanel lCreateWallPanel = new JPanel();
		lCreateWallPanel.add(new JLabel("Initial Vertice: "));
		lCreateWallPanel.add(aInitialVerticeTextField);
		lCreateWallPanel.add(new JLabel("Final Vertice:"));
		lCreateWallPanel.add(aFinalVerticeTextField);
		
		int lResult = JOptionPane.showConfirmDialog(null, lCreateWallPanel, "Wall to " + action + " information", JOptionPane.OK_CANCEL_OPTION);
		if (lResult != JOptionPane.OK_OPTION) {
			return false;
		}
		
		if( aInitialVerticeTextField.getText().trim().isEmpty() ){
			JOptionPane.showMessageDialog(TRLMainFrame.this, "Initial vertice cannot be empty.", "Error" ,JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if( aFinalVerticeTextField.getText().trim().isEmpty() ){
			JOptionPane.showMessageDialog(TRLMainFrame.this, "Final vertice cannot be empty.", "Error" ,JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		String lVerticeRegex = "^\\d+,\\d+$";
		Pattern lPattern = Pattern.compile(lVerticeRegex);				
		if ( !lPattern.matcher(aInitialVerticeTextField.getText()).matches()) {
			JOptionPane.showMessageDialog(TRLMainFrame.this, "Initial vertice format is not valid. Use X,Y format. Where X and Y are non-negative integers.", "Error" ,JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if ( !lPattern.matcher(aFinalVerticeTextField.getText()).matches() ) {
			JOptionPane.showMessageDialog(TRLMainFrame.this, "Final vertice format is not valid. Use X,Y format. Where X and Y are non-negative integers.", "Error" ,JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		int lInitialVerticeXCoordinate = TRLWallUtil.getSharedInstance().getVerticeXCoordinate( aInitialVerticeTextField.getText() );
		int lInitialVerticeYCoordinate = TRLWallUtil.getSharedInstance().getVerticeYCoordinate( aInitialVerticeTextField.getText() );
		int lFinalVerticeXCoordinate   = TRLWallUtil.getSharedInstance().getVerticeXCoordinate( aFinalVerticeTextField.getText() );
		int lFinalVerticeYCoordinate   = TRLWallUtil.getSharedInstance().getVerticeYCoordinate( aFinalVerticeTextField.getText() );
		
		if( lInitialVerticeXCoordinate != lFinalVerticeXCoordinate && lInitialVerticeYCoordinate != lFinalVerticeYCoordinate ){
			JOptionPane.showMessageDialog(TRLMainFrame.this, "Only horizontal and vertical walls are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if( lInitialVerticeXCoordinate < 0 || lFinalVerticeXCoordinate < 0 || lInitialVerticeYCoordinate < 0 || lFinalVerticeYCoordinate < 0) {
			JOptionPane.showMessageDialog(TRLMainFrame.this, "Vertices coordinates cannot be smaller than 0.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if( lFinalVerticeXCoordinate > fNumberOfColumns ) {
			JOptionPane.showMessageDialog(TRLMainFrame.this, "The final vertice X coordinate " + lFinalVerticeXCoordinate + " cannot be greater than the number of columns of the grid " + fNumberOfColumns + "." , "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if( lFinalVerticeYCoordinate > fNumberOfRows ) {
			JOptionPane.showMessageDialog(TRLMainFrame.this, "The final vertice Y coordinate " + lFinalVerticeYCoordinate + " cannot be greater than the number of rows of the grid " + fNumberOfRows + "." , "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}			
		return true;
	}	
}
