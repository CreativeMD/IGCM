package com.creativemd.igcm.machines;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;
import com.creativemd.creativecore.common.recipe.BetterShapelessRecipe;
import com.creativemd.creativecore.common.recipe.RecipeLoader;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.controls.gui.GuiStateButton;
import com.creativemd.igcm.api.machine.RecipeMachine;
import com.creativemd.igcm.api.segments.advanced.AddRecipeSegment;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class WorkbenchMachine extends RecipeMachine<IRecipe> {

	public WorkbenchMachine(String id, String title, ItemStack avatar) {
		super(id, title, avatar);
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
	public int getOutputCount() {
		return 1;
	}

	@Override
	public void addRecipeToList(Side side, IRecipe recipe) {
		CraftingManager.getInstance().getRecipeList().add(recipe);
	}

	@Override
	public void clearRecipeList(Side side) {
		CraftingManager.getInstance().getRecipeList().clear();
	}

	@Override
	public ItemStack[] getOutput(IRecipe recipe) {
		return new ItemStack[]{recipe.getRecipeOutput()};
	}

	@Override
	public List<IRecipe> getAllExitingRecipes() {
		return CraftingManager.getInstance().getRecipeList();
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
			stack.setCount(1);
		return stack;
	}
	
	public ItemStack[] ObjectoItemStack(Object object)
	{
		try{
			if(object instanceof Item){
				return new ItemStack[]{new ItemStack((Item) object)};
			}else if(object instanceof Block){
				return new ItemStack[]{new ItemStack((Block) object)};
			}else if(object instanceof ItemStack){
				ItemStack stack = ((ItemStack) object).copy(); 
				if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
					stack.setItemDamage(0);
				return new ItemStack[]{stack};
			}else if(object instanceof List<?>){
				List stacks = (List) object;
				ItemStack[] result = new ItemStack[stacks.size()];
				for(int zahl = 0; zahl < stacks.size(); zahl++)
					if(stacks.get(zahl) instanceof ItemStack)
					{
						ItemStack stack = ((ItemStack) stacks.get(zahl)).copy();
						if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
							stack.setItemDamage(0);
						result[zahl] = stack;
					}
						
				return result;
						
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ItemStack[0];
	}
	
	@Override
	public void fillGrid(ItemStack[] grid, IRecipe recipe) {
		getInput(grid, RecipeLoader.getInput(recipe), recipe.getRecipeSize(), RecipeLoader.getWidth(recipe));
	}

	@Override
	public boolean doesSupportStackSize() {
		return false;
	}

	@Override
	public void fillGridInfo(InfoStack[] grid, IRecipe recipe) {
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
	public IRecipe parseRecipe(InfoStack[] input, ItemStack[] output, NBTTagCompound nbt, int width, int height) {
		if(output.length == 1 && output[0] != null)
		{
			ItemStack result = output[0].copy();
			if(nbt.getBoolean("shaped"))
			{
				return new BetterShapedRecipe(width, input, result);
			}else{
				ArrayList<InfoStack> info = new ArrayList<InfoStack>();
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
	public void writeExtraInfo(IRecipe recipe, NBTTagCompound nbt)
	{
		nbt.setBoolean("shaped", recipe instanceof BetterShapedRecipe);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
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
	@SideOnly(Side.CLIENT)
	public void onControlCreated(IRecipe recipe, boolean isAdded, int x, int y, int maxWidth, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls)
	{
		if(isAdded)
			guiControls.add(new GuiStateButton("type", recipe instanceof BetterShapelessRecipe ? 1 : 0, x+maxWidth-80, y+30, 70, 14, "Shaped", "Shapeless"));
	}

	@Override
	public boolean hasJEISupport() {
		return true;
	}

}
