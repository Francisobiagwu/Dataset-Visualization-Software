package edu.drexel.se577.grouptwo.viz.dataset;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

public interface Attribute {

    void accept(Visitor visitor);

    public static final class Mapping implements Attribute {
        private final Map<String, Attribute> mapping = new HashMap<>();

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

        public Optional<? extends Attribute> get() {
            return Optional.empty();
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static interface Visitor {
        void visit(Mapping mapping);
    }
}
