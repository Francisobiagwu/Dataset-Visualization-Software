package edu.drexel.se577.grouptwo.viz;

import java.util.Optional;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
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
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;
import edu.drexel.se577.grouptwo.viz.filetypes.FileContents;
import edu.drexel.se577.grouptwo.viz.filetypes.FileInputHandler;

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

    @Override
    Dataset createDataset(Definition def) {
        return new DemoDataset();
    }

    @Override
    Optional<? extends FileInputHandler> getFileHandler(String contentType) {
        
        return Optional.of(new DemoFileInputHandler());
    }

    @Override
    Collection<? extends Visualization> listVisualizations() {
        Visualization viz = new Visualization() {
            @Override
            public String getId() {
                return "any-old-id";
            }
            @Override
            public String getName() {
                return "Demo Visualization";
            }
            @Override
            public void accept(Visualization.Visitor visitor) {
            }
            @Override
            public Visualization.Image render() {
                // TODO: implement this with a static photo.
                return null;
            }
        };
        return Stream.of(viz).collect(Collectors.toList());
    };

    private final class DemoFileInputHandler implements FileInputHandler {
        private final Dataset model = new DemoDataset();

        @Override
        public Optional<? extends FileContents> parseFile(String name, byte[] buffer) {
            return Optional.of(new FileContents() {
                @Override
                public Definition getDefinition() {
                    return model.getDefinition();
                }
                @Override
                public List<Sample> getSamples() {
                    return model.getSamples();
                }
            });
        }
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
            definition.put(new Attribute.FloatingPoint(
                        "temperature",30.0, -5.0));
            definition.put(new Attribute.Int("capacity",500,10));
            definition.put(new Attribute.Enumerated("color", "Green", "Yellow", "Blue"));
            definition.put(new Attribute.Arbitrary("comment"));
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
