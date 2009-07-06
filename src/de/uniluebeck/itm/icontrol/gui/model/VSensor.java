package de.uniluebeck.itm.icontrol.gui.model;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

/**
 * This class represents a robot's sensor with all important information like
 * sensor range, current sensor value and sensor name.
 * @see VRobot
 * 
 * @author Johannes Kotzerke
 */
public class VSensor {
	
	/**
	 * The sensor's name
	 */
	private String name;
	
	/**
	 * The sensor's current value and the minimum and maximum value the sensor can reach
	 */
	private int currentValue, min, max;
	
	public VSensor(String name, int min, int max){
		System.out.println("Created sensor " + name);
		this.name = name;
		this.min = min;
		this.max = max;
		currentValue = min;
	}
	
	/**
	 * Returns the current sensor value
	 * @return
	 * 		the value
	 */
	public int getCurrentValue (){
		return currentValue;
	}

	/**
	 * Returns the sensor's name
	 * @return
	 * 		the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the sensor's receivable minimum
	 * @return
	 * 		the minimum
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Returns the sensor's receivable maximum
	 * @return
	 * 		the maximum
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Sets the current sensor value to the given value. If the given one is
	 * out of range, the value will be set to the minimum respectively the maximum.
	 * 
	 * @param currentValue
	 * 		the given value
	 */
	public void setCurrentValue(int currentValue) {
		if (currentValue < min) {
			this.currentValue = min;
			System.out.println("Sensor value out of range (to small)!");
		} else if (currentValue > max) {
			this.currentValue = max;
			System.out.println("Sensor value out of range (to big)!");
		} else
			this.currentValue = currentValue;
	}
	
	
}
