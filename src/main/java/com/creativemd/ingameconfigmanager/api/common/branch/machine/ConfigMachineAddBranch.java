package com.creativemd.ingameconfigmanager.api.common.branch.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.AddRecipeSegment;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigMachineAddBranch extends ConfigBranch{

	public RecipeMachine machine;
	public ArrayList allRecipes;
	public ArrayList disabledRecipes;
	
	public ConfigMachineAddBranch(RecipeMachine machine, String name) {
		super(name);
		this.machine = machine;
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			avatar = getAvatar();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected Avatar getAvatar() {
		if(machine != null)
			return new AvatarItemStack(machine.getAvatar());
		return null;
	}
	
	@Override
	public boolean doesInputSupportStackSize()
	{
		return machine.doesSupportStackSize();
	}
	
	@Override
	public void loadCore() {
		
	}

	@Override
	public void createConfigSegments() {
		segments.add(new AddRecipeSegment("0", machine, null));
	}

	@Override
	public boolean needPacket() {
		return true;
	}
	
	public void onBeforeReceived(boolean isServer)
	{
		segments.clear();
	}
	
	public int findNextId(ConfigSegmentCollection collection)
	{
		int id = 0;
		while(collection.getSegmentByID("" + id) != null)
			id++;
		return id;
	}

	@Override
	public void onRecieveFrom(boolean isServer, ConfigSegmentCollection collection) {
		if(!machine.sendingUpdate)
		{
			if(machine.hasDisableBranch())
			{
				machine.sendingUpdate = true;
				machine.disableBranch.onRecieveFrom(isServer, new ConfigSegmentCollection(machine.disableBranch.getConfigSegments()));
				machine.sendingUpdate = false;
			}else{
				machine.clearRecipeList();
			}
		}
		if(segments != null)
		{
			int i = 0;
			while(i < segments.size())
			{
				if(segments.get(i).value != null)
				{
					machine.addRecipeToList(segments.get(i).value);
					i++;
				}else
					segments.remove(i);
			}
			int id = findNextId(collection);
			segments.add(new AddRecipeSegment("" + id, machine, null));
		}
	}
	
	@Override
	public ConfigSegment onFailedLoadingSegment(ConfigSegmentCollection collection, String id, String input, int currentIndex)
	{
		//int index = findNextId(new ConfigSegmentCollection(segments));
		AddRecipeSegment segment = new AddRecipeSegment("" + findNextId(collection), machine, null);
		return segment;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSearchable()
	{
		return false;
	}
}
