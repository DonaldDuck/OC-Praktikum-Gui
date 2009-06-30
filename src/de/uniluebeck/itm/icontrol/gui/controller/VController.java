package de.uniluebeck.itm.icontrol.gui.controller;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

import java.util.LinkedList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uniluebeck.itm.icontrol.PluginiControl2iShell;
import de.uniluebeck.itm.icontrol.communication.listener.FeatureListener;
import de.uniluebeck.itm.icontrol.communication.listener.MessageListener;
import de.uniluebeck.itm.icontrol.gui.model.VLinkStatus;
import de.uniluebeck.itm.icontrol.gui.model.VRobot;
import de.uniluebeck.itm.icontrol.gui.view.Gui;

/**
 * The <code>VController</code> class controls the graphical user interface and updates the
 * <code>VRobot</code> models.
 * 
 * @author Johannes Kotzerke
 */
public class VController implements FeatureListener, MessageListener {

	/**
	 * The communication gateway
	 */
	private PluginiControl2iShell control;

	/**
	 * List of all available robots
	 */
	private LinkedList<VRobot> robotList;

	/**
	 * The list position of the currently displayed robot
	 */
	private int displayedRobot;
	
	private Display display;
	
	private VLinkStatus linkStatus;

	private Gui gui;

	public VController(final PluginiControl2iShell control, final Composite container) {
		this.control = control;
		this.display = container.getDisplay();
		this.robotList = new LinkedList<VRobot>();
		this.linkStatus = new VLinkStatus();
		this.gui = new Gui(this, container);
	}

	/**
	 * Checks if a robot with the given id is in the <code>robotList</code>
	 * 
	 * @param id
	 *            the robot's id
	 * @return -1 if there isn't a robot having that id, else the robot's position in the
	 *         <code>robotList</code>
	 */
	private int robotInList(final int id) {
		for (final VRobot robot : robotList) {
			if (robot.getId() == id) {
				return robotList.indexOf(robot);
			}
		}
		return -1;
	}
	
	public int[] getLinkStatus() {
		return linkStatus.getLinkStatus(displayedRobot);
	}

	/**
	 * Sets which robot the GUI currently displays
	 * 
	 * @param id
	 *            the robot's id
	 */
	public void setDisplayedRobot(final int id) {
		displayedRobot = id;
	}
	
	public int[] getSensorsMinMax(String name) {
		return robotList.get(robotInList(displayedRobot)).getSensorsMinMax(name);
	}
	
	public boolean getContainsBattery() {
		return robotList.get(robotInList(displayedRobot)).getContainsBattery();
	}
	
	public String[] getAllSensorNames() {
		return robotList.get(robotInList(displayedRobot)).getAllSensorNames();
	}

	/**
	 * Returns all parameters of the given feature of the currently displayed robot
	 * 
	 * @param featureNumber
	 *            the feature number in the list
	 * @return the parameters' names in a <code>String[]</code>
	 */
	public String[] getFeatureParameters(final int featureNumber) {
		return robotList.get(robotInList(displayedRobot)).getFeatureParameters(featureNumber);
	}
	
	/**
	 * Calls <code>showMeWhatYouGot()</code> on the known gateway.
	 */
	public void showMeWhatYouGot(){
		control.showMeWhatYouGot();
	}
	
	/**
	 * Returns the current value of the by its name given sensor of the currently
	 * displayed robot
	 * 
	 * @param name the sensor's name
	 * @return the sensor's current value
	 */
	public int getCurrentSensorValue(String name) {
		return robotList.get(robotInList(displayedRobot)).getSensorValue(name);
	}

	/**
	 * Returns the ids of all currently known robots
	 * 
	 * @return all robots' ids in an <code>int[]</code>
	 */
	public int[] getAllRobotNames() {
		int[] thatNames = new int[robotList.size()];
		for (int i = 0; i < robotList.size(); i++) {
			thatNames[i] = robotList.get(i).getId();
		}
		return thatNames;
	}

	/**
	 * Returns all features' names of the currently displayed robot
	 * 
	 * @return the names of all features in a <code>String[]</code>
	 */
	public String[] getAllFeaturesNames() {
		return robotList.get(robotInList(displayedRobot)).getAllFeatureNames();
	}

	/**
	 * Gives the chosen task to the communication gateway
	 * 
	 * @param taskName
	 *            the name of the chosen task
	 * @param parameters
	 *            the given parameters
	 */
	public void doTask(final String taskName, final int[] parameters) {
		control.getGateway().doTask(robotList.get(robotInList(displayedRobot)).getId(), taskName, parameters.length, parameters);
	}
	
	public boolean[] getDisplayedSensors() {
		return robotList.get(robotInList(displayedRobot)).getDisplayedSensors();
	}
	
	public void setDisplayedSensors(boolean[] displayedSensors) {
		robotList.get(robotInList(displayedRobot)).setDisplayedSensors(displayedSensors);
		if (Display.getCurrent() != null) {
			System.out.println("start gui");
			gui.updateDisplayedSensors();
		} else {
			if (!display.isDisposed()) {
				display.asyncExec(new Runnable() {
					public void run() {
						if (display.isDisposed()) {
							return;
						}
						gui.updateDisplayedSensors();
					}
				});
			}
		}
	}

	/**
	 * Deletes all used stuff by setting to null
	 */
	public void killAll() {
		gui = null;
		robotList = null;
	}
	
	// FeatureListener
	@Override
	public synchronized void onAction(int robotId, int taskListLength, String[] taskList, int[] paramListLength, String[][] paramList, int sensorLength, String[] sensors, int[] sensorRange) {
		System.out.println("onAction");
		if (robotId < 0)
			robotId = robotId * -1;
		if (robotList.isEmpty()) {
			System.out.println("empty");
			robotList.add(new VRobot(robotId, taskListLength, taskList, paramListLength, paramList, sensorLength, sensors, sensorRange));			
			System.out.println("added to list");
			linkStatus.addRobot(robotId);
			if (Display.getCurrent() != null) {
				System.out.println("start gui");
				gui.run();
			} else {
				if (!display.isDisposed()) {
					display.asyncExec(new Runnable() {
						public void run() {
							if (display.isDisposed()) {
								return;
							}
							gui.run();
						}
					});
				}
			}
		}
		// if the list is empty, add the robot, else check if the robot already exists. If he exists
		// ignore the given features, else add him.
		else if (robotInList(robotId) == -1) {
			robotList.add(new VRobot(robotId, taskListLength, taskList, paramListLength, paramList, sensorLength, sensors, sensorRange));
			linkStatus.addRobot(robotId);
		}else
			return;
		System.out.println("call gotnewrobot");
		if (Display.getCurrent() != null) {
			gui.gotNewRobot(getAllRobotNames());
		} else {
			if (!display.isDisposed()) {
				display.asyncExec(new Runnable() {
					public void run() {
						if (display.isDisposed()) {
							return;
						}
						gui.gotNewRobot(getAllRobotNames());
					}
				});
			}
		}
		System.out.println("done");
	}
	

	// MessageListener
	@Override
	public void onMessage(final int robotId, final String taskName, final int valueLength, final int[] values) {
		final int position = robotInList(robotId);
		if (position == -1) {
			System.err.println("I don't know this robot!");
			return;
		}
		if (taskName.contains("sensor_")) {
				robotList.get(position).updateSensor(taskName.replaceFirst("^sensor_", ""), values[0]);
		} else if (taskName.equals("linkStatus")) {
			//linkStatus.setLinkStatus(robotId, robotId2, linkStatus);
			if (robotId == displayedRobot) {
				// gui.updateLinkStatus
				// remove if-{}
			}
		}
	}

}
