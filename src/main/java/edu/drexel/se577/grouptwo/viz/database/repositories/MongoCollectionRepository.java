
package edu.drexel.se577.grouptwo.viz.database.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.storage.DatasetImpl;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.database.serialization.GsonSerializationProxy;

import edu.drexel.se577.grouptwo.viz.logging.Logging;
import edu.drexel.se577.grouptwo.viz.database.serialization.MongoSerialization;
import edu.drexel.se577.grouptwo.viz.database.repositories.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoCollectionRepository implements Repository {
    protected MongoClient _mongoClient;
    protected MongoDatabase _database;
    protected Logging _Logger;
    protected MongoSerialization _gSonProxy;
    private MongoCollection<Document> _dsCollection;
    private MongoCollection<Document> _vizCollection;
    private String datasetCollName = "DatasetCollection";
    private String vizCollName = "VisualizationCollection";

    public MongoCollectionRepository(MongoClient mongoClient, 
    		MongoDatabase mongoDatabase) 
    {
    	/*Create new mongoClient*/
    	this(mongoClient, new GsonSerializationProxy(),mongoDatabase);     
       	_dsCollection = _database.getCollection(datasetCollName);
    	_vizCollection = _database.getCollection(vizCollName);
    }
    
    public MongoCollectionRepository(MongoClient client, 
    		GsonSerializationProxy gSonProxy, MongoDatabase mongoDatabase) {
        _mongoClient = client;
        _gSonProxy = gSonProxy;
        _database = mongoDatabase;
    }

    public MongoCollectionRepository(MongoClient client, MongoDatabase mongoDatabase, 
    		Logging logger) {
        _mongoClient = client;
        _Logger = logger;
        _database = mongoDatabase;
    } 
    
    public MongoSerialization Serializer(){
        return _gSonProxy;
    }

    @Override
    public Dataset create(Definition definition) {
    	Dataset dataset = null;
    	
        try{
        	
        	if (nonDupDataset(definition))
        	{      	
        		Document doc = _gSonProxy.getDocument(definition);
        		_dsCollection.insertOne(doc);       
        		dataset = new DatasetImpl();
        		return dataset;
        	}
            
        }catch(Exception e){
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:create error for document " + ". Exception: " + e);
            }
        
        }
        
        return dataset;

    }
    
    private boolean nonDupDataset(Definition definition)
    {
        try{
        	
            	BasicDBObject query = new BasicDBObject();
            	query.put("name", definition.name);
            	Document doc = _dsCollection.find(query).first(); 
            
            	if (doc != null)
            	{
            		return false; /*document exists*/
            	}
            
        }catch(Exception e){
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:create error for document " + ". Exception: " + e);
            }
        }
        
        return true;
    }

    @Override
    public Visualization createViz(Visualization visualization) {
    	
        try{
            Document doc = _gSonProxy.getDocument(visualization);
            _vizCollection.insertOne(doc);

        }catch(Exception e){
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:create error for document " + ". Exception: " + e);
            }
        }
        
        return visualization;
    }    
    
    @Override
    public void addSample(Dataset dataset) {
        // Query Object
    	BasicDBObject query = new BasicDBObject();
        query.put("name", dataset.getName());

        update(dataset, query);
    }
    
    private void update(Dataset dataset, BasicDBObject query) {

        try {
            Document doc = _gSonProxy.getDocument(dataset);
            _dsCollection.findOneAndReplace(query, doc); 
            
        } catch (Exception e) {
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:update error for document "  + ". Exception: " + e);
            }
        }
        
    }

    @Override 
    public Dataset find(Definition definition) {
        // Query Object
       	Dataset _dataset = null;
       	
        try {  
           	BasicDBObject query = new BasicDBObject();
           	query.put("name", definition.name);
           	Document doc = _dsCollection.find(query).first(); 
        
           	if (doc != null)
           	{
           		_dataset = _gSonProxy.toDataset(doc);
           	}
        
        } catch (Exception e) {
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:error for finding dataset "  + ". Exception: " + e);
            }
        }
        
        return _dataset;
    }
    
    @Override 
    public Dataset find(String name) {
        // Query Object
    	Dataset _dataset = null;
    		
    	try {  		
        		
        		BasicDBObject query = new BasicDBObject();
        		query.put("name", new ObjectId(name));
        		Document doc = _dsCollection.find(query).first(); 
        
        	if (doc != null)
        	{
        		_dataset = _gSonProxy.toDataset(doc);
        	}
        
        	} catch (Exception e) {
        		if(_Logger != null){
        			_Logger.LogCritical("MongoCollectionRepository:update error for document "  + ". Exception: " + e);
        		}
        	}
        return _dataset;

    }

    @Override
	public List<Sample> getSamples(String name) {
    	
    		List<Sample> myList = new ArrayList<Sample>();
    		BasicDBObject query = new BasicDBObject();
    		query.put("name", name);
    		
    		try {
    		
	        	Document doc = _dsCollection.find(query).first();         
	        	Dataset _dataset =  _gSonProxy.toDataset(doc);
	        	myList = _dataset.getSamples();

        	} catch (Exception e) {
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:find error using query " + query.toJson() + ". Exception: " + e);
            	}
        	}

	        return myList;

	}
    
    @Override
    public List<String> getDatasetNames()
    {
   	 List<String> myList = new ArrayList<String>();
   	 BasicDBObject query = new BasicDBObject();
 
   	 try {
   		       
   		 /*Make this better - _collection.distinct requiring field and class)*/
   		 FindIterable<Document> docs  = _dsCollection.find(query);

   		 for (Document doc : docs) { 
   			 	String name = _gSonProxy.getDatasetName(doc);
   			 	if (!myList.contains(name))
   			 	{
   			 		myList.add(name);
   			 	}
   			}

        } catch (Exception e) {
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:find error using query " + query.toJson() + ". Exception: " + e);
            }
        }

        return myList;
    }

	@Override
    public List<String> getVisualizationNames()
    {
    	
    	List<String> myList = new ArrayList<String>();
    	BasicDBObject query = new BasicDBObject();
    	
    	/*make this better*/
        try {
        	FindIterable<Document> docs = _vizCollection.find(query);


            for (Document doc : docs) { 
   			 	String name = _gSonProxy.getVizName(doc);
			 	if (!myList.contains(name))
			 	{
			 		myList.add(name);
			 	}
            }
        } catch (Exception e) {
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:find error using query " + query.toJson() + ". Exception: " + e);
            }
        }

        return myList;
    }

}


