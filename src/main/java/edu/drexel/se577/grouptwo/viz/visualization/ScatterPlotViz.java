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

class ScatterPlotViz extends Visualization.Scatter {
    private final String name; 
	
	ScatterPlotViz(String name, String id, Dataset dataset, Attribute.Arithmetic xAxis, Attribute.Arithmetic yAxis)
	{
		super(id, dataset, xAxis, yAxis);
        this.name = name;
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
	public String getName() {
		return name;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DataPoint> data() {
		// TODO Auto-generated method stub
		List<Sample> list = getDataset().getSamples();
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
