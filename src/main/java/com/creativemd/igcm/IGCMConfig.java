package com.creativemd.igcm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.jei.JEIHandler;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class IGCMConfig {
	
	public static Configuration coreConfig;
	public static File currentFile;
	public static File ModConfigurationDirectory;
	
	public static String profileName;
	public static ArrayList<String> profiles;
	
	public static void initConfig(FMLPreInitializationEvent event)
	{
		coreConfig = new Configuration(event.getSuggestedConfigurationFile());
		coreConfig.load();
		String[] profile = coreConfig.get("General", "profiles", new String[0]).getStringList();
		profileName = coreConfig.get("General", "profileName", "new1").getString();
		if(profileName.equals(""))
			profileName = "new1";
		
		profiles = new ArrayList<String>(Arrays.asList(profile));
		if(!profiles.contains(profileName))
			profiles.add(profileName);
		
		coreConfig.save();
		ModConfigurationDirectory = event.getModConfigurationDirectory();
	}
	
	/**Used for loading configs on startup and changing profile**/
	public static void loadConfig()
	{
		currentFile = new File(ModConfigurationDirectory, "IGCM" + File.separator + profileName + ".nbt");
		try {
			NBTTagCompound nbt = CompressedStreamTools.read(currentFile);
			if(nbt == null)
				ConfigTab.root.initDefault();
			else
				ConfigTab.root.load(nbt);
			
			ArrayList<ConfigSegment> allSegments = ConfigTab.root.getAllSegments();
			for (int i = 0; i < allSegments.size(); i++) {
				if(allSegments.get(i) instanceof ConfigBranch)
				{
					ConfigBranch branch = (ConfigBranch) allSegments.get(i);
					
					branch.onRecieveFromPre(Side.SERVER);
					branch.onRecieveFrom(Side.SERVER);
					branch.onRecieveFromPost(Side.SERVER);
					
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void saveConfig()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		ConfigTab.root.save(nbt);
		
		try {
			currentFile.getParentFile().mkdirs();
			CompressedStreamTools.write(nbt, currentFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveProfiles()
	{
		coreConfig.load();
		coreConfig.get("General", "profileName", "new1").set(profileName);
		coreConfig.get("General", "profiles", new String[0]).set(profiles.toArray(new String[0]));
		coreConfig.save();
	}
}
