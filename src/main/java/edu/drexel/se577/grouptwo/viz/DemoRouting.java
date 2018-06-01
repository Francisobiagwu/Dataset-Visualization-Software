package edu.drexel.se577.grouptwo.viz;

import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.filetypes.FileContents;
import edu.drexel.se577.grouptwo.viz.filetypes.FileInputFactory;
import edu.drexel.se577.grouptwo.viz.filetypes.FileInputHandler;
import edu.drexel.se577.grouptwo.viz.filetypes.XLSFileContents;
import edu.drexel.se577.grouptwo.viz.parsers.CSVInputHandler;
import edu.drexel.se577.grouptwo.viz.parsers.XLSInputHandler;
import edu.drexel.se577.grouptwo.viz.storage.Dataset;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class DemoRouting extends Routing {
    
    FileInputFactory _fileInputFactory;

    DemoRouting(){
        List<FileInputHandler> _fileParsers = new ArrayList<>();
        _fileParsers.add(new XLSInputHandler());
        _fileParsers.add(new CSVInputHandler());
        _fileInputFactory = new FileInputFactory(_fileParsers);
    }

    @Override
    Collection<?extends Dataset> listDatasets() {
        return Stream.of(new DemoDataset()).collect(Collectors.toList());
    }

    @Override
    Optional<?extends Dataset> getDataset(String id) {
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
    Optional<?extends FileInputHandler> getFileHandler(String contentType) {
        return Optional.of(_fileInputFactory.GetFileInputHandler(contentType));
    }

    private final class DemoFileInputHandler implements FileInputHandler {
        private final Dataset model = new DemoDataset();

        @Override
        public Optional<?extends FileContents> parseFile(String name,
            byte[] buffer) {
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

		@Override
		public boolean CanParse(String ext) {
			return true;
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
            definition.put(new Attribute.FloatingPoint("temperature", 30.0, -5.0));
            definition.put(new Attribute.Int("capacity", 500, 10));
            definition.put(new Attribute.Enumerated("color", "Green", "Yellow",
                    "Blue"));
            definition.put(new Attribute.Arbitrary("comment"));

            return definition;
        }

        @Override
        public List<Sample> getSamples() {
            Sample sample = new Sample();
            sample.put("temperature", new Value.FloatingPoint(25.0));
            sample.put("capacity", new Value.Int(100));
            sample.put("color", new Value.Enumerated("Green"));
            sample.put("comment",
                new Value.Arbitrary("I don't know how this will be used"));

            return Stream.of(sample, sample, sample).collect(Collectors.toList());
        }

        @Override
        public void addSample(Sample sample) {
        }
    }
}
