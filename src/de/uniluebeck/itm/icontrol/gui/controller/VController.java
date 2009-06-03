package de.uniluebeck.itm.icontrol.gui.controller;
//Lizenz

import java.util.LinkedList;

import org.eclipse.swt.widgets.Composite;

import de.uniluebeck.itm.icontrol.communication.Communication;
import de.uniluebeck.itm.icontrol.communication.listener.FeatureListener;
import de.uniluebeck.itm.icontrol.communication.listener.MessageListener;
import de.uniluebeck.itm.icontrol.gui.model.VRobot;
import de.uniluebeck.itm.icontrol.gui.view.Gui;

/**
 * The <code>VController</code> class controls the graphical user interface
 * and updates the <code>VRobot</code> models.
 * 
 *  @author Johannes Kotzerke
 */
public class VController implements FeatureListener, MessageListener{
	
	/**
	 * The communication gateway
	 */
	private Communication gateway;
	
	/**
	 * List of all available robots
	 */
	private LinkedList<VRobot> robotList;
	
	/**
	 * The list position of the currently displayed robot
	 */
	private int displayedRobot;
	
	private Gui gui;
	
	public VController(Communication gateway, Composite container){
		this.gateway = gateway;
		this.robotList = new LinkedList<VRobot>();
		this.gui = new Gui(this, container);
		this.gateway.showMeWhatYouGot();
	}
	
	/**
	 * Checks if a robot with the given id is in the <code>robotList</code>
	 * @param id the robot's id
	 * @return -1 if there isn't a robot having that id, else the robot's position in the <code>robotList</code> 
	 */
	private int robotInList(int id){
		for (VRobot robot : robotList){
			if (robot.getId() == id)
				return robotList.indexOf(robot);
		}
		return -1;
	}
	
	/**
	 * Sets which robot the GUI currently displays
	 * @param id the robot's id
	 */
	public void setDisplayedRobot(int id){
		displayedRobot = id;
		// gui.updateRobot();
	}
	
	/**
	 * Returns all parameters of the given feature of the currently displayed robot
	 * @param featureNumber the feature number in the list
	 * @return the parameters' names in a <code>String[]</code> 
	 */
	public String[] getFeatureParameters(int featureNumber){
		return robotList.get(robotInList(displayedRobot)).getFeatureParameters(featureNumber);
	}
	
	/**
	 * Returns the ids of all currently known robots
	 * @return all robots' ids in an <code>int[]</code>
	 */
	public int[] getAllRobotNames(){
		int[] thatNames = new int[robotList.size()];
		for (int i = 0; i < robotList.size(); i++)
			thatNames[i] = robotList.get(i).getId();
		return thatNames;
	}
	
	/**
	 * Returns all features' names of the currently displayed robot 
	 * @return the names of all features in a <code>String[]</code>
	 */
	public String[] getAllFeaturesNames(){
		return robotList.get(robotInList(displayedRobot)).getAllFeatureNames();
	}
	
	/**
	 * Gives the chosen task to the communication gateway and sets this task in the <code>VRobot</code> status
	 * @param taskName the name of the chosen task
	 * @param parameters the given parameters
	 */
	public void doTask(String taskName, int[] parameters){
		gateway.doTask(robotInList(displayedRobot), taskName, parameters.length, parameters);
		robotList.get(robotInList(displayedRobot)).setTask(taskName);
		//gui.updateStatus();
	}
	
	/**
	 * Deletes all used stuff by setting to null
	 */
	public void killAll(){
		gui = null;
		robotList = null;
	}
	
	// FeatureListener
	@Override
	public void onAction(int robotId, int taskListLength, String[] taskList, int[] paramListLength, String[][] paramList) {
		if (robotList.isEmpty()){
			robotList.add(new VRobot(robotId, taskListLength, taskList, paramListLength, paramList));
			//gui.setDisplayedRobot(Integer.toHexString(robotId).toUpperCase(), taskList);
			gui.run();
		}
		// if the list is empty, add the robot, else check if the robot already exists. If he exists ignore the given features, else add him.
		else if (robotInList(robotId) == -1)
			robotList.add(new VRobot(robotId, taskListLength, taskList, paramListLength, paramList));
		gui.gotNewRobot(getAllRobotNames());
	}

	//MessageListener
	@Override
	public void onMessage(int robotId, String taskName, int valueLength, int[] values) {
		if (taskName.equals("status")){
			int position = robotInList(robotId);
			if (position == -1)
				System.err.println("I don't know this robot!");
			else{	
				robotList.get(position).updateStatus(values);
				if (position == displayedRobot){
					//gui.updateStatus
					// remove if-{} 
				}
			}
		}else if(taskName.equals("linkStatus")){
			int position = robotInList(robotId);
			if (position == -1)
				System.err.println("I don't know this robot!");
			else{	
				robotList.get(position).updateLinkStatus(values);
				if (position == displayedRobot){
					//gui.updateLinkStatus
					// remove if-{}
				}
			}
		}
		
	}

}
