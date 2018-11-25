package com.creativemd.igcm.jei;

import java.util.List;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.container.client.GuiSlotControl;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.block.BlockAdvancedWorkbench;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AdvCraftingRecipeCategory implements IRecipeCategory<IRecipeWrapper> {
	
	public static final String CategoryUiD = "igcm.advcrafting";
	
	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = BlockAdvancedWorkbench.outputs;
	
	public static final int width = 116;
	public static final int height = 54;
	
	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;
	@Nonnull
	private final ICraftingGridHelper craftingGridHelper;
	
	public AdvCraftingRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");
		
		background = new IDrawable() {
			
			@Override
			public int getWidth() {
				return 180;
			}
			
			@Override
			public int getHeight() {
				return 180;
			}
			
			@Override
			public void draw(Minecraft minecraft, int xOffset, int yOffset) {
				GlStateManager.translate(xOffset, yOffset, 0);
				draw(minecraft);
			}
			
			@Override
			public void draw(Minecraft minecraft) {
				int borderWidth = 1;
				Style style = GuiSlotControl.slotStyle;
				int width = 18;
				int height = 18;
				for (int y = 0; y < BlockAdvancedWorkbench.gridSize; ++y) {
					for (int x = 0; x < BlockAdvancedWorkbench.gridSize; ++x) {
						GlStateManager.pushMatrix();
						GlStateManager.translate(x * 18, y * 18, 0);
						style.getBorder(null).renderStyle(GuiRenderHelper.instance, width, height);
						
						GlStateManager.translate(borderWidth, borderWidth, 0);
						style.getBackground(null).renderStyle(GuiRenderHelper.instance, width - borderWidth * 2, height - borderWidth * 2);
						GlStateManager.popMatrix();
					}
				}
				
				for (int i = 0; i < BlockAdvancedWorkbench.outputs; i++) {
					GlStateManager.pushMatrix();
					GlStateManager.translate(120, 18 + i * 18, 0);
					style.getBorder(null).renderStyle(GuiRenderHelper.instance, width, height);
					
					GlStateManager.translate(borderWidth, borderWidth, 0);
					style.getBackground(null).renderStyle(GuiRenderHelper.instance, width - borderWidth * 2, height - borderWidth * 2);
					GlStateManager.popMatrix();
				}
			}
		};
		localizedName = Translator.translateToLocal("Advanced Crafting Table");
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
	}
	
	@Override
	@Nonnull
	public String getUid() {
		return CategoryUiD;
	}
	
	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}
	
	@Override
	@Nonnull
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		
		for (int i = 0; i < BlockAdvancedWorkbench.outputs; i++) {
			guiItemStacks.init(craftOutputSlot + i, false, 120, 18 + i * 18);
		}
		
		for (int y = 0; y < BlockAdvancedWorkbench.gridSize; ++y) {
			for (int x = 0; x < BlockAdvancedWorkbench.gridSize; ++x) {
				int index = craftInputSlot1 + x + (y * BlockAdvancedWorkbench.gridSize);
				guiItemStacks.init(index, true, x * 18, y * 18);
			}
		}
		
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
		
		if (recipeWrapper instanceof IShapedCraftingRecipeWrapper) {
			IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) recipeWrapper;
			craftingGridHelper.setInputs(guiItemStacks, inputs, wrapper.getWidth(), wrapper.getHeight());
		} else {
			craftingGridHelper.setInputs(guiItemStacks, inputs);
			recipeLayout.setShapeless();
		}
		
		guiItemStacks.set(craftOutputSlot, outputs.get(0));
	}
	
	@Override
	public String getModName() {
		return IGCM.modid;
	}
	
}