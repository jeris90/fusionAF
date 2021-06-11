package fusion;

import java.util.Vector;
//Mean aggregation function
public class AggregateMean extends Aggregate_Function {

	@Override
	public Vector<Float> choosenAggregate(Models mod) {
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
