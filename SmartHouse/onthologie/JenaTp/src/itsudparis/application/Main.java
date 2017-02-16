/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itsudparis.application;

import java.util.Scanner;

import com.hp.hpl.jena.rdf.model.Model;
import itsudparis.tools.JenaEngine;


/**
 *
 * @author DO.ITSUDPARIS
 */
public class Main {
   // public static final String ns = "http://www.semanticweb.org/nacer&samir/ontologies/2016/10/Famille-ontology#";
	
		public static final String ns = "http://www.semanticweb.org/nacer/ontologies/smartHome#";
	public static String inputDataOntology = "data/smartHome.owl";
    public static String inputRule = "data/rules.txt";
    public static String inputQuery = "data/query.txt";
    public static String inputQuery2 = "data/query2.txt";

    /**
     * @param args rhe command line arguments
     */

    public static void main(String[] args){
        Model model = JenaEngine.readModel(inputDataOntology);
        Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, inputRule);
        JenaEngine.updateValueOfDataTypeProperty(inferedModel, ns, "Peter", "age", 10);
        JenaEngine.updateValueOfObjectProperty(inferedModel, ns, "Peter", "estFilsDe", "Femme1");
        JenaEngine.createInstanceOfClass(model, ns, "Person", "yacine");
       JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, "yacine","hasName", "yacineg");
        // JenaEngine.updateValueOfDataTypeProperty(inferedModel, ns, "yacine", "hasName", "yacine");
        //query on the model
        System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery));
       // System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery2));
       
        
        Scanner sc = new Scanner(System.in);
        String nom="";
        System.out.println("Donner le nom :) :");
        nom=sc.nextLine();
        System.out.println("nom =" + nom);
        
        System.out.println(JenaEngine.executeQueryFileWithParameter(inferedModel, inputQuery, nom));
    }

}
