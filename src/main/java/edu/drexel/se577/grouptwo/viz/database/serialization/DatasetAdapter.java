package edu.drexel.se577.grouptwo.viz.database.serialization;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.storage.DatasetImpl;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import edu.drexel.se577.grouptwo.viz.storage.Dataset;

	public class DatasetAdapter extends TypeAdapter<Dataset>   {
		
		@Override
		 public Dataset read(JsonReader reader) throws IOException {
			    if (reader.peek() == JsonToken.NULL) {
			      reader.nextNull();
			      return null;
			    }
			    
			    Gson gson = new Gson();
			    Type mapToken = new TypeToken<Map<String, Value>>(){}.getType();
			    
			    DatasetImpl _dataset = new DatasetImpl();
			    String fieldname = null;
			    String json = null;
			    
			    reader.beginObject();
			    while (reader.hasNext())
			    {
			    	JsonToken token = reader.peek();
			    	
			    	if (token.equals(JsonToken.NAME)) {
			    		fieldname=reader.nextName();
			    		
			    	} else if ("name".equals(fieldname)) {
			    		token=reader.peek();
			    		try
			    		{
			    		_dataset.setName(reader.nextString());
			    		}
			    		catch (Exception exception)
			    		{
			    			reader.skipValue();
			    		}
			    		
			    	/*} else if ("_id".equals(fieldname)) {
			    		token=reader.peek();
			    		try
			    		{
				    		Object myInt = reader.nextInt();
					    	_dataset.setId(myInt.toString());	
			    		}
			    		catch (IllegalStateException exception)
			    		{
			    			reader.skipValue();
			    		}		   */  			
			    		
			    	} else if ("definition".equals(fieldname)) {
			    		token=reader.peek();
			    		try
			    		{
				    		json = gson.toJson(reader.nextString());
				    		Definition definition = gson.fromJson(json, Definition.class);
				    		_dataset.setDefinition(definition);
			    		}
			    		catch (Exception exception)
			    		{
			    			reader.skipValue();
			    		}		 
			    		
			    	} else if ("samples".equals(fieldname)) {
			    		json = gson.toJson(reader.nextString());
			    		Map<String,Value> map = new HashMap<String,Value>();
			    		map = gson.fromJson(json, mapToken);
			    		
			    			/*Iterate over hashmap and add to sample class*/
			    		   Iterator it = map.entrySet().iterator();
			    		    while (it.hasNext()) {
			    		        Map.Entry pair = (Map.Entry)it.next();
			    		        _dataset.setSample(pair.getKey().toString(),(Value) pair.getValue());
			    		   	 it.remove(); // avoids a ConcurrentModificationException
			    		    	}

	
				    		
			    	} else
			    	{
			    		reader.skipValue();
			    	}
			    
			    
			    }
			    
			    reader.endObject();
			    

			   
			    return _dataset;
			  }
		
			  @Override
			  public void write(JsonWriter writer, Dataset value) throws IOException {
			    if (value == null) {
			      writer.nullValue();
			      return;
			    }
			    		    
			    Type mapToken = new TypeToken<Map<String, Value>>(){}.getType();
			    
			    Gson gson = new Gson();
			    writer.beginObject();
			    writer.name("name").value(value.getName());
			    writer.name("datasetID").value(value.getId());
			    writer.name("definition").value(gson.toJson(value.getDefinition(), Definition.class));
			    
			    List<Sample> samples = value.getSamples();
			    
			    for(Sample s : samples) {
			    	Collection<String> keyColl = s.getKeys();
			    	
			    	for (String entry: keyColl)
			    	{
			    		writer.name("Key").value(entry);
			    		Optional<? extends Value> attribute = null;  
			    		
			    		if(s.get(entry) != null)
			    		{	
			    			attribute = s.get(entry);
			    			writer.name("Attribute").value(attribute.toString());
			    		}

			    	} 

			    }
			    
			    writer.endObject();
	    
			  }

	
}

