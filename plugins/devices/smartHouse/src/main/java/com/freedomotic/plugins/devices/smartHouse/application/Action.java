package com.freedomotic.plugins.devices.smartHouse.application;

import java.util.ArrayList;
import java.util.List;

import com.freedomotic.model.ds.Config;
import com.freedomotic.things.EnvObjectLogic;
import com.freedomotic.things.impl.Light;

public class Action {




	public void switchOnLight(List<EnvObjectLogic> envObjectLogicList , OntologieApp app){
		ArrayList<String> idLight = app.switchOnLightQuery();
		for (EnvObjectLogic object : envObjectLogicList) {
			for(String idL : idLight){
				if(object.getPojo().getUUID().equals(idL)){
					Light light = (Light) object;
					light.executePowerOn(new Config());
				}
			}
		}
	}
	
	public void switchOffLight(List<EnvObjectLogic> envObjectLogicList , OntologieApp app){
		ArrayList<String> idLight = app.switchOffLightQuery();
		for (EnvObjectLogic object : envObjectLogicList) {
			for(String idL : idLight){
				if(object.getPojo().getUUID().equals(idL)){
					Light light = (Light) object;
					light.executePowerOff(new Config());
				}
			}
		}
		
	}
}
