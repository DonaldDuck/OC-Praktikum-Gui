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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import de.uniluebeck.itm.icontrol.gui.controller.VController;

public class VAllRobotsTaskDialog extends Dialog implements Listener {

	private LinkedList<Button> buttonList;
	private Button ok, cancel;
	private Shell shell;
	private VController controller;
	private Gui gui;
	private String name;
	private int[] values, robotIds;
	
	public VAllRobotsTaskDialog(Composite container, Gui gui, VController controller) {
		super(container.getShell(), SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		this.controller = controller;
		this.gui = gui;
	}
	
	/**
	 * Opens this dialog and loads the current values.
	 */
	public void open(String name, String[] parameters, int[] values) {
		this.name = name;
		this.values = values;
		this.robotIds = controller.getAllRobotNames();
		if (parameters == null) {
			shell.dispose();
			return;
		}
		boolean[] hasThatFeature = controller.robotsWithThatFeature(name, parameters);
		shell = new Shell(getParent(), getStyle());
		shell.setText("Choose robots!");
		RowLayout rowLayout2 = new RowLayout(SWT.VERTICAL);
		rowLayout2.fill = true;
		shell.setLayout(rowLayout2);
		
		Group group = new Group(shell, SWT.SHADOW_ETCHED_IN);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		group.setText("Choose robots!");
		buttonList = new LinkedList<Button>();
		
		for (int i = 0; i < robotIds.length; i++) {
			Button b = new Button(group, SWT.CHECK);
			b.setText(Integer.toHexString(robotIds[i]));
			b.setSelection(hasThatFeature[i]);
			if (!hasThatFeature[i])
				b.setEnabled(false);
			buttonList.add(b);
		}
		Composite container = new Composite(shell, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.pack = false;
		container.setLayout(rowLayout);
	    cancel = new Button(container, SWT.PUSH);
	    cancel.setText("Cancel");
	    ok = new Button(container, SWT.PUSH);
	    ok.setText("Send task");
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
		Object source = e.widget;
		if (source.equals(cancel)) {
			System.out.println("cancel");
			shell.dispose();
		}else if (source.equals(ok)){
			System.out.println("Button ok");
			int [] robotIds = new int[buttonList.size()];
			int j = 0;
			for (int i = 0; i < robotIds.length; i++) {
				if (buttonList.get(i).getSelection()) {
					robotIds[j] = this.robotIds[i];
					j++;
				}
			}
			int[] thatIds = new int[j];
			System.arraycopy(robotIds, 0, thatIds, 0, j);
			controller.doTaskFor(thatIds, name, values);
		}
		shell.dispose();
	}
	
}
