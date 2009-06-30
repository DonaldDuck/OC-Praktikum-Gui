package de.uniluebeck.itm.icontrol.gui.model;

/* ----------------------------------------------------------------------
 * This file is part of the WISEBED project.
 * Copyright (C) 2009 by the Institute of Telematics, University of Luebeck
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the BSD License. Refer to bsd-licence.txt
 * file in the root of the source tree for further details.
 ------------------------------------------------------------------------*/

import java.util.Vector;

public class VLinkStatus {
	
	private Vector<Vector<Integer>> linkStatus;
	
	private Vector<Integer> robotList;
	
	public VLinkStatus() {
		linkStatus = new Vector<Vector<Integer>>(0, 1);
		robotList = new Vector<Integer>(0,1);
	}
	
	public void addRobot(int robotId) {
		if (robotPosition(robotId) != -1)
			return;
		robotList.add(new Integer(robotId));
		
		linkStatus.add(new Vector<Integer>(0, 1));
		for (int i = 0; i < robotList.size()-1; i++)
			linkStatus.lastElement().add(new Integer(-1));
		for (int i = 0; i < robotList.size(); i++) {
			if (i == robotList.size()-1)
				linkStatus.get(i).add(new Integer(100));
			else
				linkStatus.get(i).add(new Integer(-1));
		}
		System.out.println("added " + robotId);
	}
	
	public void setLinkStatus(int robotId, int robotId2, int linkStatus) {
		int i = robotPosition(robotId);
		int j = robotPosition(robotId2);
		if (i == -1 || j == -1)
			return;
		this.linkStatus.get(i).set(j, new Integer(linkStatus));
		this.linkStatus.get(j).set(i, new Integer(linkStatus));
	}
	
	private int robotPosition(int robotId) {
		for (int i = 0; i < robotList.size(); i++) {
			if (robotList.get(i).equals(robotId))
				return i;
		}
		return -1;
	}
	
	public int[] getLinkStatus(int robotId) {
		int i = robotPosition(robotId);
		if (i == -1)
			return null;
		int[] linkStatus = new int[this.linkStatus.size()];
		for (int j = 0; j < this.linkStatus.size(); j++)
			linkStatus[j] = this.linkStatus.get(j).get(i);
		return linkStatus;
	}
	
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
