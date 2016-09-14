package com.creativemd.ingameconfigmanager.api.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.core.CreativeCoreClient;
import com.creativemd.creativecore.gui.opener.GuiHandler;
import com.creativemd.ingameconfigmanager.api.client.IGCMClient;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.command.CommandGUI;
import com.creativemd.ingameconfigmanager.api.common.event.ConfigEventHandler;
import com.creativemd.ingameconfigmanager.api.common.gui.InGameGuiHandler;
import com.creativemd.ingameconfigmanager.api.common.packets.BranchInformationPacket;
import com.creativemd.ingameconfigmanager.api.common.packets.CraftResultPacket;
import com.creativemd.ingameconfigmanager.api.common.packets.RequestInformationPacket;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.creativemd.ingameconfigmanager.mod.ConfigManagerModLoader;
import com.creativemd.ingameconfigmanager.mod.block.BlockAdvancedWorkbench;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = IGCM.modid, version = IGCM.version, name = "InGameConfigManager",acceptedMinecraftVersions="")
public class IGCM { 
	
	public static Logger logger = LogManager.getLogger(IGCM.modid);
	
	public static final String modid = "ingameconfigmanager";
	public static final String version = "1.1";
	
	public static ConfigEventHandler eventHandler = new ConfigEventHandler();
	
	public static int maxSegments = 10;
	
	public static Configuration coreConfig;
	public static Configuration currentProfile;
	public static File ModConfigurationDirectory;
	
	public static String profileName;
	public static ArrayList<String> profiles;
	
	public static boolean overrideWorkbench = false;
	public static Block advancedWorkbench = new BlockAdvancedWorkbench().setUnlocalizedName("advancedWorkbench").setRegistryName(IGCM.modid, "advancedWorkbench").setCreativeTab(CreativeTabs.DECORATIONS);
	
	
	
	/**Used for loading configs on startup and changing profile**/
	public static void loadConfig()
	{
		currentProfile = new Configuration(new File(ModConfigurationDirectory, "InGameConfigManager" + File.separator + profileName + ".cfg"));
		currentProfile.load();
		for (ConfigBranch branch : ConfigBranch.branches.values()) {
			loadConfig(branch, false);
		}
		currentProfile.save();
	}
	
	public static void loadConfig(ConfigBranch branch, boolean full)
	{
		if(full)
			currentProfile.load();
		
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		branch.onBeforeReceived(FMLCommonHandler.instance().getEffectiveSide().isServer());
		
		ConfigSegmentCollection collection = new ConfigSegmentCollection(segments);
		ConfigSegmentCollection collectionOld = new ConfigSegmentCollection(new ArrayList<>(segments));
		//ArrayList<ConfigSegment> newSegments = new ArrayList<>();
		Set<java.util.Map.Entry<String, Property>> children = currentProfile.getCategory(getCat(branch)).entrySet();
		int cIndex = 0;
		for (Map.Entry<String, Property> entry : children)
		{
			ConfigSegment segment = collectionOld.getSegmentByID(entry.getKey());
			String input = entry.getValue().getString();
			if(segment != null)
			{
				segment.receivePacketInformation(input);
			}else{
				ConfigSegment fSegment = branch.onFailedLoadingSegment(collection, entry.getKey(), input, cIndex);
				if(fSegment != null)
				{
					fSegment.receivePacketInformation(input);
					collection.asList().add(fSegment);
					//newSegments.add(fSegment);
				}
			}
			cIndex++;
		}
		boolean isServer = FMLCommonHandler.instance().getEffectiveSide().isServer();
		
		//collection.asList().addAll(newSegments);
		
		branch.onRecieveFromPre(isServer, collection);
		branch.onRecieveFrom(isServer, collection);
		branch.onRecieveFromPost(isServer, collection);
		logger.info("Loaded " + getCat(branch) + " branch containing " + branch.getConfigSegments().size() + " segments of " + children.size() + " config entries!");
		
		if(full)
			currentProfile.save();
	}
	
	public static void saveConfig(ConfigBranch branch)
	{
		currentProfile.load();
		currentProfile.getCategory(getCat(branch)).clear();
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		ConfigSegmentCollection collection = new ConfigSegmentCollection(segments);
		branch.onPacketSend(FMLCommonHandler.instance().getEffectiveSide().isServer(), collection);
		for (int i = 0; i < segments.size(); i++) {
			String input = segments.get(i).createPacketInformation(FMLCommonHandler.instance().getEffectiveSide().isServer());
			if(input != null)
			{
				currentProfile.get(getCat(branch), segments.get(i).getID().toLowerCase(), "").set(input);
			}
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
		//FMLCommonHandler.instance().bus().register(eventHandler);
		
		CreativeCorePacket.registerPacket(BranchInformationPacket.class, "IGCMBranch");
		CreativeCorePacket.registerPacket(RequestInformationPacket.class, "IGCMRequest");
		CreativeCorePacket.registerPacket(CraftResultPacket.class, "IGCMCraftResult");
		
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
	
	public static final String guiID = "IGCM";
	
	@EventHandler
	public static void Init(FMLInitializationEvent event)
	{
		ConfigManagerModLoader.loadMod();
		
		GameRegistry.registerBlock(advancedWorkbench);
		
		GuiHandler.registerGuiHandler(guiID, new InGameGuiHandler());
		
		//if(Loader.isModLoaded("NotEnoughItems") && FMLCommonHandler.instance().getEffectiveSide().isClient())
			//NEIAdvancedRecipeHandler.load();
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			initCLient();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initCLient()
	{
		IGCMClient.initClient();
	}
	
	@EventHandler
	public static void postLoading(FMLLoadCompleteEvent event)
	{
		for (ConfigBranch branch : ConfigBranch.branches.values()) {
			branch.loadCore();
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
		nbt.setString("name", branch.name);
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
	
	public static void openModOverviewGui(EntityPlayer player, String mod)
	{
		ModTab tab = TabRegistry.getTabByID(mod);
		if(tab != null)
		{
			
			if(tab.branches.size() > 1){
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("gui", 1);
				nbt.setString("name", mod);
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
		for (ConfigBranch branch : ConfigBranch.branches.values()) {
			sendUpdatePacket(branch, player);
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
		for (ConfigBranch branch : ConfigBranch.branches.values()) {
			sendUpdatePacket(branch);
		}
	}
	
	public static void sendUpdatePacket(ConfigBranch branch)
	{
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		int amount = (int) Math.ceil((double)segments.size()/(double)maxSegments);
		ArrayList<CreativeCorePacket> packets = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			CreativeCorePacket packet = new BranchInformationPacket(branch, i*maxSegments, Math.min(i*maxSegments+maxSegments, segments.size()));
			packets.add(packet);
		}
	
		for (int i = 0; i < packets.size(); i++) {
			if(FMLCommonHandler.instance().getEffectiveSide().isClient())
				PacketHandler.sendPacketToServer(packets.get(i));
			else
				PacketHandler.sendPacketToAllPlayers(packets.get(i));
		}
		
	}
}