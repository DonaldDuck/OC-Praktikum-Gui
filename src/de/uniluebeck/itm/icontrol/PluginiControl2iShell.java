package de.uniluebeck.itm.icontrol;

import ishell.device.MessagePacket;
import ishell.util.IconTheme;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import de.uniluebeck.itm.icontrol.communication.Communication;
import de.uniluebeck.itm.icontrol.communication.Gateway;
import de.uniluebeck.itm.icontrol.gui.controller.VController;

public class PluginiControl2iShell extends ishell.plugins.Plugin {

	// Bitte hier Packet-Type aendern und unten receivePacket(MessagePacket arg0) ausfuellen!
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
		// iShell init (called on each plug-in start)

		final CTabItem tabItem = getTabItem(getName());
		tabItem.setImage(IconTheme.lookup("network-wireless"));

		final Composite container = this.getTabContainer(true);
		container.setLayout(new FillLayout());

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
		// TODO Auto-generated method stub

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
