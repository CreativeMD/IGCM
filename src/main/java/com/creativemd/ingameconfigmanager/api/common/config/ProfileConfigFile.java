package com.creativemd.ingameconfigmanager.api.common.config;

import net.minecraft.entity.player.EntityPlayerMP;

import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;
import com.n247s.N2ConfigApi.api.core.ConfigFile;
import com.n247s.N2ConfigApi.api.core.ConfigHandler.ProxySide;
import com.n247s.N2ConfigApi.api.core.ConfigSectionCollection;

import cpw.mods.fml.common.FMLCommonHandler;

public class ProfileConfigFile extends ConfigFile
{
	private static final String[] description = new String[] {	" ",
																"This configFile is a profile ConfigFile of the IngameConfigManager.",
																"Profile name: %s.",
																" ",
																"Do not change this file, or things might go Horribly wrong!",
																" "};
	private static final String sectionOutline = "--------------------------------------------------------------------------------------------------------------";

	public ProfileConfigFile(String fileName)
	{
		super(fileName, ProxySide.Server);// ServerSide to disable Synchronizing.
		this.setCustomSectionStarter(null);
		this.setCustomSectionHeadEnder(sectionOutline);
		description[2] = String.format(description[2], fileName.substring(0, fileName.length() - 7));
		this.setDescription(description);
		this.setCustomSectionEnder(sectionOutline);
	}

	@Override
	public void generateFile()
	{
		this.clearAllConfigSections();
		
		for(ConfigBranch currentBranch: ConfigBranch.branches)
		{
			ConfigSectionCollection collection = new ConfigSectionCollection(InGameConfigManager.getCat(currentBranch), null, true);
			for(ConfigSegment currentSegment : currentBranch.getConfigSegments())
			{
				String input = currentSegment.createPacketInformation(FMLCommonHandler.instance().getEffectiveSide().isServer());
				collection.addNewStringSection(currentSegment.getID().toLowerCase(), null, input, true);
			}
			this.addNewSection(collection);
		}
	}

	@Override
	public boolean getPermission(EntityPlayerMP player)
	{
		return false;//No synchronizing from any player is allowed!
	}

}
