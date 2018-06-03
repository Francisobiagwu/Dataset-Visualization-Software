package edu.drexel.se577.grouptwo.viz.visualization;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import edu.drexel.se577.grouptwo.viz.dataset.Attribute.Countable;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.storage.EngineSingleton;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization.Histogram.DataPoint;

public class HistogramViz extends Visualization.Histogram{

	private HistogramViz hisViz = null;
	protected HistogramViz(String datasetId, Countable attribute) {
		super(datasetId, attribute);
		// TODO Auto-generated constructor stub
	}

	public HistogramViz getInstance(String datasetId, Countable attribute)
	{
		if(hisViz == null)
			hisViz = new HistogramViz(datasetId, attribute);
		return hisViz;
	}
	
	@Override
	public Image render() {
		// TODO Auto-generated method stub
/*		Random generator = new Random();
		for(int i = 0; i<100; i++)
		{
			value[i]=generator.nextDouble();
		}
		int number=10;
*/		
		List<DataPoint> datapoints = data();
		double[] value = new double[datapoints.size()];
		int i = 0;
		for(DataPoint dp : datapoints)
		{
			value[i++] = dp.value.value;
		}
		double[] unique = Arrays.stream(value).distinct().toArray();
		int number = unique.length;
		
		HistogramDataset hdataset = new HistogramDataset();
		hdataset.setType(HistogramType.RELATIVE_FREQUENCY);
		hdataset.addSeries("Histogram", value, number);
		String plotTitle = "Histogram";
		String xAxis = "Number";
		String yAxis = "Value";
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		JFreeChart chart = ChartFactory.createHistogram(plotTitle, xAxis, yAxis, hdataset, orientation, true, toolTips, urls);
		int width = 500;
		int height = 300;
		try {
			ChartUtilities.saveChartAsPNG(new File("histogramTest.png"), chart, width, height);
		}catch(Exception e) {
			e.printStackTrace();
		}
		BufferedImage bufferedImage;
		ImageImpl image =  null; 
		try {
			bufferedImage = ImageIO.read(new File("histogramTest.png"));
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
	public List<DataPoint> data() {
		// TODO Auto-generated method stub
		EngineSingleton engine = EngineSingleton.getInstance();
		Optional<Dataset> datasetOp = engine.forId(datasetId);
		Dataset dataset = datasetOp.get();
		List<Sample> list = dataset.getSamples();
		List<DataPoint> dataPoints = new ArrayList<>();
		DataPoint dp = new DataPoint();
		for(Sample loop : list)
		{
			@SuppressWarnings("unchecked")
			Optional <Value.Int> val = (Optional<Value.Int>) loop.get(attribute.name()); 
			dp.setValues(val.get());
			dataPoints.add(dp);
		}		
		return dataPoints;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return datasetId;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return attribute.name();
	}

}
