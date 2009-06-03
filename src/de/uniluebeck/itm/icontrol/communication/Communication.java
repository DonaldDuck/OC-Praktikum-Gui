package de.uniluebeck.itm.icontrol.communication;

public interface Communication {
	
	public void showMeWhatYouGot();
	public void doTask(int robotId, String taskName, int paramLength, int[] parameters);

	
}
