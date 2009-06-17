package de.uniluebeck.itm.icontrol.gui.model;
//Lizenz

import java.util.LinkedList;

/**
 * The <code>VRobot</code> class is a model of the robot for drawing. It contains all
 * important information, which could be displayed.
 *  @see VFeature, VController
 *  
 * @author Johannes Kotzerke 
 */
public class VRobot {
	
	/**
	 * This value contains the unique robot id, which consists of four
	 * hexadecimal values (the last four digits of the robot's mac adress).
	 */
	private int id;
	
	/**
	 * This array contains all status information:
	 * 
	 * status[0]: Information about the left bumper. The value is <code>1</code> if the bumper is pressed, else <code>0</code>
	 * status[1]: Information about the right bumper. The value is <code>1</code> if the bumper is pressed, else <code>0</code>
	 * status[2]: Information about the left wheel. The value is <code>1</code> if the wheel is released, else <code>0</code>
	 * status[3]: Information about the right wheel. The value is <code>1</code> if the wheel is released, else <code>0</code>
	 * status[4]: Percentage of the robot's battery. 
	 * status[5]: Contains the position of the feature in <code>featureList</code>, which was last sent to the robot.
	 * 
	 */
	private int[] status;
	
	/**
	 * This array contains the current link quality to every other node in range.
	 * linkStatus[2*n] (all even numbers) = ID
	 * linkStatus[2*n+1] (all odd numbers) = corresponding link quality
	 */
	private int[] linkStatus;
	
	/**
	 * This list contains all features the robot supports.
	 */
	private LinkedList<VFeature> featureList;
	
	public VRobot(int robotId, int taskListLength, String[] taskList, int[] paramListLength, String[][] paramList){
		this.id = robotId;
		this.status = new int[]{0,0,0,0,0,-1};
		this.featureList = new LinkedList<VFeature>();
		for (int i = 0; i < taskListLength; i++){
			featureList.add(new VFeature(taskList[i], paramListLength[i], paramList[i]));
		}
	}
	
	
	/**
	 * Returns all parameters to the given feature number in a <code>String[]</code>
	 * @param featureNumber the number of the feature in the current <code>featureList</code>
	 * @return all parameters' names
	 */
	public String[] getFeatureParameters(int featureNumber){
		return featureList.get(featureNumber).getParameters();
	}
	
	/**
	 * Returns a list containing all supported features
	 * @return the feature list
	 */
	public LinkedList<VFeature> getFeatureList(){
		return featureList;
	}
	
	/**
	 * Returns the position of the feature corresponding to the given name in the <code>featureList</code>
	 * @param name the feature's name
	 * @return the feature's position if the name is found, else -1
	 */
	public int getFeatureNumber(String name){
		for (VFeature feature : featureList){
			if (feature.getName() == name)
				return featureList.indexOf(feature);
		}
		return -1;
	}
	
	/**
	 * Returns all feature names in a <code>String[]</code>
	 * @return the name array
	 */
	public String[] getAllFeatureNames(){
		String[] names = new String[featureList.size()];
		for (int i = 0; i < names.length; i++)
			names[i] = featureList.get(i).getName();
		return names;
	}
	
	/**
	 * Sets the current task of a robot in <code>status</code> using the
	 * feature's position in the <code>featureList</code>. If the name isn't
	 * found, -1 will be entered.
	 *  
	 * @param name the task's name
	 */
	public void setTask(String name){
		for (VFeature feature : featureList){
			if (feature.getName().equals(name))
				status[5] = featureList.indexOf(feature);
			else
				status[5] = -1;
		}
	}
	
	/**
	 * Saves a new status to the private variable <code>status</code>
	 * @param status the new status
	 */
	public void updateStatus(int[] status){
		this.status = status;
	}
	
	/**
	 * Returns the current status
	 * @return the status
	 */
	public int[] getStatus(){
		return status;
	}
	
	/**
	 * Saves a new link status to the private variable <code>linkStatus</code>
	 * @param linkStatus the new link status
	 */
	public void updateLinkStatus(int[] linkStatus){
		this.linkStatus = linkStatus;
	}
	
	/**
	 * Returns the current link status
	 * @return the link status
	 */
	public int[] getLinkStatus(){
		return linkStatus;
	}
	
	/**
	 * Returns the robot's id
	 * @return the id
	 */
	public int getId(){
		return id;
	}

}
