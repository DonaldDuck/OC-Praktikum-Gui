package de.uniluebeck.itm.icontrol.gui.model;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

/**
 * @class VRobot
 * @author Johannes Kotzerke
 * @brief robot model
 * @detailed The <code>VRobot</code> class is a model of the robot for drawing.
 * It contains all important information, which could be displayed.
 * @see VFeature, VController, VSensor
 */

import java.util.LinkedList;

public class VRobot {
	
	/**
	 * This value contains the unique robot id, which consists of four
	 * hexadecimal values (the last four digits of the robot's mac adress).
	 */
	private int id;
	
	/**
	 * This list contains all features the robot supports.
	 */
	private LinkedList<VFeature> featureList;
	
	/**
	 * This list contains all sensors the robot has.
	 */
	private LinkedList<VSensor> sensorList;
	
	/**
	 * <code>true</code> if the robot has a sensor called "battery"
	 */
	private boolean containsBattery = false;
	
	/**
	 * The sensor model for that battery
	 */
	private VSensor battery;
	
	/**
	 * Every <code>boolean</code> is true if the corresponding sensor (same order
	 * as in <code>sensorList</code>) is currently displayed in the GUI. 
	 */
	private boolean[] displayedSensors;
	
	public VRobot(int robotId, int taskListLength, String[] taskList, int[] paramListLength, String[][] paramList, int sensorLength, String[] sensors, int[] sensorRange){
		this.id = robotId;
		this.featureList = new LinkedList<VFeature>();
		for (int i = 0; i < taskListLength; i++){
			String[] parameters = new String[paramListLength[i]];
			System.arraycopy(paramList[i], 0, parameters, 0, paramListLength[i]);
			if (!featureExists(taskList[i], parameters)) {
				featureList.add(new VFeature(taskList[i], paramListLength[i], parameters));
			}
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
	
	/**
	 * Checks if the robot has the by name and parameters specified feature.
	 * @see VFeature
	 * 
	 * @param name
	 * 			the feature's name
	 * @param parameters
	 * 			all parameters' names
	 * @return
	 * 			<code>true</code> if there is such a feature.
	 */	
	public boolean featureExists(String name, String[] parameters) {
		if (featureList.isEmpty())
			return false;
		for (VFeature feature : featureList) {
			if (feature.equals(name, parameters))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the range of the sensor given by name
	 * 
	 * @param name
	 * 		the sensor's name
	 * @return
	 * 		the sensor's range. <code>int[0]</code> is the minimum value and
	 * 		<code>int[1]</code> is the maximum value. 
	 */
	public int[] getSensorsMinMax(String name) {
		if (name.equals("battery") && containsBattery)
			return new int[]{0, 100};
		int[] range = new int[2];
		range[0] = sensorList.get(getSensorNumber(name)).getMin();
		range[1] = sensorList.get(getSensorNumber(name)).getMax();
		return range;
	}
	
	/**
	 * Returns if this robot has a sensor called 'battery'.
	 * @return
	 * 		<code>true</code> if there is such a sensor, else <code>false</code>
	 */
	public boolean getContainsBattery() {
		return containsBattery;
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
	 * Returns the robot's id
	 * @return the id
	 */
	public int getId(){
		return id;
	}

}
