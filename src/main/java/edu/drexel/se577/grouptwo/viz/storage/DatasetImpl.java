package edu.drexel.se577.grouptwo.viz.storage;

import java.util.ArrayList;
import java.util.List;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;

import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.database.repositories.Repository;

/* Dataset Implementation */
public class DatasetImpl implements Dataset {

	protected String datasetID, name;
	protected Definition definition;
	protected List<Sample> samples;
	private Repository repo;
	
	public DatasetImpl()
	{	
		samples = new ArrayList<Sample>();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setId(String datasetID)
	{
		this.datasetID = datasetID;
	}

	@Override
	public void addRepo(Repository repo)
	{
		this.repo = repo;
	}

	@Override
	public String getName()
	{
		return name;	
	}

	@Override
	public String getId()
	{
		return datasetID;	
	}

	@Override
	public Definition getDefinition()
	{
		return definition;	
	}

	@Override
	public void addSample(Sample sample)
	{
		samples.add(sample);
		repo.addSample(this);
	}
	
	@Override
	public List<Sample> getSamples()
	{	
		return samples;
	}
	
	@Override
	public void setSample(String key, Value value)
	{
		Sample sample = new Sample();
		sample.put(key,  value);
		samples.add(sample);
	}

}
