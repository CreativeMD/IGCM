package com.creativemd.igcm.api.common.gui;

import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.opener.CustomGuiHandler;
import com.creativemd.igcm.api.client.gui.SubGuiBranch;
import com.creativemd.igcm.api.client.gui.SubGuiModOverview;
import com.creativemd.igcm.api.client.gui.SubGuiMods;
import com.creativemd.igcm.api.client.gui.SubGuiProfile;
import com.creativemd.igcm.api.common.branch.ConfigBranch;
import com.creativemd.igcm.api.common.container.SubContainerBranch;
import com.creativemd.igcm.api.common.container.SubContainerMods;
import com.creativemd.igcm.api.core.TabRegistry;
import com.creativemd.igcm.api.tab.ModTab;
import com.creativemd.igcm.mod.block.SubContainerAdvancedWorkbench;
import com.creativemd.igcm.mod.block.SubGuiAdvancedWorkbench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InGameGuiHandler extends CustomGuiHandler{

	@Override
	public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
		int gui = nbt.getInteger("gui");
		String name = nbt.getString("name");
		switch(gui)
		{
		/**mods**/
		case 0:
		case 3:
			return new SubContainerMods(player);
		/**mod overview**/
		case 1:
			ModTab tab = TabRegistry.getTabByID(name);
			if(tab != null)
				return new SubContainerMods(player);
			break;
		/**branch**/
		case 2:
			ConfigBranch branch = ConfigBranch.getBranchByID(name);
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
		String name = nbt.getString("name");
		switch(gui)
		{
		/**mods**/
		case 0:
			return new SubGuiMods();
		/**mod overview**/
		case 1:
			ModTab tab = TabRegistry.getTabByID(name);
			if(tab != null)
				return new SubGuiModOverview(tab);
			break;
		/**branch**/
		case 2:
			ConfigBranch branch = ConfigBranch.getBranchByID(name);
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
