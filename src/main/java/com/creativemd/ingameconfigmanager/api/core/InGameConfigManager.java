package com.creativemd.ingameconfigmanager.api.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

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
import com.creativemd.ingameconfigmanager.api.common.event.ConfigEventHandler;
import com.creativemd.ingameconfigmanager.api.common.packets.BranchInformationPacket;
import com.creativemd.ingameconfigmanager.api.common.packets.ConfigGuiPacket;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.creativemd.ingameconfigmanager.api.tab.SubTab;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = InGameConfigManager.modid, version = InGameConfigManager.version, name = "InGameConfigManager")
public class InGameConfigManager { 
	
	public static Logger logger = LogManager.getLogger(InGameConfigManager.modid);
	
	public static final String modid = "ingameconfigmanager";
	
	public static final String version = "1.0.0";
	
	public static ConfigEventHandler eventHandler = new ConfigEventHandler();
	
	public static int maxSegments = 10;
	
	public static Configuration coreConfig;
	
	public static Configuration currentProfile;
	
	public static File ModConfigurationDirectory;
	
	public static String profileName;
	public static ArrayList<String> profiles;
	
	/**Used for loading configs on startup and changing profile**/
	public static void loadConfig()
	{
		currentProfile = new Configuration(new File(ModConfigurationDirectory, "InGameConfigManager" + File.separator + profileName + ".cfg"));
		currentProfile.load();
		for (int i = 0; i < ConfigBranch.branches.size(); i++) {
			ArrayList<ConfigSegment> segments = ConfigBranch.branches.get(i).getConfigSegments();
			for (int j = 0; j < segments.size(); j++) {
				if(currentProfile.hasKey(getCat(ConfigBranch.branches.get(i)), segments.get(j).getID().toLowerCase()))
				{
					String input = currentProfile.get(getCat(ConfigBranch.branches.get(i)), segments.get(j).getID().toLowerCase(), "").getString();
					segments.get(j).receivePacketInformation(input);
				}
			}
			boolean isServer = FMLCommonHandler.instance().getEffectiveSide().isServer();
			ConfigSegmentCollection collection = new ConfigSegmentCollection(segments);
			ConfigBranch.branches.get(i).onRecieveFromPre(isServer, collection);
			ConfigBranch.branches.get(i).onRecieveFrom(isServer, collection);
			ConfigBranch.branches.get(i).onRecieveFromPost(isServer, collection);
			logger.info("Loaded " + getCat(ConfigBranch.branches.get(i)) + " branch");
		}
		currentProfile.save();
	}
	
	public static void saveConfig(ConfigBranch branch)
	{
		currentProfile.load();
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		ConfigSegmentCollection collection = new ConfigSegmentCollection(segments);
		branch.onPacketSend(FMLCommonHandler.instance().getEffectiveSide().isServer(), collection);
		for (int i = 0; i < segments.size(); i++) {
			String input = segments.get(i).createPacketInformation();
			currentProfile.get(getCat(branch), segments.get(i).getID().toLowerCase(), "").set(input);
		}
		currentProfile.save();
	}
	
	public static void saveProfiles()
	{
		coreConfig.load();
		coreConfig.get("General", "profileName", "new1").set(profileName);
		coreConfig.get("General", "profiles", new String[0]).set(profiles.toArray(new String[0]));
		coreConfig.save();
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
		CreativeCorePacket.registerPacket(ConfigGuiPacket.class, "IGCMGUI");
		
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
		ConfigGuiPacket packet = new ConfigGuiPacket(2, branch.id);
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			PacketHandler.sendPacketToServer(packet);
			packet.executeClient(player);
		}else{
			PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
			packet.executeServer(player);
		}
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
				ConfigGuiPacket packet = new ConfigGuiPacket(1, mod);
				if(FMLCommonHandler.instance().getEffectiveSide().isClient()){
					PacketHandler.sendPacketToServer(packet);
					packet.executeClient(player);
				}else{
					PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
					packet.executeServer(player);
				}
			}else if(tab.branches.size() == 1){
				openBranchGui(player, tab.branches.get(0));
			}
		}
	}
	
	public static void openModsGui(EntityPlayer player)
	{
		ConfigGuiPacket packet = new ConfigGuiPacket(0, 0);
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			PacketHandler.sendPacketToServer(packet);
			packet.executeClient(player);
		}else{
			PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
			packet.executeServer(player);
		}
	}
	
	public static void openProfileGui(EntityPlayer player)
	{
		ConfigGuiPacket packet = new ConfigGuiPacket(3, 0);
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			PacketHandler.sendPacketToServer(packet);
			packet.executeClient(player);
		}else{
			PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) player);
			packet.executeServer(player);
		}
	}
	
	public static void sendAllUpdatePackets(EntityPlayer player)
	{
		ArrayList<ConfigBranch> branches = ConfigBranch.branches;
		for (int i = 0; i < branches.size(); i++) {
			sendUpdatePacket(branches.get(i));
		}
	}
	
	public static void sendUpdatePacket(ConfigBranch branch, EntityPlayer player)
	{
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		int amount = (int) Math.floor((double)segments.size()/(double)maxSegments);
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