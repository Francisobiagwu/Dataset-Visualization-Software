package edu.drexel.se577.grouptwo.viz;

import java.util.Optional;
import java.util.Random;
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
        return Stream.of(new DemoDataset("1","Dataset A"), new DemoDataset("2","Dataset B"), new DemoDataset("3","Dataset B"))
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

        String _id;
        String _name;

        public DemoDataset(){
            _id = "any-old-id";
            Random rand = new Random();
            _name = "Demo Dataset" + rand.nextInt();
        }

        public DemoDataset(String id, String name){
            _id = id;
            _name = name;
        }

        @Override
        public String getId() {
            return _id;
        }
        @Override
        public String getName() {
            return _name;
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
