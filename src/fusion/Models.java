package fusion;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

public class Models {
	/**
	 * Each Collection<String> corresponds to a model of the integrity constraint
	 */
	private Collection<Collection<String>> models = null;

	/**
	 * Matrix of distances between the models of the constraint and the AFs in the
	 * profile. Each Vector<Float> corresponds to the distances between one fixed AF
	 * in the profile and the models of the constraint
	 */
	private Vector<Vector<Float>> distance = new Vector<>();

	/**
	 * Initializes a Models object from a Collection<Collection<String>>, where each
	 * Collection<String> corresponds to a model of the integrity constraint.
	 * 
	 * @param models the models of the integrity constraint
	 */
	public Models(Collection<Collection<String>> models) {
		this.models = models;
	}

	/**
	 * 
	 * @return the models of the integrity constraint
	 */
	public Collection<Collection<String>> getModels() {
		return this.models;
	}

	/**
	 * Adds the vector of distances between an AF and the models of the constraint
	 * to the attribute distance.
	 * 
	 * @param distVector the vector of distances between a fixed AF and the models
	 *                   of the constraint
	 */
	public void addDistance(Vector<Float> distVector) {
		this.distance.add(distVector);
	}

	public Vector<Vector<Float>> getDistance() {
		return this.distance;
	}

	/**
	 * This method allows to get the the minimal models of the constraint, with
	 * respect to a vector of scores (that should correspond to the aggregated
	 * distances).
	 * 
	 * @param scores
	 * @return
	 */
	public Collection<Collection<String>> getCandidates(Vector<Float> scores) {
		Collection<Collection<String>> result = new Vector<>();
		int index = 0;
		float dist;
		float min = Collections.min(scores);

		for (Collection<String> currentModel : this.getModels()) {
			dist = scores.get(index);
			if (dist == min) {
				result.add(currentModel);
			}
			index++;
		}

		return result;
	}

	/***
	 * Print the candidate(s) (if it/they exist(s))
	 */
	public void printModel() {
		if (this.models.isEmpty()) {
			System.out
					.println("There is no candidate for the aggregation (i.e. the integrity constraint has no model).");
		} else {
			System.out.print(this.models.size() + " candidate(s) for the aggregation : ");
			System.out.println(this.models);
		}
	}

	/**
	 * Transposes the matrix of distances, such that each Vector<Float> corresponds
	 * to the distances between one fixed model of the constraint and all the AFs in
	 * the profile.
	 * 
	 * @param vect the initial matrix of distances
	 * @return the matrix of distances
	 */
	public static Vector<Vector<Float>> transpose(Vector<Vector<Float>> vect) {
		Vector<Vector<Float>> transposed = new Vector<Vector<Float>>();
		for (int i = 0; i < vect.get(0).size(); i++)
			transposed.add(new Vector<Float>());

		for (int i = 0; i < vect.size(); i++)
			for (int j = 0; j < vect.get(i).size(); j++)
				transposed.get(j).add(vect.get(i).get(j));
		return transposed;
	}
}
