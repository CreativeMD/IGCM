package com.creativemd.igcm.machines;

import java.util.List;

import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.igcm.api.machine.RecipeMachine;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.common.brewing.VanillaBrewingRecipe;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

public class BrewingStandMachine extends RecipeMachine<IBrewingRecipe> {
	
	public BrewingStandMachine(String id, String title, ItemStack avatar) {
		super(id, title, avatar);
	}

	public static class IGCMBrewingRecipe implements IBrewingRecipe {
		
		public InfoStack input;
		public InfoStack ingredient;
		public ItemStack output;
		
		public IGCMBrewingRecipe(InfoStack input, InfoStack ingredient, ItemStack output) {
			this.input = input;
			this.ingredient = ingredient;
			this.output = output;
		}
		
		@Override
		public boolean isInput(ItemStack input) {
			return this.input.isInstance(input);
		}

		@Override
		public boolean isIngredient(ItemStack ingredient) {
			return this.ingredient.isInstance(ingredient);
		}

		@Override
		public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
			return output;
		}
		
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 2;
	}

	@Override
	public int getOutputCount() {
		return 1;
	}
	
	private static List<IBrewingRecipe> brewingRecipes = ReflectionHelper.getPrivateValue(BrewingRecipeRegistry.class, null, "recipes");
	
	@Override
	public void addRecipeToList(Side side, IBrewingRecipe recipe) {
		brewingRecipes.add(recipe);
	}

	@Override
	public void clearRecipeList(Side side) {
		brewingRecipes.clear();
	}

	@Override
	public ItemStack[] getOutput(IBrewingRecipe recipe) {
		ItemStack[] output = new ItemStack[1];
		if(recipe instanceof VanillaBrewingRecipe)
			output[0] = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD);
		else{
			try{
				output[0] = recipe.getOutput(ItemStack.EMPTY, ItemStack.EMPTY);
			}catch(Exception e){
				
			}
			if(output[0] == null)
				output[0] = ItemStack.EMPTY;
		}
		return output;
	}

	@Override
	public List<IBrewingRecipe> getAllExitingRecipes() {
		return brewingRecipes;
	}

	@Override
	public void fillGrid(ItemStack[] grid, IBrewingRecipe recipe) {
		if(recipe instanceof VanillaBrewingRecipe)
		{
			grid[0] = new ItemStack(Items.NETHER_WART);
			grid[1] = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
		}
	}

	@Override
	public boolean doesSupportStackSize() {
		return true;
	}

	@Override
	public void fillGridInfo(InfoStack[] grid, IBrewingRecipe recipe) {
		if(recipe instanceof IGCMBrewingRecipe)
		{
			grid[0] = ((IGCMBrewingRecipe) recipe).ingredient;
			grid[1] = ((IGCMBrewingRecipe) recipe).input;
		}
	}

	@Override
	public IBrewingRecipe parseRecipe(InfoStack[] input, ItemStack[] output, NBTTagCompound nbt, int width,
			int height) {
		return new IGCMBrewingRecipe(input[1], input[0], output[0]);
	}

	@Override
	public boolean hasJEISupport() {
		// TODO Auto-generated method stub
		return false;
	}

}
