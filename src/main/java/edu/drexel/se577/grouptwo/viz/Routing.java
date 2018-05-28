package edu.drexel.se577.grouptwo.viz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import spark.Spark;
import spark.Route;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import java.util.Optional;

public abstract class Routing {
    private static final String INTEGER = "integer";
    private static final String FLOAT = "floating-point";
    private static final String ENUMERATED = "enumerated";
    private static final String ARBITRARY = "arbitrary";

    private static final URI DATASETS_PATH = URI.create("/api/datasets/");
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(Sample.class, new SampleGsonAdapter())
        .registerTypeAdapter(Value.class, new ValueGsonAdapter())
        .create();

    abstract Collection<? extends Dataset> listDatasets();
    abstract Optional<? extends Dataset> getDataset(String id);
    abstract URI storeDataset(Definition def);

    final String allDatasets() {
        Collection<? extends Dataset> datasets = listDatasets();
        DatasetRef[] refs = datasets.stream()
            .map(dataset -> {
                DatasetRef ref = new DatasetRef();
                URI id = URI.create(dataset.getId());
                ref.name = dataset.getName();
                ref.location = DATASETS_PATH.resolve(id);
                return ref;
            }).toArray(DatasetRef[]::new);
        return gson.toJson(refs);
    }

    final String selectDataset(String id) {
        return getDataset(id)
            .map(Routing::serializeDataset)
            .orElse(null);
    };

    final static String serializeDataset(Dataset dataset) {
        DatasetRep rep = new DatasetRep();
        rep.definition = convert(dataset.getDefinition());
        rep.samples = dataset.getSamples();
        return gson.toJson(rep);
    }

    final String appendSample(String id, String body) {
        final Sample sample = gson.fromJson(body, Sample.class);
        return getDataset(id)
            .map(dataset -> {
                dataset.addSample(sample);
                return serializeDataset(dataset);
            }).orElse(null);
    }

    final URI instanciateDefinition(String body) {
        DefinitionRep rep = gson.fromJson(body, DefinitionRep.class);
        final Definition def = new Definition(rep.name);
        Stream.of(rep.attributes)
            .forEach(attr -> {
                convert(attr).ifPresent(attribute -> {
                    def.put(attr.name, attribute);
                });
            });
        return DATASETS_PATH.resolve(storeDataset(def));
    }

    private static Optional<Attribute> convert(DefinitionRep.Attribute attr) {
        switch (attr.type) {
        case INTEGER:
        case FLOAT:
        case ENUMERATED:
        case ARBITRARY:
        }
        return Optional.empty();
    }

    static class DatasetRep {
        DefinitionRep definition;
        List<Sample> samples; // This is probably serialize only
    }

    static class DefinitionRep {
        static class Bounds {
            // Double used to represent integers or floating point bounds
            // not great, but simplifies the system
            double upper;
            double lower;
        }
        static class Attribute {
            String name; // Always present
            String type; // Always present
            Bounds bounds; // Conditionally present
            String[] values; // Conditionally present
        }
        String name;
        DefinitionRep.Attribute[] attributes;
    }

    private static DefinitionRep convert(final Definition def) {
        DefinitionRep rep = new DefinitionRep();
        rep.name = def.name;
        rep.attributes = def.getKeys().stream()
            .map(name -> AttributeSerializer.forName(name, def))
            .toArray(DefinitionRep.Attribute[]::new);
        return rep;
    }

    private static final class SampleGsonAdapter implements JsonSerializer<Sample>, JsonDeserializer<Sample> {
        @Override
        public JsonElement serialize(
                final Sample sample,
                java.lang.reflect.Type typeOfT,
                final JsonSerializationContext context)
        {
            final JsonObject obj = new JsonObject();
            sample.getKeys().stream()
                .forEach(name -> {
                    sample.get(name).ifPresent(value -> {
                        obj.add(name,context.serialize(value, Value.class));
                    });
                });
            return obj;
        }

        @Override
        public Sample deserialize(
                JsonElement json,
                java.lang.reflect.Type typeOfT,
                JsonDeserializationContext context)
        {
            final Sample sample = new Sample();
            if (!json.isJsonObject()) throw new JsonParseException(
                    "Sample not formatted correctly");
            final JsonObject asObject = json.getAsJsonObject();
            asObject.keySet().stream()
                .forEach(key -> {
                    sample.put(key, context.deserialize(
                                asObject.get(key),
                                Value.class));
                });
            return sample;
        }
    }

    private static final class ValueGsonAdapter implements JsonDeserializer<Value>, JsonSerializer<Value> {

        static class ValueSerializer implements Value.Visitor {
            Optional<? extends JsonElement> elem = Optional.empty();

            @Override
            public void visit(Value.Int value) {
                JsonObject obj = new JsonObject();
                obj.addProperty("type", INTEGER);
                obj.addProperty("value", Integer.valueOf(value.value));
                elem = Optional.of(obj);
            }

            @Override
            public void visit(Value.FloatingPoint value) {
                JsonObject obj = new JsonObject();
                obj.addProperty("type", FLOAT);
                obj.addProperty("value", Double.valueOf(value.value));
                elem = Optional.of(obj);
            }

            @Override
            public void visit(Value.Enumerated value) {
                JsonObject obj = new JsonObject();
                obj.addProperty("type", ENUMERATED);
                obj.addProperty("value", value.value);
                elem = Optional.of(obj);
            }

            @Override
            public void visit(Value.Arbitrary value) {
                JsonObject obj = new JsonObject();
                obj.addProperty("type", ARBITRARY);
                obj.addProperty("value", value.value);
                elem = Optional.of(obj);
            }

            @Override
            public void visit(Value.Mapping mapping) {
                // NOOP for this version
            }
        }

        @Override
        public JsonElement serialize(
                Value value,
                java.lang.reflect.Type typeOfT,
                JsonSerializationContext context)
        {
            ValueSerializer ser = new ValueSerializer();
            value.accept(ser);
            return ser.elem.orElse(null);
        }

        @Override
        public Value deserialize(
                JsonElement json,
                java.lang.reflect.Type typeOfT,
                JsonDeserializationContext context)
        {
            if (!json.isJsonObject()) throw new JsonParseException(
                    "Sample value formatted incorrectly");
            JsonObject asObject = json.getAsJsonObject();
            if (!asObject.has("type")) throw new JsonParseException(
                    "Missing type attribute");

            String type = asObject.getAsJsonPrimitive("type").getAsString();

            switch (type) {
            case INTEGER:
                return new Value.Int(asObject.getAsJsonPrimitive("value")
                        .getAsInt());
            case FLOAT:
                return new Value.FloatingPoint(
                        asObject.getAsJsonPrimitive("value").getAsDouble());
            case ENUMERATED:
                return new Value.Enumerated(
                        asObject.getAsJsonPrimitive("value").getAsString());
            case ARBITRARY:
                return new Value.Arbitrary(
                        asObject.getAsJsonPrimitive("value").getAsString());
            default:
                throw new JsonParseException("Unknown type attribute");
            }
        }
    }

    static class DatasetRef {
        String name;
        URI location;
    }

    private static Route getDefinitions = (request, reply) -> {
        reply.type("application/json");
        return getInstance().allDatasets();
    };

    private static Route postDefinition = (request, reply) -> {
        URI location = getInstance().instanciateDefinition(request.body());
        reply.header("Location", location.toString());
        reply.status(201);
        return "";
    };

    private static Route getDataset = (request, reply) -> {
        String id = request.params(":id");
        reply.type("application/json");
        return getInstance().selectDataset(id);
    };

    private static Route postSample = (request, reply) -> {
        String id = request.params(":id");
        return getInstance().appendSample(id, request.body());
    };


    private static Routing instance = null;

    static Routing getInstance() {
        instance = Optional.ofNullable(instance).orElse(new DemoRouting());
        return instance;
    }

    private static class AttributeSerializer implements Attribute.Visitor {
        private final String name;
        private Optional<DefinitionRep.Attribute> product;
        private AttributeSerializer(String name) {
            this.name = name;
            product = Optional.empty();
        }
        static DefinitionRep.Attribute forName(String name, Definition def) {
            Attribute attr = def.get(name)
                .orElseThrow(() -> new RuntimeException("No such Attribute"));
            AttributeSerializer ser = new AttributeSerializer(name);
            attr.accept(ser);
            return ser.product
                .orElseThrow(() -> new RuntimeException("Failed to convert attribute"));
        }

        @Override
        public void visit(Attribute.Arbitrary attr) {
            DefinitionRep.Attribute tmp = new DefinitionRep.Attribute();
            tmp.name = name;
            tmp.type = "arbitrary";
            product = Optional.of(tmp);
        }

        @Override
        public void visit(Attribute.Mapping attr) {
            // Tiered structures not currently supported
        }

        @Override
        public void visit(Attribute.Int attr) {
            DefinitionRep.Attribute tmp = new DefinitionRep.Attribute();
            tmp.name = name;
            tmp.type = "integer";
            tmp.bounds = new DefinitionRep.Bounds();
            tmp.bounds.upper = attr.max;
            tmp.bounds.lower = attr.min;

            product = Optional.of(tmp);
        }

        @Override
        public void visit(Attribute.Enumerated attr) {
            DefinitionRep.Attribute tmp = new DefinitionRep.Attribute();
            tmp.name = name;
            tmp.type = "enumerated";
            tmp.values = attr.choices.toArray(new String[0]);

            product = Optional.of(tmp);
        }

        @Override
        public void visit(Attribute.FloatingPoint attr) {
            DefinitionRep.Attribute tmp = new DefinitionRep.Attribute();
            tmp.name = name;
            tmp.type = "floating-point";
            tmp.bounds = new DefinitionRep.Bounds();
            tmp.bounds.upper = attr.max;
            tmp.bounds.lower = attr.min;

            product = Optional.of(tmp);
        }
    }

    public static void main(String[] args) {
        Spark.staticFileLocation("/public");
        Spark.path("/api", () -> {
            Spark.path("/datasets", () -> {
                Spark.get("", Routing.getDefinitions);
                Spark.post("", Routing.postDefinition);
                Spark.get("/:id", Routing.getDataset);
                Spark.post("/:id", Routing.postSample);
            });
        });
        Spark.init();
    }
}
