package edu.drexel.se577.grouptwo.viz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Value;

final class Datasets {
    private static Optional<Datasets> instance = Optional.empty();;
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(Value.class, new ValueDeserializer())
        .create();
    private final Handler handler;

    // This will probably need to change a bit before the merge request.
    // for now it should do for driving API format testing.
    static interface Handler {
        DatasetRef[] all();
        DatasetRep forId(String id);
        DatasetRep postDefinition(Definition def);
    }

    private static final class ValueDeserializer implements JsonDeserializer<Value> {
        private static final String INTEGER = "integer";
        private static final String FLOAT = "floating-point";
        private static final String ENUMERATED = "enumerated";
        private static final String ARBITRARY = "arbitrary";

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

    final String all() {
        return gson.toJson(handler.all());
    }

    final String getDataset(String id) {
        return gson.toJson(handler.forId(id));
    }

    /**
     * Create a new dataset and retrieve it's URI.
     * <p>
     * The URI provide is only a fragment. It's meant to be incorporated into
     * a larger URI.
     *
     * @param body The body of the POST request sent to create the dataset.
     */
    final URI createDataset(String body) {
        System.err.println(body);
        DefinitionRep rep = gson.fromJson(body, DefinitionRep.class);
        System.err.println(rep.toString());
        return URI.create("any-old-id");
    }

    private final static class DemoHandler implements Handler {
        @Override
        public DatasetRef[] all() {
            DatasetRef demo = new DatasetRef();
            demo.name = "Demo Dataset";
            demo.location = URI.create("/api/datasets/not-final");
            return Stream.of(demo).toArray(DatasetRef[]::new);
        }

        public DatasetRep forId(String id) {
            DatasetRep rep = new DatasetRep();
            Map<String, Object> sample = new HashMap<>();
            sample.put("temperature", Double.valueOf(25.0));
            sample.put("capacity", Integer.valueOf(100));
            sample.put("color", "Green");
            sample.put("comment", "I don't know how this will be used");
            Definition definition = new Definition("Demo Dataset");
            definition.put("temperature", new Attribute.FloatingPoint(30.0, -5.0));
            definition.put("capacity", new Attribute.Int(500, 10));
            definition.put("color",  new Attribute.Enumerated("Green", "Yellow", "Blue"));
            definition.put("comment", new Attribute.Arbitrary());
            rep.definition = convert(definition);
            rep.samples = Stream.of(sample,sample,sample)
                .collect(Collectors.toList());
            return rep;
        }

        public DatasetRep postDefinition(Definition def) {
            return forId("any");
        }
    }

    private Datasets(Handler handler) {
        this.handler = handler;
    }

    static class DatasetRef {
        String name;
        URI location;
    }

    static Datasets getInstance() {
        instance = Optional.of(instance.orElseGet(
                    () ->  new Datasets(new DemoHandler())));

        return instance.get();
    }

    static class DatasetRep {
        DefinitionRep definition;
        List<Map<String, Object>> samples; // This is probably serialize only
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
}
