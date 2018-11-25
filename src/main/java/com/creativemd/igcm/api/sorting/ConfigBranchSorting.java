package com.creativemd.igcm.api.sorting;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.creativecore.common.utils.type.SortingList;
import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.segments.BooleanSegment;
import com.creativemd.igcm.api.segments.advanced.InfoSegment;
import com.creativemd.igcm.client.gui.SubGuiConfigSegement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigBranchSorting extends ConfigBranch {
	
	public SortingList defaultList;
	
	public SortingList sortingList;
	
	public ConfigBranchSorting(String title, ItemStack avatar, SortingList defaultList) {
		super(title, avatar);
		this.defaultList = new SortingList(defaultList);
		sortingList = defaultList;
	}
	
	@Override
	public void createChildren() {
		registerElement("whitelist", new BooleanSegment("Whiltelist", defaultList.isWhitelist()));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiCreatesSegments(SubGuiConfigSegement gui, ArrayList<ConfigSegment> segments) {
		for (int i = 0; i < sortingList.size(); i++) {
			segments.add(new InfoSegment("Info", sortingList.get(i)));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoadedAllSegments(SubGuiConfigSegement gui, GuiScrollBox box, ArrayList<ConfigSegment> segments) {
		box.addControl(new GuiButton("add filter", 5, gui.height) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				box.controls.remove(this);
				segments.add(new InfoSegment("Segment", null));
			}
		});
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoadSegment(SubGuiConfigSegement gui, GuiScrollBox box, ArrayList<ConfigSegment> segments, ConfigSegment segment) {
		if (segment instanceof InfoSegment) {
			GuiButton button = new GuiButton("X", 200, gui.height, 14) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					gui.removeSegment(segment);
				}
			};
			
			segment.getGuiControls().add(button);
			box.addControl(button);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiSavesSegments(SubGuiConfigSegement gui, ArrayList<ConfigSegment> segments) {
		for (int i = 0; i < segments.size(); i++) {
			segments.get(i).saveFromControls();
		}
		
		sortingList.clear();
		
		for (int i = 0; i < segments.size(); i++) {
			ConfigSegment segment = segments.get(i);
			if (segment instanceof InfoSegment && ((InfoSegment) segment).value != null)
				sortingList.add(((InfoSegment) segment).value);
		}
	}
	
	@Override
	public boolean requiresSynchronization() {
		return true;
	}
	
	@Override
	public void onRecieveFrom(Side side) {
		sortingList.setListType((Boolean) getValue("whitelist"));
	}
	
	@Override
	public void loadExtra(NBTTagCompound nbt) {
		sortingList.clear();
		
		ArrayList<InfoStack> removed = new ArrayList<>();
		
		NBTTagList list = nbt.getTagList(getKey() + "removed", 10);
		for (int i = 0; i < list.tagCount(); i++) {
			InfoStack info = InfoStack.parseNBT(list.getCompoundTagAt(i));
			if (info != null)
				removed.add(info);
		}
		
		for (int i = 0; i < defaultList.size(); i++) {
			if (!removed.contains(defaultList.get(i)))
				sortingList.add(defaultList.get(i));
		}
		
		list = nbt.getTagList(getKey() + "added", 10);
		for (int i = 0; i < list.tagCount(); i++) {
			InfoStack info = InfoStack.parseNBT(list.getCompoundTagAt(i));
			if (info != null)
				sortingList.add(info);
		}
	}
	
	@Override
	public void saveExtra(NBTTagCompound nbt) {
		NBTTagList removed = new NBTTagList();
		for (int i = 0; i < defaultList.size(); i++) {
			if (!sortingList.contains(defaultList.get(i)))
				removed.appendTag(defaultList.get(i).writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag(getKey() + "removed", removed);
		
		NBTTagList added = new NBTTagList();
		for (int i = 0; i < sortingList.size(); i++) {
			if (!defaultList.contains(sortingList.get(i)))
				added.appendTag(sortingList.get(i).writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag(getKey() + "added", added);
	}
	
	@Override
	public boolean doesInputSupportStackSize() {
		return false;
	}
	
}
