package com.creativemd.igcm.container;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.IGCMConfig;
import com.creativemd.igcm.IGCMGuiManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class SubContainerProfile extends SubContainer {
	
	public SubContainerProfile(EntityPlayer player) {
		super(player);
	}
	
	@Override
	public void createControls() {
		if (!player.world.isRemote) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("profile", IGCMConfig.profileName);
			NBTTagList list = new NBTTagList();
			for (int i = 0; i < IGCMConfig.profiles.size(); i++) {
				list.appendTag(new NBTTagString(IGCMConfig.profiles.get(i)));
			}
			nbt.setTag("profiles", list);
			sendNBTToGui(nbt);
		}
	}
	
	@Override
	public void onPacketReceive(NBTTagCompound nbt) {
		IGCMConfig.profileName = nbt.getString("profile");
		NBTTagList list = nbt.getTagList("profiles", 8);
		IGCMConfig.profiles = new ArrayList<>();
		for (int i = 0; i < list.tagCount(); i++) {
			IGCMConfig.profiles.add(list.getStringTagAt(i));
		}
		IGCMConfig.saveProfiles();
		IGCMConfig.loadConfig();
		IGCM.sendAllUpdatePackets();
		IGCMGuiManager.openConfigGui(getPlayer());
		IGCMConfig.saveConfig();
	}
	
}