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
 * @brief Changes sensor refreshing time and displayed sensors. 
 * @detailed  This class is a dialog for changing the displayed sensors and
 * their refreshing time.
 */

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniluebeck.itm.icontrol.gui.controller.VController;

public class VSensorDialog extends Dialog implements Listener{
	private LinkedList<Button> buttonList;
	private Button ok, cancel;
	private Shell shell;
	private Text text;
	private boolean[] oldConfig;
	private boolean[] newConfig;
	private VController controller;
	private boolean activated = true;
	private int oldTime;
	private Gui gui;
	private String lastAction = "none";
	
	public VSensorDialog(Composite container, Gui gui, VController controller) {
		super(container.getShell(), SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		this.controller = controller;
		this.gui = gui;
	}
	
	/**
	 * Opens this dialog and loads the current values.
	 */
	public void open() {
		oldConfig = controller.getDisplayedSensors();
		newConfig = new boolean[oldConfig.length];
		String[] sensorNames = controller.getAllSensorNames();
		shell = new Shell(getParent(), getStyle());
		shell.setText("Change sensor configuration!");
		RowLayout rowLayout2 = new RowLayout(SWT.VERTICAL);
		rowLayout2.fill = true;
		shell.setLayout(rowLayout2);
		
		Group group2 = new Group(shell, SWT.SHADOW_ETCHED_IN);
		group2.setLayout(new GridLayout(1, false));
		group2.setText("Set refresh time (in ms)");
		text = new Text(group2, SWT.RIGHT | SWT.BORDER);
		GridData grid = new GridData();
		grid.widthHint = 80;
		text.setLayoutData(grid);
		oldTime = gui.getTime();
		text.setText("" + oldTime);
		text.setToolTipText("No values smaller than 1 allowed!");
		text.addListener(SWT.KeyUp, this);
		
		Group group = new Group(shell, SWT.SHADOW_ETCHED_IN);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		group.setText("Choose displayed sensors!");
		buttonList = new LinkedList<Button>();
		
		for (int i = 0; i < sensorNames.length; i++) {
			Button b = new Button(group, SWT.CHECK);
			b.setText(sensorNames[i]);
			b.setSelection(oldConfig[i]);
			buttonList.add(b);
		}
		Composite container = new Composite(shell, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.pack = false;
		container.setLayout(rowLayout);
	    cancel = new Button(container, SWT.PUSH);
	    cancel.setText("Cancel");
	    ok = new Button(container, SWT.PUSH);
	    ok.setText("Ok");
	    cancel.addListener(SWT.Selection, this);
	    ok.addListener(SWT.Selection, this);
	      
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
			}
		}
	}

	@Override
	public void handleEvent(Event e) {
		System.out.println("event");
		if (activated && e.type == SWT.KeyUp){
			switch (e.keyCode){
				case SWT.ARROW_LEFT:
					controller.doTask("turn", new int[]{42, 0});
					break;
				case SWT.ARROW_RIGHT:
					controller.doTask("turn", new int[]{-42, 0});
					break;
				case SWT.ARROW_UP:
					if (lastAction.equals("backward")) {
						controller.doTask("stop", new int[]{});
						lastAction = "stop";
					} else {
						controller.doTask("drive", new int[]{500, 32768});
						lastAction = "forward";
					}
					break;
				case SWT.ARROW_DOWN:
					if (lastAction.equals("forward")) {
						controller.doTask("stop", new int[]{});
						lastAction = "stop";
					} else {
						controller.doTask("drive", new int[]{-500, 32768});
						lastAction = "backward";
					}
					break;
			}
			return;
		}
		int parse = 1;
		try{
			if (text.getText().equals(""))
				text.setText("1");
			parse = Integer.valueOf(text.getText());
			if (parse <= 0)
				parse = 1;
			text.setBackground(new Color(shell.getDisplay(), 255, 255, 255));
		}catch(NumberFormatException exception){				
			text.setBackground(new Color(shell.getDisplay(), 255, 84, 84));
			return;
		}
		Object source = e.widget;
		if (source.equals(cancel)) {
			System.out.println("cancel");
			shell.dispose();
			return;
		}else if (source.equals(ok)){
			System.out.println("Button ok");
			gui.setTime(parse);
			for (int i = 0; i < buttonList.size(); i++) {
        		newConfig[i] = buttonList.get(i).getSelection();
			}
			boolean somethingChanged = false;
			for (int i = 0; i < newConfig.length; i++)
				if (oldConfig[i] != newConfig[i]) {
					somethingChanged = true;
					break;
				}
			if (somethingChanged)
				controller.setDisplayedSensors(newConfig);
		}
		shell.dispose();
	}
}
