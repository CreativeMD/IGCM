package com.creativemd.ingameconfigmanager.mod;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.creativemd.ingameconfigmanager.mod.furnace.FurnaceMachine;
import com.creativemd.ingameconfigmanager.mod.workbench.WorkbenchMachine;

public class ConfigManagerModLoader {
	
	public static ModTab WorkBenchTab = new ModTab("Crafting Table", new AvatarItemStack(new ItemStack(Blocks.crafting_table)));
	public static ModTab FurnaceTab = new ModTab("Furnace", new AvatarItemStack(new ItemStack(Blocks.furnace)));
	public static WorkbenchMachine workbench;
	public static FurnaceMachine furnace;
	
	public static void loadMod()
	{
		workbench = new WorkbenchMachine(WorkBenchTab, "Crafting Table");
		TabRegistry.registerModTab(WorkBenchTab);
		
		furnace = new FurnaceMachine(FurnaceTab, "Furnace");
		TabRegistry.registerModTab(FurnaceTab);
	}
	
}
