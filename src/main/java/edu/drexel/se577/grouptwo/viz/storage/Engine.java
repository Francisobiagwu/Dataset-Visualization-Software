package edu.drexel.se577.grouptwo.viz.storage;

import java.util.List;
import java.util.Optional;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;

public interface Engine {
    public Dataset create(Definition definition);
	public Visualization createViz(Visualization visualization);
	public Optional<Dataset> forId(Definition definition);
	public List<String> getDatasetNames();
	public List<String> getVisualizationNames();
	
}
