package com.creativemd.igcm.machines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.creativemd.creativecore.common.gui.ContainerControl;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.utils.stack.InfoBlock;
import com.creativemd.creativecore.common.utils.stack.InfoItem;
import com.creativemd.creativecore.common.utils.stack.InfoItemStack;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.igcm.api.machine.RecipeMachine;
import com.creativemd.igcm.api.segments.advanced.AddRecipeSegment;
import com.creativemd.igcm.jei.JEIHandler;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.plugins.vanilla.furnace.SmeltingRecipeMaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FurnaceMachine extends RecipeMachine<FurnaceRecipe> {
	
	public FurnaceMachine(String id, String title, ItemStack avatar) {
		super(id, title, avatar);
	}
	
	@Override
	public int getWidth() {
		return 1;
	}
	
	@Override
	public int getHeight() {
		return 1;
	}
	
	@Override
	public int getOutputCount() {
		return 1;
	}
	
	@Override
	public void addRecipeToList(Side side, FurnaceRecipe recipe) {
		InfoStack info = recipe.input[0];
		ItemStack output = recipe.output[0];
		if (info != null && output != null) {
			
			if (info instanceof InfoBlock)
				FurnaceRecipes.instance().addSmeltingRecipeForBlock(((InfoBlock) info).block, output, recipe.experience);
			else if (info instanceof InfoItem)
				FurnaceRecipes.instance().addSmelting(((InfoItem) info).item, output, recipe.experience);
			else if (info instanceof InfoItemStack)
				FurnaceRecipes.instance().addSmeltingRecipe(((InfoItemStack) info).stack.copy(), output, recipe.experience);
			else {
				ArrayList<ItemStack> stacks = info.getAllPossibleItemStacks();
				for (int i = 0; i < stacks.size(); i++) {
					FurnaceRecipes.instance().addSmeltingRecipe(stacks.get(i).copy(), output, recipe.experience);
				}
			}
		}
	}
	
	@Override
	public void clearRecipeList(Side side) {
		FurnaceRecipes.instance().getSmeltingList().clear();
	}
	
	@Override
	public ItemStack[] getOutput(FurnaceRecipe recipe) {
		return recipe.output;
	}
	
	@Override
	public ArrayList<FurnaceRecipe> getAllExitingRecipes() {
		ArrayList<FurnaceRecipe> recipes = new ArrayList<FurnaceRecipe>();
		
		for (Iterator<Entry<ItemStack, ItemStack>> iterator = FurnaceRecipes.instance().getSmeltingList().entrySet().iterator(); iterator.hasNext();) {
			Entry<ItemStack, ItemStack> recipe = iterator.next();
			recipes.add(new FurnaceRecipe(recipe.getValue(), recipe.getKey(), FurnaceRecipes.instance().getSmeltingExperience(recipe.getValue())));
		}
		return recipes;
	}
	
	@Override
	public void fillGrid(ItemStack[] grid, FurnaceRecipe recipe) {
		if (recipe.input[0] != null)
			grid[0] = recipe.input[0].getItemStack();
	}
	
	@Override
	public void fillGridInfo(InfoStack[] grid, FurnaceRecipe recipe) {
		grid[0] = recipe.input[0];
	}
	
	@Override
	public FurnaceRecipe parseRecipe(InfoStack[] input, ItemStack[] output, NBTTagCompound nbt, int width, int height) {
		if (input.length == 1 && input[0] != null && output.length == 1 && output[0] != null)
			return new FurnaceRecipe(output[0], input[0], nbt.getFloat("exp"));
		return null;
	}
	
	@Override
	public void writeExtraInfo(FurnaceRecipe recipe, NBTTagCompound nbt) {
		nbt.setFloat("exp", recipe.experience);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void parseExtraInfo(NBTTagCompound nbt, AddRecipeSegment segment, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls) {
		for (int i = 0; i < guiControls.size(); i++) {
			if (guiControls.get(i).is("exp")) {
				float exp = 0;
				try {
					exp = Float.parseFloat(((GuiTextfield) guiControls.get(i)).text);
				} catch (Exception e) {
					exp = 0;
				}
				nbt.setFloat("exp", exp);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onControlCreated(FurnaceRecipe recipe, boolean isAdded, int x, int y, int maxWidth, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls) {
		if (isAdded) {
			guiControls.add(new GuiTextfield("exp", recipe != null ? "" + recipe.experience : "0.0", x + maxWidth - 80, y, 40, 14).setFloatOnly());
		} else {
			guiControls.add(new GuiLabel("exp: " + recipe.experience, x + maxWidth - 60, y));
		}
	}
	
	@Override
	public boolean doesSupportStackSize() {
		return false;
	}
	
	@Override
	public boolean hasJEISupport() {
		return true;
	}
	
	@Override
	public String getJEICategory() {
		return VanillaRecipeCategoryUid.SMELTING;
	}
	
	@Override
	@Method(modid = "jei")
	public List getJEIRecipes() {
		return SmeltingRecipeMaker.getFurnaceRecipes(((IModRegistry) JEIHandler.modRegistry).getJeiHelpers());
	}
	
}
