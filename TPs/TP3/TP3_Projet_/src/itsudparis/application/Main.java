/*
* To change this template, choose Tools | Templates10
* and open the template in the editor.
*/
package itsudparis.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.hp.hpl.jena.rdf.model.Model;
import itsudparis.tools.JenaEngine;

/**
 * @author DO.ITSUDPARIS
 */
public class Main {
	/**
	 * @param args
	 *            the command line arguments
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String NS = "";
		// lire le model a partir d'une ontologie
		Model model = JenaEngine.readModel("data/family.owl");
		if (model != null) {
			// lire le Namespace de l’ontologie
			NS = model.getNsPrefixURI("");
			// modifier le model
			// Ajouter une nouvelle femme dans le modele: Nora,50, estFilleDe
			// Peter
			JenaEngine.createInstanceOfClass(model, NS, "Femme", "Nora");
			JenaEngine.updateValueOfDataTypeProperty(model, NS, "Nora", "age", 50);
			JenaEngine.updateValueOfObjectProperty(model, NS, "Nora", "estFilleDe", "Peter");
			// Ajouter un nouvel homme dans le modele: Rob, 51, seMarierAvec
			// Nora
			JenaEngine.createInstanceOfClass(model, NS, "Homme", "Rob");
			JenaEngine.updateValueOfDataTypeProperty(model, NS, "Rob", "age", 51);
			JenaEngine.updateValueOfDataTypeProperty(model, NS, "Rob", "nom", "Rob Yeung");
			JenaEngine.updateValueOfObjectProperty(model, NS, "Rob", "seMarierAvec", "Nora");
			// apply owl rules on the model
			Model owlInferencedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/owlrules.txt");
			// apply our rules on the owlInferencedModel
			Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(owlInferencedModel, "data/rules.txt");
			// query on the model after inference
			boolean continu = true;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (continu) {
				System.out.println("entrer le nom d'une personne : ");
				try {
					String nom = br.readLine();
					System.out.println("les parents de "+nom+" sont : ");
					System.out.println(JenaEngine.executeQueryFileWithParameter(inferedModel, "data/query5.txt", nom));
					System.out.println("voici les Freres de "+nom+" : sont ");
					System.out.println(JenaEngine.executeQueryFileWithParameter(inferedModel, "data/query6.txt", nom));
					System.out.println("voici les Soeurs de "+nom+" : sont ");
					System.out.println(JenaEngine.executeQueryFileWithParameter(inferedModel, "data/query7.txt", nom));
					System.out.println("voici le conjoint de "+nom+" : ");
					System.out.println(JenaEngine.executeQueryFileWithParameter(inferedModel, "data/query8.txt", nom));
					System.out.print("Entrer 0 pour continuer, autre chose sinon:");
					String val = br.readLine();
					int i = Integer.parseInt(br.readLine());
					if (!val.equals("0")) {
						continu = false;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Error when reading model from ontology");
		}
	}
}
