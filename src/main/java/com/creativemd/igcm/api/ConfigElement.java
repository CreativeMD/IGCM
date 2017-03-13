package com.creativemd.igcm.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ConfigElement<T extends ConfigElement> {
	
	public String title;
	
	public ConfigElement(String title) {
		this.title = title;
		this.key = title;
	}
	
	public ConfigElement parent;
	String key;
	
	protected HashMap<String, T> childs = new HashMap<>();
	
	public T registerElement(String key, T element)
	{
		if(childs.containsKey(key))
			return null;//throw new IllegalArgumentException("Key '" + key + "' is already taken!");
		childs.put(key, element);
		element.parent = this;
		element.key = key;
		return element;
	}
	
	public void initDefault()
	{
		for (Iterator<T> iterator = childs.values().iterator(); iterator.hasNext();) {
			iterator.next().initDefault();;
		}
	}
	
	public void initCore()
	{
		for (Iterator<T> iterator = childs.values().iterator(); iterator.hasNext();) {
			iterator.next().initCore();;
		}
	}
	
	public String getKey()
	{
		return key;
	}
	
	public String getPath()
	{
		if(parent == null)
			return title;
		return parent.getPath() + "." + key;
	}
	
	public Collection<T> getChilds()
	{
		return childs.values();
	}
	
	public Set<String> getChildKeys()
	{
		return childs.keySet();
	}
	
	public T getChildByKey(String key)
	{
		return childs.get(key);
	}
	
	public void clearChilds()
	{
		childs.clear();
	}
	
}
