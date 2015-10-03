package com.creativemd.ingameconfigmanager.api.common.machine;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.machine.ConfigMachineAddBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.machine.ConfigMachineDisableBranch;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.AddRecipeSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.DisableRecipeSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.RecipeSegment;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**A machine/worbench/table. It is something which has inputs and has an output**/
public abstract class RecipeMachine<T>{
	
	public boolean sendingUpdate;
	public String name;
	public ConfigMachineDisableBranch disableBranch;
	public ConfigMachineAddBranch addBranch;
	
	public RecipeMachine(ModTab tab, String name)
	{
		super();
		this.name = name;
		if(hasDisableBranch())
		{
			disableBranch = new ConfigMachineDisableBranch(this, name + " Disable");
			tab.addBranch(disableBranch);
		}
		
		if(hasAddedBranch())
		{
			addBranch = new ConfigMachineAddBranch(this, name + " Add");
			tab.addBranch(addBranch);
		}
	}
	
	public boolean hasDisableBranch()
	{
		return true;
	}
	
	public boolean hasAddedBranch()
	{
		return true;
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract int getOutputCount();
	
	/**Add the recipe to the "main" list of the "real" machine/block/whatever.
	 * Workbench: Add it to the crafting list
	 */
	public abstract void addRecipeToList(T recipe);
	
	/**Clear the "main" list of the "real" machine/block/whatever.
	 * Workbench: Remove all existing crafting recipes
	 */
	public abstract void clearRecipeList();
	
	public abstract ItemStack[] getOutput(T recipe);
	
	/**To add extra information or configuration to a recipe**/
	public void onControlsCreated(T recipe, boolean isAdded, int x, int y, int maxWidth, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls) {}
	
	/**Don't forget to set the offset of the segments**/
	public ArrayList<ConfigSegment> getCustomSegments()
	{
		return new ArrayList<ConfigSegment>();
	}
	
	//==================Disabled Recipes only==================
	
	public abstract ArrayList<T> getAllExitingRecipes();
	
	public abstract void fillGrid(ItemStack[] grid, T recipe);
	
	//==================Added Recipes only==================
	
	public abstract boolean doesSupportStackSize();
	
	public abstract void fillGridInfo(StackInfo[] grid, T recipe);
	
	/**Save extra information into the nbt tag**/
	public void onBeforeSave(T recipe, NBTTagCompound nbt) {}
	
	/**Parse extra information into the nbt tag**/
	public void parseExtraInfo(NBTTagCompound nbt, AddRecipeSegment segment, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls) {}
	
	/**Parse a recipe having input, output and your nbt tag**/
	public abstract T parseRecipe(StackInfo[] input, ItemStack[] output, NBTTagCompound nbt, int width, int height);
	
	//==================Decoration==================
	
	public abstract ItemStack getAvatar();
	
}
