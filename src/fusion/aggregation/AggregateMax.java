package fusion.aggregation;

import java.util.Vector;

import fusion.Models;
//Maximum aggregation function
public class AggregateMax extends AggregationFunction {
	@Override
	public Vector<Float> aggregate(Models mod) {
		Vector<Float> max = new Vector<>();
		for (Vector<Float> model : mod.getDistance())
			for (int i = 0; i < model.size(); i++)
				if (max.size() <= i)
					max.add((float)model.get(i));
				else
					max.set(i, Math.max(max.get(i), model.get(i)));
		return max;
	}
}
