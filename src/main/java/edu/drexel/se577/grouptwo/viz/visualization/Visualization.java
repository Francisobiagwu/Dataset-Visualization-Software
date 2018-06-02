package edu.drexel.se577.grouptwo.viz.visualization;

import java.util.List;

import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Value;

/**
 * Interface for the definition and realization of visualizations.
 * <p>
 * The visitor pattern is to assist with definition details on both
 * the REST API side and the storage side of the back end.
 * <p>
 * The image interface provides the necessary methods to provide an image
 * in various formats to the front end.
 * <p>
 * The render method is working under the expectation that we will not
 * be choosing at request time what kind of image will be generated.
 * We can change that decision later.
 * <p>
 * The data access methods for each type of visualization are meant to
 * serve both as utilities for fetching the data if desired, and to assist
 * with providing the raw data for visualization to the front end if it is
 * requested.
 */
public interface Visualization {

    String getName();
    String getId();

    void accept(Visitor visitor);

    Image render(); // Should we add image type?

    public interface Image {
        String mimeType();
        byte[] data();
    }

    public abstract class Series implements Visualization {
        public final String datasetId;
        public final Attribute.Arithmetic attribute;

        protected Series(String datasetId, Attribute.Arithmetic attribute) {
            this.datasetId = datasetId;
            this.attribute = attribute;
        }

        @Override
        public final void accept(Visitor visitor) {
            visitor.visit(this);
        }

        public abstract List<Value> data();
    }

    public abstract class Histogram implements Visualization {
        public final String datasetId;
        public final Attribute.Countable attribute;

        public static final class DataPoint {
            public final Value bin;
            public final long count;
            public DataPoint(Value bin, long count) {
                this.bin = bin;
                this.count = count;
            }
        }

        protected Histogram(String datasetId, Attribute.Countable attribute) {
            this.datasetId = datasetId;
            this.attribute = attribute;
        }

        @Override
        public final void accept(Visitor visitor) {
            visitor.visit(this);
        }

        public abstract List<DataPoint> data();
    }

    public abstract class Scatter implements Visualization {
        public final String datasetId;
        public final Attribute.Arithmetic xAxis;
        public final Attribute.Arithmetic yAxis;

        public static final class DataPoint {
            public final Value x;
            public final Value y;

            public DataPoint(Value x, Value y) {
                this.x = x;
                this.y = y;
            }
        }

        protected Scatter(
                String datasetId,
                Attribute.Arithmetic xAxis,
                Attribute.Arithmetic yAxis)
        {
            this.datasetId = datasetId;
            this.xAxis = xAxis;
            this.yAxis = yAxis;
        }

        @Override
        public final void accept(Visitor visitor) {
            visitor.visit(this);
        }

        public abstract List<DataPoint> data();
    }

    public interface Visitor {
        void visit(Series viz);
        void visit(Histogram viz);
        void visit(Scatter viz);
    }
}
