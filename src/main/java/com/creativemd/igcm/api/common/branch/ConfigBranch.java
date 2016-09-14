package com.creativemd.igcm.api.common.branch;

import java.util.ArrayList;
import java.util.HashMap;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.igcm.api.common.segment.ConfigSegment;
import com.creativemd.igcm.api.tab.ModTab;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ConfigBranch{
	
	/**List should only give information and maybe helpful for some custom custom functionalities**/
	public static HashMap<String, ConfigBranch> branches = new HashMap<>();
	
	/*public static int indexOf(ConfigBranch branch)
	{
		return branches.indexOf(branch);
	}
	
	public static ConfigBranch getBranchByID(int id)
	{
		if(id >= 0 && id < branches.size())
			return branches.get(id);
		return null;
	}*/
	
	public static ConfigBranch getBranchByID(String id)
	{
		return branches.get(id);
	}
	
	public ModTab tab;
	
	/**The Title of the branch**/
	public final String name;
	
	//public final int id;
	
	@SideOnly(Side.CLIENT)
	public Avatar avatar;
	
	public ConfigBranch(String name)
	{
		branches.put(name, this);
		this.name = name;
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			avatar = getAvatar();
	}
	
	@SideOnly(Side.CLIENT)
	protected abstract Avatar getAvatar();
	
	@SideOnly(Side.CLIENT)
	public boolean isSearchable()
	{
		return true;
	}
	
	//Using Forge libs instead
	//@SideOnly(Side.CLIENT)
	///**Should return ALL segments for configuration which does only effect the client side (No synchronization)*/
	//public abstract ArrayList<ConfigSegment> getClientSegments();
	
	protected ArrayList<ConfigSegment> segments = null; 
	
	public ArrayList<ConfigSegment> getConfigSegments()
	{
		if(segments == null)
		{
			segments = new ArrayList<ConfigSegment>();
			createConfigSegments();
		}
		return segments;
	}
	
	/**Used for getting default settings. Example: Get all existing recipes and save it. It is needed for both sides**/
	public abstract void loadCore();
	
	/**Should return ALL segments for configuration which does also effect the server/all other players (synchronization)*/
	public abstract void createConfigSegments();
	
	public abstract boolean needPacket();
	
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
	
	public void onBeforeReceived(boolean isServer) {}
	
	public ConfigSegment onFailedLoadingSegment(ConfigSegmentCollection collection, String id, String input, int currentIndex)
	{
		return null;
	}

	public boolean doesInputSupportStackSize()
	{
		return true;
	}

}
