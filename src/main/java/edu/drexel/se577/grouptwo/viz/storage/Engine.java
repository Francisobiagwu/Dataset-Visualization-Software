package edu.drexel.se577.grouptwo.viz.storage;

import java.util.Collection;
import java.util.Optional;

import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;

public interface Engine {
	public Optional<Dataset> forId(String id);	
	public Dataset create(Definition definition);
	public Collection<Dataset> listDatasets();
	
	public Visualization createViz(Visualization visualization);
	public Optional<Visualization> getVisualization(String id);
	public Collection<Visualization> listVisualizations();
	
}
