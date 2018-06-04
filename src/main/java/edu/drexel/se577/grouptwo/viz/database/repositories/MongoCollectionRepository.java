
package edu.drexel.se577.grouptwo.viz.database.repositories;

import java.lang.reflect.Type;
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
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoCollectionRepository implements Repository {
    protected MongoClient _mongoClient;
    protected MongoDatabase _database;
    protected Logging _Logger;
    protected MongoSerialization _gSonProxy;

    public MongoCollectionRepository(MongoClient mongoClient, 
    		MongoDatabase mongoDatabase) 
    {
    	/*Create new mongoClient*/
    	this(mongoClient, new GsonSerializationProxy(),mongoDatabase);      
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
    	MongoCollection<Document> _collection = _database.getCollection("DatasetCollection");
    	
        try{
            Document doc = _gSonProxy.getDocument(definition);
            _collection.insertOne(doc);       
            dataset = new DatasetImpl();
            dataset.setName(definition.name);
            
            return dataset;
            
        }catch(Exception e){
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:create error for document " + ". Exception: " + e);
            }
        }
        
        return dataset;

    }

    @Override
    public Visualization createViz(Visualization visualization) {
    	MongoCollection<Document> _collection = _database.getCollection("VizCollection");
    	
        try{
            Document doc = _gSonProxy.getDocument(visualization);
            _collection.insertOne(doc);

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
    	MongoCollection<Document> _collection = _database.getCollection("DatasetCollection");   	
        BasicDBObject query = new BasicDBObject();
        query.put("name", dataset.getName());

        update(dataset, query);
    }
    
    private void update(Dataset dataset, BasicDBObject query) {
    	MongoCollection<Document> _collection = _database.getCollection("DatasetCollection");  
    	
        try {
            Document doc = _gSonProxy.getDocument(dataset);
            _collection.findOneAndReplace(query, doc); 
            
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
           	MongoCollection<Document> _collection = _database.getCollection("DatasetCollection"); 
           	BasicDBObject query = new BasicDBObject();
           	query.put("name", definition.name);
           	Document doc = _collection.find(query).first(); 
        
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
        		MongoCollection<Document> _collection = _database.getCollection("DatasetCollection"); 	
        		BasicDBObject query = new BasicDBObject();
        		query.put("name", new ObjectId(name));
        		Document doc = _collection.find(query).first(); 
        
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
    	
    		MongoCollection<Document> _collection = _database.getCollection("DatasetCollection"); 
    		List<Sample> myList = new ArrayList<Sample>();
    		BasicDBObject query = new BasicDBObject();
    		query.put("name", name);
    		
    		try {
    			/*FindIterable<Document> docs = _collection.find(query); */
	        	Document doc = _collection.find(query).first();         
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
    	 MongoCollection<Document> _collection = _database.getCollection("DatasetCollection"); 
    	 List<String> myList = new ArrayList<String>();
    	 BasicDBObject query = new BasicDBObject();	
    	 query.put("name", 1);
    	 
    	 try {
    		         
    		 FindIterable<Document> docs  = _collection.find(query);

    		 for (Document doc : docs) { 
    			    myList.add(_gSonProxy.getDatasetName(doc));;
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
    	MongoCollection<Document> _collection = _database.getCollection("VizCollection"); 
    	List<String> myList = new ArrayList<String>();
    	BasicDBObject query = new BasicDBObject();
    	
        try {
	    FindIterable<Document> docs = _collection.find(query);


            for (Document doc : docs) {                
            	String name = _gSonProxy.getVizName(doc);
                myList.add(name);
            }
        } catch (Exception e) {
            if(_Logger != null){
                _Logger.LogCritical("MongoCollectionRepository:find error using query " + query.toJson() + ". Exception: " + e);
            }
        }

        return myList;
    }

}


