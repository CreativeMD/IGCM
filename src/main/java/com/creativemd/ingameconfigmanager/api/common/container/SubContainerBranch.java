package com.creativemd.ingameconfigmanager.api.common.container;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.gui.controls.GuiAvatarButton;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.creativecore.common.gui.premade.SubContainerDialog;
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
	public void onGuiPacket(int controlID, NBTTagCompound nbt, EntityPlayer player) {
		
	}
	
	@Override
	public SubContainer createLayerFromPacket(World world, EntityPlayer player, NBTTagCompound nbt)
    {
		if(nbt.getBoolean("ItemDialog"))
			return new SubContainerDialog(player);
		return super.createLayerFromPacket(world, player, nbt);
    }

}
