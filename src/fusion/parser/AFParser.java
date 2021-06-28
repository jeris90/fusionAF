package fusion.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

import net.sf.jargsemsat.jargsemsat.datastructures.DungAF;

public abstract class AFParser {

	/***
	 * Reads an AF from a TGF file.
	 * @param afFile	the tgf file describing the AF
	 * @return a DungAF object
	 */
	public static DungAF readingTGF(String afFile) {
		Vector<String> arguments = new Vector<String>();
		Vector<String[]> attacks = new Vector<String[]>();
		
		String file_arguments = new String();
		// path to the file to be read
		String link = new String(afFile);
		String[] splited = new String[2];
		Boolean attack = false;
		
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(link));
			String line = reader.readLine();
			while (line != null) {
				try {
					if (line.equals("#")) {
						attack = true;
					} else {
						file_arguments = line;
						if (attack == false) {
							arguments.add(file_arguments);
						} else {
							splited = file_arguments.split(" ");
							attacks.add(splited);
						}
					}
				} catch (NumberFormatException e) {
					System.err.println("Error : " + line + ".");
				}
				line = reader.readLine();
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage() + "Excep 2");
		}
		
		return new DungAF(arguments, attacks);

	}

	/***
	 * Reads an AF from a APX file.
	 * @param afFile	the apx file describing the AF
	 * @return a DungAF object
	 */
	public static DungAF readingAPX(String afFile) {
		Vector<String> arguments = new Vector<String>();
		Vector<String[]> attacks = new Vector<String[]>();
		
		File file = new File(afFile);
		
		Scanner reader;
		try {
			reader = new Scanner(file);
			while(reader.hasNextLine()){
	        	String line = reader.nextLine();
	        	String command = line.split("\\(") [0];
	            String res = "";
	            
	            if(checkingBeginningCommand(line))
	                res = line.split("\\(") [1];
	            
	            if(checkingEndCommand(line))
	                res = res.split("\\)") [0];
	            
	            if(command.equals("arg")) {
	            	arguments.add(res);
	            }
	            else {
	            	if(command.equals("att")) {
	            		if(checkingAttackFormat(res)) {
	            			String arg1 = res.split(",") [0];
	                        String arg2 = res.split(",") [1];
	                        
	                        if(arguments.contains(arg1) && arguments.contains(arg2)) {
	                        	attacks.add(res.split(","));
	                        }
	                        else {
	                        	System.err.println("Unknown argument in the file " + afFile + " : " + "(" + arg1 + "," + arg2 + ")");
	                        	System.exit(1);
	                        }
	            		}
	            	}
	            	else {
	            		System.err.println("Unknown line in file " + afFile + " : " + line);
	            		System.exit(1);
	            	}
	            }
	            
	        }
	        reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return new DungAF(arguments, attacks);
	}
	
    
    public static Boolean checkingBeginningCommand(String line){
        return (line.contains("("))? true : false;
    }

    
    public static Boolean checkingAttackFormat(String line){
        return (line.contains(","))? true : false;
    }

    
    public static Boolean checkingEndCommand(String line){
        return (line.contains(")"))? true : false;
    }
	


	/**
	 * Prints an array of integer in a humanly readable way.
	 * 
	 * @param tab
	 */
	public static void printTab(int[] tab) {
		System.out.print("\t[");
		for (int i = 0; i < tab.length; i++) {
			System.out.print(" " + tab[i]);
		}
		System.out.println("]");
	}

	


	/**
	 * Reads all the AFs contained in a given directory
	 * 
	 * @param afDirectory the directory where the AF files are stored
	 * @return the AFs
	 */
	public static Vector<DungAF> readAFDirectory(String afDirectory, String input_format) {
		Vector<DungAF> afs = new Vector<DungAF>();
		// Reading Af's files
		File dir = new File(afDirectory);
		if(! dir.isDirectory()) { // if the directory does not exist
			System.err.println("Unknown directory : " + afDirectory);
			System.exit(1);
		}
		
		File[] liste = dir.listFiles();
			
		for (File item : liste) {
			if (item.isFile()) {
				DungAF af = null;
				if(input_format.equals("tgf")) {
					af = readingTGF(item.getAbsolutePath());
				}
				else {
					if(input_format.equals("apx")) {
						af = readingAPX(item.getAbsolutePath());
					}
					else {
						System.err.println("Unknown format for the option -f (apx or tgf).");
						System.exit(1);
					}
				}
				//DungAF af = readingAPX(item.getAbsolutePath());
				afs.add(af);
			}
			else {
				System.err.println(item.getAbsolutePath() + " is not taken into account because it is not a file. ");
			}
		}
		
		if(afs.size() == 0) { // if the directory exists but is empty (or contains only directories), the program is stopped
			System.err.println("The directory " + afDirectory + " is empty or does not contain any file.");
			System.exit(1);
		}
		
		return afs;
	}

	/**
	 * Retrieve file names from a directory of AF files
	 * 
	 * @param afDirectory
	 * @return
	 */
	public static Vector<String> getFileNames(String afDirectory) {
		Vector<String> listFile = new Vector<String>();
		String it = new String();
		File dir = new File(afDirectory);
		File[] liste = dir.listFiles();
		for (File item : liste) {
			if (item.isFile()) {
				it = item.toString();
				listFile.add(it);
			}

		}
		return listFile;
	}
}
