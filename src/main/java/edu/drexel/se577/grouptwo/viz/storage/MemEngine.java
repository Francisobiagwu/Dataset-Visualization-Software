package edu.drexel.se577.grouptwo.viz.storage;

import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.Collections;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;

final class MemEngine implements Engine {
    final Map<UUID, Dataset> datasets = new HashMap<>();

    @Override
    public Dataset create(Definition def) {
        MemDataset dataset = new MemDataset(def);
        datasets.put(dataset.id, dataset);
        return dataset;
    }

    @Override
    public Optional<Dataset> forId(String id) {
        return Optional.ofNullable(datasets.get(UUID.fromString(id)));
    }

    @Override
    public String createViz(Visualization visualization) {
        return "FOO";
    }

    static final class MemDataset implements Dataset {
        final UUID id;
        private final Definition definition;
        private final List<Sample> samples;

        {
            id = UUID.randomUUID();
            samples = new ArrayList<>();
        }

        MemDataset(Definition definition) {
            this.definition = definition;
        }

        @Override
        public String getId() {
            return id.toString();
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
            return Collections.unmodifiableList(samples);
        }

        @Override
        public void addSample(Sample sample) {
            // TODO: validate this
            samples.add(sample);
        }
    }
}
