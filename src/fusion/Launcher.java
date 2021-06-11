package fusion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import fusion.aggregation.AggregateLexiMax;
import fusion.aggregation.AggregateLexiMin;
import fusion.aggregation.AggregateMax;
import fusion.aggregation.AggregateMean;
import fusion.aggregation.AggregateMed;
import fusion.aggregation.AggregateMin;
import fusion.aggregation.AggregateMul;
import fusion.aggregation.AggregateSum;
import fusion.aggregation.AggregationFunction;
import fusion.distance.CalculDistance;
import fusion.distance.Distance;
import fusion.distance.DistanceHamming;
import fusion.parser.ConstraintManager;
import fusion.parser.AFParser;
import net.sf.jargsemsat.jargsemsat.datastructures.DungAF;

public class Launcher {
	static Vector<Float> resultat = new Vector<>();
	static Collection<Collection<String>> vec_candidats = new Vector<>();
	static boolean addMod = true;

	public static void help() {
		System.out.println("Usage: jarfile <af_path> <constrain_path> <distance> <aggregation_function>");
		System.out.println("\taf_path: The path to the tgf af file.");
		System.out.println("\tconstrain_path: The path to the constrain file.");
		System.out.println("\tdistance: Distance type one of: \"HM\" for Hamming, \"LV\" for Leveinshtein.");
		System.out.println(
				"\taggregation_function: SUM for sum, MIN for Minimum, MAX for Maximum, MUL for Multiplication, MEAN for mean,"
						+ " MED for Mediane, LMIN for LexiMin, LMAX for LexiMax.");
		System.out.println(
				"The default parameters used are : <af_path> = Afs, <constrain_path> = constraint.txt, <distance> = HM, <aggregation_function> = SUM ");
	}

	public static void main(String args[]) throws IOException {

		List<String> args_d = null;
		if (args.length != 4) {
			help();
			args_d = Arrays.asList("Afs", "constraint.txt", "HM", "SUM");
		} else
			args_d = Arrays.asList(args);

		if (!(new File(args_d.get(0))).isDirectory()) {
			System.out.println("\"" + args_d.get(0) + "\" is not a valid file");
			return;
		}
		if (!Files.isWritable(Paths.get(args_d.get(1)))) {
			System.out.println("\"" + args_d.get(1) + "\" is not a valid file");
			return;
		}

		Distance distance = null;
		switch (args_d.get(2)) {
		case "HM":
			distance = new DistanceHamming();
			break;
		/*
		 * case "LV": distance = new DistanceLevenshtein(); break;
		 */
		default:
			System.err.println("Error distance \"" + args_d.get(2) + "\" not handled");
			return;
		}
		AggregationFunction as = null;

		switch (args_d.get(3)) {
		case "SUM":
			as = new AggregateSum();
			break;
		case "MIN":
			as = new AggregateMin();
			break;
		case "MAX":
			as = new AggregateMax();
			break;
		case "MUL":
			as = new AggregateMul();
			break;
		case "MEAN":
			as = new AggregateMean();
			break;
		case "LMIN":
			as = new AggregateLexiMin();
			break;
		case "MED":
			as = new AggregateMed();
			break;
		case "LMAX":
			as = new AggregateLexiMax();
			break;
		default:
			System.err.println("Error Agregation function \"" + args_d.get(3) + "\" not handled");
			return;
		}
		// Calculation of execution time
		long tempsDebut = System.nanoTime();
		// Reading model
		Collection<Collection<String>> model = ConstraintManager.getModels(args_d.get(1));
		Models mod = new Models(model);
		String sem = new String();

		Vector<String> fileName = AFParser.getFileNames(args_d.get(0));
		boolean supported = true;
		int j = 0;

		mod.printModel();
		// if there is no constraint file
		if (mod.getModels().isEmpty()) {
			for (DungAF af : AFParser.readAFDirectory(args_d.get(0))) {
				if (addMod) {
					model = toCollec(af.getArguments());
					mod = new Models(model);
					addMod = false;
				}
				mainFunc(fileName, af, mod, sem, distance, supported, j);
				j++;
			}

		} else {
			// if there is a constraint file
			for (DungAF af : AFParser.readAFDirectory(args_d.get(0))) {
				mainFunc(fileName, af, mod, sem, distance, supported, j);
				j++;
			}
		}

		mainAgregate(mod, as, supported);

		long tempsFin = System.nanoTime();
		double seconds = (tempsFin - tempsDebut) / 1e9;
		System.out.println();
		System.out.println("Pour " + args_d.get(0) + " Arguments Opération effectuée en: " + seconds + " secondes.");
	}

	public static Collection<Collection<String>> toCollec(Collection<String> defaultModel) {
		Collection<Collection<String>> returnStat = new HashSet<Collection<String>>();
		returnStat.add(defaultModel);
		return returnStat;
	}

	public static void mainFunc(Vector<String> fileName, DungAF af, Models mod, String sem, Distance distance,
			boolean supported, int j) {
		System.out.format("****************  " + fileName.get(j) + "  *************** \n");
		if (fileName.get(j).toString().endsWith("co")) {
			sem = "CO";
		} else {
			if (fileName.get(j).toString().endsWith("gr")) {
				sem = "GR";
			} else {
				if (fileName.get(j).toString().endsWith("st")) {
					sem = "ST";
				} else {
					if (fileName.get(j).toString().endsWith("pr")) {
						sem = "PR";
					} else {
						if (fileName.get(j).toString().endsWith("txt") || fileName.get(j).toString().endsWith("tgf")) {
							sem = "CO";
						} else {
							System.err.println("file extension not supported : " + fileName.get(j).toString()
									+ "\nsupported extentions: .co for complet, .pr for preferred, gr for grounded, st for stable ");
							supported = false;
							return;
						}
					}
				}
			}
		}

		// calculating distance
		CalculDistance.calculDistance(af, mod, distance, sem);
	}

	public static void mainAgregate(Models mod, AggregationFunction agg_function, boolean supported) {
		int ind = 0;
		if (supported) {
			resultat = agg_function.aggregate(mod);

			System.out.println("\nResults from the chosen aggregation function for each candidate :");
			for (Collection<String> cand : mod.getModels()) {
				System.out.println(cand + " : " + resultat.get(ind));
				ind++;
			}
			// System.out.println("Distance for each candidate : " + resultat);
			vec_candidats = mod.getCandidats(resultat);
			System.out.println(
					"\nThe result of the aggregation is the following set of sets of arguments : " + vec_candidats);

		} else {
			System.err.println("Interruped process file extension not supported ");
		}
	}

}
