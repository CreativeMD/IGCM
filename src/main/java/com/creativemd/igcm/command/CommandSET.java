package com.creativemd.igcm.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.utils.mc.ChatFormatting;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.api.segments.ICommandSupport;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandSET extends CommandBase {
	
	@Override
	public String getName() {
		return "igcm";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "get/set values of igcm configuration";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		ConfigSegment segment = null;
		if (args.length > 0) {
			segment = ConfigTab.getSegmentByPath(args[0]);
			if (segment != null) {
				if (segment instanceof ICommandSupport) {
					if (args.length > 1) {
						try {
							((ICommandSupport) segment).parseValue(args[1]);
							sender.sendMessage(new TextComponentString("'" + ChatFormatting.RED + args[0] + ChatFormatting.WHITE + "' was set to '" + ChatFormatting.YELLOW + args[1] + ChatFormatting.WHITE + "'!"));
							IGCM.sendUpdatePacket(segment.getParentBranch());
						} catch (CommandException e) {
							TextComponentTranslation ret = new TextComponentTranslation(e.getMessage(), e.getErrorObjects());
							ret.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(ret);
						}
					} else
						sender.sendMessage(new TextComponentString("'" + ChatFormatting.RED + args[0] + ChatFormatting.WHITE + "'='" + ChatFormatting.YELLOW + ((ICommandSupport) segment).printValue() + ChatFormatting.WHITE + "' ('" + ChatFormatting.BLUE + ((ICommandSupport) segment).printDefaultValue() + ChatFormatting.WHITE + "')"));
				} else
					sender.sendMessage(new TextComponentString("'" + args[0] + "' does not provide command support!"));
			} else
				sender.sendMessage(new TextComponentString("'" + args[0] + "' does not exist!"));
		} else
			sender.sendMessage(new TextComponentString("/igcm <path of segment> (value)"));
		
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		ArrayList<String> completions = new ArrayList<>();
		if (args.length == 1) {
			String[] patch = args[0].split("\\.");
			if (!args[0].contains("."))
				completions.add("root");
			else {
				String path = args[0];
				if (path.endsWith("."))
					path = path.substring(0, path.length() - 1);
				else {
					path = "";
					for (int i = 0; i < patch.length - 1; i++) {
						path += (i > 0 ? "." : "") + patch[i];
					}
				}
				ConfigSegment segment = ConfigTab.getSegmentByPath(path);
				if (segment != null) {
					for (Iterator<ConfigSegment> iterator = segment.getChilds().iterator(); iterator.hasNext();) {
						ConfigSegment child = iterator.next();
						String childPath = child.getPath();
						if (childPath.startsWith(args[0]))
							completions.add(child.getPath());
					}
					
				}
			}
		} else if (args.length == 2) {
			ConfigSegment segment = ConfigTab.getSegmentByPath(args[0]);
			if (segment != null && segment instanceof ICommandSupport) {
				String[] tabs = ((ICommandSupport) segment).getPossibleValues();
				if (tabs != null)
					completions.addAll(Arrays.asList(tabs));
			}
		}
		return completions;
	}
	
}
