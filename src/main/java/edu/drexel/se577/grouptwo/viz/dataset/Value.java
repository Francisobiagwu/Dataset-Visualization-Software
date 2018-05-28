package edu.drexel.se577.grouptwo.viz.dataset;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public interface Value {
    void accept(Visitor visitor);

    public static final class Arbitrary implements Value {
        public final String value;

        public Arbitrary(String value) {
            this.value = value;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class Int implements Value {
        public final int value;

        public Int(int value) {
            this.value = value;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class FloatingPoint implements Value {
        public final double value;

        public FloatingPoint(double value) {
            this.value = value;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class Enumerated implements Value {
        public final String value;

        public Enumerated(String value) {
            this.value = value;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class Mapping implements Value {
        public final Map<String, Value> mapping = new HashMap<>();

        Mapping() {
        }

        Collection<String> getKeys() {
            return mapping.keySet();
        }

        void put(String name, Value value) {
            mapping.put(name, value);
        }

        public Optional<? extends Value> get(String name) {
            return Optional.ofNullable(mapping.get(name));
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static interface Visitor {
        void visit(Arbitrary value);
        void visit(Int value);
        void visit(FloatingPoint value);
        void visit(Enumerated value);
        void visit(Mapping mapping);
    }
}
