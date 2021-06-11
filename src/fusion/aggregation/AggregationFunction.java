package fusion.aggregation;

import java.util.Vector;

import fusion.Models;

//Abstract aggregation function
public abstract class AggregationFunction {
	public abstract Vector<Float> aggregate(Models mod);
}
