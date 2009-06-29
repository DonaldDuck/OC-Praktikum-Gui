package de.uniluebeck.itm.icontrol.gui.model;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

/**
 * The <code>VFeature</code> class is the model of a robot's supported feature.
 * 
 * @see VRobot
 * @author Johannes Kotzerke 
 */
public class VFeature {

	/**
	 * The feature's name
	 */
	private String name;
	
	/**
	 * Contains all parameters names'
	 */
	private String[] parameters;
	
	public VFeature(String name, int numberOfParams, String[] parameters){
		this.name = name;
		this.parameters = parameters;
	}
	
	/**
	 * Returns the feature's name
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the parameters' names in a <code>String[]</code>
	 * @return the names
	 */
	public String[] getParameters(){
		return parameters;
	}
}
