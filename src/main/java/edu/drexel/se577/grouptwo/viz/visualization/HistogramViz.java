package edu.drexel.se577.grouptwo.viz.visualization;

import java.util.List;
import java.util.Optional;

import edu.drexel.se577.grouptwo.viz.dataset.Attribute.Countable;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.storage.EngineSingleton;

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
		return null;
	}

	@Override
	public List<DataPoint> data() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void createDataPoint() {
		// TODO Auto-generated method stub
		EngineSingleton engine = EngineSingleton.getInstance();
		Optional<Dataset> datasetOp = engine.forId(datasetId);
		Dataset dataset = datasetOp.get();
		List<Sample> list = dataset.getSamples();
		
	}

}
