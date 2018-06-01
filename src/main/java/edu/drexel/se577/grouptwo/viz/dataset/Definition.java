package edu.drexel.se577.grouptwo.viz.dataset;

import java.util.Collection;
import java.util.Optional;


public class Definition {
    public final String name;
    private final Attribute.Mapping mapping = new Attribute.Mapping("base");

    public Definition(String name) {
        this.name = name;
    }

    public void put(Attribute attribute) {
        mapping.put(attribute);
    }

    public Optional<?extends Attribute> get(String name) {
        return mapping.get(name);
    }

    public Collection<String> getKeys() {
        return mapping.getKeys();
    }
}
