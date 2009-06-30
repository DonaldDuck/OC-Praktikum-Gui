package de.uniluebeck.itm.icontrol.gui.view;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
* Draws a image of a battery with the given percentage and writes that
 * percentage in the battery. The following color encodings are used:
 * green: 100% <= charging level <= 30%
 * orange: 30% < charging level <= 10%
 * red: 10% < charging level <= 0%
 * non color: else 
 * 
 * @author Johannes Kotzerke
 *
 */
public class VBatteryDisplay {
	
	private Composite container;
	
	private Image batteryImage;
	
	private CLabel battery;
	
	public VBatteryDisplay(Composite container) {
		this.container = container;
		try {
			batteryImage = new Image(container.getDisplay(), VBatteryDisplay.class.getResourceAsStream("images/battery.png"));
		} catch (Exception e) {
			System.out.println("Exception " + e);
			for (int i = 0; i < 20; i++)
				System.out.println("Exception");
		}
		battery = new CLabel(container, SWT.CENTER);	
		update(-1);
	}
	
	/**
	 * Updates the battery image
	 * @param percentage
	 * 			the percentage how much the battery is charged 
	 */
	public void update(int percentage) {
    	GC gc = new GC(batteryImage);
    	gc.setBackground(new Color(container.getDisplay(), 128, 128, 128));
    	gc.fillRectangle(2, 2, 50, 20);
    	int x = 17;
    	if (percentage <= 100 && percentage >= 0){
	    	if (percentage >= 30){
	    		if (percentage == 100)
	    			x = 10;
	    		gc.setBackground(new Color(container.getDisplay(), 128, 255, 64));
	    	}else if (percentage >= 10){
	    		gc.setBackground(new Color(container.getDisplay(), 255, 158, 64));
	    	}else{
	    		gc.setBackground(new Color(container.getDisplay(), 242, 10, 10));
	    		x = 24;
	    	}
	    	gc.setForeground(container.getDisplay().getSystemColor(SWT.COLOR_BLACK));
	    	gc.fillRectangle(2, 2, percentage/2, 20);
	    	gc.drawString(percentage + "%", x, 4, true);
    	}else{
    		gc.setForeground(container.getDisplay().getSystemColor(SWT.COLOR_BLACK));
	    	gc.drawString("--%", x, 4, true);
    	}
    	gc.dispose();
    	battery.setImage(batteryImage);
    	battery.redraw();
//    	container.layout(true, true);
	}
	
	public void kill() {
		battery.dispose();
	}
}
