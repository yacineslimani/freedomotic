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
import com.freedomotic.events.LocationEvent;
import com.freedomotic.events.MessageEvent;
import com.freedomotic.events.ObjectHasChangedBehavior;
import com.freedomotic.events.ZoneHasChanged;
import com.freedomotic.exceptions.UnableToExecuteException;
import com.freedomotic.model.ds.Config;
import com.freedomotic.model.geometry.FreedomPoint;
import com.freedomotic.things.EnvObjectLogic;
import com.freedomotic.things.ThingRepository;
import com.freedomotic.reactions.Command;
import com.freedomotic.things.GenericPerson;
import com.freedomotic.things.impl.ElectricDevice;
import com.google.inject.Inject;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    protected void onRun(){

          for (EnvObjectLogic thing : thingsRepository.findAll()) {
            LOG.info("HelloWorld sees Thing 1: {}", thing.getPojo().getName());
            LOG.info("HelloWorld sees Thing 2: {}", thing.getPojo().getClass());
            LOG.info("HelloWorld sees Thing 2: {}", thing.getPojo().getType());
            
        }
      
    /*  for (EnvObjectLogic object : getApi().things().findAll()) {
          
          
        //  System.out.println("Object Name : "+object.getPojo().getName());
        //  System.out.println("Object Class : "+object.getClass());
          
            if (object instanceof ElectricDevice) {
                ElectricDevice device = (ElectricDevice) object;
            //    FreedomPoint location = randomLocation();
                
           //     LocationEvent event = new LocationEvent(this, person.getPojo().getUUID(), location);
          //      notifyEvent(event);
         // System.out.println("je reconnais un electicDivice");
          device.setRandomLocation();
          Config c = new Config();
          
          
          System.out.println("device Name : "+ device.getPojo().getName());
          System.out.println("device Environment : "+device.getClass()+"\n");
          System.out.println("-------------------------------------");
          
          
            }
        }*/
        
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
