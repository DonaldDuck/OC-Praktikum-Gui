package de.uniluebeck.itm.icontrol;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniluebeck.itm.icontrol.gui.controller.VController;

public class IControlApp extends ApplicationWindow{
	
	private VController controller;

	private static Shell shell;
	//private AppWindow appWindow;
	
	public IControlApp(final Shell shell) {
		super(shell);
		shell.setLayout(new GridLayout(3, false));
	    CLabel text2 = new CLabel(shell, SWT.CENTER);
	    text2.setText("Please wait, \ncollecting robot data!");
	    text2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
	    
	}
	
	public static void main(final String[] args) {

		for (int i = 0; i<30; i++)
			System.err.println("main wird ausgefuehrt");
		// SWT stuff
		final DeviceData data = new DeviceData();
		final Display display = new Display(data);
		shell = new Shell(display);
		
		IControlApp app = null;
		try {
			app = new IControlApp(shell);
			app.open();
		} catch (final Exception e) {}
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void shutdown() {
	}

}
