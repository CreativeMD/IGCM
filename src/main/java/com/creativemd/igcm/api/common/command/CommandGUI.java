package com.creativemd.igcm.api.common.command;

import com.creativemd.igcm.api.core.IGCM;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandGUI extends CommandBase {
	@Override
	public String getCommandName() {
		return "ConfigManager";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/ConfigManager";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(sender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) sender;
			IGCM.openModsGui(player);
		}
	}
}
