package fusion;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

public class Models {
	private Collection<Collection<String>> models = null;
	private Vector<Vector<Float>> distance = new Vector<>();

	public Models(Collection<Collection<String>> model) {
		this.models = model;
	}

	public Collection<Collection<String>> getModels() {
		return this.models;
	}

	public void setDistance(Vector<Float> dist) {
		this.distance.add(dist);
	}

	public Vector<Vector<Float>> getDistance() {
		return this.distance;
	}

	public Collection<Collection<String>> getCandidats(Vector<Float> scores) {
		Collection<Collection<String>> result = new Vector<>();
		int index = 0;
		float dist;
		float min = Collections.min(scores);
		
		for (Collection<String> current_mod : this.getModels()) {
			dist = scores.get(index);
			if (dist == min) {
				result.add(current_mod);
			}
			index++;
		}
		
		return result;
	}
	
	/***
	 * Print the candidate(s) (if it/they exist(s))
	 */
	public void afficheModel() {
		if(this.models.isEmpty()) {
			System.out.println("There is no candidate for the aggregation (i.e. the integrity constraint has no model).");
		}
		else {
			System.out.print(this.models.size() +  " candidate(s) for the aggregation : ");
			System.out.println(this.models);
		}
	}
	
	static Vector<Vector<Float>> transpose(Vector<Vector<Float>> vect) {
		Vector<Vector<Float>> transposed = new Vector<>();
		for (int i = 0; i < vect.get(0).size(); i++)
			transposed.add(new Vector<Float>());

		for (int i = 0; i < vect.size(); i++)
			for (int j = 0; j < vect.get(i).size(); j++)
				transposed.get(j).add(vect.get(i).get(j));
		return transposed;
	}
}
