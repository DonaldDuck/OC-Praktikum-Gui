package de.uniluebeck.itm.icontrol;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

import ishell.device.MessagePacket;
import ishell.util.IconTheme;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import de.uniluebeck.itm.icontrol.communication.Communication;
import de.uniluebeck.itm.icontrol.communication.Gateway;
import de.uniluebeck.itm.icontrol.gui.controller.VController;

public class PluginiControl2iShell extends ishell.plugins.Plugin {

	private static final int ICONTROL_PACKET_TYPE = 10;

	private Communication gateway;

	private VController controller;

	@Override
	public String getDescription() {
		return getName() + " is a control panel for different robots using iSense nodes for communication.";
	}

	@Override
	public String getName() {
		return "iControl";
	}

	@Override
	public int[] init() {

		final CTabItem tabItem = getTabItem(getName());
		tabItem.setImage(IconTheme.lookup("network-wireless"));

		final Composite container = this.getTabContainer(true);

		controller = new VController(this, container);
		gateway = new Gateway(this);
		gateway.addFeatureListener(controller);
		gateway.addMessageListener(controller);
		
		try {

		} catch (final Exception e) {
			System.out.println(e.toString());
			this.removeTabItem();
			this.shutdown();
			return new int[] {};
		}
		return new int[] { ICONTROL_PACKET_TYPE };
	}

	@Override
	public void shutdown() {
		if (gateway != null)
			gateway = null;
		if (controller != null)
			controller.killAll();
	}

	@Override
	public void receivePacket(final MessagePacket arg0) {
		gateway.receive(arg0);
	}

	public Communication getGateway() {
		return gateway;
	}
	
	public void showMeWhatYouGot(){
		gateway.showMeWhatYouGot();
	}

}
