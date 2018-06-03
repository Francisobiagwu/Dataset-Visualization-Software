package edu.drexel.se577.grouptwo.viz.visualization;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.dataset.Value.FloatingPoint;
import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.storage.EngineSingleton;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization.Image;

public class ScatterPlotViz extends Visualization.Scatter{
	
	private static volatile ScatterPlotViz scatterPlot = null;
	
	protected ScatterPlotViz(String datasetId, Attribute xAxis, Attribute yAxis)
	{
		super(datasetId, xAxis, yAxis);
	}
	
	public ScatterPlotViz getInstance(String datasetId, Attribute xAxis, Attribute yAxis)
	{
		if(scatterPlot == null)
			scatterPlot = new ScatterPlotViz(datasetId, xAxis, yAxis);
		return scatterPlot;
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
	
	private static File createChartPanel() {
		String chartTitle = "Object1 XY Scatter Plot";
		String xAxisLabel = "Key";
		String yAxisLabel = "Value";
		XYDataset dataset = createDataset();
		JFreeChart chart = ChartFactory.createXYAreaChart(chartTitle, xAxisLabel, yAxisLabel, dataset);
		int width = 500;
		int height = 300;
		File file = null;
		try {
			file = new File("xyScatterTest.png");
			ChartUtilities.saveChartAsPNG(file, chart, width, height);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	@Override
	public Image render() {
		// TODO Auto-generated method stub
		File file = createChartPanel();
		BufferedImage bufferedImage;
		ImageImpl image =  null; 
		try {
			bufferedImage = ImageIO.read(file);
			WritableRaster raster = bufferedImage .getRaster();
			DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
			image = new ImageImpl("png", data.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	 return image;
	}

	
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return datasetId;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		String returnName = xAxis.name() + " " + yAxis.name();
		return returnName;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DataPoint> data() {
		// TODO Auto-generated method stub
		EngineSingleton engine = EngineSingleton.getInstance();
		Optional<Dataset> datasetOp = engine.forId(datasetId);
		Dataset dataset = datasetOp.get();
		List<Sample> list = dataset.getSamples();
		List<DataPoint> dataPoints = new ArrayList<>();
		for(Sample loop : list)
		{
			Optional <Value.FloatingPoint> val1 = (Optional<FloatingPoint>) loop.get(xAxis.name());
			Optional <Value.FloatingPoint> val2 = (Optional<FloatingPoint>) loop.get(yAxis.name());
			DataPoint dp = new DataPoint(val1.get(), val2.get());
			dataPoints.add(dp);
		}		
		return dataPoints;
	}

}