package edu.drexel.se577.grouptwo.viz.storage;

import java.util.Collection;
import java.util.Optional;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.visualization.Visualization;

public interface Engine {
    Optional<Dataset> forId(String id);
    Dataset create(Definition definition);
    Collection<Dataset> listDatasets();
    String createViz(Visualization visualization);

    public static Engine getInstance() {
        return MemEngine.getInstance();
    }
}
