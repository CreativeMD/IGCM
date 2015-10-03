package com.creativemd.ingameconfigmanager.mod.general;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.segment.BooleanSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.IntegerSegment;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GeneralBranch extends ConfigBranch{

	public GeneralBranch(String name) {
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected Avatar getAvatar() {
		return new AvatarItemStack(new ItemStack(Items.paper));
	}

	@Override
	public void loadCore() {
		
	}

	@Override
	public void createConfigSegments() {
		segments.add(new BooleanSegment("overrideWorkbench", "override default workbench", false));
		segments.add(new IntegerSegment("maxSegments", "segments per packet", 10, 1, 1000));
	}

	@Override
	public boolean needPacket() {
		return true;
	}

	@Override
	public void onRecieveFrom(boolean isServer, ConfigSegmentCollection collection) {
		InGameConfigManager.overrideWorkbench = (Boolean)collection.getSegmentValue("overrideWorkbench");
		InGameConfigManager.maxSegments = (Integer)collection.getSegmentValue("maxSegments");
	}

}
