package de.uniluebeck.itm.icontrol.gui.model;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

import java.util.LinkedList;

/**
 * The <code>VRobot</code> class is a model of the robot for drawing. It contains all
 * important information, which could be displayed.
 *  @see VFeature, VController
 *  
 * @author Johannes Kotzerke 
 */
public class VRobot {
	
	/**
	 * This value contains the unique robot id, which consists of four
	 * hexadecimal values (the last four digits of the robot's mac adress).
	 */
	private int id;
	
	/**
	 * This array contains the current link quality to every other node in range.
	 * linkStatus[2*n] (all even numbers) = ID
	 * linkStatus[2*n+1] (all odd numbers) = corresponding link quality
	 */
	private int[] linkStatus;
	
	/**
	 * This list contains all features the robot supports.
	 */
	private LinkedList<VFeature> featureList;
	
	/**
	 * This list contains all sensors the robot has.
	 */
	private LinkedList<VSensor> sensorList;
	
	private boolean containsBattery = false;
	
	private VSensor battery;
	
	private int health;
	
	/**
	 * Every <code>boolean</code> is true if the corresponding sensor (same order
	 * as in <code>sensorList</code>) is currently displayed in the GUI. 
	 */
	private boolean[] displayedSensors;
	
	public VRobot(int robotId, int taskListLength, String[] taskList, int[] paramListLength, String[][] paramList, int sensorLength, String[] sensors, int[] sensorRange){
		this.id = robotId;
		this.featureList = new LinkedList<VFeature>();
		for (int i = 0; i < taskListLength; i++){
			featureList.add(new VFeature(taskList[i], paramListLength[i], paramList[i]));
		}
		this.sensorList = new LinkedList<VSensor>();
		for (int i = 0; i < sensorLength; i++){
			if (sensors[i].equals("battery")) {
				containsBattery = true;
				battery = new VSensor(sensors[i], sensorRange[2*i], sensorRange[2*i+1]);
			} else
				sensorList.add(new VSensor(sensors[i], sensorRange[2*i], sensorRange[2*i+1]));
		}
		displayedSensors = new boolean[sensorList.size()];
		for (int i = 0; i < sensorList.size(); i++){
			displayedSensors[i] = true;
		}
	}
	
	public int[] getSensorsMinMax(String name) {
		if (name.equals("battery") && containsBattery)
			return new int[]{0, 100};
		int[] range = new int[2];
		range[0] = sensorList.get(getSensorNumber(name)).getMin();
		range[1] = sensorList.get(getSensorNumber(name)).getMax();
		return range;
	}
	
	public boolean getContainsBattery() {
		return containsBattery;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	/**
	 * Returns all parameters to the given feature number in a <code>String[]</code>
	 * @param featureNumber the number of the feature in the current <code>featureList</code>
	 * @return all parameters' names
	 */
	public String[] getFeatureParameters(int featureNumber){
		return featureList.get(featureNumber).getParameters();
	}
	
	/**
	 * Returns a list containing all supported features
	 * @return the feature list
	 */
	public LinkedList<VFeature> getFeatureList(){
		return featureList;
	}
	
	/**
	 * Returns the position of the sensor corresponding to the given name in the <code>sensorList</code>
	 * 
	 * @param name the sensor's name
	 * @return the sensor's position if the name is found, else -1
	 */
	public int getSensorNumber (String name) {
		for (VSensor sensor : sensorList) {
			if (sensor.getName().equals(name))
				return sensorList.indexOf(sensor);
		}
		System.out.println("Sorry, I don't know the sensor " + name + "!");
		return -1;
	}
	
	/**
	 * Returns the current value of the by its name given sensor 
	 * 
	 * @param name the sensor's name
	 * @return the sensor's current value
	 */
	public int getSensorValue(String name) {
		if (name.equals("battery") && containsBattery)
			return battery.getCurrentValue();
		return sensorList.get(getSensorNumber(name)).getCurrentValue();
	}
	
	/**
	 * Returns all by the robot supported sensors by their names
	 * 
	 * @return all sensor names
	 */
	public String[] getAllSensorNames() {
		String[] names = new String[sensorList.size()];
		for (int i = 0; i < sensorList.size(); i++)
			names[i] = sensorList.get(i).getName();
		return names;
	}
	
	/**
	 * Updates the value of the sensor with that given name by the given value 
	 * @param name the sensor's name
	 * @param currentValue the new value of that sensor
	 */
	public void updateSensor(String name, int currentValue) {
		if (name.equals("battery") && containsBattery)
			battery.setCurrentValue(currentValue);
		int position = getSensorNumber(name);
		if (position == -1) {
			System.out.println("I don't know this sensor!");
			return;
		}
		sensorList.get(position).setCurrentValue(currentValue);
	}
	
	/**
	 * Returns the position of the feature corresponding to the given name in the <code>featureList</code>
	 * 
	 * @param name the feature's name
	 * @return the feature's position if the name is found, else -1
	 */
	public int getFeatureNumber(String name){
		for (VFeature feature : featureList){
			if (feature.getName().equals(name))
				return featureList.indexOf(feature);
		}
		return -1;
	}
	
	/**
	 * Returns all feature names in a <code>String[]</code>
	 * @return the name array
	 */
	public String[] getAllFeatureNames(){
		String[] names = new String[featureList.size()];
		for (int i = 0; i < names.length; i++)
			names[i] = featureList.get(i).getName();
		return names;
	}
	
	/**
	 * Returns the currently displayed sensors
	 * @return <code>true</code> if the corresponding sensor is displayed
	 */
	public boolean[] getDisplayedSensors() {
		return displayedSensors;
	}
	
	/**
	 * Sets the currently displayed sensors and calls a refresh method on the GUI
	 * @param displayedSensors
	 * 					<code>true</code> if the corresponding sensor is displayed 
	 */
	public void setDisplayedSensors(boolean[] displayedSensors) {
		this.displayedSensors = displayedSensors;
	}
	
	/**
	 * Saves a new link status to the private variable <code>linkStatus</code>
	 * @param linkStatus the new link status
	 */
	public void updateLinkStatus(int[] linkStatus){
		this.linkStatus = linkStatus;
	}
	
	/**
	 * Returns the current link status
	 * @return the link status
	 */
	public int[] getLinkStatus(){
		return linkStatus;
	}
	
	/**
	 * Returns the robot's id
	 * @return the id
	 */
	public int getId(){
		return id;
	}

}
