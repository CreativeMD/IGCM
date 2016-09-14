package com.creativemd.igcm.api.common.container;

import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.premade.SubContainerEmpty;
import com.creativemd.igcm.api.common.branch.ConfigBranch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SubContainerBranch extends SubContainer{
	
	public ConfigBranch branch;
	
	public SubContainerBranch(EntityPlayer player, ConfigBranch branch) {
		super(player);
		this.branch = branch;
	}
	
	@Override
	public SubContainer createLayerFromPacket(World world, EntityPlayer player, NBTTagCompound nbt)
    {
		if(nbt.getBoolean("ItemDialog"))
			return new SubContainerEmpty(player);
		return super.createLayerFromPacket(world, player, nbt);
    }

	@Override
	public void createControls() {
		
	}

	@Override
	public void onPacketReceive(NBTTagCompound nbt) {
		
	}

}
