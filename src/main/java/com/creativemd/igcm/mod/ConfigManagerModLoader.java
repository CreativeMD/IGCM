package com.creativemd.igcm.mod;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.igcm.api.core.IGCM;
import com.creativemd.igcm.api.core.TabRegistry;
import com.creativemd.igcm.api.tab.ModTab;
import com.creativemd.igcm.mod.block.AdvancedWorkbench;
import com.creativemd.igcm.mod.furnace.FurnaceMachine;
import com.creativemd.igcm.mod.general.GeneralBranch;
import com.creativemd.igcm.mod.workbench.WorkbenchMachine;

public class ConfigManagerModLoader {
	
	public static ModTab WorkBenchTab = new ModTab("Crafting Table", new ItemStack(Blocks.CRAFTING_TABLE));
	public static ModTab FurnaceTab = new ModTab("Furnace", new ItemStack(Blocks.FURNACE));
	public static WorkbenchMachine workbench;
	public static FurnaceMachine furnace;
	
	public static ModTab tab = new ModTab("InGameConfigManager", new ItemStack(Blocks.REDSTONE_TORCH));
	
	public static AdvancedWorkbench advancedWorkbench;
	
	public static void loadMod()
	{
		workbench = new WorkbenchMachine(WorkBenchTab, "Crafting Table");
		advancedWorkbench = new AdvancedWorkbench(WorkBenchTab, "Advanced Workbench");
		TabRegistry.registerModTab(WorkBenchTab);
		
		furnace = new FurnaceMachine(FurnaceTab, "Furnace");
		TabRegistry.registerModTab(FurnaceTab);
		
		tab.addBranch(new GeneralBranch("general"));
		TabRegistry.registerModTab(tab);
		
		
	}
	
}
