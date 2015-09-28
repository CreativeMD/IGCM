package com.creativemd.ingameconfigmanager.mod.block;

import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiProgressBar;
import com.creativemd.creativecore.common.gui.controls.container.GuiSlotControl;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler.ClientHandshakeStateEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiAdvancedWorkbench extends SubGui {
	
	public SubGuiAdvancedWorkbench() {
		super(176, 200);
	}
	
	public boolean crafting = false;
	
	@Override
	public void createControls() {
		 controls.add(new GuiButton("Craft!", 130, 85, 40, 20));
		 controls.add(new GuiProgressBar("progress", 132, 30, 36, 10));
	}
	
	@SideOnly(Side.CLIENT)
	public static long lastTick;
	
	@Override
	public void onTick()
	{
		if(crafting)
		{
			if(lastTick == 0)
				lastTick = System.currentTimeMillis();
			GuiProgressBar bar = (GuiProgressBar) getControl("progress");
			
			double timeLeft = (System.currentTimeMillis() - lastTick);
			
			bar.pos += timeLeft;
			if(bar.pos >= bar.max)
			{
				sendPacketToServer(0, new NBTTagCompound());
				bar.pos = bar.max;
				crafting = false;
				getControl("Craft!").enabled = true;
				
				for (int i = 0; i < controls.size(); i++) {
					if(controls.get(i) instanceof GuiSlotControl)
						controls.get(i).enabled = true;
				}
			}
			
		}
		lastTick = System.currentTimeMillis();
	}
	
	@CustomEventSubscribe
	public void onClicked(ControlClickEvent event)
	{
		if(event.source.is("Craft!"))
		{
			AdvancedGridRecipe recipe = null;
			for (int i = 0; i < BlockAdvancedWorkbench.recipes.size(); i++) {
				if(BlockAdvancedWorkbench.recipes.get(i).isValidRecipe(((SubContainerAdvancedWorkbench) container).crafting, 6, 6))
				{
					recipe = BlockAdvancedWorkbench.recipes.get(i);
					break;
				}
			}
			if(recipe != null)
			{
				for (int i = 0; i < controls.size(); i++) {
					if(controls.get(i) instanceof GuiSlotControl)
						controls.get(i).enabled = false;
				}
				
				GuiProgressBar bar = (GuiProgressBar) getControl("progress");
				bar.pos = 0;
				bar.max = recipe.duration;
				event.source.enabled = false;
				crafting = true;
			}
		}
	}

	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		
	}

}
