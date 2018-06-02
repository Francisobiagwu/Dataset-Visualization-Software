package edu.drexel.se577.grouptwo.viz.visualization;

import java.io.File;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@SuppressWarnings("serial")
public class XYScatterPlotFrame extends JFrame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		createChartPanel();

	}

	public static XYDataset createDataset()
	{
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("Object1");
		double[] key = new double[25], value = new double[25];
		Random generator = new Random();
		for(int i = 0; i<25; i++)
		{
			key[i] = i;
			value[i]=generator.nextDouble();
			series1.add(key[i], value[i]);
		}
		dataset.addSeries(series1);
		return dataset;		
	}
	
	private static void createChartPanel() {
		String chartTitle = "Object1 XY Scatter Plot";
		String xAxisLabel = "Key";
		String yAxisLabel = "Value";
		XYDataset dataset = createDataset();
		JFreeChart chart = ChartFactory.createXYAreaChart(chartTitle, xAxisLabel, yAxisLabel, dataset);
		int width = 500;
		int height = 300;
		try {
			ChartUtilities.saveChartAsPNG(new File("E:\\Quarter3\\CS680\\Project\\group-2-project-feature-backend-abstraction-850c96c952f2e552604e1291749012b9278b8e6a\\xyScatterTest.png"), chart, width, height);
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
}
