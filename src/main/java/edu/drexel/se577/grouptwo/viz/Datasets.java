package edu.drexel.se577.grouptwo.viz;

import com.google.gson.Gson;
import java.util.Map;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Attribute;

final class Datasets {
    private static Optional<Datasets> instance = Optional.empty();;
    private static final Gson gson = new Gson();
    private final Handler handler;

    static interface Handler {
        DatasetRef[] all();
        DatasetRep forId(String id);
    }

    final String all() {
        return gson.toJson(handler.all());
    }

    final String getDataset(String id) {
        return gson.toJson(handler.forId(id));
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
            Definition definition = new Definition("Demo Dataset");
            definition.put("temperature", new Attribute.FloatingPoint(30.0, -5.0));
            definition.put("capacity", new Attribute.Int(500, 10));
            definition.put("color",  new Attribute.Enumerated("Green", "Yellow", "Blue"));
            definition.put("comment", new Attribute.Arbitrary());
            rep.definition = convert(definition);
            return rep;
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
        Attribute[] attributes;
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
