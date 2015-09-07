package com.creativemd.ingameconfigmanager.api.common.branch.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.AddRecipeSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.DisableRecipeSegment;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
			return machine.getAvatar();
		return null;
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

	@Override
	public void onRecieveFrom(boolean isServer, ConfigSegmentCollection collection) {
		if(!machine.sendingUpdate)
		{
			machine.sendingUpdate = true;
			machine.disableBranch.onRecieveFrom(isServer, new ConfigSegmentCollection(machine.disableBranch.getConfigSegments()));
			machine.sendingUpdate = false;
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
			int id = 0;
			while(collection.getSegmentByID("" + id) != null)
				id++;
			segments.add(new AddRecipeSegment("" + id, machine, null));
		}
	}
	
	@Override
	public void onFailedLoadingSegment(String id, String input)
	{
		AddRecipeSegment segment = new AddRecipeSegment(id, machine, null);
		segment.receivePacketInformation(input);
		segments.add(segment);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSearchable()
	{
		return false;
	}
}
