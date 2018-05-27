package edu.drexel.se577.grouptwo.viz.dataset;

import java.util.Optional;
import java.util.Collection;

public class Definition {
    public final String name;
    private final Attribute.Mapping mapping = new Attribute.Mapping();

    public Definition(String name) {
        this.name = name;
    }

    public void put(String value, Attribute attribute) {
        mapping.put(value, attribute);
    }

    public Optional<? extends Attribute> get(String name) {
        return mapping.get(name);
    }

    public Collection<String> getKeys() {
        return mapping.getKeys();
    }
}
