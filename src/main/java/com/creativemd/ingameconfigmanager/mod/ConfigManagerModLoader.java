package com.creativemd.ingameconfigmanager.mod;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.creativemd.ingameconfigmanager.mod.workbench.WorkbenchMachine;

public class ConfigManagerModLoader {
	
	public static ModTab tab = new ModTab("Crafting Table", new AvatarItemStack(new ItemStack(Blocks.crafting_table)));
	public static WorkbenchMachine workbench;
	
	public static void loadMod()
	{
		workbench = new WorkbenchMachine(tab, "Crafting Table");
		TabRegistry.registerModTab(tab);
	}
	
}
