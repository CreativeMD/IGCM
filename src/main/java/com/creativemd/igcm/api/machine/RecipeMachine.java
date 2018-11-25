package com.creativemd.igcm.api.machine;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.ContainerControl;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.igcm.api.ConfigElement;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.api.segments.advanced.AddRecipeSegment;
import com.creativemd.igcm.jei.JEIHandler;

import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** A machine/worbench/table. It is something which has inputs and has an output **/
public abstract class RecipeMachine<T> {
	
	public boolean sendingUpdate;
	public String id;
	public ConfigMachineDisableBranch disableBranch;
	public ConfigMachineAddBranch addBranch;
	public ConfigMachineBranch mainBranch;
	public ItemStack avatar;
	
	public RecipeMachine(String id, String title, ItemStack avatar) {
		this(ConfigTab.root, id, title, avatar);
	}
	
	public RecipeMachine(ConfigElement parent, String id, String title, ItemStack avatar) {
		super();
		this.id = id;
		this.avatar = avatar;
		mainBranch = new ConfigMachineBranch(title, avatar, this);
		parent.registerElement(id, mainBranch);
		
	}
	
	public String recipeToString(T recipe) {
		ItemStack[] input = new ItemStack[getHeight() * getWidth()];
		fillGrid(input, recipe);
		
		boolean emptyRecipe = true;
		StringBuilder builder = new StringBuilder("{");
		for (int i = 0; i < input.length; i++) {
			if (i > 0)
				builder.append(",");
			if (input[i] != null && !input[i].isEmpty()) {
				try {
					if (input[i].getItem() instanceof ItemBlock)
						builder.append(Block.REGISTRY.getNameForObject(Block.getBlockFromItem(input[i].getItem())).toString());
					else
						builder.append(Item.REGISTRY.getNameForObject(input[i].getItem()).toString());
					builder.append(":" + input[i].getItemDamage());
					emptyRecipe = false;
				} catch (Exception e) {
					
				}
			}
		}
		builder.append("}{");
		
		ItemStack[] output = getOutput(recipe);
		for (int i = 0; i < output.length; i++) {
			if (i > 0)
				builder.append(",");
			if (output[i] != null && !output[i].isEmpty()) {
				try {
					if (output[i].getItem() instanceof ItemBlock)
						builder.append(Block.REGISTRY.getNameForObject(Block.getBlockFromItem(output[i].getItem())).toString());
					else
						builder.append(Item.REGISTRY.getNameForObject(output[i].getItem()).toString());
					builder.append(":" + output[i].getItemDamage());
					emptyRecipe = false;
				} catch (Exception e) {
					
				}
			}
		}
		builder.append("}");
		if (emptyRecipe)
			return recipe.getClass().getName();
		return builder.toString();
	}
	
	public boolean hasDisableBranch() {
		return true;
	}
	
	public boolean hasAddedBranch() {
		return true;
	}
	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
	public abstract int getOutputCount();
	
	/**
	 * Add the recipe to the "main" list of the "real" machine/block/whatever.
	 * Workbench: Add it to the crafting list
	 */
	public abstract void addRecipeToList(Side side, T recipe);
	
	/**
	 * Clear the "main" list of the "real" machine/block/whatever.
	 * Workbench: Remove all existing crafting recipes
	 */
	public abstract void clearRecipeList(Side side);
	
	public abstract ItemStack[] getOutput(T recipe);
	
	/** To add extra information or configuration to a recipe **/
	@SideOnly(Side.CLIENT)
	public void onControlCreated(T recipe, boolean isAdded, int x, int y, int maxWidth, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls) {
	}
	
	/** Don't forget to set the offset of the segments **/
	public ArrayList<ConfigSegment> getCustomSegments() {
		return new ArrayList<ConfigSegment>();
	}
	
	public void createExtraSegments() {
	}
	
	public void onReceiveFrom(Side side) {
	}
	
	public void onUpdateSendToClient(EntityPlayer player) {
	}
	
	//==================Disabled Recipes only==================
	
	public abstract List<T> getAllExitingRecipes();
	
	public abstract void fillGrid(ItemStack[] grid, T recipe);
	
	//==================Added Recipes only==================
	
	public void onRecipeParsed(List<AddRecipeSegment> segments) {
	}
	
	public abstract boolean doesSupportStackSize();
	
	public abstract void fillGridInfo(InfoStack[] grid, T recipe);
	
	/** Save extra information into the nbt tag **/
	public void writeExtraInfo(T recipe, NBTTagCompound nbt) {
	}
	
	/** Parse a recipe having input, output and your nbt tag **/
	public abstract T parseRecipe(InfoStack[] input, ItemStack[] output, NBTTagCompound nbt, int width, int height);
	
	/** Parse extra information into the nbt tag **/
	@SideOnly(Side.CLIENT)
	public void parseExtraInfo(NBTTagCompound nbt, AddRecipeSegment segment, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls) {
	}
	
	//==================JEI==================
	
	public abstract boolean hasJEISupport();
	
	public abstract String getJEICategory();
	
	@Method(modid = "jei")
	public abstract List getJEIRecipes();
	
	@Method(modid = "jei")
	public void updateJEI() {
		if (hasJEISupport() && JEIHandler.isActive && JEIHandler.recipeRegistry != null) {
			IRecipeCategory category = ((IRecipeRegistry) JEIHandler.recipeRegistry).getRecipeCategory(getJEICategory());
			
			/* List<IRecipeWrapper> oldRecipes = ((IRecipeRegistry) JEIHandler.recipeRegistry).getRecipeWrappers(category);
			 * for (IRecipeWrapper recipe : oldRecipes) {
			 * ((IRecipeRegistry) JEIHandler.recipeRegistry).removeRecipe(recipe, category.getUid());
			 * } */
			
			JEIHandler.clearCategory(category.getUid());
			
			List recipes = getJEIRecipes();
			for (Object recipe : recipes) {
				IRecipeWrapper wrapper = ((IRecipeRegistry) JEIHandler.recipeRegistry).getRecipeWrapper(recipe, category.getUid());
				if (wrapper != null)
					((IRecipeRegistry) JEIHandler.recipeRegistry).addRecipe(wrapper, category.getUid());
			}
		}
	}
	
}
