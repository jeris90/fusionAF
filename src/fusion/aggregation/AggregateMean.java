package fusion.aggregation;

import java.util.Vector;

import fusion.Models;
//Mean aggregation function
public class AggregateMean extends AggregationFunction {

	@Override
	public Vector<Float> aggregate(Models mod) {
		Vector<Float> result = new Vector<>();
		
		float sum;
		
		for (int i = 0; i < mod.getModels().size(); i++) {
			result.add(0f);
			sum = 0;
			for (int j = 0; j < mod.getDistance().size(); j++) {
				sum += mod.getDistance().get(j).get(i);
			}
			result.set(i, sum/mod.getDistance().size());
		}
		return result;
	}
}
