package nlp;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Principale {
	static HashMap<String, ArrayList<String>> Regles_verbes = new HashMap <String, ArrayList<String>>(); //liste règles pour verbes
	static HashMap<String, ArrayList<String>> Regles_mots = new HashMap <String, ArrayList<String>>();  //liste règles pour le reste des mots
	//static HashMap<String, ArrayList<String>> Regles_adj = new HashMap <String, ArrayList<String>>(); 
	static HashMap<String, String> mots_generes= new HashMap <String, String>();  //hasmap mot générés 
	static HashMap<String, String> mots_retenus= new HashMap <String, String>();  //hashmap mot gardés
	static ArrayList<String> entrees = new ArrayList<String>();   // liste des mots en entrée
	
	public static void main(String[] args) throws IOException {
		
		fetchEntrees();		//methode cherche les donnée d'entrée et le ajoute dans l'arraylist entree	  
		fetchRegles();		//methode 
		derivation();		//methode 
		verificationABU();	//methode 
		affichage();		//methode 

		}
	
	public static void affichage(){
		System.out.println("  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*  ");
		System.out.println("|                entrées :                     |");
		System.out.println("  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*  ");
		for (String mot : entrees) {
			System.out.println("|"+mot);
			System.out.println("----------------------------------------------");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*  ");
		System.out.println("|              Mots générés:                   |");
		System.out.println("  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*  ");
		for (String mot : mots_generes.keySet()) {
			System.out.println("|"+mot+" --> "+mots_generes.get(mot));
			System.out.println("----------------------------------------------");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*  ");
		System.out.println("|       Mots retenus après vérification:       |");
		System.out.println("  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*  ");
		for (String mot : mots_retenus.keySet()) {
			System.out.println("|"+mot+" --> "+mots_retenus.get(mot));
			System.out.println("----------------------------------------------");
		}
		//System.out.println("Règles Mots : "+Regles_mots);
		//System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		System.out.println("Règles Verbes: "+Regles_verbes);
		
		
	}
	public static void derivation() throws IOException{
		ABU.createDico("dico.txt");
		for (String mot : entrees) {
			if (ABU.isVerb(mot)) {
				for (String terminaison : Regles_verbes.keySet()) {
					for (String derivation : Regles_verbes.get(terminaison)) {
						if (derivation.startsWith("!pre!")) {
							String mot_genere = derivation.split(":")[0].trim().replace("!pre!", "")+mot;
							
							mots_generes.put(mot_genere.trim(), derivation.split(":")[1]);
						}
						else {
							if (mot.endsWith(terminaison)) {
								String mot_genere = (mot.substring(0,mot.length()-terminaison.length())+derivation.split(":")[0]).trim();
								mots_generes.put(mot_genere, derivation.split(":")[1]);
							}
						}
					}
				}
			}
			if (!ABU.isVerb(mot)) {
				for (String terminaison : Regles_mots.keySet()) {
					for (String derivation : Regles_mots.get(terminaison)) {
						if (mot.endsWith(terminaison)) {
							String mot_genere = (mot.substring(0,mot.length()-terminaison.length())+derivation.split(":")[0]).trim();
							mots_generes.put(mot_genere.trim(), derivation.split(":")[1]);
						}
					}
				}
			}
		}
	}
	public static void verificationABU(){
		for (String mot_genere : mots_generes.keySet()) {
			//System.out.println("TEEEEST --"+mot_genere.trim());
			if (ABU.map.containsKey(mot_genere.trim()) || ABU.map.containsValue(mot_genere.trim())) {
				mots_retenus.put(mot_genere, mots_generes.get(mot_genere));
			}
		}
	}
	public static void fetchEntrees() throws IOException{
		BufferedReader buffer = Files.newBufferedReader(Paths.get("entrées.txt"), Charset.forName("UTF-8")); //mise en mémoire bfr using 
		String tmp;
		while ((tmp=buffer.readLine()) != null) {
			entrees.add(tmp);
		}
	}
	public static void fetchRegles() throws IOException{
		BufferedReader buffer = Files.newBufferedReader(Paths.get("règles_de_déformations.txt"), Charset.forName("UTF-8"));
		String tmp;
		String category = "";
		String premisse = "";
		String conclusion = "";
		while ((tmp=buffer.readLine()) != null) {    //partage en catégories verbes et mots
			if (tmp.startsWith("[")){
				if (tmp.equals("[V]")) {category = "[V]";}
				if (tmp.equals("[M]")) {category = "[M]";}
			}
			else {
				
				if (category.equals("[V]")) {
					if (tmp.split("->")[0].trim().startsWith("$") && tmp.split("->")[1].trim().startsWith("$")) {
						premisse = tmp.split("->")[0].substring(1).trim();
						conclusion = tmp.split("->")[1].substring(2).trim();
					}
					else if (tmp.split("->")[0].startsWith("$") && !tmp.split("->")[1].trim().startsWith("$")) 
					{
						premisse = tmp.split("->")[0].substring(1).trim();
						conclusion = "!pre!"+tmp.split("->")[1].split(":")[0].split("$")[0].substring(0, tmp.split("->")[1].split(":")[0].split("$")[0].length()-2);
						conclusion = conclusion+" : "+tmp.split("->")[1].split(":")[1];
					}
					else if (!tmp.split("->")[0].trim().startsWith("$") && tmp.split("->")[1].trim().startsWith("$")) 
					{
						premisse = "!pre!"+tmp.split("->")[0].split("$")[0].trim();
						conclusion = tmp.split("->")[1].substring(2).trim();
					}
					
					if (Regles_verbes.keySet().contains(premisse)) {
						Regles_verbes.get(premisse).add(conclusion);					
					}
					else 
					{
						Regles_verbes.put(premisse,new ArrayList <String> ());
						Regles_verbes.get(premisse).add(conclusion);
					}
				}	
				if (category.equals("[M]")) {
					premisse = tmp.split("->")[0].substring(1).trim();
					conclusion = tmp.split("->")[1].substring(2).trim();
					if (Regles_mots.keySet().contains(premisse)) {
						Regles_mots.get(premisse).add(conclusion);					
					}
					else 
					{
						Regles_mots.put(premisse,new ArrayList <String> ());
						Regles_mots.get(premisse).add(conclusion);
					}
				}
			}
		}
	}
	}
	
