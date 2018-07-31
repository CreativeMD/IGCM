package com.creativemd.igcm;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.gui.opener.GuiHandler;
import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigElement;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.block.BlockAdvancedWorkbench;
import com.creativemd.igcm.client.IGCMClient;
import com.creativemd.igcm.command.CommandGUI;
import com.creativemd.igcm.command.CommandSET;
import com.creativemd.igcm.event.ConfigEventHandler;
import com.creativemd.igcm.machines.AdvancedWorkbench;
import com.creativemd.igcm.machines.BrewingStandMachine;
import com.creativemd.igcm.machines.FurnaceMachine;
import com.creativemd.igcm.machines.WorkbenchMachine;
import com.creativemd.igcm.packets.BranchInformationPacket;
import com.creativemd.igcm.packets.CraftResultPacket;
import com.creativemd.igcm.packets.OpenGUIPacket;
import com.creativemd.igcm.packets.RequestInformationPacket;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;

@Mod(modid = IGCM.modid, version = IGCM.version, name = "InGameConfigManager",acceptedMinecraftVersions="", dependencies = "required-before:creativecore")
public class IGCM {
	
public static Logger logger = LogManager.getLogger(IGCM.modid);
	
	public static final String modid = "igcm";
	public static final String version = "1.2.2";
	
	public static ConfigEventHandler eventHandler = new ConfigEventHandler();
	
	public static boolean overrideWorkbench = false;
	public static Block advancedWorkbenchBlock = null;
	public static Item advancedWorkbenchItem = null;
	
	public static boolean enableWorkbench;
	public static boolean enableFurnace;
	public static boolean enableBrewing;
	
	public static final String guiID = "IGCM";
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		enableWorkbench = config.getBoolean("enableWorkbench", "Module", true, "If false you will not be able to modify crafting recipes");
		enableFurnace = config.getBoolean("enableFurnace", "Module", true, "If false you will not be able to modify furnace recipes");
		enableBrewing = config.getBoolean("enableBrewing", "Module", true, "If false you will not be able to modify brewing recipes");
		config.save();
		
		event.getModMetadata().version = version;
		MinecraftForge.EVENT_BUS.register(eventHandler);
		//FMLCommonHandler.instance().bus().register(eventHandler);
		
		CreativeCorePacket.registerPacket(BranchInformationPacket.class, "IGCMBranch");
		CreativeCorePacket.registerPacket(RequestInformationPacket.class, "IGCMRequest");
		CreativeCorePacket.registerPacket(CraftResultPacket.class, "IGCMCraftResult");
		CreativeCorePacket.registerPacket(OpenGUIPacket.class, "IGCMGUI");
		
		IGCMConfig.initConfig(event);
		
		MinecraftForge.EVENT_BUS.register(IGCM.class);
	}
	
	public static WorkbenchMachine workbench;
	public static FurnaceMachine furnace;
	public static AdvancedWorkbench advancedWorkbench;
	public static BrewingStandMachine brewingStand;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		advancedWorkbenchBlock = new BlockAdvancedWorkbench().setUnlocalizedName("advancedWorkbench").setRegistryName(IGCM.modid, "advancedworkbench").setCreativeTab(CreativeTabs.DECORATIONS);
	    event.getRegistry().registerAll(advancedWorkbenchBlock);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		advancedWorkbenchItem = new ItemBlock(advancedWorkbenchBlock).setRegistryName(advancedWorkbenchBlock.getRegistryName()).setCreativeTab(CreativeTabs.DECORATIONS);
	    event.getRegistry().registerAll(advancedWorkbenchItem);
	    
	    if(FMLCommonHandler.instance().getSide().isClient())
			initClientPre();
	}
	
	@EventHandler
	public static void Init(FMLInitializationEvent event)
	{		
		GuiHandler.registerGuiHandler(guiID, new IGCMGuiManager());
		
		if(FMLCommonHandler.instance().getSide().isClient())
			initClient();
		
		if(enableWorkbench)
			workbench = new WorkbenchMachine("workbench", "Crafting Table", new ItemStack(Blocks.CRAFTING_TABLE));
		if(enableFurnace)
			furnace = new FurnaceMachine("furnace", "Furnace", new ItemStack(Blocks.FURNACE));
		advancedWorkbench = new AdvancedWorkbench("advWorkbench", "Advanced Workbench", new ItemStack(advancedWorkbenchBlock));
		if(enableBrewing)
			brewingStand = new BrewingStandMachine("brewing", "Brewing Stand", new ItemStack(Items.BREWING_STAND));
	}
	
	@SideOnly(Side.CLIENT)
	public static void initClientPre()
	{
		IGCMClient.initClientPre();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initClient()
	{
		IGCMClient.initClient();
	}
	
	@EventHandler
	public static void postLoading(FMLLoadCompleteEvent event)
	{
		ConfigTab.root.initCore();
	}
	
	public static CommandBase gui;
	public static CommandBase set;
	
	@EventHandler
	public static void serverStarting(FMLServerStartingEvent event)
	{
		gui = new CommandGUI();
		set = new CommandSET();
		event.registerServerCommand(gui);
		event.registerServerCommand(set);
		IGCMConfig.loadConfig();
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean needsSynchronization()
	{
		return !Minecraft.getMinecraft().isSingleplayer();
	}
	
	public static void sendAllUpdatePackets(EntityPlayer player)
	{
		ArrayList<ConfigElement> elements = new ArrayList<>(ConfigTab.root.getChilds());
		while(elements.size() > 0)
		{
			if(elements.get(0) instanceof ConfigBranch)
				sendUpdatePacket((ConfigBranch) elements.get(0), player);
			else
				elements.addAll(elements.get(0).getChilds());
			elements.remove(0);
		}
	}
	
	public static void sendUpdatePacket(ConfigBranch branch, EntityPlayer player)
	{
		if(!branch.requiresSynchronization())
			return ;
		if(player.world.isRemote)
			PacketHandler.sendPacketToServer(new BranchInformationPacket(branch));
		else
			PacketHandler.sendPacketToPlayer(new BranchInformationPacket(branch), (EntityPlayerMP) player);
	}
	
	public static void sendAllUpdatePackets()
	{
		ArrayList<ConfigElement> elements = new ArrayList<>(ConfigTab.root.getChilds());
		while(elements.size() > 0)
		{
			if(elements.get(0) instanceof ConfigBranch)
				sendUpdatePacket((ConfigBranch) elements.get(0));
			else
				elements.addAll(elements.get(0).getChilds());
			elements.remove(0);
		}
	}
	
	public static void sendUpdatePacket(ConfigBranch branch)
	{
		if(!branch.requiresSynchronization())
			return ;
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			PacketHandler.sendPacketToServer(new BranchInformationPacket(branch));
		else
			PacketHandler.sendPacketToAllPlayers(new BranchInformationPacket(branch));
		
	}
	
}
