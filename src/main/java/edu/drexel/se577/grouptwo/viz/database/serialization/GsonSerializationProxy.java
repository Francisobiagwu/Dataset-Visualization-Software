package edu.drexel.se577.grouptwo.viz.database.serialization;

import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;
import edu.drexel.se577.grouptwo.viz.storage.DatasetImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.database.serialization.MongoSerialization;

import org.bson.Document;

public class GsonSerializationProxy implements MongoSerialization {
    public Gson _gsonExt = null;

    public GsonSerializationProxy(){
    	GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Dataset.class, new InheritanceAdapter<Dataset>());
        builder.serializeNulls();
        _gsonExt = builder.create();  


    }

    public Gson Gson(){
        return _gsonExt;
    }
   
    @Override
    public Document getDocument(Dataset data)
    {
        if(data == null) return null;
        /*String innerjson = _gsonExt.toJson(data.getSamples());*/
        String json = _gsonExt.toJson(data);
        return Document.parse(json);
    }
    
    @Override
    public Document getDocument(Sample data)    
    {
        if(data == null) return null;
        String json = _gsonExt.toJson(data);
        return Document.parse(json);
    }
 
    @Override
    public Document getDocument(Definition data)    
    {
        if(data == null) return null;
        String json = _gsonExt.toJson(data);
         return Document.parse(json);
    }
  
    @Override
    public Document getDocument(Visualization data)    
    {
        if(data == null) return null;
        String json = _gsonExt.toJson(data);
        return Document.parse(json);
    }
   
    @Override
    public Visualization toVisualization(Document document)
    {
    	Visualization _visualization = _gsonExt.fromJson(document.toJson(), Visualization.class);
    	return _visualization;
    }

    @Override
    public Dataset toDataset(Document document)
    {	
    	DatasetImpl _dataset  = _gsonExt.fromJson(document.toJson(), DatasetImpl.class); 	
    	return _dataset;
    }

    @Override
    public String getDatasetName(Document document)
    {
    	DatasetImpl _dataset = _gsonExt.fromJson(document.toJson(), DatasetImpl.class);
    	return _dataset.getName();
    }
    
    @Override
    public String getVizName(Document document)
    {
    	Visualization _viz = _gsonExt.fromJson(document.toJson(), Visualization.class);
    	return _viz.toString();
    }
}