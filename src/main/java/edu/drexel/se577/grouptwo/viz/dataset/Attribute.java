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

    String name();
    void accept(Visitor visitor);

    public interface Countable extends Attribute {
    }

    public interface Arithmetic extends Attribute {
    }

    public static final class Mapping implements Attribute {
        private final String name;
        private final Map<String, Attribute> mapping = new HashMap<>();

        Mapping(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return this.name();
        }

        /**
         * Package private put method.
         * <p>
         * If we want to support arbitrary attribute graphs,
         * this will need to be public. For now, it can be package private
         * as limiting access to it allows us to control and flatten the
         * attribute graph through the Dataset class.
         */
        void put(Attribute value) {
            mapping.put(value.name(), value);
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

    public static final class Int implements Attribute, Countable, Arithmetic {
        private String name;
        public final int max;
        public final int min;

        public Int(String name, int max, int min) {
            this.name = name;
            this.max = max;
            this.min = min;
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }


    }

    public static final class FloatingPoint implements Attribute, Arithmetic {
        private final String name;
        public final double max;
        public final double min;

        public FloatingPoint(String name, double max, double min) {
            this.name = name;
            this.max = max;
            this.min = min;
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class Enumerated implements Attribute, Countable {
        private final String name;
        public final Set<String> choices;

        public Enumerated(String name, Set<String> choices) {
            this.name = name;
            this.choices = Collections.unmodifiableSet(choices);
        }

        public Enumerated(String name, String... choices) {
            this(name, Stream.of(choices).collect(Collectors.toSet()));
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class Arbitrary implements Attribute, Countable {
        private final String name;

        public Arbitrary(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return this.name;
        }

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
