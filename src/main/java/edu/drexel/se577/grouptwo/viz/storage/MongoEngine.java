package edu.drexel.se577.grouptwo.viz.storage;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;

import org.bson.BsonValue;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonArray;
import org.bson.types.ObjectId;
import org.bson.BsonInt32;
import org.bson.BsonDouble;
import org.bson.BsonString;
import org.bson.Document;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

class MongoEngine implements Engine {
    private static final String DATASET_COLLECTION = "DatasetCollection";
    private static final String VISUALIZATION_COLLECTION = "VisualizationCollection";

    private static final String INTEGER = "integer";
    private static final String FLOATING_POINT = "floating-point";
    private static final String ARBITRARY = "arbitrary";
    private static final String ENUMERATED = "enumerated";
    private final MongoClient client;
    private final MongoDatabase database;

    {
        client = MongoClients.create();
        database = client.getDatabase("viz");
    }

    private static MongoEngine instance = null;

    static MongoEngine getInstance() {
        instance = Optional.ofNullable(instance).orElseGet(MongoEngine::new);
        return instance;
    }

    private MongoEngine() {
    }

    private static class AttributeEncoder implements Attribute.Visitor {
        private final BsonDocument doc;
        AttributeEncoder(BsonDocument doc) {
            this.doc = doc;
        }

        @Override
        public void visit(Attribute.Mapping mapping) {
            // Ignoring this case as we currently only support flat
            // structures.
        }

        @Override
        public void visit(Attribute.Int attr) {
            BsonDocument bounds = new BsonDocument();
            doc.put("type", new BsonString(INTEGER));
            doc.put("name", new BsonString(attr.name()));
            bounds.put("max", new BsonInt32(attr.max));
            bounds.put("min", new BsonInt32(attr.max));
            doc.put("bounds", bounds);
        }

        @Override
        public void visit(Attribute.FloatingPoint attr) {
            BsonDocument bounds = new BsonDocument();
            doc.put("type", new BsonString(FLOATING_POINT));
            doc.put("name", new BsonString(attr.name()));
            bounds.put("max", new BsonDouble(attr.max));
            bounds.put("min", new BsonDouble(attr.max));
            doc.put("bounds", bounds);
        }

        @Override
        public void visit(Attribute.Arbitrary attr) {
            doc.put("type", new BsonString(ARBITRARY));
            doc.put("name", new BsonString(attr.name()));
        }

        @Override
        public void visit(Attribute.Enumerated attr) {
            final BsonArray values = new BsonArray();
            doc.put("type", new BsonString(ENUMERATED));
            doc.put("name", new BsonString(attr.name()));
            attr.choices.stream()
                .forEach(choice -> {
                    values.add(new BsonString(choice));
                });
            doc.put("values", values);
        }
    }

    private static BsonValue toBson(final Attribute attr) {
        BsonDocument doc = new BsonDocument();
        attr.accept(new AttributeEncoder(doc));
        return doc;
    }

    private static BsonValue toBson(final Definition definition) {
        BsonDocument doc = new BsonDocument();
        final BsonArray attributes = new BsonArray();
        doc.put("name", new BsonString(definition.name));
        definition.getKeys().stream()
            .forEach(key -> {
                definition.get(key).ifPresent(attr -> {
                    attributes.add(toBson(attr));
                });
            });
        doc.put("attributes", attributes);
        return doc;
    }

    @Override
    public Optional<Dataset> forId(String _id) {
        ObjectId id = new ObjectId(_id);
        // TODO: Implement
        return Optional.empty();
    }

    @Override
    public Dataset create(Definition definition) {
        MongoCollection<Document> datasets =
            database.getCollection(DATASET_COLLECTION);
        // TODO: Implement
        Document doc = new Document();
        doc.put("definition", toBson(definition));
        System.err.println(doc.toJson());
        datasets.insertOne(doc);
        ObjectId id = doc.get("_id",ObjectId.class);
        System.err.println(id.toString());
        System.err.println(doc.toJson());
        return new MongoDataset(id, definition, datasets);
    }

    @Override
    public Collection<Dataset> listDatasets() {
        // TODO: Implement
        return Collections.emptyList();
    }

    @Override
    public Visualization createViz(Visualization visualization) {
        // TODO: Implement
        return null;
    }

    @Override
    public Optional<Visualization> getVisualization(String id) {
        // TODO: Implement
        return Optional.empty();
    }

    @Override
    public Collection<Visualization> listVisualizations() {
        // TODO: Implement
        return Collections.emptyList();
    }
}
