package fusion;

import java.util.Vector;
// Minimum aggregation function
public class AggregateMin extends AggregationFunction {
	@Override
	public Vector<Float> aggregate(Models mod) {
		Vector<Float> min = new Vector<>();
		for (Vector<Float> model : mod.getDistance())
			for (int i = 0; i < model.size(); i++)
				if (min.size() <= i)
					min.add((float)model.get(i));
				else
					min.set(i, Math.min(min.get(i), model.get(i)));
		return min;
	}
}
