package de.uniluebeck.itm.icontrol.gui.view;

//Lizenz

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import de.uniluebeck.itm.icontrol.gui.controller.VController;

public class Gui implements Listener, SelectionListener {
	private VController controller;
	private Composite container;
	private Combo combo;
	private CLabel name, text, battery;
	private Image batteryImage;
	private Image[] robotImages;
	private LinkedList<Button> buttonList;
	private LinkedList<Text> textList;
	private Group features, parameters;
	private Button button;
	private int selectedFeature = -1;
	
	public Gui(VController controller, Composite container){
		this.controller = controller;
		this.container = container;
		
		init();
	}
	
	
	/**
	 * This method creates a single centered message on screen, that tells you
	 * to wait and prepares running by loading images.
	 */
	private void init(){
		container.setLayout(new GridLayout(3, false));
	    text = new CLabel(container, SWT.CENTER);
	    text.setText("Please wait, \ncollecting robot data!");
	    text.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
	    try {
	    	batteryImage = new Image(container.getDisplay(), Gui.class.getResourceAsStream("images/battery.png"));
	    	robotImages = new Image[8];
	    	for (int i = 1; i < robotImages.length/2 + 1; i++){
	    		robotImages[2 * (i - 1)] = new Image(container.getDisplay(), Gui.class.getResourceAsStream("images/"+ i +"_off.png"));
	    		robotImages[2 * i - 1] = new Image(container.getDisplay(), Gui.class.getResourceAsStream("images/"+ i +"_on.png"));
	    	}
		} catch (Exception e) {
			System.out.println("Exception " + e);
			for (int i = 0; i < 20; i++)
				System.out.println("Exception");
		}
		
	}
	
	/**
	 * It disposes the waiting message and creates more or less all needed SWT
	 * thingies for displaying e.g. the robot's name.
	 * NOTE: Should be called after <code>init()</code>, but before working
	 * --> if the first robot is added to the dropdown menu.
	 */
	public void run(){
		text.dispose();
		
	    name = new CLabel(container, SWT.NONE);
	    name.setFont(new Font(container.getDisplay(), new FontData("Sans Serif", 18, SWT.BOLD)));
	    name.setForeground(new Color(null, 0x66, 0x66, 0x66));
	    name.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
	    
	    combo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
	    combo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
	    
	    button = new Button(container, SWT.PUSH);
	    button.setText("Send Task!");
	    button.addListener(SWT.Selection, this);
	    button.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, true, 1, 1));
	    
	    features = new Group(container, SWT.SHADOW_ETCHED_IN);
	    parameters = new Group(container, SWT.SHADOW_ETCHED_IN);
	    battery = new CLabel(container, SWT.CENTER);
	    //test
	    updateBattery(75);
	}
	
	/**
	 * Updates the dropdown menu. If there wasn't any robot before registered,
	 * the display switches to the new one, else that won't happen and the new
	 * robot is only added to the dropdown menu.
	 * NOTE: Should always called if the controller found a new robot.  
	 * @param robotIds
	 * 					the ids of all currently known robots
	 */
	public void gotNewRobot(int[] robotIds){
		for (int i = combo.getItemCount(); i < robotIds.length; i++){
			combo.add("" + robotIds[i]);
			combo.addListener(SWT.Selection, this);
		}
		if (combo.getSelectionIndex() == -1){
			combo.select(0);
			controller.setDisplayedRobot(Integer.valueOf(combo.getItem(combo.getSelectionIndex())));
			//System.out.println(Integer.valueOf(combo.getItem(combo.getSelectionIndex())));
			name.setText("iRobot: " + combo.getItem(combo.getSelectionIndex()));
			updateButtonList(controller.getAllFeaturesNames());
		}
		container.layout(true, true);
	}
	
	/**
	 * It updates the <code>buttonList</code> with the given feature names.
	 * NOTE: Should be called if another robot was selected for displaying. 
	 * 
	 * @param features
	 * 					the feature names of the displayed robot
	 */
	private void updateButtonList(String[] features){
		buttonList = null;
		if(!this.features.isDisposed())
			this.features.dispose();
		this.features = new Group(container, SWT.SHADOW_ETCHED_IN);
		this.features.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));
		this.features.setText("Choose action:");
		this.features.setLayout(new RowLayout(SWT.VERTICAL));
		buttonList = new LinkedList<Button>();
		for (int i = 0; i < features.length; i++){
			Button b = new Button(this.features, SWT.RADIO);
			buttonList.add(b);
			b.setText(features[i]);
			b.addSelectionListener(this);
			if (i == 0)
				b.setSelection(true);
		}
		selectedFeature = -1;
		updateParameters(controller.getFeatureParameters(0), 0);
		this.container.layout(true, true);
	}
	
	/**
	 * Updates the displayed parameters to the ones of the current selected button.
	 * NOTE: Should always be called if another button were selected.
	 * 
	 * @param parameters
	 * 							the names of all parameters in a <code>String[]</code>
	 * @param featureToSelect
	 * 							the number of the feature in the robot's
	 * 							<code>featureList</code> for checking if the new
	 * 							selection is already selected
	 */
	private void updateParameters(String[] parameters, int featureToSelect){
		if (selectedFeature == featureToSelect)
			return;
		if(!this.parameters.isDisposed())
			this.parameters.dispose();
		selectedFeature = featureToSelect;
		if (parameters.length == 0)
			return;
		this.parameters = new Group(container, SWT.SHADOW_ETCHED_IN);
		this.parameters.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 1, 1));
		this.parameters.setText("Enter parameters:");
		this.parameters.setLayout(new GridLayout(parameters.length, false));
		textList = new LinkedList<Text>();
		for (int i = 0; i < parameters.length; i++){
			Label l = new Label(this.parameters, SWT.LEFT);
			l.setText(parameters[i] + ":");
			Text t = new Text(this.parameters, SWT.BORDER);
			t.setTextLimit(9);
			t.setSize(20, 12);
			t.addListener(SWT.Verify, this);
			textList.add(t);
		}
		this.container.layout(true, true);
	}
	
	/**
	 * Draws a image of a battery with the given percentage and writes that
	 * percentage in the battery. The following color encodings are used:
	 * green: 100% <= charging level <= 30%
	 * orange: 30% < charging level <= 10%
	 * red: 10% < charging level <= 0%
	 * non color: else 
	 * 
	 * @param percentage
	 * 						the percentage how much the battery is charged 
	 */
	private void updateBattery(int percentage){
		if (!battery.isDisposed())
			this.battery.dispose();
		CLabel label = new CLabel(container, SWT.CENTER );
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
    	label.setImage(batteryImage);
    	this.battery = label;
	}
	
	/**
	 * Takes the by the user entered parameters and puts them into an <code>int[]</code>.
	 * 
	 * @return that <code>int[]</code>
	 */
	private int[] parseParameters(){
		int[] parsingResult = new int[textList.size()];
		System.out.println("textList.size: " + textList.size());
		boolean anythingWrong = false;
		for(int i = 0; i < textList.size(); i++){
			int parse = 0;
			try{
				if (textList.get(i).getText().equals(""))
					textList.get(i).setText("0");
				parse = Integer.valueOf(textList.get(i).getText());
				textList.get(i).setBackground(new Color(container.getDisplay(), 255, 255, 255));
			}catch(NumberFormatException e){				
				anythingWrong = true;
				textList.get(i).setBackground(new Color(container.getDisplay(), 255, 84, 84));
			}

			parsingResult[i] = parse;
			
			System.out.println(parsingResult[i]);
		}
		if (!anythingWrong)
			return parsingResult;
		else
			return null;
	}

	@Override
	public void handleEvent(Event e) {
		System.out.println("handleEvent");
		if(e.type == SWT.Verify){
			e.doit = true;
//			e.doit = true;
//			Object source = e.widget;
//			for (Text text: textList)
//				if (source.equals(text)){
//					try{
//						Integer.parseInt(text.getText());
//					}catch (NumberFormatException exception){
//						if (text.getText().equals(""))
//							return;	
//						text.setBackground(new Color(container.getDisplay(), 255, 84, 84));
//						return;
//					}
//					text.setBackground(new Color(container.getDisplay(), 255, 255, 255));
//			}
			
			//e.doit = e.text.matches("[^\\p{Alpha}\\p{Punct}]*");
			//e.doit = e.text.matches("[\\d\b]*");
		}else if(e.type == SWT.Selection){
			Object source = e.widget;
			if (source.equals(button)){
				if (parseParameters() != null){
//					for(int i = 0; i < 20; i++)
//						System.out.println("Sending");
					controller.doTask(buttonList.get(selectedFeature).getText(), parseParameters());
				}
			} else if(source.equals(combo)){
				String[] split = name.getText().split("[ ]");
				if (Integer.valueOf(split[1]) == Integer.valueOf(combo.getItem(combo.getSelectionIndex())))
					return;
				controller.setDisplayedRobot(Integer.valueOf(combo.getItem(combo.getSelectionIndex())));
				//System.out.println(Integer.valueOf(combo.getItem(combo.getSelectionIndex())));
				name.setText("iRobot: " + combo.getItem(combo.getSelectionIndex()));
				updateButtonList(controller.getAllFeaturesNames());
			}
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		System.out.println("widgetDefaultSelected");
		Object source = e.widget;
		for (Button b: buttonList){
			if (source.equals(b)){
				System.out.println(buttonList.indexOf(b));
				break;
			}
		}
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		System.out.println("widgetSelected");
		Object source = e.widget;
		for (Button b: buttonList){
			if (source.equals(b)){
				System.out.println(buttonList.indexOf(b));
				updateParameters(controller.getFeatureParameters(buttonList.indexOf(b)), buttonList.indexOf(b));
				break;
			}
		}
	}
}
