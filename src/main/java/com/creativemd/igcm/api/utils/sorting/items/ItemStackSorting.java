package com.creativemd.igcm.api.utils.sorting.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackSorting extends ItemSorting {
	
	public int damage;
	
	public boolean NeedNBT;
	public NBTTagCompound nbt;
	
	public ItemStackSorting(ItemStack stack) {
		this(stack, false);
	}
	
	public ItemStackSorting(ItemStack stack, boolean NeedNBT) {
		super(stack.getItem());
		this.damage = stack.getItemDamage();
		this.NeedNBT = NeedNBT;
		this.nbt = stack.getTagCompound();
	}

	@Override
	protected boolean isObject(ItemStack stack) {
		if(super.isObject(stack))
		{
			if(stack.getItemDamage() == damage)
				if(NeedNBT)
				{
					if(nbt == null)
						return stack.getTagCompound() == nbt;
					else if(stack.hasTagCompound())
						return nbt.equals(stack.getTagCompound());
				}else
					return true;
		}
		return false;
	}

}
