package com.creativemd.ingameconfigmanager.api.utils.recipe;

import java.util.ArrayList;
import java.util.HashMap;

import com.creativemd.ingameconfigmanager.api.core.ItemStackInfo;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BetterShapedRecipe extends ShapedOreRecipe{

	public ItemStackInfo[] itemData;
	public int recipeWidth;
	public int recipeHeight;
	public boolean mirrored;
	
	public BetterShapedRecipe(ItemStack par4ItemStack, Object[] itemInfo, Object ... par3ArrayOfItemStack) {
		super(par4ItemStack, par3ArrayOfItemStack);
		int height = 0;
		int width = 0;
		String shape = "";
        int idx = 0;
        mirrored = true;
        if (itemInfo[idx] instanceof Boolean)
        {
        	mirrored = (Boolean)itemInfo[idx];
            if (itemInfo[idx+1] instanceof Object[])
            {
            	itemInfo = (Object[])itemInfo[idx+1];
            }
            else
            {
                idx = 1;
            }
        }

        if (itemInfo[idx] instanceof String[])
        {
            String[] parts = ((String[])itemInfo[idx++]);

            for (String s : parts)
            {
                width = s.length();
                shape += s;
            }

            height = parts.length;
        }
        else
        {
            while (itemInfo[idx] instanceof String)
            {
                String s = (String)itemInfo[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

        for (; idx < itemInfo.length; idx += 2)
        {
            Character chr = (Character)itemInfo[idx];
            Object in = itemInfo[idx + 1];
            if (in instanceof ItemStackInfo)
            {
                itemMap.put(chr, in);
            }
            else
            {
                String ret = "Invalid shaped ore recipe: ";
                throw new RuntimeException(ret);
            }
        }

        itemData = new ItemStackInfo[width * height];
        int x = 0;
        for (char chr : shape.toCharArray())
        {
        	itemData[x++] = (ItemStackInfo) itemMap.get(chr);
        }
        this.recipeHeight = height;
        this.recipeWidth = width;
    }
	
	public boolean checkItemEquals(ItemStack target, ItemStack input, ItemStackInfo info)
    {
        if (input == null && target != null || input != null && target == null)
        {
            return false;
        }
        if(info != null && !info.ore.equals(""))
        {
        	int[] id = OreDictionary.getOreIDs(target);
        	boolean found = false;
        	for (int i = 0; i < id.length; i++) {
				if(OreDictionary.getOreName(id[i]).equals(info.ore))
					found = true;
			}
        	return found;
        }
        if(target.getItem() != input.getItem())
        	return false;
        if(target.getItemDamage() != OreDictionary.WILDCARD_VALUE && target.getItemDamage() != input.getItemDamage() && info.needDamage())
        	return false;
        if(target.stackTagCompound != null && !target.stackTagCompound.equals(input.stackTagCompound) && info.needNBT())
        	return false;
        return true;
    }
	
	@Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        for (int x = 0; x <= 3 - recipeWidth; x++)
        {
            for (int y = 0; y <= 3 - recipeHeight; ++y)
            {
                if (checkMatch(inv, x, y, false))
                {
                    return true;
                }

                if (mirrored && checkMatch(inv, x, y, true))
                {
                    return true;
                }
            }
        }

        return false;
    }
	
	private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;
                ItemStackInfo info = null;

                if (subX >= 0 && subY >= 0 && subX < recipeWidth && subY < recipeHeight)
                {
                    if (mirror)
                    {
                        target = getInput()[recipeWidth - subX - 1 + subY * recipeWidth];
                        info = itemData[recipeWidth - subX - 1 + subY * recipeWidth];
                    }
                    else
                    {
                        target = getInput()[subX + subY * recipeWidth];
                        info = itemData[subX + subY * recipeWidth];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack)
                {
                    if (!checkItemEquals((ItemStack)target, slot, info))
                    {
                        return false;
                    }
                }
                else if (target instanceof ArrayList)
                {
                    boolean matched = false;

                    for (ItemStack item : (ArrayList<ItemStack>)target)
                    {
                        matched = matched || checkItemEquals(item, slot, info);
                    }

                    if (!matched)
                    {
                        return false;
                    }
                }
                else if (target == null && slot != null)
                {
                    return false;
                }
            }
        }

        return true;
    }
	
}
