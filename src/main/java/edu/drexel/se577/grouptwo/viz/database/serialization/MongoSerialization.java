package edu.drexel.se577.grouptwo.viz.database.serialization;

import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;

import org.bson.Document;

public interface MongoSerialization {
	public Document getDocument(Dataset data);
    public Document getDocument(Sample data);
    public Document getDocument(Definition data);
    public Document getDocument(Visualization data);
    
    public Visualization toVisualization(Document document);
    public Dataset toDataset(Document document);
    public String getDatasetName(Document document);
    public String getVizName(Document document);

}