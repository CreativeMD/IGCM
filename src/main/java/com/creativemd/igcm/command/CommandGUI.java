package com.creativemd.igcm.command;

import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.IGCMGuiManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandGUI extends CommandBase {
	@Override
	public String getName() {
		return "ConfigManager";
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "/ConfigManager";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(sender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) sender;
			IGCMGuiManager.openConfigGui(player);
		}
	}
}