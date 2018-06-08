package edu.drexel.se577.grouptwo.viz.storage;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;

import org.bson.BsonValue;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonArray;
import org.bson.LazyBSONList;
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

    private static Attribute toAttribute(Document doc) {
        String name = doc.get("name", String.class);
        String type = doc.get("type", String.class);
        switch (type) {
        case INTEGER:
            return toIntegerAttr(name, doc);
        case ARBITRARY:
            return toArbitraryAttr(name, doc);
        case FLOATING_POINT:
            return toFloatingPointAttr(name, doc);
        case ENUMERATED:
            return toEnumeratedAttr(name, doc);
        }
        throw new RuntimeException("Unknown Attribute Type");
    }

    private static Attribute toIntegerAttr(String name, Document doc) {
        Document bounds = doc.get("bounds", Document.class);
        int max = bounds.get("max", Integer.class);
        int min = bounds.get("min", Integer.class);
        return new Attribute.Int(name, max, min);
    }

    private static Attribute toFloatingPointAttr(String name, Document doc) {
        Document bounds = doc.get("bounds", Document.class);
        double max = bounds.get("max", Double.class);
        double min = bounds.get("min", Double.class);
        return new Attribute.FloatingPoint(name, max, min);
    }

    private static Attribute toArbitraryAttr(String name, Document doc) {
        return new Attribute.Arbitrary(name);
    }

    private static Attribute toEnumeratedAttr(String name, Document doc) {
        @SuppressWarnings("unchecked")
        List<String> vals = (List<String>) doc.get("values", List.class);
        String[] values = vals.toArray(new String[0]);
        return new Attribute.Enumerated(name, values);
    }

    private static Definition toDefinition(Document doc) {
        String name = doc.get("name", String.class);
        final Definition def = new Definition(name);
        // Mostly unavoidable here.
        @SuppressWarnings("unchecked")
        List<Document> attributes =
            (List<Document>) doc.get("attributes", List.class);
        attributes.stream().forEach(aDoc -> {
            Attribute attr = toAttribute(aDoc);
            def.put(attr);
        });
        // TODO: implement
        return def;
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
        // TODO: Implement this next
        return Optional.empty();
    }

    @Override
    public Dataset create(Definition definition) {
        MongoCollection<Document> datasets =
            database.getCollection(DATASET_COLLECTION);
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
    public Collection<? extends Dataset> listDatasets() {
        final MongoCollection<Document> datasets =
            database.getCollection(DATASET_COLLECTION);
        return StreamSupport.stream(datasets.find().spliterator(), false)
            .map(doc -> {
                ObjectId id = doc.get("_id",ObjectId.class);
                Definition def = toDefinition(
                        doc.get("definition",Document.class));
                return new MongoDataset(id, def, datasets);
            }).collect(Collectors.toList());
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
