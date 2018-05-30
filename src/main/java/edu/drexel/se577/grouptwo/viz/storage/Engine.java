package edu.drexel.se577.grouptwo.viz.storage;

import java.util.Optional;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;

public interface Engine {
    Optional<Dataset> forId(String id);
    Dataset create(Definition definition);
}
