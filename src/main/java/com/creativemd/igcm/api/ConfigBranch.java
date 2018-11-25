package com.creativemd.igcm.api;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.igcm.api.segments.ValueSegment;
import com.creativemd.igcm.client.gui.SubGuiConfigSegement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ConfigBranch extends ConfigGroupElement {
	
	public ConfigBranch(String title, ItemStack avatar) {
		super(title, avatar);
	}
	
	@Override
	public void initCore() {
		createChildren();
		super.initCore();
	}
	
	public Object getValue(String key) {
		ConfigSegment segment = getChildByKey(key);
		if (segment instanceof ValueSegment)
			return ((ValueSegment) segment).value;
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void onGuiCreatesSegments(SubGuiConfigSegement gui, ArrayList<ConfigSegment> segments) {
	}
	
	@SideOnly(Side.CLIENT)
	public void onGuiLoadSegment(SubGuiConfigSegement gui, GuiScrollBox box, ArrayList<ConfigSegment> segments, ConfigSegment segment) {
	}
	
	@SideOnly(Side.CLIENT)
	public void onGuiLoadedAllSegments(SubGuiConfigSegement gui, GuiScrollBox box, ArrayList<ConfigSegment> segments) {
	}
	
	@SideOnly(Side.CLIENT)
	public void onGuiSavesSegments(SubGuiConfigSegement gui, ArrayList<ConfigSegment> segments) {
	}
	
	public abstract void createChildren();
	
	public abstract boolean requiresSynchronization();
	
	/**
	 * This method is called every time the client/server receives an update.
	 * Only use pre if you really need to change things before
	 */
	public void onRecieveFromPre(Side side) {
	}
	
	/**
	 * This method is called every time the client/server receives an update.
	 * Use this for normal purpose
	 */
	public abstract void onRecieveFrom(Side side);
	
	/**
	 * This method is called every time the client/server receives an update.
	 * Only use post if you really need to change things after
	 */
	public void onRecieveFromPost(Side side) {
	}
	
	/** Will be called before the update packets are send, only use if the branch does need custom config handling */
	public void onPacketSend(Side side) {
	}
	
	public void onBeforeReceived(Side side) {
	}
	
	public void onChildUpdated(Side side, ConfigBranch child) {
	}
	
	public void onUpdateSendToClient(EntityPlayer player) {
	}
	
	public boolean doesInputSupportStackSize() {
		return true;
	}
	
	public void updateJEI() {
	}
}
