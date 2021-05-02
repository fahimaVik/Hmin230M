package nlp;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class ABU {
	
	
	static HashMap<String, ArrayList<String>> map;
	
	public static void createDico (String filePath) throws IOException {
	    map = new HashMap<String, ArrayList<String>>();
	    String line;
	    BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.forName("ISO-8859-1"));
	    while ((line = reader.readLine()) != null)
	    {
	        String[] parts = line.split("	");
	        if (parts.length >= 2)
	        {
	            String key = parts[0];
	            String value = parts[1];
	            
	            String deux = parts[1]+"	"+parts[2];
	            
	            if (map.keySet().contains(parts[0])) {
	            	map.get(parts[0]).add(deux);
	            }else {
	            	ArrayList<String> values = new ArrayList<String>();
	            	values.add(deux);
	            	map.put(key, values);
	            }
	            	
	        } else {
	            System.out.println(line);
	        }
	    }
	}
	
	
	public int howManyLemmes (String mot) {
		int count=0;
		if (map.keySet().contains(mot))
			for (String key : map.keySet()){
			  if(key.equals(mot))
				  for (String str : map.get(mot)){
						count++;
					}
			  }
		return count;
	}
	
	

	public String  getLemme (String mot) {
		return map.get(mot).get(0);
	}
	
	public void  getLemmes(String mot) {
		if (!map.keySet().contains(mot)) System.out.println("Le mot n'existe pas");
		else {
			  for (String key : map.keySet()){
				  if(key.equals(mot))
					  for (String str : map.get(mot)){
							String[] tab = str.split("	");
							//return tab[0];
							System.out.println("La source du mot "+mot+" est "+tab[0]);
						}
				  }
		}
	}
	
	
	public String  detecter(String mot,String s) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			  for (String key : map.keySet()){
				  if(key.equals(mot))
					  for (String str : map.get(mot)){
							String[] tab = str.split("	");
							if (tab[1].contains(s)) return tab[0];
						}
				  }
		}
		return null;
	}
	
	public String getPos(String mot) {
		if (!map.keySet().contains(mot)) System.out.println("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				return tab[1].substring(0, 3);
			}
		}
        return null;
	}

	public boolean isPluriel(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("PL")) return true;
			}
		}
        return false;
	}
	
	public boolean isAdj(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Adj")) return true;
			}
		}
        return false;
	}
	
	
	public static boolean isVerb(String mot) {
		if (!map.keySet().contains(mot)) System.out.println("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Ver")) return true;
			}
		}
        return false;
	}
	
	public boolean isMasculin(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Mas")) return true;
			}
		}
        return false;
	}
	
	
	public boolean isNom(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Nom")) return true;
			}
		}
        return false;
	}
	
	
	public boolean isFeminin(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Fem")) return true;
			}
		}
        return false;
	}
	
	
}
