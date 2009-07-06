package de.uniluebeck.itm.icontrol.gui.model;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

import java.util.Vector;

/**
 * This class saves the link quality between all robots and their health status.
 *  
 * @author Johannes Kotzerke
 */
public class VLinkStatus {
	
	/**
	 * The dynamic table for storing the link quality between robots 
	 */
	private Vector<Vector<Integer>> linkStatus;
	
	/**
	 * A own list containing all robot ids in order they registered
	 */
	private Vector<Integer> robotList;
	
	/**
	 * A list, which stores the health value for each robot.
	 */
	private Vector<Integer> health;
	
	public VLinkStatus() {
		linkStatus = new Vector<Vector<Integer>>(0, 1);
		robotList = new Vector<Integer>(0,1);
		health = new Vector<Integer>(0,1);
	}
	
	/**
	 * Adds the robot with the given id to the link quality table by adding a new
	 * row and a new column (every field contains the value -1 by initialization).
	 * Furthermore the id is stored in the <code>robotList</code> and the health
	 * status for that robot is set to healthy (2).
	 *  
	 * @param robotId
	 * 			the robot's id
	 */
	public void addRobot(int robotId) {
		if (robotPosition(robotId) != -1)
			return;
		robotList.add(new Integer(robotId));
		health.add(new Integer(2));
		
		linkStatus.add(new Vector<Integer>(0, 1));
		for (int i = 0; i < robotList.size()-1; i++)
			linkStatus.lastElement().add(new Integer(-1));
		for (int i = 0; i < robotList.size(); i++) {
			if (i == robotList.size()-1)
				linkStatus.get(i).add(new Integer(100));
			else
				linkStatus.get(i).add(new Integer(-1));
		}
		System.out.println("added " + robotId + " and set health to 2!");
	}
	
	/**
	 * Sets the link quality between two robots 
	 * 
	 * @param robotId
	 * 			one robot's id
	 * @param robotId2
	 * 			the other robot's id
	 * @param linkStatus
	 * 			the connection quality between these two robots in percentage. 
	 */
	public void setLinkStatus(int robotId, int robotId2, int linkStatus) {
		int i = robotPosition(robotId);
		int j = robotPosition(robotId2);
		if (i == -1 || j == -1)
			return;
		this.linkStatus.get(i).set(j, new Integer(linkStatus));
		this.linkStatus.get(j).set(i, new Integer(linkStatus));
	}
	
	/**
	 * Returns the position of the given robot id in the list
	 * @param robotId
	 * 			the robot id
	 * @return
	 * 		the position in <code>robotList</code>, if there isn't a robot with
	 * 		that id -1 is returned.
	 */
	private int robotPosition(int robotId) {
		for (int i = 0; i < robotList.size(); i++) {
			if (robotList.get(i).equals(robotId))
				return i;
		}
		return -1;
	}
	
	/**
	 * Removes the given robot from the <code>linkStatus</code> table and the
	 * intern <code>robotList</code>.
	 * @param robotId
	 * 			the given robot by id
	 */
	public void removeRobot(int robotId) {
		int position = robotPosition(robotId);
		if (position == -1)
			return;
		robotList.remove(position);
		linkStatus.remove(position);
		for (Vector<Integer> vector : linkStatus)
			vector.remove(position);
	}
	
	/**
	 * Sets the health status of the given robot
	 * @param robotId
	 * 		the robot's id
	 * @param health
	 * 		the health status (0: ill, 1: ok, 2: healthy)
	 */
	public void setHealth(int robotId, int health) {
		int position = robotPosition(robotId);
		if (position == -1)
			return;
		this.health.set(position, new Integer(health));
	}
	
	/**
	 * Returns the health status of all known robots ordered by their appearance
	 * in <code>robotList</code>.
	 *  
	 * @return
	 * 		the health of all robots (0: ill, 1: ok, 2: healthy)
	 */
	public int[] getHealth() {
		int[] health = new int[robotList.size()];
		for (int i = 0; i < robotList.size(); i++)
			health[i] = this.health.get(i);
		return health;
	}
	
	/**
	 * Returns the link quality between the given robot and all robots
	 * 
	 * @param robotId
	 * 		the given robot
	 * @return
	 * 		the connection quality in percentage ordered by their appearance
	 * 		in <code>robotList</code>. 
	 */
	public int[] getLinkStatus(int robotId) {
		int i = robotPosition(robotId);
		if (i == -1)
			return null;
		int[] linkStatus = new int[this.linkStatus.size()];
		for (int j = 0; j < this.linkStatus.size(); j++)
			linkStatus[j] = this.linkStatus.get(j).get(i);
		return linkStatus;
	}
	
	/**
	 * Prints both <code>robotList</code> and the <code>linkStatus</code> table.
	 */
	public void print() {
		System.out.println("RobotList:");
		System.out.print("[");
		for (int i = 0; i < robotList.size(); i++) {
			if (i == 0)
				System.out.print(robotList.get(i));
			else
				System.out.print(", " + robotList.get(i));
		}
		System.out.print("]");
		System.out.println();
		System.out.println("Table:");
		for (int i = 0; i < linkStatus.size(); i++) {
			System.out.print("[");
			for (int j = 0; j < linkStatus.get(i).size(); j++) {
				if (j == 0)
					System.out.print(linkStatus.get(i).get(j));
				else
					System.out.print(", " + linkStatus.get(i).get(j));
			}
			System.out.print("]");
			System.out.println();
		}
	}
	
}
