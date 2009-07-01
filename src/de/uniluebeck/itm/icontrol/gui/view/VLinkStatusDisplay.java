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

public class VLinkStatusDisplay {
	
	private Composite container;
	
	private Image[] linkStatusImages;
	
	private LinkedList<CLabel> healthIconList;
	
	private LinkedList<CLabel> linkStatus;	
	
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
		linkStatus = new LinkedList<CLabel>();
	}
	
	public void refreshAllHealthIcons(int[] health) {
		if (health.length > this.healthIconList.size() || health == null)
			return;
		for (int i = 0; i < health.length; i++)
			this.healthIconList.get(i).setImage(linkStatusImages[health[i]]);
	}
	
	public void refreshHealthIcon(int robotPosition, int health) {
		healthIconList.get(robotPosition).setImage(linkStatusImages[health]);
		container.layout(true, true);
	}
	
	public void refreshLinkStatus(int[] linkStatus) {
		if (linkStatus.length > this.linkStatus.size() || linkStatus == null)
			return;
		for (int i = 0; i < linkStatus.length; i++)
			this.linkStatus.get(i).setText(linkStatus[i] + "%");
		container.layout(true, true);
	}
	
	public void addRobot(int robotId) {
		CLabel label = new CLabel(container, SWT.NONE);
		label.setText(robotId + ": ");
		CLabel label2 = new CLabel(container, SWT.NONE);
		label2.setText("n.a.");
		linkStatus.add(label2);
		CLabel label3 = new CLabel(container, SWT.NONE);
		label3.setImage(linkStatusImages[2]);
		healthIconList.add(label3);
		System.out.println("VLinkStatusDisplay: set " + robotId + "and other stuff by addRobot");
	}
}
