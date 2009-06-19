package de.uniluebeck.itm.icontrol;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import ishell.device.MessagePacket;
import ishell.util.IconTheme;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import de.uniluebeck.itm.icontrol.communication.Communication;
import de.uniluebeck.itm.icontrol.communication.Gateway;
import de.uniluebeck.itm.icontrol.gui.controller.VController;

public class PluginiControl2iShell extends ishell.plugins.Plugin implements ActionListener {

	// Bitte hier Packet-Type aendern und unten receivePacket(MessagePacket arg0) ausfuellen!
	private static final int ICONTROL_PACKET_TYPE = 10;

	private Communication gateway;

	private VController controller;
	
	private JTextField text;

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

		gateway.showMeWhatYouGot();

		// Testeingaben, damit man was zu klicken hat
//		controller.onAction(123, 3, new String[] { "drive", "gather", "spread" }, new int[] { 2, 0, 0 }, new String[][] {
//				{ "direction", "distance" }, {}, {} });
//		controller.onAction(124, 3, new String[] { "drive1", "gather1", "spread1" }, new int[] { 2, 0, 0 }, new String[][] {
//				{ "direction", "distance" }, {}, {} });
//		controller.onAction(125, 3, new String[] { "drive2", "gather2", "spread2" }, new int[] { 2, 0, 0 }, new String[][] {
//				{ "direction", "distance" }, {}, {} });
//		controller.onAction(126, 3, new String[] { "drive3", "gather3", "spread3" }, new int[] { 2, 0, 0 }, new String[][] {
//				{ "direction", "distance" }, {}, {} });

		//-------------------------------------------------------------------------------------------------------------------------
		// Fuer Testzwecke

//		JFrame window = new JFrame("test robot adding");
//		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		window.getContentPane().setLayout(new FlowLayout());
//		
//		JButton ok = new JButton("add Robot");
//		ok.addActionListener(this);
//		this.text =  new JTextField();
//		text.setText("testInput");
//		window.add(text);
//		window.add(ok);
//		window.pack();
//		window.setVisible(true);
//		
		// Ende der Testzwecke
		//-------------------------------------------------------------------------------------------------------------------------
		
		
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
	
	//-------------------------------------------------------------------------------------------------------------------------
	// Test
	@Override
	public void actionPerformed(ActionEvent e) {
//		String that = text.getText();
//		String [] robots = that.split("@");
//		for (int i = 0; i < robots.length; i++){
//			String [] features = robots[i].split("#");
//			String[][] allParameters = new String[features.length-1][];
//			int[] numberOfParameters = new int[features.length-1];
//			for (int j = 1; j < features.length; j++){
//				String[] parameters = features[j].split("+");
//				String muh[] = new String[parameters.length-1];
//				for (int k = 1; k < parameters.length; k++){
//					 muh[k-1] = parameters[k];
//				}
//				allParameters[j-1] = muh; 
//				numberOfParameters[j-1] = muh.length;
//			}
//			System.out.println("Roboter: " + robots[i]);
//			System.out.println("Features: " + features.toString());
//			System.out.println("Parameter: " + allParameters[i].toString());
//			System.out.println("Anzahl Feature: " + (features.length - 1));
//			System.out.println("Anzahl Parameter: " + numberOfParameters.toString());
//		}
		
		
		
		
		controller.onAction(127, 3, new String[] { "drive3", "gather3", "spread3" }, new int[] { 2, 0, 0 }, new String[][] {{ "direction", "distance" }, {}, {} });
		
	}
	// Test Ende
	//-------------------------------------------------------------------------------------------------------------------------

}
