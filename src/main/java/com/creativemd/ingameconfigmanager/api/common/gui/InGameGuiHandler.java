package com.creativemd.ingameconfigmanager.api.common.gui;

import com.creativemd.creativecore.common.container.ContainerSub;
import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.gui.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.GuiContainerSub;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.ingameconfigmanager.api.client.gui.SubGuiBranch;
import com.creativemd.ingameconfigmanager.api.client.gui.SubGuiModOverview;
import com.creativemd.ingameconfigmanager.api.client.gui.SubGuiMods;
import com.creativemd.ingameconfigmanager.api.client.gui.SubGuiProfile;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.container.SubContainerBranch;
import com.creativemd.ingameconfigmanager.api.common.container.SubContainerMods;
import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.creativemd.ingameconfigmanager.mod.block.SubContainerAdvancedWorkbench;
import com.creativemd.ingameconfigmanager.mod.block.SubGuiAdvancedWorkbench;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class InGameGuiHandler extends CustomGuiHandler{

	@Override
	public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
		int gui = nbt.getInteger("gui");
		int index = nbt.getInteger("index");
		switch(gui)
		{
		/**mods**/
		case 0:
		case 3:
			return new SubContainerMods(player);
		/**mod overview**/
		case 1:
			ModTab tab = TabRegistry.getTabByIndex(index);
			if(tab != null)
				return new SubContainerMods(player);
			break;
		/**branch**/
		case 2:
			ConfigBranch branch = ConfigBranch.getBranchByID(index);
			if(branch != null)
				return new SubContainerBranch(player, branch);
			break;
		case 4:
			return new SubContainerAdvancedWorkbench(player);
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
		int gui = nbt.getInteger("gui");
		int index = nbt.getInteger("index");
		switch(gui)
		{
		/**mods**/
		case 0:
			return new SubGuiMods();
		/**mod overview**/
		case 1:
			ModTab tab = TabRegistry.getTabByIndex(index);
			if(tab != null)
				return new SubGuiModOverview(tab);
			break;
		/**branch**/
		case 2:
			ConfigBranch branch = ConfigBranch.getBranchByID(index);
			if(branch != null)
				return new SubGuiBranch(branch);
			break;
		case 3:
			return new SubGuiProfile();
		case 4:
			return new SubGuiAdvancedWorkbench();
		}
		return null;
	}

}
