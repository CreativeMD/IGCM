package com.creativemd.ingameconfigmanager.mod.general;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.segment.BooleanSegment;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GeneralBranch extends ConfigBranch{

	public GeneralBranch(String name) {
		super(name);
	}

	@Override
	protected Avatar getAvatar() {
		return new AvatarItemStack(new ItemStack(Items.paper));
	}

	@Override
	public void loadCore() {
		
	}

	@Override
	public void createConfigSegments() {
		segments.add(new BooleanSegment("overrideWorkbench", "Override Default Workbench", false));
	}

	@Override
	public boolean needPacket() {
		return true;
	}

	@Override
	public void onRecieveFrom(boolean isServer, ConfigSegmentCollection collection) {
		InGameConfigManager.overrideWorkbench = (Boolean)collection.getSegmentValue("overrideWorkbench");
	}

}
