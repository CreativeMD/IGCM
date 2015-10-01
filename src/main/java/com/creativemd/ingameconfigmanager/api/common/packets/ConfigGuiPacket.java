package com.creativemd.ingameconfigmanager.api.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;

import com.creativemd.creativecore.common.container.ContainerSub;
import com.creativemd.creativecore.common.gui.GuiContainerSub;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
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
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget;
import cpw.mods.fml.common.network.internal.FMLMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ConfigGuiPacket extends CreativeCorePacket{
	
	public int gui;
	public int index;
	
	public ConfigGuiPacket()
	{
		
	}
	
	public ConfigGuiPacket(int gui, int index)
	{
		this.gui = gui;
		this.index = index;
	}

	@Override
	public void writeBytes(ByteBuf buf) {
		buf.writeInt(gui);
		if(gui > 0)
			buf.writeInt(index);
	}

	@Override
	public void readBytes(ByteBuf buf) {
		gui = buf.readInt();
		if(gui > 0)
			index = buf.readInt();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void executeClient(EntityPlayer player) {
		switch(gui)
		{
		/**mods**/
		case 0:
			FMLCommonHandler.instance().showGuiScreen(new GuiContainerSub(player, new SubGuiMods(), new SubContainerMods(player)));
			break;
		/**mod overview**/
		case 1:
			ModTab tab = TabRegistry.getTabByIndex(index);
			if(tab != null)
			{
				FMLCommonHandler.instance().showGuiScreen(new GuiContainerSub(player, new SubGuiModOverview(tab), new SubContainerMods(player)));
			}
			break;
		/**branch**/
		case 2:
			ConfigBranch branch = ConfigBranch.getBranchByID(index);
			if(branch != null)
			{
				FMLCommonHandler.instance().showGuiScreen(new GuiContainerSub(player, new SubGuiBranch(branch), new SubContainerBranch(player, branch)));
			}
			break;
		case 3:
			FMLCommonHandler.instance().showGuiScreen(new GuiContainerSub(player, new SubGuiProfile(), new SubContainerMods(player)));
			break;
		case 4:
			FMLCommonHandler.instance().showGuiScreen(new GuiContainerSub(player, new SubGuiAdvancedWorkbench(), new SubContainerAdvancedWorkbench(player)));
			break;
		}
	}

	@Override
	public void executeServer(EntityPlayer player) {
		
		switch(gui)
		{
		/**mods**/
		case 0:
		case 3:
			openContainerOnServer((EntityPlayerMP) player, new ContainerSub(player, new SubContainerMods(player)));
			break;
		/**mod overview**/
		case 1:
			ModTab tab = TabRegistry.getTabByIndex(index);
			if(tab != null)
			{
				openContainerOnServer((EntityPlayerMP)player, new ContainerSub(player, new SubContainerMods(player)));
			}
			break;
		/**branch**/
		case 2:
			ConfigBranch branch = ConfigBranch.getBranchByID(index);
			if(branch != null)
			{
				openContainerOnServer((EntityPlayerMP)player, new ContainerSub(player, new SubContainerBranch(player, branch)));
			}
			break;
		case 4:
			openContainerOnServer((EntityPlayerMP) player, new ContainerSub(player, new SubContainerAdvancedWorkbench(player)));
			break;
		}
	}
	
	public void openContainerOnServer(EntityPlayerMP entityPlayerMP, Container container)
	{
		entityPlayerMP.getNextWindowId();
        entityPlayerMP.closeContainer();
        int windowId = entityPlayerMP.currentWindowId;
        entityPlayerMP.openContainer = container;
        entityPlayerMP.openContainer.windowId = windowId;
        entityPlayerMP.openContainer.addCraftingToCrafters(entityPlayerMP);
	}

}
