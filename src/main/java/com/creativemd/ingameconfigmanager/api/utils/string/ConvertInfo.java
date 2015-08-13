package com.creativemd.ingameconfigmanager.api.utils.string;

import net.minecraft.nbt.NBTTagCompound;

import com.creativemd.ingameconfigmanager.api.core.ItemStackInfo;

public class ConvertInfo extends StringConverter{

	public ConvertInfo() {
		super("ItemInfo");
	}

	@Override
	public Class getClassOfObject() {
		return ItemStackInfo.class;
	}

	@Override
	public String toString(Object object) {
		ItemStackInfo info = (ItemStackInfo) object;
		if(info.nbt != null)
			return StringUtils.ObjectsToString(info.damage, info.item, info.ore, info.nbt);
		else
			return StringUtils.ObjectsToString(info.damage, info.item, info.ore, "");
	}

	@Override
	public Object parseObject(String input) {
		Object[] objects = StringUtils.StringToObjects(input);
		if(objects.length == 4 && objects[0] instanceof Integer && objects[1] instanceof String && objects[2] instanceof String && (objects[3] instanceof NBTTagCompound || objects[3] instanceof String))
		{
			if(objects[3] instanceof NBTTagCompound)
				return new ItemStackInfo((String)objects[1], (String)objects[2], (Integer)objects[0], (NBTTagCompound)objects[3]);
			else
				return new ItemStackInfo((String)objects[1], (String)objects[2], (Integer)objects[0], null);
		}
		return null;
	}

	@Override
	public String[] getSplitter() {
		return new String[0];
	}

}
