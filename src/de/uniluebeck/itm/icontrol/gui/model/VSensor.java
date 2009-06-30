package de.uniluebeck.itm.icontrol.gui.model;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

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
	
	public int getCurrentValue (){
		return currentValue;
	}

	
	public String getName() {
		return name;
	}


	public int getMin() {
		return min;
	}


	public int getMax() {
		return max;
	}


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
