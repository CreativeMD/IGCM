package com.creativemd.ingameconfigmanager.api.utils.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.creativemd.ingameconfigmanager.api.core.ItemStackInfo;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BetterShapelessRecipe extends ShapelessOreRecipe {

	public ArrayList<ItemStackInfo> itemData = new ArrayList<ItemStackInfo>();
	
	public BetterShapelessRecipe(ItemStack par1ItemStack, ArrayList itemData, Object... par2List) {
		super(par1ItemStack, par2List);
		this.itemData = itemData;
	}
	
	@Override
    public boolean matches(InventoryCrafting var1, World world)
    {
        ArrayList required = new ArrayList(getInput());

        for (int x = 0; x < var1.getSizeInventory(); x++)
        {
            ItemStack slot = var1.getStackInSlot(x);

            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator req = required.iterator();
                int zahl = 0;
                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();
                    ItemStackInfo data = itemData.get(zahl);
                    if (next instanceof ItemStack)
                    {
                        match = checkItemEquals((ItemStack)next, slot, data);
                    }
                    else if (next instanceof ArrayList)
                    {
                        for (ItemStack item : (ArrayList<ItemStack>)next)
                        {
                            match = match || checkItemEquals(item, slot, data);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                    zahl++;
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

	public boolean checkItemEquals(ItemStack target, ItemStack input, ItemStackInfo info)
    {
        if (input == null && target != null || input != null && target == null)
        {
            return false;
        }
        if(target.getItem() != input.getItem())
        	return false;
        if(target.getItemDamage() != OreDictionary.WILDCARD_VALUE && target.getItemDamage() != input.getItemDamage() && info.needDamage())
        	return false;
        if(target.stackTagCompound != null && !target.stackTagCompound.equals(input.stackTagCompound) && info.needNBT())
        	return false;
        return true;
    }
}
