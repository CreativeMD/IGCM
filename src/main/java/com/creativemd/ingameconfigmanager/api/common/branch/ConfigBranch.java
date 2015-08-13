package com.creativemd.ingameconfigmanager.api.common.branch;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ConfigBranch{
	
	/**List should only give information and maybe helpful for some custom custom functionalities**/
	public static ArrayList<ConfigBranch> branches = new ArrayList<ConfigBranch>();
	
	public static int indexOf(ConfigBranch branch)
	{
		return branches.indexOf(branch);
	}
	
	/**The Title of the branch**/
	public String name;
	
	public final int id;
	
	public ConfigBranch(String name)
	{
		this.id = branches.size();
		branches.add(this);
		this.name = name;
	}
	
	//Using Forge libs instead
	//@SideOnly(Side.CLIENT)
	///**Should return ALL segments for configuration which does only effect the client side (No synchronization)*/
	//public abstract ArrayList<ConfigSegment> getClientSegments();
	
	/**Should return ALL segments for configuration which does also effect the server/all other players (synchronization)*/
	public abstract ArrayList<ConfigSegment> getConfigSegments();
	
	public abstract boolean needPacket();
	
	//public abstract boolean needClientConfig();
	
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
