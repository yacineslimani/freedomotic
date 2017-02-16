/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package  com.freedomotic.plugins.devices.smartHouse.application;

import java.util.Scanner;

import com.hp.hpl.jena.rdf.model.Model;
import com.freedomotic.plugins.devices.smartHouse.tools.JenaEngine;


/**
 *
 * @author DO.ITSUDPARIS
 */
public class OntologieApp {
   // public static final String ns = "http://www.semanticweb.org/nacer&samir/ontologies/2016/10/Famille-ontology#";
	
	public static final String ns = "http://www.semanticweb.org/nacer/ontologies/smartHome#";
	public static String inputDataOntology = "data/smartHome.owl";
    public static String inputRule = "data/rules.txt";
    public static String inputQuery = "data/query.txt";
    public static String inputQuery2 = "data/query2.txt";

    /**
     * @param args rhe command line arguments
     */

    public void createInstance(String cls ,String value, String uuid){
        Model model = JenaEngine.readModel(inputDataOntology);
        Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, inputRule);
        
        JenaEngine.createInstanceOfClass(model, ns, cls, value);
        JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasName", value);
        
        
        // JenaEngine.updateValueOfDataTypeProperty(inferedModel, ns, "yacine", "hasName", "yacine");
        //query on the model
        System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery));
       // System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery2));
       
        
       
    }

}
