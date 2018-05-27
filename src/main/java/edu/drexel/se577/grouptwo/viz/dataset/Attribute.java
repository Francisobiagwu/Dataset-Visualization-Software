package edu.drexel.se577.grouptwo.viz.dataset;

import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public interface Attribute {

    void accept(Visitor visitor);

    public static final class Mapping implements Attribute {
        private final Map<String, Attribute> mapping = new HashMap<>();

        Mapping() {
        }

        /**
         * Package private put method.
         * <p>
         * If we want to support arbitrary attribute graphs,
         * this will need to be public. For now, it can be package private
         * as limiting access to it allows us to control and flatten the
         * attribute graph through the Dataset class.
         */
        void put(String key, Attribute value) {
            mapping.put(key, value);
        }

        Collection<String> getKeys() {
            return mapping.keySet();
        }

        public Optional<? extends Attribute> get(String name) {
            return Optional.ofNullable(mapping.get(name));
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class Int implements Attribute {
        public final int max;
        public final int min;

        public Int(int max, int min) {
            this.max = max;
            this.min = min;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }


    }

    public static final class FloatingPoint implements Attribute {
        public final double max;
        public final double min;

        public FloatingPoint(double max, double min) {
            this.max = max;
            this.min = min;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class Enumerated implements Attribute {
        public final Set<String> choices;

        public Enumerated(Set<String> choices) {
            this.choices = Collections.unmodifiableSet(choices);
        }

        public Enumerated(String... choices) {
            this(Stream.of(choices).collect(Collectors.toSet()));
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class Arbitrary implements Attribute {
        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static interface Visitor {
        void visit(Arbitrary attribute);
        void visit(Mapping mapping);
        void visit(Int attribute);
        void visit(Enumerated attribute);
        void visit(FloatingPoint attribute);
    }
}
