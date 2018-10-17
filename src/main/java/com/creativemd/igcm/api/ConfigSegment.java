package com.creativemd.igcm.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ConfigSegment extends ConfigElement<ConfigSegment> {
	
	public ConfigSegment(String title) {
		super(title);
	}
	
	public int offsetX;
	public int offsetY;
	
	public ConfigSegment setOffset(int x, int y) {
		this.offsetX = x;
		this.offsetY = y;
		return this;
	}
	
	public void load(NBTTagCompound nbt) {
		if (nbt.hasKey("childs_" + getKey()))
			loadElements(childs, nbt.getCompoundTag("childs_" + getKey()));
		else
			loadElements(childs, new NBTTagCompound());
		loadExtra(nbt);
	}
	
	public abstract void loadExtra(NBTTagCompound nbt);
	
	public abstract void saveExtra(NBTTagCompound nbt);
	
	public void save(NBTTagCompound nbt) {
		if (!childs.isEmpty()) {
			NBTTagCompound childData = saveElements(childs, new NBTTagCompound());
			if (!childData.hasNoTags())
				nbt.setTag("childs_" + getKey(), childData);
		} else if (nbt.hasKey("childs_" + getKey()))
			nbt.removeTag("childs_" + getKey());
		
		saveExtra(nbt);
	}
	
	private ArrayList<ContainerControl> containerControls;
	
	public ArrayList<ContainerControl> getContainerControls() {
		return containerControls;
	}
	
	public void setContainerControls(ArrayList<ContainerControl> controls) {
		this.containerControls = controls;
	}
	
	public ContainerControl getContainerControl(String id) {
		ArrayList<ContainerControl> controls = getContainerControls();
		for (int i = 0; i < controls.size(); i++) {
			if (controls.get(i).is(id))
				return controls.get(i);
		}
		return null;
	}
	
	public ArrayList<ContainerControl> createContainerControls(SubContainer container, int x, int y, int maxWidth) {
		ArrayList<ContainerControl> controls = new ArrayList<>();
		for (Iterator<Entry<String, ConfigSegment>> iterator = childs.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, ConfigSegment> type = iterator.next();
			ArrayList<ContainerControl> subControls = type.getValue().createContainerControls(container, x + type.getValue().offsetX, y + type.getValue().offsetY, maxWidth);
			type.getValue().containerControls = subControls;
			controls.addAll(subControls);
		}
		return controls;
	}
	
	@SideOnly(Side.CLIENT)
	private ArrayList<GuiControl> guiControls;
	
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = new ArrayList<>();
		for (Iterator<Entry<String, ConfigSegment>> iterator = childs.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, ConfigSegment> type = iterator.next();
			ArrayList<GuiControl> subControls = type.getValue().createGuiControls(gui, x + type.getValue().offsetX, y + type.getValue().offsetY, maxWidth);
			type.getValue().guiControls = subControls;
			controls.addAll(subControls);
		}
		return controls;
	}
	
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> getGuiControls() {
		return guiControls;
	}
	
	@SideOnly(Side.CLIENT)
	public void setGuiControls(ArrayList<GuiControl> controls) {
		this.guiControls = controls;
	}
	
	@SideOnly(Side.CLIENT)
	public GuiControl getGuiControl(String id) {
		ArrayList<GuiControl> controls = getGuiControls();
		if (controls == null)
			return null;
		for (int i = 0; i < controls.size(); i++) {
			if (controls.get(i).is(id))
				return controls.get(i);
		}
		return null;
	}
	
	public int lastX;
	public int lastY;
	
	public void onSegmentLoaded(int x, int y, int maxWidth) {
		this.lastX = x;
		this.lastY = y;
	}
	
	@SideOnly(Side.CLIENT)
	public int getHeight() {
		int maxHeight = 0;
		for (int i = 0; i < guiControls.size(); i++) {
			maxHeight = Math.max(maxHeight, guiControls.get(i).height + guiControls.get(i).posY - lastY);
		}
		return maxHeight;
	}
	
	public ArrayList<ConfigSegment> getAllSegments() {
		ArrayList<ConfigSegment> segments = new ArrayList<ConfigSegment>();
		segments.add(this);
		for (Iterator<ConfigSegment> iterator = childs.values().iterator(); iterator.hasNext();) {
			ConfigSegment type = iterator.next();
			segments.addAll(type.getAllSegments());
		}
		return segments;
	}
	
	public static void loadElements(HashMap<String, ConfigSegment> segments, NBTTagCompound nbt) {
		for (Iterator iterator = segments.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, ConfigSegment> type = (Entry<String, ConfigSegment>) iterator.next();
			type.getValue().load(nbt);
		}
	}
	
	public static NBTTagCompound saveElements(HashMap<String, ConfigSegment> segments, NBTTagCompound nbt) {
		for (Iterator iterator = segments.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, ConfigSegment> type = (Entry<String, ConfigSegment>) iterator.next();
			type.getValue().save(nbt);
		}
		
		return nbt;
	}
	
	protected abstract boolean contains(String search);
	
	public boolean canBeFound(String search) {
		if (contains(search))
			return true;
		for (Iterator<ConfigSegment> iterator = childs.values().iterator(); iterator.hasNext();) {
			ConfigSegment type = iterator.next();
			if (type.canBeFound(search))
				return true;
		}
		return false;
	}
	
	public void saveFromControls() {
		for (Iterator<ConfigSegment> iterator = childs.values().iterator(); iterator.hasNext();) {
			ConfigSegment type = iterator.next();
			type.saveFromControls();
		}
	}
	
	public ConfigBranch getParentBranch() {
		if (this instanceof ConfigBranch)
			return (ConfigBranch) this;
		else if (parent instanceof ConfigSegment)
			return ((ConfigSegment) parent).getParentBranch();
		return null;
	}
	
}
