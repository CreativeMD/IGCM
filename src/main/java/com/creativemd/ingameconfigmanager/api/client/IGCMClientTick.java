package com.creativemd.ingameconfigmanager.api.client;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.packet.gui.OpenGuiPacket;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.mc.ContainerSub;
import com.creativemd.ingameconfigmanager.api.core.IGCM;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IGCMClientTick {
	
	public Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void openGui(RenderTickEvent event)
	{
		if(mc.thePlayer != null && mc.theWorld != null)
		{
			if(mc.gameSettings.isKeyDown(IGCMClient.openConfig) && !(mc.thePlayer.openContainer instanceof ContainerSub) && mc.inGameHasFocus)
				IGCM.openModsGui(mc.thePlayer);
		}
	}
	
}
