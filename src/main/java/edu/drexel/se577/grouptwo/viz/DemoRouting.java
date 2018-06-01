package edu.drexel.se577.grouptwo.viz;

import java.util.Optional;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.storage.Dataset;
import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Value;

class DemoRouting extends Routing {
    @Override
    Collection<? extends Dataset> listDatasets() {
        return Stream.of(new DemoDataset())
            .collect(Collectors.toList());
    }

    @Override
    Optional<? extends Dataset> getDataset(String id) {
        return Optional.of(new DemoDataset());
    }

    @Override
    URI storeDataset(Definition def) {
        return URI.create("any-old-id");
    }

    private final class DemoDataset implements Dataset {
        @Override
        public String getId() {
            return "any-old-id";
        }
        @Override
        public String getName() {
            return "Demo Dataset";
        }

        @Override
        public Definition getDefinition() {
            Definition definition = new Definition(getName());
            definition.put("temperature",
                    new Attribute.FloatingPoint(30.0, -5.0));
            definition.put("capacity",
                    new Attribute.Int(500,10));
            definition.put("color",
                    new Attribute.Enumerated("Green", "Yellow", "Blue"));
            definition.put("comment",
                    new Attribute.Arbitrary());
            return definition;
        }

        @Override
        public List<Sample> getSamples() {
            Sample sample = new Sample();
            sample.put("temperature", new Value.FloatingPoint(25.0));
            sample.put("capacity", new Value.Int(100));
            sample.put("color", new Value.Enumerated("Green"));
            sample.put("comment", new Value.Arbitrary("I don't know how this will be used"));
            return Stream.of(sample,sample,sample)
                .collect(Collectors.toList());
        }

        @Override
        public void addSample(Sample sample) {
        } 
    }
}
