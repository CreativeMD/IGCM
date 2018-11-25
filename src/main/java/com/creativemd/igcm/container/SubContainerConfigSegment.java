package com.creativemd.igcm.container;

import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.premade.SubContainerEmpty;
import com.creativemd.igcm.api.ConfigSegment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SubContainerConfigSegment extends SubContainer {
	
	public ConfigSegment element;
	
	public SubContainerConfigSegment(EntityPlayer player, ConfigSegment element) {
		super(player);
		this.element = element;
	}
	
	@Override
	public SubContainer createLayerFromPacket(World world, EntityPlayer player, NBTTagCompound nbt) {
		if (nbt.getBoolean("ItemDialog"))
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