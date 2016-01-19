package com.creativemd.ingameconfigmanager.api.core;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creativemd.creativecore.common.gui.GuiHandler;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.core.CreativeCore;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.command.CommandGUI;
import com.creativemd.ingameconfigmanager.api.common.config.CoreConfigFile;
import com.creativemd.ingameconfigmanager.api.common.config.ProfileConfigFile;
import com.creativemd.ingameconfigmanager.api.common.event.ConfigEventHandler;
import com.creativemd.ingameconfigmanager.api.common.gui.InGameGuiHandler;
import com.creativemd.ingameconfigmanager.api.common.packets.BranchInformationPacket;
import com.creativemd.ingameconfigmanager.api.common.packets.CraftResultPacket;
import com.creativemd.ingameconfigmanager.api.common.packets.RequestInformationPacket;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.nei.NEIAdvancedRecipeHandler;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.creativemd.ingameconfigmanager.api.tab.SubTab;
import com.creativemd.ingameconfigmanager.mod.ConfigManagerModLoader;
import com.creativemd.ingameconfigmanager.mod.block.BlockAdvancedWorkbench;
import com.creativemd.ingameconfigmanager.mod.general.GeneralBranch;
import com.n247s.N2ConfigApi.api.N2ConfigApi;
import com.n247s.N2ConfigApi.api.core.ConfigFile;
import com.n247s.N2ConfigApi.api.core.ConfigHandler;
import com.n247s.N2ConfigApi.api.core.ConfigSection;
import com.n247s.N2ConfigApi.api.core.ConfigSectionCollection;
import com.n247s.N2ConfigApi.api.core.DefaultConfigFile;
import com.n247s.N2ConfigApi.api.core.InitConfigObjectManager;
import com.n247s.N2ConfigApi.api.core.InitConfigObjectManager.Config;
import com.n247s.N2ConfigApi.api.core.parser.ConfigParser;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = InGameConfigManager.modid, version = InGameConfigManager.version, name = "InGameConfigManager")
public class InGameConfigManager { 
	
	public static Logger logger = LogManager.getLogger(InGameConfigManager.modid);
	
	public static final String modid = "ingameconfigmanager";
	public static final String version = "0.1";
	
	public static ConfigEventHandler eventHandler = new ConfigEventHandler();
	
	public static int maxSegments = 10;
	
//	public static Configuration coreConfig;//ReplacementConfigFile
	public static CoreConfigFile coreConfigFile; //	public static Configuration currentProfile;//ReplacementConfigFile
	public static ProfileConfigFile currentProfileConfig;
	public static String ModProfileConfigurationDirectoryID = modid + "_profileConfig";
	
	public static String profileName;
	public static ArrayList<String> profiles;
	
	public static boolean overrideWorkbench = false;
	public static Block advancedWorkbench = new BlockAdvancedWorkbench().setBlockName("advancedWorkbench").setCreativeTab(CreativeTabs.tabDecorations);
	
	
	
	/**Used for loading configs on startup and changing profile **/
	public static void loadConfig()
	{
		if(!currentProfileConfig.getFileName().substring(0, currentProfileConfig.getFileName().length() - 7).equals(profileName))
		{
			currentProfileConfig = new ProfileConfigFile(profileName + "_Config");
			parseOrLoadConfigFile(new File(N2ConfigApi.getFileDirFromID(ModProfileConfigurationDirectoryID), profileName + ".cfg"), currentProfileConfig, ModProfileConfigurationDirectoryID);
		}
		
		for(ConfigBranch currentBranche : ConfigBranch.branches)
		{
			boolean isServer = FMLCommonHandler.instance().getEffectiveSide().isServer();
			ArrayList<ConfigSegment> segments = currentBranche.getConfigSegments();
			currentBranche.onBeforeReceived(FMLCommonHandler.instance().getEffectiveSide().isServer());
			ConfigSegmentCollection collection = new ConfigSegmentCollection(segments);
			ConfigSectionCollection currentSectionCollection = ((ConfigSectionCollection)currentProfileConfig.getSubSection(getCat(currentBranche)));
			
			if(currentSectionCollection != null)
				for(int i = 0 ; i < currentSectionCollection.getSubSectionCount(); i++) // use getAbsoluteSubSectionCount if you use SectionCollections at this level (e.g. nested sectionCollections)
				{
					ConfigSection currentSection = currentSectionCollection.getConfigSectionAtIndex(i + 1); // use getConfigSectionAtAbsoluteIndex for reason described above.
					ConfigSegment segment = collection.getSegmentByID(currentSection.getSectionName());
					String input = (String) currentProfileConfig.getValue(currentSection.getSectionName());
					
					if(segment != null)
						segment.receivePacketInformation(input);
					else ConfigBranch.branches.get(i).onFailedLoadingSegment(currentSection.getSectionName(), input);
				}
			else
			{
				currentSectionCollection = new ConfigSectionCollection(currentBranche.name, null, true);
				for(ConfigSegment segment : currentBranche.getConfigSegments())
					currentSectionCollection.addNewStringSection(segment.getID(), null, segment.createPacketInformation(isServer), true);
				currentProfileConfig.addNewSection(currentSectionCollection);
			}
			
			currentBranche.onRecieveFromPre(isServer, collection);
			currentBranche.onRecieveFrom(isServer, collection);
			currentBranche.onRecieveFromPost(isServer, collection);
			logger.info("Loaded " + getCat(currentBranche) + " branch containing " + currentBranche.getConfigSegments().size() + " segments!");
		}
	}
	
	/** Load branch without changing profile!*/
	public static void loadConfig(ConfigBranch branch)
	{
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		ConfigSegmentCollection collection = new ConfigSegmentCollection(segments);
		branch.onBeforeReceived(FMLCommonHandler.instance().getEffectiveSide().isServer());
		ConfigSectionCollection currentSectionCollection = ((ConfigSectionCollection)currentProfileConfig.getSubSection(getCat(branch)));
		
		for(int i = 0 ; i < currentSectionCollection.getSubSectionCount(); i++) // use getAbsoluteSubSectionCount if you use SectionCollections at this level (e.g. nested sectionCollections)
		{
			ConfigSection currentSection = currentSectionCollection.getConfigSectionAtIndex(i + 1); // use getConfigSectionAtAbsoluteIndex for reason described above.
			ConfigSegment segment = collection.getSegmentByID(currentSection.getSectionName());
			String input = (String) currentProfileConfig.getValue(currentSection.getSectionName());
			
			if(segment != null)
				segment.receivePacketInformation(input);
			else ConfigBranch.branches.get(i).onFailedLoadingSegment(currentSection.getSectionName(), input);
		}
		boolean isServer = FMLCommonHandler.instance().getEffectiveSide().isServer();
		
		branch.onRecieveFromPre(isServer, collection);
		branch.onRecieveFrom(isServer, collection);
		branch.onRecieveFromPost(isServer, collection);
		logger.info("Loaded " + getCat(branch) + " branch");
//		currentProfileConfig.writeAllSections();//Only call this if changes have been made to the ConfigFile at this point.

	}
	
	public static void saveConfig(ConfigBranch branch)
	{
		ConfigSectionCollection branchCollection = (ConfigSectionCollection) currentProfileConfig.getSubSection(getCat(branch));
		branchCollection.clearAllConfigSections();
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		ConfigSegmentCollection collection = new ConfigSegmentCollection(segments);
		branch.onPacketSend(FMLCommonHandler.instance().getEffectiveSide().isServer(), collection);
		for (ConfigSegment currentSegment: segments)
		{
			String input = currentSegment.createPacketInformation(FMLCommonHandler.instance().getEffectiveSide().isServer());
			if(input != null)
			{
				branchCollection.addNewStringSection(currentSegment.getID().toLowerCase(), null, input, true);
			}
		}
		currentProfileConfig.writeAllSections();//Only do if changes should be writen at this point.
	}
	
	public static void saveProfiles()
	{
		coreConfigFile.setValue("profileName", profileName);
		coreConfigFile.setValue("profiles", profiles.toArray(new String[profiles.size()]));
		coreConfigFile.writeAllSections();	//Use if you need to write out changes immediately!,
											//This can happen on a later moment, since the values are saved in a binary format.
											//For example before serverShutDown etc.
	}
	
	public static String getCat(ConfigBranch branch)
	{
		return (branch.tab.title + "-" + branch.name).toLowerCase();
	}
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(eventHandler);
		FMLCommonHandler.instance().bus().register(eventHandler);
		
		CreativeCorePacket.registerPacket(BranchInformationPacket.class, "IGCMBranch");
		CreativeCorePacket.registerPacket(RequestInformationPacket.class, "IGCMRequest");
		CreativeCorePacket.registerPacket(CraftResultPacket.class, "IGCMCraftResult");
		
		N2ConfigApi.preInit(event);
		
		N2ConfigApi.registerCustomConfigDirectory("InGameConfigManager", ModProfileConfigurationDirectoryID);
		
		// Create ConfigFiles and preinit them /parse them.
		coreConfigFile = new CoreConfigFile(InGameConfigManager.modid + "_Core");
		currentProfileConfig = new ProfileConfigFile(profileName + "_Config");							//default ConfigFile for now TODO
		
		ConfigHandler.registerConfigFile(coreConfigFile, event.getModConfigurationDirectory());				// Needed to enable configLoading.
		ConfigHandler.registerConfigFile(currentProfileConfig, N2ConfigApi.getFileDirFromID(ModProfileConfigurationDirectoryID));
		
		File currentProfileForgeConfigFile = new File(N2ConfigApi.getConfigDir(), "InGameConfigManager" + File.separator + profileName + ".cfg");
		
		parseOrLoadConfigFile(event.getSuggestedConfigurationFile(), coreConfigFile, "Default");
		parseOrLoadConfigFile(currentProfileForgeConfigFile, currentProfileConfig, ModProfileConfigurationDirectoryID);
		
		String[] profile = coreConfigFile.getProfiles();
		profileName = coreConfigFile.getProfileName();
		
		if(profileName.equals(""))
			profileName = "new1";
		
		profiles = new ArrayList<String>(Arrays.asList(profile));
		if(!profiles.contains(profileName))
			profiles.add(profileName);
		
//		coreConfigFile.writeAllSections();//Not Needed unless changes have been made before this point!
	}
	
	private static void parseOrLoadConfigFile(File forgeConfigFileLocation, ConfigFile configFile, String DirectoryID)
	{
		File newFile = forgeConfigFileLocation.getAbsoluteFile();
		boolean isHere = newFile.exists();
		
		if(forgeConfigFileLocation.getAbsoluteFile().exists())		//Check if the Forges' configFile exists.
		{
			Configuration coreConfig = new Configuration(forgeConfigFileLocation);
			coreConfig.load();
			ConfigParser.parseConfigFile(coreConfig, configFile);
			
			if(ConfigHandler.getConfigFileFromName(configFile.getFileName()) == null)
				ConfigHandler.registerConfigFile(configFile, N2ConfigApi.getFileDirFromID(DirectoryID));
			forgeConfigFileLocation.deleteOnExit();// If you want to erase parsed ConfigFiles.
		}
		else
		{
			if(ConfigHandler.getConfigFileFromName(configFile.getFileName()) == null)
				ConfigHandler.registerConfigFile(configFile, N2ConfigApi.getFileDirFromID(DirectoryID));
			ConfigHandler.loadAndCheckConfigFile(configFile.getFileName());
		}
	}
	
	public static final String guiID = "IGCM";
	
	@EventHandler
	public static void Init(FMLInitializationEvent event)
	{
		ConfigManagerModLoader.loadMod();
		
		GameRegistry.registerBlock(advancedWorkbench, "advancedWorkbench");
		
		GuiHandler.registerGuiHandler(guiID, new InGameGuiHandler());
		
		if(Loader.isModLoaded("NotEnoughItems") && FMLCommonHandler.instance().getEffectiveSide().isClient())
			NEIAdvancedRecipeHandler.load();
	}
	
	@EventHandler
	public static void postLoading(FMLLoadCompleteEvent event)
	{
		for (int i = 0; i < ConfigBranch.branches.size(); i++) {
			ConfigBranch.branches.get(i).loadCore();
		}
	}
	
	@EventHandler
	public static void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandGUI());
		loadConfig();
	}
	
	public static void openBranchGui(EntityPlayer player, ConfigBranch branch)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("gui", 2);
		nbt.setInteger("index", branch.id);
		GuiHandler.openGui(guiID, nbt, player);
		/*
		ConfigGuiPacket packet = new ConfigGuiPacket(2, branch.id);
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			PacketHandler.sendPacketToServer(packet);
			packet.executeClient(player);
		}else{
			PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
			packet.executeServer(player);
		}*/
	}
	
	/*public static void openSubTabGui(EntityPlayer player, SubTab tab)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			PacketHandler.sendPacketToServer(new ConfigGuiPacket(2, tab.));
		}else{
			PacketHandler.sendPacketToPlayer(new ConfigGuiPacket(2, branch.id), (EntityPlayerMP) player);
		}
	}*/
	
	public static void openModOverviewGui(EntityPlayer player, int mod)
	{
		ModTab tab = TabRegistry.getTabByIndex(mod);
		if(tab != null)
		{
			
			if(tab.branches.size() > 1){
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("gui", 1);
				nbt.setInteger("index", mod);
				GuiHandler.openGui(guiID, nbt, player);
				/*ConfigGuiPacket packet = new ConfigGuiPacket(1, mod);
				if(FMLCommonHandler.instance().getEffectiveSide().isClient()){
					PacketHandler.sendPacketToServer(packet);
					packet.executeClient(player);
				}else{
					PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
					packet.executeServer(player);
				}*/
			}else if(tab.branches.size() == 1){
				openBranchGui(player, tab.branches.get(0));
			}
		}
	}
	
	public static void openModsGui(EntityPlayer player)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("gui", 0);
		nbt.setInteger("index", 0);
		GuiHandler.openGui(guiID, nbt, player);
		/*ConfigGuiPacket packet = new ConfigGuiPacket(0, 0);
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			PacketHandler.sendPacketToServer(packet);
			packet.executeClient(player);
		}else{
			PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
			packet.executeServer(player);
		}*/
	}
	
	public static void openProfileGui(EntityPlayer player)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("gui", 3);
		nbt.setInteger("index", 0);
		GuiHandler.openGui(guiID, nbt, player);
		/*ConfigGuiPacket packet = new ConfigGuiPacket(3, 0);
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			PacketHandler.sendPacketToServer(packet);
			packet.executeClient(player);
		}else{
			PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
			packet.executeServer(player);
		}*/
	}
	
	public static void sendAllUpdatePackets(EntityPlayer player)
	{
		ArrayList<ConfigBranch> branches = ConfigBranch.branches;
		for (int i = 0; i < branches.size(); i++) {
			sendUpdatePacket(branches.get(i), player);
		}
	}
	
	public static void sendUpdatePacket(ConfigBranch branch, EntityPlayer player)
	{
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		int amount = (int) Math.ceil((double)segments.size()/(double)maxSegments);
		for (int i = 0; i < amount; i++) {
			CreativeCorePacket packet = new BranchInformationPacket(branch, i*maxSegments, Math.min(i*maxSegments+maxSegments, segments.size()));
			if(FMLCommonHandler.instance().getEffectiveSide().isClient())
				PacketHandler.sendPacketToServer(packet);
			else
				PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
		}
	}
	
	public static void sendAllUpdatePackets()
	{
		ArrayList<ConfigBranch> branches = ConfigBranch.branches;
		for (int i = 0; i < branches.size(); i++) {
			sendUpdatePacket(branches.get(i));
		}
	}
	
	public static void sendUpdatePacket(ConfigBranch branch)
	{
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		int amount = (int) Math.ceil((double)segments.size()/(double)maxSegments);
		for (int i = 0; i < amount; i++) {
			CreativeCorePacket packet = new BranchInformationPacket(branch, i*maxSegments, Math.min(i*maxSegments+maxSegments, segments.size()));
			if(FMLCommonHandler.instance().getEffectiveSide().isClient())
				PacketHandler.sendPacketToServer(packet);
			else
				PacketHandler.sendPacketToAllPlayers(packet);
		}
	}
}