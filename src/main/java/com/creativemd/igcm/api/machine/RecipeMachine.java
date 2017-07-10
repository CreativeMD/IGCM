package com.creativemd.igcm.api.machine;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.igcm.api.ConfigElement;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.api.segments.advanced.AddRecipeSegment;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**A machine/worbench/table. It is something which has inputs and has an output**/
public abstract class RecipeMachine<T>{
	
	public boolean sendingUpdate;
	public String id;
	public ConfigMachineDisableBranch disableBranch;
	public ConfigMachineAddBranch addBranch;
	public ConfigMachineBranch mainBranch;
	public ItemStack avatar;
	
	public RecipeMachine(String id, String title, ItemStack avatar)
	{
		this(ConfigTab.root, id, title, avatar);
	}
	
	public RecipeMachine(ConfigElement parent, String id, String title, ItemStack avatar)
	{
		super();
		this.id = id;
		this.avatar = avatar;
		mainBranch = new ConfigMachineBranch(title, avatar, this);
		parent.registerElement(id, mainBranch);
		
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
	@SideOnly(Side.CLIENT)
	public void onControlCreated(T recipe, boolean isAdded, int x, int y, int maxWidth, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls) {}
	
	/**Don't forget to set the offset of the segments**/
	public ArrayList<ConfigSegment> getCustomSegments()
	{
		return new ArrayList<ConfigSegment>();
	}
	
	public void createExtraSegments() {}
	
	public void onReceiveFrom(Side side) {}
	
	//==================Disabled Recipes only==================
	
	public abstract List<T> getAllExitingRecipes();
	
	public abstract void fillGrid(ItemStack[] grid, T recipe);
	
	//==================Added Recipes only==================
	
	public abstract boolean doesSupportStackSize();
	
	public abstract void fillGridInfo(InfoStack[] grid, T recipe);
	
	/**Save extra information into the nbt tag**/
	public void writeExtraInfo(T recipe, NBTTagCompound nbt) {}
	
	/**Parse a recipe having input, output and your nbt tag**/
	public abstract T parseRecipe(InfoStack[] input, ItemStack[] output, NBTTagCompound nbt, int width, int height);
	
	/**Parse extra information into the nbt tag**/
	@SideOnly(Side.CLIENT)
	public void parseExtraInfo(NBTTagCompound nbt, AddRecipeSegment segment, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls) {}
	
	
	//==================JEI==================
	
	public abstract boolean hasJEISupport();
	
	public ArrayList lastAdded;
	
	public void updateJEI() {
		/*if(hasJEISupport())
		{
			if(hasDisableBranch())
				JEIHandler.removeRecipes(disableBranch.allRecipes);
			
			if(lastAdded != null)
				JEIHandler.removeRecipes(lastAdded);
			
			lastAdded = new ArrayList<>();
			for (int i = 0; i < addBranch.recipes.size(); i++) {
				if(addBranch.recipes.get(i).value != null)
					lastAdded.add(addBranch.recipes.get(i).value);
			}
			JEIHandler.addRecipes(getAllExitingRecipes());
		}*/
	}
	
}
