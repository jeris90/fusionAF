package fusion.aggregation;

public abstract class AbstractAggregationFactory {
	public abstract AggregationFunction makeAggregation(String aggregation);
}
