import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * Swing panel that plots a recovered reward function using JFreeChart.
 * The panel expects an array of rewards and simple chart configuration
 * (Rmax and regularization) and will render a bar chart when requested.
 */
public class TRLIRLRewardPanel extends JPanel {

	private double[] fRewardFunction;
	private double fRMax;
	private double fRegularization;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	private CategoryDataset createDataset( )
	{
		final DefaultCategoryDataset dataset = 
				new DefaultCategoryDataset( );  

		for( int lRewardIndex = 0; lRewardIndex < fRewardFunction.length; lRewardIndex++ ){

			dataset.addValue(fRewardFunction[lRewardIndex], lRewardIndex + "", lRewardIndex + "");
		}


		return dataset; 
	}


	public void generateChart(){
		JFreeChart lBarChart = ChartFactory.createBarChart(
				"Reward Function (RMax = " + fRMax + ", Lambda = " + String.format("%.2f",fRegularization) + ")",           
				"State",            
				"Reward",            
				createDataset(),          
				PlotOrientation.VERTICAL,           
				true, true, false);

		ChartPanel chartPanel = new ChartPanel( lBarChart );        

		setLayout(new java.awt.BorderLayout());
		add(chartPanel,BorderLayout.CENTER);
		validate();
	}


	public void setRMax(double aRMax) {
		fRMax = aRMax;
	}

	public void setRegularization(double aRegularization) {
		fRegularization = aRegularization;
	}


	public void setRewardFunction(double[] aRewardFunction) {
		fRewardFunction = aRewardFunction;
	}

}
