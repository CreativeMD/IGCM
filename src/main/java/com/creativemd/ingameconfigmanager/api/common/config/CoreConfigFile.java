package com.creativemd.ingameconfigmanager.api.common.config;

import net.minecraft.entity.player.EntityPlayerMP;

import com.n247s.N2ConfigApi.api.core.ConfigFile;
import com.n247s.N2ConfigApi.api.core.ConfigHandler.ProxySide;
import com.n247s.N2ConfigApi.api.core.ConfigSectionCollection;

public class CoreConfigFile extends ConfigFile
{
	private static final String[] description = new String[] {	" ",
																"This configFile is the core ConfigFile of the IngameConfigManager.",
																" ",
																"Do not change this file, or things might go Horribly wrong!",
																" "};
	private static final String sectionOutline = "--------------------------------------------------------------------------------------------------------------";
	
	public CoreConfigFile(String fileName)
	{
		super(fileName, ProxySide.Server);// Set to server since its a serverSide configuration.
		this.setCustomSectionStarter(null);
		this.setCustomSectionHeadEnder(sectionOutline);
		this.setDescription(description);
		this.setCustomSectionEnder(sectionOutline);
	}

	@Override
	public void generateFile()
	{
		this.clearAllConfigSections();//clears everything if something is here!
		
		ConfigSectionCollection sectionCollection = new ConfigSectionCollection("General", null, true);
		sectionCollection.addNewStringSection("profileName", null, "new1", true);
		sectionCollection.addNewStringArraySection("profiles", null, new String[0], true, false);
		this.addNewSection(sectionCollection);
	}
	
	public String getProfileName()
	{
		return (String) this.getValue("profileName");
	}
	
	public String[] getProfiles()
	{
		return (String[]) this.getValue("profiles");
	}

	@Override
	public boolean getPermission(EntityPlayerMP player)
	{
		return false;//No synchronizing from any player is allowed!
	}

}
