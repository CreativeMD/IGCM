package com.creativemd.igcm.machines;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;
import com.creativemd.creativecore.common.recipe.BetterShapelessRecipe;
import com.creativemd.creativecore.common.recipe.RecipeLoader;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.controls.gui.GuiStateButton;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.machine.RecipeMachine;
import com.creativemd.igcm.api.segments.advanced.AddRecipeSegment;
import com.creativemd.igcm.jei.JEIHandler;
import com.google.common.collect.BiMap;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.plugins.vanilla.crafting.CraftingRecipeChecker;
import mezz.jei.plugins.vanilla.crafting.TippedArrowRecipeMaker;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.recipebook.RecipeBookPage;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;

public class WorkbenchMachine extends RecipeMachine<IRecipe> {
	
	ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) GameRegistry.findRegistry(IRecipe.class);
	
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
	
	private static Field owners = ReflectionHelper.findField(ForgeRegistry.class, "owners");
	private List<RecipeBookCache> cachesServer;
	@SideOnly(Side.CLIENT)
	private RecipeBookCache cacheClient;
	
	@SideOnly(Side.CLIENT)
	private static void addEmpty()
    {
		for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY) {
			RecipeList recipelist = new RecipeList();
	        RecipeBookClient.ALL_RECIPES.add(recipelist);
	        (RecipeBookClient.RECIPES_BY_TAB.computeIfAbsent(tab, (p_194083_0_) ->
	        {
	            return new ArrayList();
	        })).add(recipelist);
		}
    }
	
	@SideOnly(Side.CLIENT)
	public void receiveClient()
	{
		cacheClient.updateBook();
		cacheClient = null;
		FMLCommonHandler.instance().resetClientRecipeBook();
		addEmpty();
	}
	
	@Override
	public void onReceiveFrom(Side side) {
		super.onReceiveFrom(side);
		
		//Save new recipe to books
		if(side.isClient())
		{
			receiveClient();
		}
		else
		{
			for (RecipeBookCache cache : cachesServer)
				cache.updateBook();
		
			cachesServer = null;
		}
		
		registry.freeze();
			
	}
	
	@Override
	public void addRecipeToList(Side side, IRecipe recipe) {
		registry.register(recipe);
		
	}
	
	@SideOnly(Side.CLIENT)
	public void setupCacheClient()
	{
		cacheClient = new RecipeBookCache(Minecraft.getMinecraft().player);
	}

	@Override
	public void clearRecipeList(Side side) {
		//Save all recipe books (client and server)
		if(side.isClient())
			setupCacheClient();
		else
		{
			cachesServer = new ArrayList<>();
			for(EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
				cachesServer.add(new RecipeBookCache(player));
		}
		
		registry.unfreeze();
		registry.clear();
		try {
			((BiMap) owners.get(registry)).clear();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String recipeToString(IRecipe recipe)
	{
		return ((IRecipe) recipe).getRegistryName().toString();
	}

	@Override
	public ItemStack[] getOutput(IRecipe recipe) {
		return new ItemStack[]{recipe.getRecipeOutput()};
	}

	@Override
	public List<IRecipe> getAllExitingRecipes() {
		return registry.getValues();
	}
	
	public void getInput(ItemStack[] grid, Object[] items, int width, int height)
	{
		if(items == null)
			return ;
		
		for(int zahl = 0; zahl < width*height; zahl++)
		{
			int row = zahl/height;
			int index = row*3 + zahl - row*width;
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
						
			}else if(object instanceof Ingredient){
				return ((Ingredient) object).getMatchingStacks();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ItemStack[0];
	}
	
	
	
	@Override
	public void fillGrid(ItemStack[] grid, IRecipe recipe) {
		int[] size = RecipeLoader.getRecipeDimensions(recipe);
		getInput(grid, RecipeLoader.getInput(recipe), size[0], size[1]);
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
			IRecipe recipe = null;
			ItemStack result = output[0].copy();
			if(nbt.getBoolean("shaped"))
			{
				recipe = new BetterShapedRecipe(width, input, result);
			}else{
				ArrayList<InfoStack> info = new ArrayList<InfoStack>();
				for (int i = 0; i < input.length; i++) {
					if(input[i] != null)
						info.add(input[i]);
				}
				if(info.size() > 0)
					recipe = new BetterShapelessRecipe(info, result);
			}
			
			if(recipe != null)
			{
				if(nbt.hasKey("location"))
					recipe.setRegistryName(new ResourceLocation(nbt.getString("location")));
				return recipe;
			}
			
			
		}
		return null;
	}
	
	private boolean containsName(List<AddRecipeSegment> segments, ResourceLocation location)
	{
		for (int i = 0; i < segments.size(); i++)
			if(((IRecipe) segments.get(i).value).getRegistryName() != null && ((IRecipe) segments.get(i).value).getRegistryName().equals(location))
				return true;
			return false;
	}
	
	private ResourceLocation findNextName(List<AddRecipeSegment> segments)
	{
		int i = 0;
		ResourceLocation location = new ResourceLocation(IGCM.modid, "added" + i);
		while(containsName(segments, location))
		{
			i++;
			location = new ResourceLocation(IGCM.modid, "added" + i);
		}
		return location;
	}
	
	@Override
	public void onRecipeParsed(List<AddRecipeSegment> segments)
	{
		for (int i = 0; i < segments.size(); i++) {
			if(((IRecipe) segments.get(i).value).getRegistryName() == null)
				((IRecipe) segments.get(i).value).setRegistryName(findNextName(segments));
		}
	}
	
	@Override
	public void writeExtraInfo(IRecipe recipe, NBTTagCompound nbt)
	{
		nbt.setString("location", recipe.getRegistryName().toString());
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
	
	@Override
	public String getJEICategory() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}
	
	@Override
	@Method(modid = "jei")
	public List getJEIRecipes() {
		List list = new ArrayList<>();
		list.addAll(CraftingRecipeChecker.getValidRecipes(((IModRegistry) JEIHandler.modRegistry).getJeiHelpers()));
		list.addAll(TippedArrowRecipeMaker.getTippedArrowRecipes());
		return list;
	}
	
	public static final Field recipePlayer = ReflectionHelper.findField(RecipeBook.class, "recipes", "field_194077_a");
	public static final Field newRecipePlayer = ReflectionHelper.findField(RecipeBook.class, "newRecipes", "field_194078_b");
	
	public class RecipeBookCache {
		
		public List<IRecipe> recipes = new ArrayList<>();
	    public List<IRecipe> newRecipes = new ArrayList<>();
	    
	    public EntityPlayer player;
	    
	    public RecipeBookCache(EntityPlayer player)
	    {
	    	this.player = player;
	    	try {
	    		RecipeBook book;
	    		if(player instanceof EntityPlayerSP)
	    			book = ((EntityPlayerSP) player).getRecipeBook();
	    		else
	    			book = ((EntityPlayerMP) player).getRecipeBook();
	    		
	    		List<IRecipe> toDelete = new ArrayList<>();
	    		BitSet recipesBook = (BitSet) recipePlayer.get(book);
	    		for(int i = 0; i < recipesBook.size(); i++)
	    			if(recipesBook.get(i))
	    			{
	    				IRecipe recipe = registry.getValue(i);
	    				recipes.add(recipe);
	    				toDelete.add(recipe);
	    			}
	    		
	    		BitSet newRecipesBook = (BitSet) newRecipePlayer.get(book);
	    		for(int i = 0; i < newRecipesBook.size(); i++)
	    			if(newRecipesBook.get(i))
	    				newRecipes.add(registry.getValue(i));
	    		
	    		player.resetRecipes(toDelete);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
	    	
	    }
	    
	    public void updateBook()
	    {
	    	RecipeBook book;
    		if(player instanceof EntityPlayerSP)
    			book = ((EntityPlayerSP) player).getRecipeBook();
    		else
    			book = ((EntityPlayerMP) player).getRecipeBook();
    		
    		try {
	    		((BitSet) recipePlayer.get(book)).clear();
	    		((BitSet) newRecipePlayer.get(book)).clear();
    		} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
    		
    		//player.resetRecipes(p_192022_1_);
    		
    		for (IRecipe recipe : recipes) {
    			if(CraftingManager.REGISTRY.getIDForObject(recipe) != -1)
    				book.unlock(recipe);
			}
    		
    		for (IRecipe recipe : newRecipes) {
    			if(CraftingManager.REGISTRY.getIDForObject(recipe) != -1)
    				book.markNew(recipe);
			}
	    }
	}
}
