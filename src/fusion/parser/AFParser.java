package fusion.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import net.sf.jargsemsat.jargsemsat.datastructures.DungAF;

public abstract class AFParser {

	/**
	 * Reads an AF from a TGF file. The data read from the file are stored in the
	 * parameters arguments and attacks.
	 * 
	 * @param arguments the Vector<String> where the arguments are stored
	 * @param attacks   the Vector<String[]> where the attacks are stored
	 * @param afFile    the tgf file describing the AF
	 */
	public static void readingTGF(Vector<String> arguments, Vector<String[]> attacks, String afFile) {
		arguments.clear();
		attacks.clear();
		String file_arguments = new String();
		// path to the file to be read
		String link = new String(afFile);
		String[] splited = new String[2];
		Boolean attaque = false;
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(link));
			String line = reader.readLine();
			while (line != null) {
				try {
					if (line.equals("#")) {
						attaque = true;
					} else {
						file_arguments = line;
						if (attaque == false) {
							arguments.add(file_arguments);
						} else {
							splited = file_arguments.split(" ");
							attacks.add(splited);
						}
					}
				} catch (NumberFormatException e) {
					System.err.println("Erreur dans le try : " + line + ".");
				}
				line = reader.readLine();
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage() + "Excep 2");
		}

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
		Vector<String> argument = new Vector<String>();
		Vector<String[]> atts = new Vector<String[]>();
		Vector<DungAF> afs = new Vector<DungAF>();
		// Reading Af's files
		File dir = new File(afDirectory);
		File[] liste = dir.listFiles();
		for (File item : liste) {
			if (item.isFile()) {
				readingTGF(argument, atts, item.getAbsolutePath());
				DungAF af = new DungAF(argument, atts);
				afs.add(af);
			}
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
