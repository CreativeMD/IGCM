package com.creativemd.igcm;

import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.gui.opener.GuiHandler;
import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.block.SubContainerAdvancedWorkbench;
import com.creativemd.igcm.block.SubGuiAdvancedWorkbench;
import com.creativemd.igcm.client.gui.SubGuiConfigSegement;
import com.creativemd.igcm.client.gui.SubGuiProfile;
import com.creativemd.igcm.container.SubContainerConfigSegment;
import com.creativemd.igcm.container.SubContainerProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IGCMGuiManager extends CustomGuiHandler{
	
	public static void openConfigGui(EntityPlayer player)
	{
		openConfigGui(player, "root");
	}
	
	public static void openConfigGui(EntityPlayer player, String path)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("gui", 0);
		nbt.setString("path", path);
		GuiHandler.openGui(IGCM.guiID, nbt, player);
	}
	
	public static void openProfileGui(EntityPlayer player)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("gui", 1);
		nbt.setInteger("index", 0);
		GuiHandler.openGui(IGCM.guiID, nbt, player);
	}
	
	@Override
	public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
		int gui = nbt.getInteger("gui");
		String name = nbt.getString("path");
		switch(gui)
		{
		case 0:
			return new SubContainerConfigSegment(player, ConfigTab.getSegmentByPath(name));
		case 1:
			return new SubContainerProfile(player);
		case 2:
			return new SubContainerAdvancedWorkbench(player);
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
		int gui = nbt.getInteger("gui");
		String name = nbt.getString("path");
		switch(gui)
		{
		case 0:
			return new SubGuiConfigSegement(ConfigTab.getSegmentByPath(name));
		case 1:
			return new SubGuiProfile();
		case 2:
			return new SubGuiAdvancedWorkbench();
		}
		return null;
	}
	
}
