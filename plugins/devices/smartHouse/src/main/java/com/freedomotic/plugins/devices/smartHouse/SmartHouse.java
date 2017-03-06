/**
 *
 * Copyright (c) 2009-2016 Freedomotic team http://freedomotic.com
 *
 * This file is part of Freedomotic
 *
 * This Program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Freedomotic; see the file COPYING. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.freedomotic.plugins.devices.smartHouse;

import com.freedomotic.api.EventTemplate;
import com.freedomotic.api.Protocol;
import com.freedomotic.behaviors.BehaviorLogic;
import com.freedomotic.environment.EnvironmentLogic;
import com.freedomotic.environment.Room;
import com.freedomotic.events.LocationEvent;
import com.freedomotic.events.MessageEvent;
import com.freedomotic.events.ObjectHasChangedBehavior;
import com.freedomotic.events.PersonEntersZone;
import com.freedomotic.events.ZoneHasChanged;
import com.freedomotic.exceptions.UnableToExecuteException;
import com.freedomotic.model.ds.Config;
import com.freedomotic.model.geometry.FreedomPoint;
import com.freedomotic.model.object.Behavior;
import com.freedomotic.model.object.EnvObject;
import com.freedomotic.plugins.devices.smartHouse.application.Action;
import com.freedomotic.plugins.devices.smartHouse.application.OntologieApp;
import com.freedomotic.plugins.devices.smartHouse.tools.JenaEngine;
import com.freedomotic.things.EnvObjectLogic;
import com.freedomotic.things.ThingRepository;
import com.freedomotic.reactions.Command;
import com.freedomotic.things.GenericPerson;
import com.freedomotic.things.impl.*;
import com.google.inject.Inject;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.freedomotic.events.LuminosityEvent;

/**
 *
 * @author Mauro Cicolella
 */
public class SmartHouse
        extends Protocol {

    private static final Logger LOG = LoggerFactory.getLogger(SmartHouse.class.getName());
    final int POLLING_WAIT;

    @Inject
    private ThingRepository thingsRepository;
    OntologieApp app= new OntologieApp();
    Action action = new Action();
    /**
     *
     */
    public SmartHouse() {
        //every plugin needs a name and a manifest XML file
        super("SmartHouse", "/smartHouse/smartHouseManifest.xml");
        //read a property from the manifest file below which is in
        //FREEDOMOTIC_FOLDER/plugins/devices/com.freedomotic.hello/hello-world.xml
        POLLING_WAIT = configuration.getIntProperty("time-between-reads", 2000);
        //POLLING_WAIT is the value of the property "time-between-reads" or 2000 millisecs,
        //default value if the property does not exist in the manifest
        setPollingWait(POLLING_WAIT); //millisecs interval between hardware device status reads
    }

    private FreedomPoint randomLocation() {
        int x;
        int y;

        Random rx = new Random();
        Random ry = new Random();
        x = rx.nextInt(getApi().environments().findAll().get(0).getPojo().getWidth());
        y = ry.nextInt(getApi().environments().findAll().get(0).getPojo().getHeight());

        return new FreedomPoint(x, y);
    }

    @Override
    protected void onShowGui() {
        /**
         * uncomment the line below to add a GUI to this plugin the GUI can be
         * started with a right-click on plugin list on the desktop frontend
         * (com.freedomotic.jfrontend plugin)
         */
        //   bindGuiToPlugin(new HelloWorldGui(this));
    }

    @Override
    protected void onHideGui() {
        //implement here what to do when the this plugin GUI is closed
        //for example you can change the plugin description
        setDescription("My GUI is now hidden");
    }

    
    protected Room getObjLocation(EnvObjectLogic p){
    	for(EnvironmentLogic flate: getApi().environments().findAll()){
       	 for(Room room : flate.getRooms()){
        
                for( EnvObject obj : room.getPojo().getObjects() ){
                	/*obj.getSimpleType().equals("user")*/ 
               	if( obj.getUUID() == p.getPojo().getUUID()){
               		
               		return (Room)room;
               	 }
                }
                
       	 }
        }
       return null;
    }
    
    protected Person getPerson(){
    	for (EnvObjectLogic object : getApi().things().findAll()) {
               if (object.getClass().toString().equals("class com.freedomotic.things.impl.Person")) {
            	   return (Person)object;
                }
    	}
    	return null;
    }
    
    
    protected Light getLightsIn(Room room){
    	String hey="";
    	String hehey="";
  
                   for( EnvObject obj : room.getPojo().getObjects() ){
                  	
                  	if( obj.getSimpleType().equals("light")){
                  		hehey=obj.getUUID();
                  	 }
                  	
                   }
    	for (EnvObjectLogic object : getApi().things().findAll()) {
            if (object.getClass().toString().equals("class com.freedomotic.things.impl.Light")) {
               Light l = (Light)object;
               hey = l.getPojo().getUUID();
               if(hey.equals(hehey))
         	   return (Light)object;
               
             }
    	}
    	
    	return null;
    }
    
    protected void saveInOntologie(){
    	
    	
    	//rooms
    	for(EnvironmentLogic flate: getApi().environments().findAll()){
          	 for(Room room : flate.getRooms()){	 
             	app.createRoomInstance(room.getPojo().getName() ,room.getPojo().getName().replaceAll(" ", ""), room.getPojo().getUuid());
            	app.createObjectInstance("Brightness" ,room.getPojo().getName()+"Brightness","hasSensorValue", 0);
            	app.createObjectInstance("Temperature" ,room.getPojo().getName()+"Temperature","hasSensorValue", "10");
            	
            	app.createObjectRelInstance(room.getPojo().getName(), "hasEnvironment", room.getPojo().getName()+"Brightness");
            	app.createObjectRelInstance(room.getPojo().getName(), "hasEnvironment", room.getPojo().getName()+"Temperature");

            	//and more...
                 
          	 }
           }
    	
    	
    	
    	
    	//object and person
    	for (EnvObjectLogic object : getApi().things().findAll()) {
            if (object.getClass().toString().equals("class com.freedomotic.things.impl.Person")) {
            	
            	System.err.println("user : "+object.getPojo().getName());
            	
            	app.createPersonInstance("Person" ,object.getPojo().getName(), object.getPojo().getUUID());
            	//ajouter personne
            	Room r = getObjLocation(object);
            	app.createObjectRelInstance(object.getPojo().getName(), "hasLocation", r.getPojo().getName());
            	
            }else
            if (object.getClass().toString().equals("class com.freedomotic.things.impl.Light")){
            	Room r = getObjLocation(object);
            	
            	//ajouter light
            	app.createObjectInstance("Light" ,object.getPojo().getName(), "hasId",object.getPojo().getUUID());
            	app.createObjectRelInstance(object.getPojo().getName(), "hasLocation", r.getPojo().getName());
            	//ajouter env brightness
            	
            	String state=null;
            	for (BehaviorLogic lightState: object.getBehaviors()) {
            		if( lightState.getName().equals("powered")){
            			state= lightState.getValueAsString();
            			System.out.println("env : " + lightState.getName());
                		System.out.println("env state : " + lightState.getValueAsString());
            		}	
				}
            	
            	if(state.equals("true")){
            		app.createObjectInstance("SwitchOn" ,object.getPojo().getName()+"-OnFct","isEnabeled", "true");
                	app.createObjectInstance("SwitchOff" ,object.getPojo().getName()+"-OffFct","isEnabeled", "false");
            	}
            	else{
            		app.createObjectInstance("SwitchOn" ,object.getPojo().getName()+"-OnFct","isEnabeled", "false");
                	app.createObjectInstance("SwitchOff" ,object.getPojo().getName()+"-OffFct","isEnabeled", "true");
            	}
            	
            	//fonctions
            	

            	app.createObjectRelInstance(object.getPojo().getName()+"-OnFct", "isFunctionOf", object.getPojo().getName());
            	app.createObjectRelInstance(object.getPojo().getName()+"-OffFct", "isFunctionOf", object.getPojo().getName());
            	app.createObjectRelInstance(object.getPojo().getName()+"-OnFct", "hasEffect", r.getPojo().getName()+"Brightness");
            	app.createObjectRelInstance(object.getPojo().getName()+"-OffFct", "hasEffect", r.getPojo().getName()+"Brightness");

            }else
            if (object.getClass().toString().equals("class com.freedomotic.things.impl.AirConditioner")){
            	Room r = getObjLocation(object);
                //ajouter light
            	app.createObjectInstance("Aircondition" ,object.getPojo().getName(), "hasId",object.getPojo().getUUID());
            	app.createObjectRelInstance(object.getPojo().getName(), "hasLocation", r.getPojo().getName());

            	//fonctions
            	app.createObjectInstance("SwitchOn" ,object.getPojo().getName()+"-OnFct","isEnabeled", "false");
            	app.createObjectInstance("SwitchOff" ,object.getPojo().getName()+"-OffFct","isEnabeled", "true");
            	app.createObjectInstance("TemperateurUp" ,object.getPojo().getName()+"-UpFct","isEnabeled", "false");
            	app.createObjectInstance("TemperateurDown" ,object.getPojo().getName()+"-DownFct","isEnabeled", "true");
 
            	app.createObjectRelInstance(object.getPojo().getName()+"-OnFct", "isFunctionOf", object.getPojo().getName());
            	app.createObjectRelInstance(object.getPojo().getName()+"-OffFct", "isFunctionOf", object.getPojo().getName());
            	app.createObjectRelInstance(object.getPojo().getName()+"-OnFct", "hasEffect", r.getPojo().getName()+"Temperature");
            	app.createObjectRelInstance(object.getPojo().getName()+"-OffFct", "hasEffect", r.getPojo().getName()+"Temperature");

            	app.createObjectRelInstance(object.getPojo().getName()+"-UpFct", "isFunctionOf", object.getPojo().getName());
            	app.createObjectRelInstance(object.getPojo().getName()+"-DownfFct", "isFunctionOf", object.getPojo().getName());
            	app.createObjectRelInstance(object.getPojo().getName()+"-UpFct", "hasEffect", r.getPojo().getName()+"Temperature");
            	app.createObjectRelInstance(object.getPojo().getName()+"-DownFct", "hasEffect", r.getPojo().getName()+"Temperature");
            }
    	}
    }
    
    
    
    
    @Override
    protected void onRun(){
    	saveInOntologie();
    	action.switchOnLight(getApi().things().findAll(), app);
    }

    @Override
    protected void onStart() {
    	
        LOG.info("HelloWorld plugin started");
    }

    @Override
    protected void onStop() {
        LOG.info("HelloWorld plugin stopped");
    }

    @Override
    protected void onCommand(Command c)
            throws IOException, UnableToExecuteException {
        LOG.info("HelloWorld plugin receives a command called {} with parameters {}", c.getName(),
                c.getProperties().toString());
    }

    @Override
    protected boolean canExecute(Command c) {
        //don't mind this method for now
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void onEvent(EventTemplate event) {
        if (event instanceof ObjectHasChangedBehavior) {
            // here what you want todo
            LOG.info("evenement", event.getEventName());
        } else if (event instanceof ZoneHasChanged) {
            // here what you want todo

        }
    }
}
