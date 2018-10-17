package com.creativemd.igcm.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class ConfigElement<T extends ConfigElement> {
	
	public String title;
	
	public ConfigElement(String title) {
		this.title = title;
		this.key = title;
	}
	
	public ConfigElement parent;
	String key;
	
	protected LinkedHashMap<String, T> childs = new LinkedHashMap<>();
	
	public T registerElement(String key, T element) {
		int index = 1;
		String tempKey = key;
		while (childs.containsKey(tempKey)) {
			index++;
			tempKey = key + index;
		}
		childs.put(tempKey, element);
		element.parent = this;
		element.key = tempKey;
		return element;
	}
	
	public void initDefault() {
		for (Iterator<T> iterator = childs.values().iterator(); iterator.hasNext();) {
			iterator.next().initDefault();
			;
		}
	}
	
	public void initCore() {
		for (Iterator<T> iterator = childs.values().iterator(); iterator.hasNext();) {
			iterator.next().initCore();
			;
		}
	}
	
	public String getKey() {
		return key;
	}
	
	public String getPath() {
		if (parent == null)
			return title;
		return parent.getPath() + "." + key;
	}
	
	public Collection<T> getChilds() {
		return childs.values();
	}
	
	public Set<String> getChildKeys() {
		return childs.keySet();
	}
	
	public T getChildByKey(String key) {
		return childs.get(key);
	}
	
	public void clearChilds() {
		childs.clear();
	}
	
}
