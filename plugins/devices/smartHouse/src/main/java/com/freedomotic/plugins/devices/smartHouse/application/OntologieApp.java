/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package  com.freedomotic.plugins.devices.smartHouse.application;

import java.util.ArrayList;
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
	public static String inputDataOntology = "data/smartHome.owl";//"data/smartHome.owl";
	public static String inputRule = "data/rules.txt";//rules.txt";
	public static String inputQuery = "data/query.txt";
	public static String inputQuery2 = "data/query2.txt";

	/**
	 * @param args rhe command line arguments
	 */

	Model model = JenaEngine.readModel(inputDataOntology);
	Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, inputRule);
	//pour tout ce qui n'est pas room ou person
	public ArrayList<String> switchOnLightQuery(){
		
		System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery));
		String result=JenaEngine.executeQueryFile(inferedModel, inputQuery);
		ArrayList<String> ListId= new ArrayList<String>() ;
		
		String[] str = result.split(";");
		for (String s : str){
			String [] tabId = s.split("\t");
			ListId.add(tabId[0]);
		}
		return ListId;
		
		
	}
	
	
	
	public void createObjectRelInstance(String obj1, String rel, String obj2){
		JenaEngine.addValueOfObjectProperty(inferedModel, ns, obj1, rel,obj2);
	}

	public void createObjectInstance(String cls ,String value,String prop, String uuid){

		JenaEngine.createInstanceOfClass(inferedModel, ns, cls, value);
		JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,prop, uuid);

	}
	//pour person 
	public void createPersonInstance(String cls ,String value, String uuid){

		JenaEngine.createInstanceOfClass(inferedModel, ns, cls, value);
		JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasName", value);
		JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasId", uuid);


		//query on the model



	}
	public void createRoomInstance(String cls ,String value, String uuid){

		JenaEngine.createInstanceOfClass(inferedModel, ns, cls, value);
		JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasName", value);
		JenaEngine.addValueOfDataTypeProperty(inferedModel, ns, value,"hasId", uuid);


		//query on the model
		//System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery));



	}

}
