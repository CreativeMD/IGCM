package com.creativemd.igcm.mod.block;

import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.container.client.GuiSlotControl;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiProgressBar;
import com.creativemd.creativecore.gui.event.gui.GuiControlClickEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiAdvancedWorkbench extends SubGui {
	
	public SubGuiAdvancedWorkbench() {
		super(176, 200);
	}
	
	public boolean crafting = false;
	
	@Override
	public void createControls() {
		 controls.add(new GuiButton("Craft!", 130, 85) {
			
			@Override
			public void onClicked(int x, int y, int button) {}
		});
		 controls.add(new GuiProgressBar("progress", 132, 30, 30, 3, 100, 0));
	}
	
	public static long lastTick;
	
	@Override
	public void onTick()
	{
		if(crafting)
		{
			if(lastTick == 0)
				lastTick = System.currentTimeMillis();
			GuiProgressBar bar = (GuiProgressBar) get("progress");
			
			double timeLeft = (System.currentTimeMillis() - lastTick);
			
			bar.pos += timeLeft;
			if(bar.pos >= bar.max)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("type", 0);
				sendPacketToServer(nbt);
				bar.pos = bar.max;
				crafting = false;
				get("Craft!").enabled = true;
				
				for (int i = 0; i < controls.size(); i++) {
					if(controls.get(i) instanceof GuiSlotControl)
						controls.get(i).enabled = true;
				}
			}
			
		}
		lastTick = System.currentTimeMillis();
	}
	
	@CustomEventSubscribe
	public void onClicked(GuiControlClickEvent event)
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
				
				GuiProgressBar bar = (GuiProgressBar) get("progress");
				bar.pos = 0;
				bar.max = recipe.duration;
				event.source.enabled = false;
				crafting = true;
			}
		}
	}

}
