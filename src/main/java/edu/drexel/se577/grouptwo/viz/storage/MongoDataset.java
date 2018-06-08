package edu.drexel.se577.grouptwo.viz.storage;

import java.util.List;
import java.util.Collections;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;

import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

final class MongoDataset implements Dataset {
    private final Definition definition;
    private final ObjectId id;
    private final MongoCollection<Document> datasets;

    MongoDataset(ObjectId id, Definition definition, MongoCollection<Document> datasets)
    {
        this.id = id;
        this.definition = definition;
        this.datasets = datasets;
    }

    @Override
    public String getId() {
        return id.toHexString();
    }

    @Override
    public String getName() {
        return definition.name;
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    @Override
    public List<Sample> getSamples() {
        return Collections.emptyList();
    }

    @Override
    public void addSample(Sample sample) {
    }
}
