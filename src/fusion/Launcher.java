package fusion;

import java.io.IOException;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fusion.aggregation.AggregationFactory;
import fusion.aggregation.AggregationFunction;
import fusion.distance.CalculDistance;
import fusion.distance.Distance;
import fusion.distance.DistanceFactory;
import fusion.parser.AFParser;
import fusion.parser.ConstraintManager;
import net.sf.jargsemsat.jargsemsat.datastructures.DungAF;


public class Launcher {
	
	Logger log = Logger.getLogger(Launcher.class.getName());
	
	static Vector<Float> resultat = new Vector<>();
	static Collection<Collection<String>> vec_candidats = new Vector<>();
	
	private static Vector<DungAF> profile_afs = null;
	
	private static String path_profile = null;
	
	private static String input_format = null;
	
	private static String path_constraint = null;
	
	private static String dist = "HM";
	
	private static String aggregation_function = "SUM";
	
	private static String task = "EE";
	
	private static String arg = null;
	
	private static boolean print = false; 
	

	/***
	 * Provides the user with the different options available.
	 * @param options
	 */
	public static void help(Options options) {
		final HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("jarfile", options, true);
	    
	    System.out.println("\nDefault parameters : <distance> = HM, <aggregation_function> = SUM, <output> = EE");
	}
	
	public static void printInformationOptions() {
		if(print) {
			System.out.println("PATH : " + path_profile);
			System.out.println("INPUT_FORMAT : " + input_format);
			System.out.println("IC : " + path_constraint);
			System.out.println("Distance : " + dist);
			System.out.println("Aggregation function : " + aggregation_function);
			System.out.println("Task : " + task);
			System.out.println("Argument : " + arg);
			System.out.println("\n");
		}
	}
	
	/***
	 * Create an Options object containing the different available options (short/long option, description, arguments, mandatory or not, etc).
	 * @return options
	 */
	private static Options configParameters() {
		
		Option profileDirectoryOption = Option.builder("dir") 
	            .longOpt("dir_profile") //
	            .desc("[Mandatory] Path of the directory containing the profile of AFs.") 
	            .hasArg(true) 
	            .argName("dir_profile")
	            .required(true)
	            .build();
		
		Option inputFormatOption = Option.builder("f") 
	            .longOpt("input_format") //
	            .desc("[Mandatory] Format of the files containing the AFs (apx or tgf).") 
	            .hasArg(true) 
	            .argName("input_format")
	            .required(true)
	            .build();
		
		Option constraintFileOption = Option.builder("IC") 
	            .longOpt("int_constraint") //
	            .desc("Path of the file containing the integrity constraint (dimacs format). If not provided, then the integrity constraint " +
	            		"is a tautology (i.e. the set of candidates is the power set of the set of arguments).") 
	            .hasArg(true) 
	            .argName("int_constraint")
	            .required(false)
	            .build();
		
		Option distanceOption = Option.builder("D") 
	            .longOpt("distance") //
	            .desc("Distance used to compare a candidate and a set of extensions. [HM for the Hamming Distance].") 
	            .hasArg(true) 
	            .argName("distance")
	            .required(false)
	            .build();
		
		Option aggregationFunctionOption = Option.builder("AGG") 
	            .longOpt("agg_function") //
	            .desc("Aggregation function used to aggregate the score of a candidate and the set of extensions of each AFs in the profile."
	            		+ "[SUM for sum, MIN for minimum, MAX for maximum, MUL for multiplication, MEAN for mean"
	            		+ " MED for mediane, LMIN for leximin, LMAX for leximax]") 
	            .hasArg(true) 
	            .argName("agg_function")
	            .required(false)
	            .build();
		
		Option outputFunctionOption = Option.builder("p") 
	            .longOpt("problem") //
	            .desc("Choice of the task to be carried out by the programme (EE for extensions enumeration, DC for credulous acceptance for a given argument, DS for skeptical acceptance for a given argument).") 
	            .hasArg(true) 
	            .argName("problem")
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
	            .desc("Prints all details of the aggregation process.") 
	            .hasArg(false) 
	            .argName("print")
	            .required(false)
	            .build();
		
		Option printHelp = Option.builder("h") 
	            .longOpt("help") //
	            .desc("Help option.") 
	            .hasArg(false) 
	            .required(false)
	            .build();
	
		Options options = new Options();
		
		options.addOption(profileDirectoryOption);
		options.addOption(inputFormatOption);
		options.addOption(constraintFileOption);
		options.addOption(distanceOption);
		options.addOption(aggregationFunctionOption);
		options.addOption(outputFunctionOption);
		options.addOption(argumentOption);
		options.addOption(printOption);
		options.addOption(printHelp);
		
		return options;
	}
	
	/***
	 * A method of retrieving the options given by the user and checking whether the options entered are valid or not.
	 * @param args
	 * @return Options
	 */
	private static Options commandLineManagement(String args[]) {
		
		Options options = configParameters();
		
		CommandLineParser parser = new DefaultParser();
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
			
			if(line.hasOption("h")) {
				help(options);
				System.exit(0);
			}
			
			if(line.hasOption("print")) {
				print = true;
			}
			
			if (line.hasOption("f")) {
				input_format = line.getOptionValue("f");
			}
			
			if (line.hasOption("dir")) {
				path_profile = line.getOptionValue("dir");
				profile_afs = AFParser.readAFDirectory(path_profile,input_format);
			}
			
			if (line.hasOption("IC")) {
				path_constraint = line.getOptionValue("IC");
			}
			
			if (line.hasOption("D")) {
				dist = line.getOptionValue("D");
			}
			
			if (line.hasOption("AGG")) {
				aggregation_function = line.getOptionValue("AGG");
			}
			
			if (line.hasOption("p")) {
				task = line.getOptionValue("p");
			}
			
			
			if (line.hasOption("a")) {
				if(!line.hasOption("p")) {
					Print.print(print,"-a option is omitted because -p option is missing (default task : EE).");
				}
				else {
					if(line.getOptionValue("p").equals("EE")) {
						Print.print(print,"INFO : -a option is omitted because -p EE (extensions enumeration) does not need a specific argument.");
					}
					else {
						arg = line.getOptionValue("a");
						if(!profile_afs.get(0).getArguments().contains(arg)) { // Check if the argument belongs to the set of arguments shared by the profile of AFs
							System.err.println("Unknown argument for the option -a : " + arg + "\nPlease choose among the following list of arguments : " + profile_afs.get(0).getArguments());
							System.exit(1);
						}
					}
						
				}
			}
			
			printInformationOptions();
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("Error parsing command-line arguments!\n");
			help(options);
			System.exit(1);
		}
		
		return options;
	}

	
	/***
	 * Extract the extension of a file. The extension is used to know which semantics applied to the AF.
	 * @param fileName_af the file containing the AF
	 * @return the extension of the file
	 */
	public static String extractSemantics(String fileName_af) {
		
		String sem = fileName_af.substring(fileName_af.lastIndexOf(".") + 1);
		
		Print.print(print,"****************  " + fileName_af + "  ***************");
		
		return sem;	
	}

	public static void aggregation(Models mod, AggregationFunction agg_function) {
		int ind = 0;
		
		resultat = agg_function.aggregate(mod);
		
		float min = resultat.get(0);

		Print.print(print,"\nResults from the chosen aggregation function for each candidate :");

		for (Collection<String> candidate : mod.getModels()) {
			if(min >= resultat.get(ind)) {
				Print.print(print,candidate + " : " + resultat.get(ind));
				min = resultat.get(ind);
			}
			ind++;
		}
		
		vec_candidats = mod.getCandidates(resultat);	
	}
	
	public static void main(String args[]) throws IOException {
				
		// OPTIONS
		Options options = commandLineManagement(args);
		
		
		// DISTANCE
		Distance distance = new DistanceFactory().makeDistance(dist);
		
		
		// AGGREGATION FUNCTION
		AggregationFunction as = new AggregationFactory().makeAggregation(aggregation_function);

		
		// Execution time
		long tempsDebut = System.nanoTime();
		
		
		// Reading model
		Collection<Collection<String>> mod = null;
		if(path_constraint == null) { 		// if no integrity constraint has been provided
			mod = ConstraintManager.fullModels(profile_afs.get(0).getArguments());
		}
		else {        						// otherwise, the models of the integrity constraint are calculated
			mod = ConstraintManager.getModels(path_constraint);
		}
		
		Models models = new Models(mod);
		
		models.printModel(print);
		
		if(models.getModels().isEmpty()) {		// if there are no models, the result of the aggregation is necessarily empty
			System.out.println("The integrity constraint has no model so the result of the aggregation is empty.");
			System.exit(1);
		}
		
		// Store the name of each file containing an AF (to extract the extension later) 
		Vector<String> fileNameAFs = AFParser.getFileNames(path_profile);
		
		for(int j = 0 ; j<profile_afs.size() ; j++) { // for each AF in the profile
			DungAF af = profile_afs.get(j);
			String semantics = extractSemantics(fileNameAFs.get(j));
			
			CalculDistance.computeDistance(af, models, distance, semantics, print); // compute the distance of each model with a given AF for a given semantics
		}
		
		aggregation(models, as);
		

		
		// Print results according to a given task (EE, DC or DS)
		Print.print(print,"\nResult : \n");	
		
		Results result = new Results(vec_candidats);
		
		if(task.equals("EE")) {
			System.out.println(result.printExtensionsEnumeration());
		}
		else {
			if(task.equals("DC") || task.equals("DS")) {
				System.out.println(result.printAcceptance(task,arg));
			}
			else {
				System.err.println("Unknown argument for the option -p.");
				System.exit(1);
			}
		}

		// Print the execution time
		long tempsFin = System.nanoTime();
		double seconds = (tempsFin - tempsDebut) / 1e9;
		Print.print(print,"\nTime : " + seconds + " sec");
	}
}
