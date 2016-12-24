/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itsudparis.application;

import com.hp.hpl.jena.rdf.model.Model;
import itsudparis.tools.JenaEngine;

/**
 *
 * @author DO.ITSUDPARIS
 */
public class Main {
    public static final String ns = "http://www.owl-ontologies.com/Ontology1291196007.owl#";
    public static String inputDataOntology = "data/family.owl";
    public static String inputRule = "data/rules.txt";
    public static String inputQuery = "data/query.txt";

    /**
     * @param args rhe command line arguments
     */

    public static void main(String[] args){
        Model model = JenaEngine.readModel(inputDataOntology);
        Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, inputRule);
        JenaEngine.updateValueOfDataTypeProperty(inferedModel, ns, "Peter", "age", 10);
        JenaEngine.updateValueOfObjectProperty(inferedModel, ns, "Peter", "estFilsDe", "Femme1");
        JenaEngine.createInstanceOfClass(model, ns, "Femme", "abc");
        //query on the model
        System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery));
    }

}
