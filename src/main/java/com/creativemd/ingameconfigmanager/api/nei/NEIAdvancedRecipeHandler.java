package com.creativemd.ingameconfigmanager.api.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.creativemd.creativecore.common.recipe.GridRecipe;
import com.creativemd.creativecore.common.recipe.IRecipeInfo;
import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;
import com.creativemd.ingameconfigmanager.mod.block.AdvancedGridRecipe;
import com.creativemd.ingameconfigmanager.mod.block.BlockAdvancedWorkbench;

import codechicken.core.ReflectionManager;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRectHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class NEIAdvancedRecipeHandler implements ICraftingHandler, IUsageHandler{

	public static void load()
	{
		API.registerRecipeHandler(new NEIAdvancedRecipeHandler());
		API.registerUsageHandler(new NEIAdvancedRecipeHandler());
	}
	
	@Override
	public String getRecipeName() {
		return "AdvancedRecipe";
	}
	
	public LinkedList<RecipeTransferRect> transferRects = new LinkedList();
	
	public NEIAdvancedRecipeHandler() {
		loadTransferRects();
		RecipeTransferRectHandler.registerRectsToGuis(getRecipeTransferRectGuis(), transferRects);
	}
	
	public ArrayList<CachedAdvRecipe> arecipes = new ArrayList();

	public class CachedAdvRecipe
    {
		final long offset = System.currentTimeMillis();
		
        public ArrayList<PositionedStack> ingredients;
        public ArrayList<PositionedStack> result;

        public CachedAdvRecipe(int width, int height, Object[] items, ItemStack[] out) {
        	result = new ArrayList<PositionedStack>();
        	for (int i = 0; i < out.length; i++) {
        		if(out[i] != null)
        			result.add(new PositionedStack(out[i], 129, 3+i*18));
			}
            
            ingredients = new ArrayList<PositionedStack>();
            setIngredients(width, height, items);
        }

        public CachedAdvRecipe(GridRecipe recipe) {
            this(recipe.width, recipe.height, recipe.getInputStacks(), recipe.output);
        }

        /**
         * @param width
         * @param height
         * @param items  an ItemStack[] or ItemStack[][]
         */
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (items[y * width + x] == null)
                        continue;

                    PositionedStack stack = new PositionedStack(items[y * width + x], 10 + x * 18, 6 + y * 18, false);
                    stack.setMaxSize(1);
                    ingredients.add(stack);
                }
            }
        }
        
        public List<PositionedStack> getOtherStacks()
        {
        	ArrayList<PositionedStack> stacks = new ArrayList();
        	for (int i = 1; i < result.size(); i++) {
				stacks.add(result.get(i));
			}
        	return stacks;
        }
        
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }
        
        public PositionedStack getResult() {
        	if(result.size() > 0)
        		return result.get(0);
        	return null;
        }

        /*public ArrayList<PositionedStack> getResult() {
            return result;
        }*/

        public void computeVisuals() {
            for (PositionedStack p : ingredients)
                p.generatePermutations();
        }
        
        public List<PositionedStack> getCycledIngredients(int cycle, List<PositionedStack> ingredients)
        {
        	for (int itemIndex = 0; itemIndex < ingredients.size(); itemIndex++) {
        		randomRenderPermutation((PositionedStack)ingredients.get(itemIndex), cycle + itemIndex);
        	}
        	return ingredients;
       	}
        
        public void randomRenderPermutation(PositionedStack stack, long cycle) {
        	Random rand = new Random(cycle + offset);
        	stack.setPermutationToRender(Math.abs(rand.nextInt()) % stack.items.length);
        }
        
        public void setIngredientPermutation(Collection<PositionedStack> ingredients, ItemStack ingredient)
        {
        	for (PositionedStack stack : ingredients) {
        		for (int i = 0; i < stack.items.length; i++) {
        			if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, stack.items[i])) {
        				ItemStack item = stack.items[i];
        				item.setItemDamage(ingredient.getItemDamage());
        				stack.items = new ItemStack[] { item };
        				stack.setPermutationToRender(0);
        				break;
        			}
        		}
        	}
        }
        
        public boolean contains(Collection<PositionedStack> ingredients, ItemStack ingredient)
        {
        	for (PositionedStack stack : ingredients) {
        		if (stack.contains(ingredient))
        			return true;
        	}
        	return false;
        }
        
       	public boolean contains(Collection<PositionedStack> ingredients, Item ingred)
       	{
        	for (PositionedStack stack : ingredients)
        		if (stack.contains(ingred))
        			return true;
        	return false;
       	}
    }
	
	public int cycleticks = Math.abs((int)System.currentTimeMillis());
	
	public void onUpdate() {
		if (!NEIClientUtils.shiftKey())
			cycleticks += 1;
	}
    
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
    }
    
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrafting.class;
    }
    
    public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis()
    {
    	Class<? extends GuiContainer> clazz = getGuiClass();
    	if (clazz != null) {
    		LinkedList<Class<? extends GuiContainer>> list = new LinkedList();
    		list.add(clazz);
    		return list;
    	}
    	return null;
    }
    
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (getClass() == NEIAdvancedRecipeHandler.class) {
        	if((outputId.equals("crafting")))
        	{
	            for (AdvancedGridRecipe irecipe : BlockAdvancedWorkbench.recipes) {
	            	CachedAdvRecipe recipe = new CachedAdvRecipe(irecipe);
	                recipe.computeVisuals();
	                arecipes.add(recipe);
	            }
        	}
        	if(outputId.equals("item"))
        		loadCraftingRecipes((ItemStack) results[0]);
        }
    }

    
    public void loadCraftingRecipes(ItemStack result) {
        for (AdvancedGridRecipe irecipe : BlockAdvancedWorkbench.recipes) {
        	for (int i = 0; i < irecipe.output.length; i++) {
        		if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.output[i], result)) {
        			CachedAdvRecipe recipe = new CachedAdvRecipe(irecipe);
                    recipe.computeVisuals();
                    arecipes.add(recipe);
                    break;
                }
			}
            
        }
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        for (AdvancedGridRecipe irecipe : BlockAdvancedWorkbench.recipes) {
        	CachedAdvRecipe recipe = new CachedAdvRecipe(irecipe);
            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
                continue;

            recipe.computeVisuals();
            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    /*public CachedInfoShapedRecipe forgeShapedRecipe(ShapedOreRecipe recipe) {
        int width;
        int height;
        try {
            width = ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 4);
            height = ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 5);
        } catch (Exception e) {
            NEIClientConfig.logger.error("Error loading recipe", e);
            return null;
        }

        Object[] items = recipe.getInput();
        for (Object item : items)
            if (item instanceof List && ((List<?>) item).isEmpty())//ore handler, no ores
                return null;

        return new CachedInfoShapedRecipe(width, height, items, recipe.getRecipeOutput());
    }*/

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        //IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
        //if (renderer != null)
            //return renderer;

        IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
        if (positioner == null)
            return null;
        return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
    }

    public boolean isRecipe2x2(int recipe) {
        for (PositionedStack stack : getIngredientStacks(recipe))
            if (stack.relx > 43 || stack.rely > 24)
                return false;

        return true;
    }

	@Override
	public int numRecipes() {
		return arecipes.size();
	}

	@Override
	public void drawBackground(int paramInt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawForeground(int paramInt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PositionedStack> getIngredientStacks(int paramInt) {
		return ((CachedAdvRecipe)arecipes.get(paramInt)).getIngredients();
	}

	@Override
	public List<PositionedStack> getOtherStacks(int paramInt) {
		return ((CachedAdvRecipe)arecipes.get(paramInt)).getOtherStacks();
	}

	@Override
	public PositionedStack getResultStack(int paramInt) {
		return ((CachedAdvRecipe)arecipes.get(paramInt)).getResult();
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
		/*if ((GuiContainerManager.shouldShowTooltip(gui)) && (currenttip.size() == 0)) {
			Point offset = gui.getRecipePosition(recipe);
			currenttip = transferRectTooltip(gui, transferRects, x, y, currenttip);
		}*/
		return currenttip;
	}

	@Override
	public List<String> handleItemTooltip(GuiRecipe paramGuiRecipe, ItemStack paramItemStack, List<String> currenttip,
			int paramInt) {
		return currenttip;
	}

	@Override
	public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe) {
		if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe"))
			return transferRect(gui, recipe, false);
		if (keyCode == NEIClientConfig.getKeyBinding("gui.usage")) {
			return transferRect(gui, recipe, true);
		}
		return false;
	}

	@Override
	public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
		if (button == 0)
			return transferRect(gui, recipe, false);
		if (button == 1) {
			return transferRect(gui, recipe, true);
		}
		return false;
	}
	
	/*public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals("item")) {
			loadCraftingRecipes((ItemStack)results[0]);
		}
	}*/
	
	public void loadUsageRecipes(String inputId, Object... ingredients)
	{
		if (inputId.equals("item")) {
			loadUsageRecipes((ItemStack)ingredients[0]);
		}
	}
	
	private boolean transferRect(GuiRecipe gui, int recipe, boolean usage) {
		Point offset = gui.getRecipePosition(recipe);
		return transferRect(gui, transferRects, offset.x, offset.y, usage);
	}

	@Override
	public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
		NEIAdvancedRecipeHandler handler = newInstance();
		handler.loadCraftingRecipes(outputId, results);
		return handler;
	}
	
	@Override
	public IUsageHandler getUsageHandler(String inputId, Object... ingredients) {
		NEIAdvancedRecipeHandler handler = newInstance();
		handler.loadUsageRecipes(inputId, ingredients);
		return handler;
	}
	
	public NEIAdvancedRecipeHandler newInstance() {
		try {
			return (NEIAdvancedRecipeHandler)getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static boolean transferRect(GuiContainer gui, Collection<RecipeTransferRect> transferRects, int offsetx, int offsety, boolean usage) {
		Point pos = GuiDraw.getMousePosition();
		/*Point relMouse = new Point(pos.x - guiLeft - offsetx, pos.y - guiTop - offsety);
		for (RecipeTransferRect rect : transferRects) {
			if ((rect.contains(relMouse)) && (usage ? GuiUsageRecipe.openRecipeGui(outputId, results) : GuiCraftingRecipe.openRecipeGui(outputId, results)))
			{
				return true;
			}
		}*/
		return false;
	}

	@Override
	public boolean hasOverlay(GuiContainer paramGuiContainer, Container paramContainer, int paramInt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IOverlayHandler getOverlayHandler(GuiContainer paramGuiContainer, int paramInt) {
		// TODO Auto-generated method stub
		return null;
	}

}
