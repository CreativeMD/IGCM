package com.creativemd.ingameconfigmanager.mod.workbench;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;
import com.creativemd.creativecore.common.recipe.BetterShapelessRecipe;
import com.creativemd.creativecore.common.recipe.RecipeLoader;
import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.controls.gui.GuiStateButton;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.AddRecipeSegment;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorkbenchMachine extends RecipeMachine<IRecipe>{
	
	public WorkbenchMachine(ModTab tab, String name) {
		super(tab, name);
	}

	@Override
	public int getOutputCount() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getAvatar() {
		return new ItemStack(Blocks.CRAFTING_TABLE);
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}

	@Override
	public void addRecipeToList(IRecipe recipe) {
		CraftingManager.getInstance().getRecipeList().add(recipe);
	}

	@Override
	public void clearRecipeList() {
		CraftingManager.getInstance().getRecipeList().clear();		
	}

	@Override
	public ArrayList<IRecipe> getAllExitingRecipes() {
		ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();
		List vanillaRecipes = CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < vanillaRecipes.size(); i++) {
			recipes.add((IRecipe) vanillaRecipes.get(i));
		}
		return recipes;
	}

	@Override
	public ItemStack[] getOutput(IRecipe recipe) {
		return new ItemStack[]{recipe.getRecipeOutput()};
	}
	
	public void getInput(ItemStack[] grid, Object[] items, int size, int cols)
	{
		if(items == null)
			return ;
		if(cols == 0)
		{
			switch(size)
			{
			case 1:
				cols = 1;
			case 2:
				cols = 2;
				break;
			case 3:
				cols = 3;
				break;
			case 4:
				cols = 2;
				break;
			case 6:
				cols = 3;
				break;
			default:
				cols = 3;
				break;
			}
		}
		for(int zahl = 0; zahl < size; zahl++)
		{
			int row = zahl/cols;
			int index = row*3 + zahl - row*cols;
			if(items.length > zahl && index < 9 && index > -1)
			{
				grid[index] = getItemStack(items[zahl]);
			}
		}
	}
	
	public ItemStack getItemStack(Object object)
	{
		ItemStack[] result = ObjectoItemStack(object);
		ItemStack stack = null;
		if(result.length > 0)
			stack = result[result.length-1];
		if(stack != null)
			stack.stackSize = 1;
		return stack;
	}
	
	public ItemStack[] ObjectoItemStack(Object object)
	{
		if(object instanceof Item){
			return new ItemStack[]{new ItemStack((Item) object)};
		}else if(object instanceof Block){
			return new ItemStack[]{new ItemStack((Block) object)};
		}else if(object instanceof ItemStack){
			return new ItemStack[]{((ItemStack) object).copy()};
		}else if(object instanceof List<?>){
			List stacks = (List) object;
			ItemStack[] result = new ItemStack[stacks.size()];
			for(int zahl = 0; zahl < stacks.size(); zahl++)
				if(stacks.get(zahl) instanceof ItemStack)
					result[zahl] = ((ItemStack) stacks.get(zahl)).copy();
					
			return result;
					
		}
		return new ItemStack[0];
	}

	@Override
	public void fillGrid(ItemStack[] grid, IRecipe recipe) {
		getInput(grid, RecipeLoader.getInput(recipe), recipe.getRecipeSize(), RecipeLoader.getWidth(recipe));
		/*if(recipe instanceof IRecipeInfo)
		{
			getInput(grid, ((IRecipeInfo) recipe).getInput(), recipe.getRecipeSize(), ((IRecipeInfo) recipe).getWidth());
		}
		else if(recipe instanceof ShapedRecipes)
		{
			ShapedRecipes newrecipe = (ShapedRecipes) recipe;
			getInput(grid, newrecipe.recipeItems, newrecipe.getRecipeSize(), newrecipe.recipeWidth);
		}
		else if(recipe instanceof ShapelessOreRecipe)
		{
			ShapelessOreRecipe newrecipe = (ShapelessOreRecipe) recipe;
			getInput(grid, newrecipe.getInput().toArray(), 9, 0);
		}	
		else if(recipe instanceof ShapelessRecipes)
		{
			ShapelessRecipes newrecipe = (ShapelessRecipes) recipe;
			getInput(grid, newrecipe.recipeItems.toArray(), 9, 0);
		}
		else if(recipe instanceof ShapedOreRecipe)
		{
			ShapedOreRecipe newrecipe = (ShapedOreRecipe) recipe;
			getInput(grid, newrecipe.getInput(), 9, 0);
		}*/
	}

	@Override
	public void fillGridInfo(StackInfo[] grid, IRecipe recipe) {
		if(recipe instanceof BetterShapedRecipe)
		{
			for (int i = 0; i < ((BetterShapedRecipe) recipe).info.length; i++) {
				int row = i/((BetterShapedRecipe) recipe).getWidth();
				int index = row*3+(i-row*((BetterShapedRecipe) recipe).getWidth());
				grid[index] = ((BetterShapedRecipe) recipe).info[i];
			}
		}else if (recipe instanceof BetterShapelessRecipe){
			for (int i = 0; i < ((BetterShapelessRecipe) recipe).info.size(); i++) {
				grid[i] = ((BetterShapelessRecipe) recipe).info.get(i);
			}
		}
	}
	
	@Override
	public void onBeforeSave(IRecipe recipe, NBTTagCompound nbt)
	{
		nbt.setBoolean("shaped", recipe instanceof BetterShapedRecipe);
	}
	
	@Override
	public void parseExtraInfo(NBTTagCompound nbt, AddRecipeSegment segment, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls)
	{
		for (int i = 0; i < guiControls.size(); i++) {
			if(guiControls.get(i).is("type"))
			{
				nbt.setBoolean("shaped", ((GuiStateButton)guiControls.get(i)).getState() == 0);
			}
		}
	}
	
	@Override
	public void onControlsCreated(IRecipe recipe, boolean isAdded, int x, int y, int maxWidth, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls)
	{
		if(isAdded)
		{
			guiControls.add(new GuiStateButton("type", recipe instanceof BetterShapelessRecipe ? 1 : 0, x+maxWidth-80, y, 70, 14, "Shaped", "Shapeless"));
		}
	}

	@Override
	public IRecipe parseRecipe(StackInfo[] input, ItemStack[] output, NBTTagCompound nbt, int width, int height) {
		if(output.length == 1 && output[0] != null)
		{
			ItemStack result = output[0].copy();
			if(nbt.getBoolean("shaped"))
			{
				return new BetterShapedRecipe(width, input, result);
			}else{
				ArrayList<StackInfo> info = new ArrayList<StackInfo>();
				for (int i = 0; i < input.length; i++) {
					if(input[i] != null)
						info.add(input[i]);
				}
				if(info.size() > 0)
					return new BetterShapelessRecipe(info, result);
			}
		}
		return null;
	}

	@Override
	public boolean doesSupportStackSize() {
		return false;
	}

}
