package fusion;

import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
	
	private static String path_profile = null;
	
	private static String path_constraint = null;
	
	private static String dist = "HM";
	
	private static String aggregation_function = "SUM";
	
	private static String task = "EE";
	
	private static String arg = null;
	
	private static boolean print = false; 
	

	public static void help(Options options) {
		System.out.println("Usage: jarfile -dir <af_path> -CI <constrain_path> -D <distance> -AGG <aggregation_function>");
		System.out.println("\t<af_path>: " + options.getOption("dir").getDescription());
		System.out.println("\t<constrain_path>: " + options.getOption("CI").getDescription());
		System.out.println("\t<distance>: " + options.getOption("D").getDescription());
		System.out.println(
				"\t<aggregation_function>: " + options.getOption("AGG").getDescription());
		System.out.println(
				"The default parameters used are : <distance> = HM, <aggregation_function> = SUM ");
	}
	
	private static Options configParameters() {
		
		Option profileDirectoryOption = Option.builder("dir") 
	            .longOpt("dir_profile") //
	            .desc("Path of the directory containing the profile of AFs") 
	            .hasArg(true) 
	            .argName("dir_profile")
	            .required(true)
	            .build();
		
		Option constraintFileOption = Option.builder("CI") 
	            .longOpt("int_constraint") //
	            .desc("Path of the file containing the integrity constraint (dimacs format)") 
	            .hasArg(true) 
	            .argName("int_constraint")
	            .required(false)
	            .build();
		
		Option distanceOption = Option.builder("D") 
	            .longOpt("distance") //
	            .desc("Distance used to compare a candidate and a set of extensions (HM for the Hamming Distance).") 
	            .hasArg(true) 
	            .argName("distance")
	            .required(false)
	            .build();
		
		Option aggregationFunctionOption = Option.builder("AGG") 
	            .longOpt("agg_function") //
	            .desc("Aggregation function used to aggregate the score of a candidate and the set of extensions of each AFs in the profile."
	            		+ "SUM for sum, MIN for Minimum, MAX for Maximum, MUL for Multiplication, MEAN for mean"
	            		+ " MED for Mediane, LMIN for LexiMin, LMAX for LexiMax.") 
	            .hasArg(true) 
	            .argName("agg_function")
	            .required(false)
	            .build();
		
		Option outputFunctionOption = Option.builder("p") 
	            .longOpt("output") //
	            .desc("Choice of the task to be carried out by the programme (EE for extensions enumeration, DC for credulous acceptance for a given argument, DS for skeptical acceptance for a given argument).") 
	            .hasArg(true) 
	            .argName("output")
	            .required(false)
	            .build();
		
		Option argumentOption = Option.builder("a") 
	            .longOpt("arg") //
	            .desc("Option mandatory with the option -p DC or DS to specify the targeted argument.") 
	            .hasArg(true) 
	            .argName("argument")
	            .required(false)
	            .build();
		
		Option printOption = Option.builder("print") 
	            .desc("Prints all details of the aggregation process ") 
	            .hasArg(false) 
	            .argName("print")
	            .required(false)
	            .build();
	
		Options options = new Options();
		
		options.addOption(profileDirectoryOption);
		options.addOption(constraintFileOption);
		options.addOption(distanceOption);
		options.addOption(aggregationFunctionOption);
		options.addOption(outputFunctionOption);
		options.addOption(argumentOption);
		options.addOption(printOption);
		
		return options;
	}

	
	/***
	 * Extract the extension of a file. The extension is used to know which semantics applied to the AF.
	 * @param fileName_af the file containing the AF
	 * @return the extension of the file
	 */
	public static String extractSemantics(String fileName_af) {
		
		String sem = fileName_af.substring(fileName_af.lastIndexOf(".") + 1);
		
		System.out.format("****************  " + fileName_af + "  *************** \n");
		
		return sem;	
	}

	public static void aggregation(Models mod, AggregationFunction agg_function) {
		int ind = 0;
		resultat = agg_function.aggregate(mod);

		System.out.println("\nResults from the chosen aggregation function for each candidate :");
		for (Collection<String> candidate : mod.getModels()) {
			System.out.println(candidate + " : " + resultat.get(ind));
			ind++;
		}
		
		vec_candidats = mod.getCandidates(resultat);	
	}
	
	public static void main(String args[]) throws IOException {
				
		Options options = configParameters();
		
		CommandLineParser parser = new DefaultParser();
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
			
			if (line.hasOption("dir")) {
				path_profile = line.getOptionValue("dir");
				System.out.println("PATH : " + path_profile);
			}
			
			if (line.hasOption("CI")) {
				path_constraint = line.getOptionValue("CI");
				System.out.println("CI : " + path_constraint);
			}
			
			if (line.hasOption("D")) {
				dist = line.getOptionValue("D");
				System.out.println("Distance : " + dist);
			}
			
			if (line.hasOption("AGG")) {
				aggregation_function = line.getOptionValue("AGG");
				System.out.println("Aggregation function : " + aggregation_function);
			}
			
			if (line.hasOption("p")) {
				task = line.getOptionValue("p");
				System.out.println("Task : " + task);
			}
			
			
			if (line.hasOption("a")) {
				if(!line.hasOption("p")) {
					System.out.println("-a option is omitted because -p option is missing.");
				}
				else {
					if(line.getOptionValue("p").equals("EE")) {
						System.out.println("-a option is omitted because -p EE does not need a specific argument.");
					}
					else {
						arg = line.getOptionValue("a");
						System.out.println("Argument : " + arg);
					}
						
				}
			}
			
			if(line.hasOption("print")) {
				print = true;
				System.out.println("Print details : " + print);
			}
			
			System.out.println("\n");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Error parsing command-line arguments!\n");
			help(options);
			//e.printStackTrace();
			System.exit(1);
		}

		
		Distance distance = null;
		switch (dist) {
			case "HM":
				distance = new DistanceHamming();
				break;
			/*
			 * case "LV": distance = new DistanceLevenshtein(); break;
			 */
			default:
				System.err.println("Error distance \"" + dist + "\" not handled. \n");
				help(options);
				return;
		}
		
		
		
		AggregationFunction as = null;

		switch (aggregation_function) {
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
				System.err.println("Error Agregation function \"" + aggregation_function + "\" not handled");
				help(options);
				return;
		}
		
		
		// Calculation of execution time
		long tempsDebut = System.nanoTime();
		
		// Import a profile of AFs 
		Vector<DungAF> profile_afs= AFParser.readAFDirectory(path_profile);
		Vector<String> fileNameAFs = AFParser.getFileNames(path_profile);
		
		// Reading model
		Collection<Collection<String>> mod = null;
		if(path_constraint == null) { 		// if no integrity constraint has been provided
			mod = ConstraintManager.fullModels(profile_afs.get(0).getArguments());
		}
		else {        						// otherwise, the models of the integrity constraint are calculated
			mod = ConstraintManager.getModels(path_constraint);
		}
		
		Models models = new Models(mod);
		
		models.printModel();
		
		if(models.getModels().isEmpty()) {		// if there are no models, the result of the aggregation is necessarily empty
			System.out.println("The integrity constraint has no model so the result of the aggregation is empty.");
			System.exit(1);
		}
		
		
		for(int j = 0 ; j<profile_afs.size() ; j++) { // for each AF in the profile
			DungAF af = profile_afs.get(j);
			String semantics = extractSemantics(fileNameAFs.get(j));
			
			CalculDistance.computeDistance(af, models, distance, semantics); // compute the distance of each model with a given AF for a given semantics
		}
		
		aggregation(models, as);
		

		System.out.println("\nThe result of the aggregation is the following set of sets of arguments : ");	
		
		Results result = new Results(vec_candidats);
		
		if(task.equals("EE")) {
			System.out.println(result.printExtensionsEnumeration());
		}
		else {
			System.out.println(result.printAcceptance(task,arg));
		}

		long tempsFin = System.nanoTime();
		double seconds = (tempsFin - tempsDebut) / 1e9;
		System.out.println();
		System.out.println("Pour " + path_profile + " Arguments Opération effectuée en: " + seconds + " secondes.");
	}
}
