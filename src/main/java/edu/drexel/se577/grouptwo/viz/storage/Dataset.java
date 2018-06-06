package edu.drexel.se577.grouptwo.viz.storage;

import java.util.List;

import edu.drexel.se577.grouptwo.viz.database.repositories.Repository;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;

public interface Dataset {
	public void setName(String name);
	public void setId(String datasetID);
	public void addRepo(Repository repo);
    public String getId();
    public String getName();
    public Definition getDefinition();
    public List<Sample> getSamples();
    public void addSample(Sample sample);
    public void setSample(String key, Value value);
    public void setDefinition(Definition definition);
    
}
