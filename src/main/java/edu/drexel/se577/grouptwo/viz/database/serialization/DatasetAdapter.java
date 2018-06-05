package edu.drexel.se577.grouptwo.viz.database.serialization;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.storage.DatasetImpl;
import com.google.gson.TypeAdapter;
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
			    
			    DatasetImpl _dataset = new DatasetImpl();
			    String fieldname = null;
			    
			    reader.beginObject();
			    while (reader.hasNext())
			    {
			    	JsonToken token = reader.peek();
			    	
			    	if (token.equals(JsonToken.NAME)) {
			    		fieldname=reader.nextName();
			    		
			    	}
			    	
			    	if ("name".equals(fieldname)) {
			    		token=reader.peek();
			    		_dataset.setName(reader.nextString());
			    	}
			    	
			    	if ("datasetID".equals(fieldname)) {
			    		token=reader.peek();
			    		Object myInt = reader.nextInt();
			    		_dataset.setId(myInt.toString());
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
			    
			    writer.beginObject();
			    writer.name("name").value(value.getName());
			    writer.name("datasetID").value(value.getId());
			    
			    List<Sample> samples = value.getSamples();
			    
			    for(Sample s : samples) {
			    	writer.beginArray();
			    	
			    	/*Value.Mapping mapList = s.getMap();*/	
			    	Collection<String> keyColl = s.getKeys();
			    				    	
			    	for (String entry: keyColl)
			    	{
			    		
			    		writer.beginObject();
			    		writer.name("Key").value(entry);
			    		Optional<? extends Value> attribute = null;  
			    		
			    		if(s.get(entry) != null)
			    		{	
			    			attribute = s.get(entry);
			    			writer.name("Attribute").value(attribute.toString());
			    		}
			    		
			    		/*gson.getAdapter(new TypeToken<String>(){}).write(writer, entry.getKey());
			    		writer.name("Attribute");
			    		gson.getAdapter(new TypeToken<Value>() {}).write(writer, s.get(entry));*/
			    		writer.endObject();
			    	}
			    	
			    	writer.endArray();
			    }
	    
			  }

	
}

