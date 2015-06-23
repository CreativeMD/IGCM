package com.creativemd.ingameconfigmanager.api.common.branch;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ConfigBranch{
	
	/**The Title of the branch**/
	public String name;
	
	public ConfigBranch(String name)
	{
		this.name = name;
	}
	
	
	@SideOnly(Side.CLIENT)
	/**Should return ALL segments for configuration which does only effect the client side (No synchronization)*/
	public abstract ArrayList<ConfigSegment> getClientSegments();
	
	/**Should return ALL segments for configuration which does also effect the server/all other players (synchronization)*/
	public abstract ArrayList<ConfigSegment> getServerSegments();
	
	public abstract boolean needPacket();
	
	public abstract boolean needClientConfig();
	
	public abstract void onSegmentChanged(boolean isServer, ConfigSegment segment);
	
	/**This method is called everytime the client/server receives an update.
	 * Only use pre if you really need to change things before*/
	public void onRecieveFromPre(boolean isServer, ConfigSegmentCollection collection){}
	
	/**This method is called everytime the client/server receives an update.
	 * Use this for normal purpose*/
	public abstract void onRecieveFrom(boolean isServer, ConfigSegmentCollection collection);
	
	/**This method is called everytime the client/server receives an update.
	 * Only use post if you really need to change things after*/
	public void onRecieveFromPost(boolean isServer, ConfigSegmentCollection collection){}
	
	/**Gets called before the update packets are send, only use if the branch does need custom config handling*/
	public void onPacketSend(boolean isServer, ConfigSegmentCollection collection) {}

}
