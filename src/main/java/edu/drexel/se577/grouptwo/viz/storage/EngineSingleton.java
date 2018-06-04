package edu.drexel.se577.grouptwo.viz.storage;

import edu.drexel.se577.grouptwo.viz.storage.Engine;
import edu.drexel.se577.grouptwo.viz.database.repositories.Repository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.List;
import java.util.Optional;

import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;

import edu.drexel.se577.grouptwo.viz.database.repositories.MongoCollectionRepository;

public class EngineSingleton implements Engine {
	private static EngineSingleton singletonInstance = null;
	private Repository repo;
	
	public static EngineSingleton getEngineInstance()
	{
		if (singletonInstance == null)
		{
			singletonInstance = new EngineSingleton();
		}
		return singletonInstance;
	}
	
	private EngineSingleton()
	{
		/*Create MongoDB connection*/
	        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
	        MongoDatabase mongoDatabase = mongoClient.getDatabase("TheDatabaseName");     
	        repo = new MongoCollectionRepository(mongoClient, mongoDatabase);
	        
	}
	   
	@Override	
	public Dataset create(Definition definition)
	{
		/*create new dataset*/	
		Dataset _dataset = repo.create(definition);
		_dataset.addRepo(repo);	
		return _dataset;
	}
	
	@Override
	public Visualization createViz(Visualization visualization) {	
		return repo.createViz(visualization);
	}
	
	@Override 
	public Optional<Dataset> forId(Definition definition) {
		Dataset _dataset = repo.find(definition);
		
		if (_dataset != null)
		{
			_dataset.addRepo(repo);	

		}
		
		Optional<Dataset> d = Optional.of(_dataset);	
		return d; 
	}
	
	@Override
	public List<String> getDatasetNames()
	{
		return repo.getDatasetNames();
	};
	
	@Override
	public List<String> getVisualizationNames()
	{
		return repo.getVisualizationNames();
	};
}
