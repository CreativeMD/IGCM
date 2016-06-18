package com.creativemd.ingameconfigmanager.api.common.container;

import com.creativemd.creativecore.gui.container.SubContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerMods extends SubContainer{

	public SubContainerMods(EntityPlayer player) {
		super(player);
	}

	@Override
	public void createControls() {
		
	}

	@Override
	public void onPacketReceive(NBTTagCompound nbt) {
		
	}

}
