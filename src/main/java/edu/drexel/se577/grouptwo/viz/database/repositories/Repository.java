package edu.drexel.se577.grouptwo.viz.database.repositories;

import java.util.Collection;
import java.util.List;

import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;

public interface Repository  {

    public Dataset create(Definition definition);
    public String createViz(Visualization visualization);
    public Dataset find(Definition definition);
    public Dataset find(String id);
    public void addSample(Dataset dataset);
    public List<Sample> getSamples(String id);
    public Collection<Dataset> listDatasets();
    public Collection<Visualization> listVisualizations();
    public Visualization getVisualization(String id);
    
}