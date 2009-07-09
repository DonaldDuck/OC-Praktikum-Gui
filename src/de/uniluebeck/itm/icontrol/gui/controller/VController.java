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
	 * The id of the currently displayed robot
	 */
	private int displayedRobot;

	/**
	 * The swt display, that is currently used
	 */
	private Display display;

	/**
	 * The link status and health of all known robots
	 */
	private VLinkStatus linkStatus;

	/**
	 * The used gui
	 */
	private Gui gui;

	public VController(PluginiControl2iShell control, Composite container) {
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
	private int robotInList(int id) {
		for (VRobot robot : robotList) {
			if (robot.getId() == id) {
				return robotList.indexOf(robot);
			}
		}
		return -1;
	}

	/**
	 * Returns the link status of the displayed robot
	 * 
	 * @return this <code>int[]</code> contains the link quality of the connection between the
	 *         displayed robot and all robots in order the ids are stored in the
	 *         <code>robotList</code>.
	 */
	public int[] getLinkStatus() {
		return linkStatus.getLinkStatus(displayedRobot);
	}

	/**
	 * Sets which robot the GUI currently displays
	 * 
	 * @param id
	 *            the robot's id
	 */
	public void setDisplayedRobot(int id) {
		displayedRobot = id;
	}

	/**
	 * Returns the the displayed robot's sensor's range of the given sensor name
	 * 
	 * @param name
	 *            the sensor name
	 * @return <code>int[0]</code> the sensor's minimum <code>int[1]</code> the sensor's maximum
	 */
	public int[] getSensorsMinMax(String name) {
		return robotList.get(robotInList(displayedRobot)).getSensorsMinMax(name);
	}

	/**
	 * Returns if the currently displayed robot has a sensor called 'battery'.
	 * 
	 * @return <code>true</code> if there is such a sensor else <code>false</code>
	 */
	public boolean getContainsBattery() {
		return robotList.get(robotInList(displayedRobot)).getContainsBattery();
	}

	/**
	 * Returns all sensor names the currently displayed robot has registered.
	 * 
	 * @return all sensor names in order they were registered
	 */
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
	public String[] getFeatureParameters(int featureNumber) {
		return robotList.get(robotInList(displayedRobot)).getFeatureParameters(featureNumber);
	}

	/**
	 * Calls <code>showMeWhatYouGot()</code> on the known gateway.
	 */
	public void showMeWhatYouGot() {
		control.showMeWhatYouGot();
	}

	/**
	 * Removes the given robot from the <code>robotList</code> and the <code>linkStatus</code>.
	 * 
	 * @param robotId
	 *            the given robot by id
	 */
	public void removeRobot(int robotId) {
		int position = robotInList(robotId);
		if (position == -1) {
			return;
		}
		robotList.remove(position);
		linkStatus.removeRobot(robotId);
	}

	/**
	 * Checks if only one robot is in the <code>robotList</code>
	 * 
	 * @return <code>true</code> if there is only one robot in the list, else <code>false</code>
	 */
	public boolean isLastRobot() {
		return (robotList.size() == 1);
	}

	/**
	 * Returns the current value of the by its name given sensor of the currently displayed robot
	 * 
	 * @param name
	 *            the sensor's name
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
	 * Checks for every robot if it has the by name and parameters specified
	 * feature.
	 * 
	 * @param name
	 * 			the feature's name
	 * @param parameters
	 * 			the parameters' names
	 * @return
	 * 			the <code>boolean[]</code> has the same order as the
	 * 			<code>robotList</code>. Every cell is <code>true</code>	if that
	 * 			robot has the specified feature, else <code>false</code>.
	 */
	public boolean[] robotsWithThatFeature(String name, String[] parameters) {
		boolean[] thatRobots = new boolean[robotList.size()];
		for (int i = 0; i < robotList.size(); i++)
			thatRobots[i] =  robotList.get(i).featureExists(name, parameters);
		return thatRobots;
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
	public void doTask(String taskName, int[] parameters) {
		System.out.println("ausgelesenes doTask: ");
		System.out.println("task: " + taskName);
		System.out.println("parameters: [");
		for (int i = 0; i < parameters.length; i++) {
			if (i == 0) {
				System.out.print(parameters[i] + "");
			} else {
				System.out.print(", " + parameters[i]);
			}
		}
		System.out.print("]");
		control.getGateway().doTask(robotList.get(robotInList(displayedRobot)).getId(), taskName, parameters.length, parameters);
	}
	
	/**
	 * Musst du ausfuellen
	 * 
	 * @param robotIds
	 * @param taskName
	 * @param parameters
	 */
	public void doTaskFor(int[] robotIds, String taskName, int[] parameters) {
		for (int i = 0; i < robotIds.length; i++) {
			if (robotInList(robotIds[i]) != -1)
				control.getGateway().doTask(robotIds[i], taskName, parameters.length, parameters);
		}
	}

	/**
	 * Returns a <code>boolean[]</code> with the length how much sensors are registered for the
	 * displayed robot. The sensors are in order they were registered.
	 * 
	 * @return <code>true</code> if this sensor is currently displayed in the gui else
	 *         <code>false</code>
	 */
	public boolean[] getDisplayedSensors() {
		return robotList.get(robotInList(displayedRobot)).getDisplayedSensors();
	}

	/**
	 * Sets the health status for the given robot.
	 * 
	 * @param robotId
	 *            the given robot by id
	 * @param health
	 *            the new health status (0: ill, 1: ok, 2: healthy)
	 */
	public void updateHealth(int robotId, int health) {
		if (robotInList(robotId) == -1) {
			return;
		}
		linkStatus.setHealth(robotId, health);
		if (Display.getCurrent() != null) {
			gui.updateHealthStatus(linkStatus.getHealth());
		} else {
			if (!display.isDisposed()) {
				display.asyncExec(new Runnable() {
					public void run() {
						if (display.isDisposed()) {
							return;
						}
						gui.updateHealthStatus(linkStatus.getHealth());
					}
				});
			}
		}
	}

	/**
	 * Sets the displayed sensors of the currently displayed robot
	 * 
	 * @param displayedSensors
	 *            this <code>boolean[]</code> contains for every robot sensor <code>true</code> if
	 *            this sensor should be displayed, else <code>false</code>
	 */
	public void setDisplayedSensors(boolean[] displayedSensors) {
		robotList.get(robotInList(displayedRobot)).setDisplayedSensors(displayedSensors);
		if (Display.getCurrent() != null) {
			System.out.println("update displayed sensors");
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
	public synchronized void onAction(int robotId, int taskListLength, String[] taskList, int[] paramListLength,
			String[][] paramList, int sensorLength, String[] sensors, int[] sensorRange) {
		System.out.println("onAction");
		if (robotId < 0) {
			robotId = robotId * -1;
		}
		if (robotList.isEmpty()) {
			System.out.println("empty");
			robotList.add(new VRobot(robotId, taskListLength, taskList, paramListLength, paramList, sensorLength, sensors, sensorRange));
			for (int i = 0; i < paramList.length; i++) {
				print(paramList[i]);
			}
			System.out.println("added to list");
			linkStatus.addRobot(robotId);
			if (Display.getCurrent() != null) {
				System.out.println("start gui");
				gui.run();
				// gui.updateLinkStatus(linkStatus.getLinkStatus(displayedRobot));
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
		} else {
			return;
		}
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

	private void print(String[] paramList) {
		System.out.println("paramList:");
		System.out.print("[");
		for (int i = 0; i < paramList.length; i++) {
			if (i == 0) {
				System.out.print(paramList[i]);
			} else {
				System.out.print(", " + paramList[i]);
			}
		}
		System.out.print("]");
	}

	// MessageListener
	@Override
	public void onMessage(int robotId, String taskName, int valueLength, int[] values) {
		if (taskName.equals("healthStatus")) {
			if (values.length % 2 == 1) {
				return;
			}
			for (int i = 0; i < values.length / 2; i++) {
				linkStatus.setHealth(values[2 * i], values[2 * i + 1]);
			}
			System.out.println("healthStatus set!");
			if (Display.getCurrent() != null) {
				gui.updateHealthStatus(linkStatus.getHealth());
			} else {
				if (!display.isDisposed()) {
					display.asyncExec(new Runnable() {
						public void run() {
							if (display.isDisposed()) {
								return;
							}
							gui.updateHealthStatus(linkStatus.getHealth());
						}
					});
				}
			}
			return;
		}
		int position = robotInList(robotId);
		if (position == -1) {
			System.err.println("I don't know this robot!");
			return;
		}
		if (taskName.equals("#message#")){
			// mach was mit der Nachricht und Sprechblase und so
		} else if (taskName.contains("sensor_")) {
			robotList.get(position).updateSensor(taskName.replaceFirst("^sensor_", ""), values[0]);
		} else if (taskName.equals("linkStatus")) {
			if (values.length % 2 == 1) {
				return;
			}
			for (int i = 0; i < values.length / 2; i++) {
				linkStatus.setLinkStatus(robotId, values[2 * i], values[2 * i + 1]);
			}
			if (Display.getCurrent() != null) {
				gui.updateLinkStatus(linkStatus.getLinkStatus(displayedRobot));
			} else {
				if (!display.isDisposed()) {
					display.asyncExec(new Runnable() {
						public void run() {
							if (display.isDisposed()) {
								return;
							}
							gui.updateLinkStatus(linkStatus.getLinkStatus(displayedRobot));
						}
					});
				}
			}
		}
	}

}
