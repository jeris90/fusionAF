package fusion.aggregation;

public class AggregationFactory extends AbstractAggregationFactory {
	public AggregationFunction makeAggregation(String aggregation) {
		switch (aggregation) {
		case "SUM":
			return new AggregateSum();
		case "MIN":
			return new AggregateMin();
		case "MAX":
			return new AggregateMax();
		case "MUL":
			return new AggregateMul();
		case "MEAN":
			return new AggregateMean();
		case "LMIN":
			return new AggregateLexiMin();
		case "MED":
			return new AggregateMed();
		case "LMAX":
			return new AggregateLexiMax();
		default:
			throw new IllegalArgumentException("Aggregation function " + aggregation + " does not exist.");
		}
	}
}
