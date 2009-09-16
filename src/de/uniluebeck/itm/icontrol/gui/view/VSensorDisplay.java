package de.uniluebeck.itm.icontrol.gui.view;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

/**
 * @class VSensorDisplay
 * @author Johannes Kotzerke
 * @brief Graphical sensor representation.
 * @detailed This class realizes a graphical representation of the given sensor
 * including a devolution.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import de.uniluebeck.itm.icontrol.gui.controller.VController;

public class VSensorDisplay {

	private CLabel valueDevolutionLabel, currentValueLabel, nameLabel;
	private Image imageValueDevolution, imageCurrent;
	private int currentValue, oldValue, step;
	private final int min, max;
	private boolean start = true;
	private Composite container;
	private VController controller;
	private String name;
	
	public VSensorDisplay(VController controller, Composite container, String name, int min, int max){
		this.controller = controller;
		this.min = min;
		this.max = max;
		this.container = container;
		this.name = name;
		// set name
		//-------------------------------------------------------------------------
		this.nameLabel = new CLabel(container, SWT.CENTER | SWT.NONE);
		this.nameLabel.setText(this.name + ":");
		this.nameLabel.setToolTipText("maximum value: " + max + "\nminimum value: " + min);
		
		// create description image
		//-------------------------------------------------------------------------
		this.currentValueLabel = new CLabel(container, SWT.CENTER | SWT.SHADOW_IN);
		imageCurrent = new Image(container.getDisplay(), 40, 40);
		GC gc2 = new GC(imageCurrent);
		gc2.setBackground(new Color(container.getDisplay(), 0, 0, 0));
		gc2.setForeground(new Color(container.getDisplay(), 51, 102, 153));
		gc2.fillRectangle(0, 0, 40, 40);
		//Vertical Lines
    	for (int i = 1; i < 4; i++)
    		gc2.drawLine(i*10, 39, i*10, 0);
    	//Horizontal Lines
    	for (int i = 1; i < 4; i++)
    		gc2.drawLine(0, i*10, 39, i*10);
		gc2.dispose();
		currentValueLabel.setImage(imageCurrent);
		currentValueLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		
		// create value devolution image
		//-------------------------------------------------------------------------
		this.valueDevolutionLabel = new CLabel(container, SWT.CENTER | SWT.SHADOW_IN);
		imageValueDevolution = new Image(container.getDisplay(), 200, 40);
	    GC gc = new GC(imageValueDevolution);
    	gc.setBackground(new Color(container.getDisplay(), 0, 0, 0));
    	gc.setForeground(new Color(container.getDisplay(), 51, 102, 153));
    	gc.fillRectangle(0, 0, 200, 40);
    	//Vertical Lines
    	for (int i = 1; i < 21; i++)
    		gc.drawLine(i*10, 39, i*10, 0);
    	//Horizontal Lines
    	for (int i = 1; i < 4; i++)
    		gc.drawLine(0, i*10, 199, i*10);
    	gc.dispose();    	
	    valueDevolutionLabel.setImage(imageValueDevolution);
	    valueDevolutionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
	}
	
	/**
	 * Updates the image. That means that the background grid is moved 2px to
	 * the left side and completed. Furthermore the current value is drawn and
	 * connected with the last data point by a vertical line.
	 * 
	 * @param value the current value
	 */
	public void update(int value){
		oldValue = currentValue;
		currentValue = value;
		
		// updates current
		//-------------------------------------------------------------------------
		GC gc2 = new GC(imageCurrent);
		gc2.setBackground(new Color(container.getDisplay(), 0, 0, 0));
		gc2.setForeground(new Color(container.getDisplay(), 51, 102, 153));
		gc2.fillRectangle(0, 0, 40, 40);
		//Vertical Lines
    	for (int i = 1; i < 4; i++)
    		gc2.drawLine(i*10, 39, i*10, 0);
    	//Horizontal Lines
    	for (int i = 1; i < 4; i++)
    		gc2.drawLine(0, i*10, 39, i*10);
    	gc2.setForeground(new Color(container.getDisplay(), 51, 204, 255));
    	// center that by changing 14 to ??
		gc2.drawString("" + currentValue, 14, 12, true);
		gc2.dispose();
		currentValueLabel.redraw();
		
		// updates valueDevolution
		//-------------------------------------------------------------------------		
		GC gc = new GC(imageValueDevolution);
		gc.setBackground(new Color(container.getDisplay(), 0, 0, 0));
		gc.setForeground(new Color(container.getDisplay(), 51, 102, 153));
		if (start) {
    		start = false;
    		step = 1;
    		gc.setForeground(new Color(container.getDisplay(), 51, 102, 153));
    		gc.drawLine(199, 39, 199, 0);
    		//draw sensor value
			gc.setForeground(new Color(container.getDisplay(), 51, 204, 255));
	    	gc.drawPoint(199, valueOf(value));
	    	gc.dispose();
	    	return;
    	}
		// copy old image part
		gc.copyArea(2, 0, 198, 40, 0, 0);
		// draw background
		gc.fillRectangle(198, 0, 2, 40);
		gc.setForeground(new Color(container.getDisplay(), 51, 102, 153));
		// draw horizontal lines
		for (int i = 1; i < 5; i++)
    		gc.drawLine(198, i*10, 199, i*10);
		if (step%5 == 0) {
			step = 0;
			// draw vertical line
    		gc.drawLine(199, 39, 199, 0);
		}
		step++;
		// connect with the old value
		gc.setForeground(new Color(container.getDisplay(), 51, 204, 255));
    	gc.drawLine(198, valueOf(oldValue), 198, valueOf(value));
		//draw sensor value
		gc.setForeground(new Color(container.getDisplay(), 51, 204, 255));
    	gc.drawPoint(199, valueOf(value));
    	gc.dispose();
		valueDevolutionLabel.redraw();
	}
	
	/**
	 * Calls the <code>update(int value)</code> with the current sensor value
	 * got from the <code>VController</code>.
	 */
	public void update() {
//		update((int)((max - min) * Math.random()) + min);
		update(controller.getCurrentSensorValue(name));
	}
	
	/**
	 * Calculates the vertical pixel coordinate to the given sensor value.
	 * @param value the sensor value
	 * @return the calculated vertical pixel coordinate 
	 */
	private int valueOf(int value){
		if (value == min)
			return 39;
		return 39 - (int)(39.0 / Math.abs(max - min) * (value - min));
	}
	
	/**
	 * Disposes all created CLabels.
	 */
	public void kill(){
		valueDevolutionLabel.dispose();
		currentValueLabel.dispose();
		nameLabel.dispose();
	}
	
}
