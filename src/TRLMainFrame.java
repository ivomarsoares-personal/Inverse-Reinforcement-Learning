import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


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

	public TRLMainFrame( ){

		//make sure the program exits when the frame closes
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("GridWorld - Inverse Reinforcement Learning");

		setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );

		//This will center the JFrame in the middle of the screen
		setLocationRelativeTo(null);

		JMenuBar lMenuBar = new JMenuBar();

		JMenu lGridMenu = new JMenu("Create");
		JMenuItem lCreateGridMenuItem = new JMenuItem("1. Grid");
		JMenuItem lCreateWallMenuItem = new JMenuItem("2. Wall");
		JMenuItem lCreateAgentMenuItem = new JMenuItem("3. Agent");		
		JMenuItem lCreateRewardFunctionMenuItem = new JMenuItem("4. Reward Function");
		lGridMenu.add(lCreateGridMenuItem);
		lGridMenu.add(lCreateWallMenuItem);
		// Disable wall creation from the menu (not available in this UI)
		lCreateWallMenuItem.setEnabled(false);
		lGridMenu.add(lCreateAgentMenuItem);
		lGridMenu.add(lCreateRewardFunctionMenuItem);
		
		JMenu lRLMenu = new JMenu("Reinforcement Learning");
		JMenuItem lValueIterationMenuItem = new JMenuItem("1. Value Iteration");
		JMenuItem lIRLMenuItem            = new JMenuItem("2. Inverse Reinforcement Learning");
		lRLMenu.add(lValueIterationMenuItem);
		lRLMenu.add(lIRLMenuItem);
		
		JMenu lHelpMenu = new JMenu("Help");
		JMenuItem lTutorialMenuItem = new JMenuItem("Tutorial");
		JMenuItem lAboutMenuItem = new JMenuItem("About GridWorld");
		lHelpMenu.add(lTutorialMenuItem);
		lHelpMenu.add(lAboutMenuItem);

		lMenuBar.add(lGridMenu);
		lMenuBar.add(lRLMenu);
		lMenuBar.add(lHelpMenu);
		
		fGridPanel = new TRLGridPanel();
		
		fTabbedPane = new JTabbedPane();
		fTabbedPane.addTab("Grid", fGridPanel);


		add(lMenuBar, BorderLayout.NORTH);
		add( fTabbedPane, BorderLayout.CENTER);

		fGrid = TRLGridUtil.getSharedInstance().createSquareGrid(5);

		fGridPanel.setGrid(fGrid);
		((ARLGrid)fGrid).addObserver(fGridPanel);

		//make sure the JFrame is visible
		setVisible(true);

		lCreateGridMenuItem.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {


				fGrid = null;
				fGridPanel.setGrid(null);
				fGridPanel.setAgent(null);
				fTabbedPane.removeAll();
				fTabbedPane.addTab("Grid",fGridPanel);


				String lNumberOfRowsColumnsAsString = (String)JOptionPane.showInputDialog(
						TRLMainFrame.this,
						"Number of Rows/Columns",
						"Gridworld",
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						fNumberOfRows + "");

				try{
					fNumberOfRows = Integer.parseInt(lNumberOfRowsColumnsAsString);
					fNumberOfColumns = fNumberOfRows;
				}
				catch( NumberFormatException aNumberFormatException ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Not a number.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}

				fGrid = TRLGridUtil.getSharedInstance().createSquareGrid(fNumberOfRows);
				fGridPanel.setGrid(fGrid);
				((ARLGrid)fGrid).addObserver(fGridPanel);
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
				JTextField lDiscountingFactorTextField        = new JTextField(5);
				lDiscountingFactorTextField.setText(0.9+"");
				JTextField lCorrectActionProbabilityTextField = new JTextField(5);
				lCorrectActionProbabilityTextField.setText(0.7+"");
				JTextField lActionNoiseProbabilityTextField   = new JTextField(5);
				lActionNoiseProbabilityTextField.setText(0.3+"");
				
				JPanel lCreateAgentPanel = new JPanel();
							
				lCreateAgentPanel.add(new JLabel("Initial State Index:"));
				lCreateAgentPanel.add(lInitialStateTextField);
				lCreateAgentPanel.add(new JLabel("Final State Index:"));
				lCreateAgentPanel.add(lFinalStateTextField);
				lCreateAgentPanel.add(new JLabel("Discounting Factor:"));
				lCreateAgentPanel.add(lDiscountingFactorTextField);
				lCreateAgentPanel.add(new JLabel("Correct Action Probability:"));
				lCreateAgentPanel.add(lCorrectActionProbabilityTextField);
				lCreateAgentPanel.add(new JLabel("Action Noise Probability:"));
				lCreateAgentPanel.add(lActionNoiseProbabilityTextField);

				int lResult = JOptionPane.showConfirmDialog(null, lCreateAgentPanel, "Agent information", JOptionPane.OK_CANCEL_OPTION);
				if (lResult != JOptionPane.OK_OPTION) {
					return;
				}
				
				int lInitialStateIndex = 0;
				int lFinalStateIndex = 0;
				double lCorrectActionProbability = 0;
				double lActionNoiseProbability = 0;
				double lDiscountingFactor = 0;
				
				try{
					lInitialStateIndex = Integer.parseInt(lInitialStateTextField.getText());
					lFinalStateIndex = Integer.parseInt(lFinalStateTextField.getText());
					lCorrectActionProbability = Double.parseDouble(lCorrectActionProbabilityTextField.getText());
					lActionNoiseProbability = Double.parseDouble(lActionNoiseProbabilityTextField.getText());
					lDiscountingFactor = Double.parseDouble(lDiscountingFactorTextField.getText());
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
				

				fAgent = TRLAgentUtil.getSharedInstance().createAgent(
						fGrid, 
						lInitialStateIndex, 
						lFinalStateIndex,
						lCorrectActionProbability,
						lActionNoiseProbability);
				fAgent.setDiscountingFactor(lDiscountingFactor);
				fGridPanel.setAgent(fAgent);
				((ARLAgent)fAgent).addObserver(fGridPanel);
				fGridPanel.repaint();

			}
		});
		
		
		lCreateWallMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fGrid == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create grid first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JTextField lInitialVerticeTextField = new JTextField(5);
				JTextField lFinalVerticeTextField = new JTextField(5);				
				
				JPanel lCreateWallPanel = new JPanel();
				lCreateWallPanel.add(new JLabel("Initial Vertice: "));
				lCreateWallPanel.add(lInitialVerticeTextField);
				lCreateWallPanel.add(new JLabel("Final Vertice:"));
				lCreateWallPanel.add(lFinalVerticeTextField);
				
				int lResult = JOptionPane.showConfirmDialog(null, lCreateWallPanel, "Wall information", JOptionPane.OK_CANCEL_OPTION);
				if (lResult != JOptionPane.OK_OPTION) {
					return;
				}
				
				int lInitialVerticeXCoordinate = TRLWallUtil.getSharedInstance().getVerticeXCoordinate( lInitialVerticeTextField.getText() );
				int lInitialVerticeYCoordinate = TRLWallUtil.getSharedInstance().getVerticeYCoordinate( lInitialVerticeTextField.getText() );
				int lFinalVerticeXCoordinate   = TRLWallUtil.getSharedInstance().getVerticeXCoordinate( lFinalVerticeTextField.getText() );
				int lFinalVerticeYCoordinate   = TRLWallUtil.getSharedInstance().getVerticeYCoordinate( lFinalVerticeTextField.getText() );
				
				if( lInitialVerticeXCoordinate != lFinalVerticeXCoordinate && lInitialVerticeYCoordinate != lFinalVerticeYCoordinate ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Only horizontal and vertical walls are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lInitialVerticeXCoordinate < 0 || lFinalVerticeXCoordinate < 0 || lInitialVerticeYCoordinate < 0 || lFinalVerticeYCoordinate < 0) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Vertices coordinates cannot be smaller than 0.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lFinalVerticeXCoordinate > fNumberOfColumns ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "The final vertice X coordinate " + lFinalVerticeXCoordinate + " cannot be greater than the number of columns of the grid " + fNumberOfColumns + "." , "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lFinalVerticeYCoordinate > fNumberOfRows ) {
					JOptionPane.showMessageDialog(TRLMainFrame.this, "The final vertice Y coordinate " + lFinalVerticeYCoordinate + " cannot be greater than the number of rows of the grid " + fNumberOfRows + "." , "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}			
				
				TRLWallUtil.getSharedInstance().createWall(fGrid, lInitialVerticeTextField.getText(), lFinalVerticeTextField.getText());
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

				JPanel lCreateAgentPanel = new JPanel();
				lCreateAgentPanel.add(new JLabel("Reward at non absorbing states:"));
				lCreateAgentPanel.add(lRewardAtNonAbsorbingStateTextField);
				lCreateAgentPanel.add(Box.createHorizontalStrut(15)); // a spacer
				lCreateAgentPanel.add(new JLabel("Reward at absorbing state:"));
				lCreateAgentPanel.add(lRewardAtAbsorbingStateTextField);

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
				
				IRLRewardFunction lRewardFunction = TRLRewardFunctionUtil.getSharedInstance().createRewardFunction(fAgent, lRewardAtNonAbsorbingStates, lRewardAtAbsorbingState);
				fAgent.getGrid().setRewardFunction(lRewardFunction);
				
			}
		});


		
		lValueIterationMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				if( fAgent == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create agent first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( fAgent.getGrid().getRewardFunction() == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Create reward function first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				double[] lOptimalValueFunction = TRLPolicyUtil.getSharedInstance().solveValueIterationAssynchronously(fAgent );
				IRLPolicy lOptimalPolicy = TRLPolicyUtil.getSharedInstance().createPolicyForGivenOptimalValueFunction(fAgent, lOptimalValueFunction);
				lOptimalPolicy.setValueFunction(lOptimalValueFunction);
				
				HashMap<IRLState, HashMap<IRLAction, Double>> lActionValueFunctionHashMap = TRLPolicyUtil.getSharedInstance().solveBellmansEquationsForActionValueFunction(fAgent, lOptimalPolicy.getValueFunction());
				lOptimalPolicy.setQValueHashMap(lActionValueFunctionHashMap);
				
				fAgent.setPolicy(lOptimalPolicy);
			
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
				
				IRLPolicy lPolicy = fAgent.getPolicy();
				if( lPolicy == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Run value iteration first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if( lPolicy.getValueFunction() == null ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Run value iteration first.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				

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
				
				double lLambda = 0;
				double lMinLambda = 0;
				double lMaxLambda = 0;
				double lStepLambda = 0;
				try{ 
					lMinLambda = Double.parseDouble(lMinRegularizationTextField.getText());
					lMaxLambda = Double.parseDouble(lMaxRegularizationTextField.getText());
					lStepLambda = Double.parseDouble(lRegularizationStepTextField.getText() );
				}
				catch(NumberFormatException aNumberFormatException ){
					JOptionPane.showMessageDialog(TRLMainFrame.this, "Not a number.", "Error" ,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				for( lLambda = lMinLambda; lLambda <= lMaxLambda; lLambda = lLambda + lStepLambda ){
					try{
						System.out.println("Trying Lambda: " + lLambda);
						double lRMax = Double.parseDouble(lRMaxTextField.getText());
						double[] lIRLRewardFunction = TRLIRLUtil.getSharedInstance().solveIRL( fAgent, lRMax, lLambda );	
						if( lDisplayAllGraphsCheckBox.isSelected() || TRLIRLUtil.getSharedInstance().iRLSolutionFound(fAgent, lRMax, lIRLRewardFunction)){
							fIRLRewardPanel = new TRLIRLRewardPanel();
							fIRLRewardPanel.setRewardFunction(lIRLRewardFunction);
							fIRLRewardPanel.setRMax(lRMax);
							fIRLRewardPanel.setRegularization(lLambda);
							fIRLRewardPanel.generateChart();
							fTabbedPane.add("Reward Function (" + lRMaxTextField.getText() + "," + String.format("%.2f", lLambda) + ")" , fIRLRewardPanel);
							if( ! lDisplayAllGraphsCheckBox.isSelected() ){
								break;
							}
						}
					}

					catch(org.apache.commons.math3.optim.linear.UnboundedSolutionException aUnboundedSolutionException ){
						System.err.println("UnboundedSolutionException for Lambda " + lLambda );
						continue;
					}
				}
			}
		});
		
		lTutorialMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				
				String lTutorialString = String.format("%s\n%s\n%s\n%s", 
						"1. Create a Reinforcement Learning Environment by running the actions at the Create Menu.",
						"2. Find an optimal policy by running the action Value Iteration. The optimal value function is shown in blue and optimal Q values are shown on red. An optimal policy is shown by arrows in blue.",
						"3. Finally find the reward function by running the action Inverse Reinforcement Learning. Depending on the configuration, "
						+ "not always a reward function can be found.",
						"OBS.: The algorithm and RL environnment is from the paper Algorithms for Inverse Reinforcement Learning, Andrew Ng."
						);
				
				JOptionPane.showMessageDialog(TRLMainFrame.this, lTutorialString, "Gridworld" ,JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		lAboutMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent aActionEvent) {
				JOptionPane.showMessageDialog(TRLMainFrame.this, "Ivomar Brito Soares, britosoaresivomar@gmail.com", "Gridworld" ,JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
}
