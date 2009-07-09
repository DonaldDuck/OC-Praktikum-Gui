package de.uniluebeck.itm.icontrol.gui.view;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Realizes the displaying of the link quality and robot health.
 * @see Gui
 * 
 * @author Johannes Kotzerke
 */
public class VLinkStatusDisplay {
	
	/**
	 * The <code>Composite</code> in which all that stuff is drawn.
	 */
	private Composite container;
	
	/**
	 * Contains the three images. These images make clearer how healthy a robot is.
	 */
	private Image[] linkStatusImages;
	
	/**
	 * Contains all health icons.
	 */
	private LinkedList<CLabel> healthIconList;
	
	/**
	 * Contains all <code>CLabel</code> containing the link quality of a
	 * robot-robot-connection.
	 */
	private LinkedList<CLabel> linkStatusList;	
	
	public VLinkStatusDisplay(Composite container) {
		this.container = container;
		linkStatusImages = new Image[3];
	    try {
			linkStatusImages[0] = new Image(container.getDisplay(), VLinkStatusDisplay.class.getResourceAsStream("images/health_red.png"));
			linkStatusImages[1] = new Image(container.getDisplay(), VLinkStatusDisplay.class.getResourceAsStream("images/health_orange.png"));
			linkStatusImages[2] = new Image(container.getDisplay(), VLinkStatusDisplay.class.getResourceAsStream("images/health_green.png"));
		} catch (Exception e) {}
		container.setLayout(new GridLayout(3, false));
		healthIconList = new LinkedList<CLabel>();
		linkStatusList = new LinkedList<CLabel>();
	}
	
	/**
	 * Updates all health icons with the given <code>int[]</code>
	 * @param health
	 * 			As long as the <code>healthIconList</code> and  every array cell
	 * 			contains one of the following values:
	 * 				0: ill --> red icon,
	 * 				1: ok --> orange icon,
	 * 				2: healthy --> green icon.
	 */	
	public void refreshAllHealthIcons(int[] health) {
		if (health.length > this.healthIconList.size() || health == null)
			return;
		for (int i = 0; i < health.length; i++)
			this.healthIconList.get(i).setImage(linkStatusImages[health[i]]);
	}
	
	/**
	 * Updates the health icon of a special robot, given by its position in the
	 * <code>robotList</code>.
	 * @param robotPosition
	 * 			the robot's position in the <code>robotList</code>
	 * @param health
	 * 			one of the following values:
	 * 				0: ill --> red icon,
	 * 				1: ok --> orange icon,
	 * 				2: healthy --> green icon.
	 */
	public void refreshHealthIcon(int robotPosition, int health) {
		healthIconList.get(robotPosition).setImage(linkStatusImages[health]);
		container.layout(true, true);
	}
	
	/**
	 * Refreshes the link status of all <code>CLabel</code>s in the list by the
	 * given <code>int[]</code>.
	 *  
	 * @param linkStatus
	 * 			as long as the <code>linkStatusList</code>. Every array cell
	 * 			contains the corresponding link quality. 
	 */
	public void refreshLinkStatus(int[] linkStatus) {
		if (linkStatus.length > this.linkStatusList.size() || linkStatus == null)
			return;
		for (int i = 0; i < linkStatus.length; i++)
			this.linkStatusList.get(i).setText(linkStatus[i] + "%");
		container.layout(true, true);
	}
	
	/**
	 * Adds the given robot to the displayed ones, including its health and link quality.
	 * @param robotId
	 * 			the robot's id
	 */
	public void addRobot(int robotId) {
		CLabel label = new CLabel(container, SWT.NONE);
		label.setText(Integer.toHexString(robotId) + ": ");
		CLabel label2 = new CLabel(container, SWT.NONE);
		label2.setText("n.a.");
		linkStatusList.add(label2);
		CLabel label3 = new CLabel(container, SWT.NONE);
		label3.setImage(linkStatusImages[2]);
		healthIconList.add(label3);
		System.out.println("VLinkStatusDisplay: set " + robotId + "and other stuff by addRobot");
	}
}
