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
	public static String inputDataOntology = "/home/hakubi/Téléchargements/smartHome.owl";//"data/smartHome.owl";
    public static String inputRule = "/home/hakubi/Téléchargements/NewRULES.txt";//rules.txt";
    public static String inputQuery = "/home/hakubi/freedomotic/plugins/devices/smartHouse/data/query.txt";
    public static String inputQuery2 = "/home/hakubi/freedomotic/plugins/devices/smartHouse/data/query2.txt";

    /**
     * @param args rhe command line arguments
     */
    
    Model model = JenaEngine.readModel(inputDataOntology);
    Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, inputRule);
    //pour tout ce qui n'est pas room ou person
    public void doQuery(){
        System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery));

    }
    public void createObjectRelInstance(String obj1, String rel, String obj2){

        
        JenaEngine.addValueOfObjectProperty(inferedModel, ns, obj1, rel,obj2);
      
    }
    
    public void createObjectInstance(String cls ,String value,String prop, String uuid){

        JenaEngine.createInstanceOfClass(model, ns, cls, value);
        JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,prop, uuid);
 
    }
    //pour person 
    public void createPersonInstance(String cls ,String value, String uuid){

        JenaEngine.createInstanceOfClass(model, ns, cls, value);
        JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasName", value);
        JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasId", uuid);
        
     
        //query on the model
       
        
       
    }
    public void createRoomInstance(String cls ,String value, String uuid){

        JenaEngine.createInstanceOfClass(model, ns, cls, value);
        JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasName", value);
        JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasId", uuid);
        
     
        //query on the model
        //System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery));
       
        
       
    }

}
