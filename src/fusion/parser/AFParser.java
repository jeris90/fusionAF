package fusion.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	public static Vector<DungAF> readAFDirectory(String afDirectory) {
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
				DungAF af = readingTGF(item.getAbsolutePath());
				afs.add(af);
			}
			else {
				System.out.println(item.getAbsolutePath() + " is not taken into account because it is not a file. ");
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
