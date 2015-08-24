package com.creativemd.ingameconfigmanager.api.common.container;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.gui.controls.GuiAvatarButton;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;

public class SubContainerBranch extends SubContainer{
	
	public ConfigBranch branch;
	
	public SubContainerBranch(EntityPlayer player, ConfigBranch branch) {
		super(player);
		this.branch = branch;
	}

	@Override
	public void createControls() {
		
	}

	@Override
	public void onGuiPacket(int controlID, NBTTagCompound nbt,
			EntityPlayer player) {
		
	}

}
