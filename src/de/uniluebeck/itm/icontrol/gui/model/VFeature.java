package de.uniluebeck.itm.icontrol.gui.model;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

/**
 * @class VFeature
 * @author Johannes Kotzerke
 * @brief Model for every robot feature
 * @detailed This model stores one robot feature including its name and parameters' names.
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

	public VFeature(final String name, final int numberOfParams, final String[] parameters) {
		this.name = name;
		this.parameters = parameters;
	}

	/**
	 * Returns the feature's name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the parameters' names in a <code>String[]</code>
	 * 
	 * @return the names
	 */
	public String[] getParameters() {
		return parameters;
	}

	/**
	 * Checks if this feature and the by name and parameters' names given one
	 * are the same.
	 * NOTE: Only <code>true</code> if the parameters' names are in the same order
	 * 
	 * @param name
	 * 		the feature's name
	 * @param parameters
	 * 		all parameters' names
	 * @return
	 * 		<code>true</code> if this feature and the given one have the same name,
	 * 		the same parameter's names in the same order and the same number of
	 * 		parameters, else <code>false</code>
	 */
	public boolean equals(String name, String[] parameters) {
		if (!name.equals(this.name))
			return false;
		if (parameters.length != this.parameters.length)
			return false;
		for (int i = 0; i < parameters.length; i++) {
			if (!parameters[i].equals(this.parameters[i]))
				return false;
		}
		return true;
	}
}
