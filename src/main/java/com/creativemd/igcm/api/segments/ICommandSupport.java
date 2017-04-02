package com.creativemd.igcm.api.segments;

import net.minecraft.client.util.JsonException;
import net.minecraft.command.CommandException;

public interface ICommandSupport {
	
	public boolean hasCommandSupport();
	
	public void parseValue(String arg) throws CommandException;
	
	public String printValue();
	
	public String printDefaultValue();
	
	public String[] getPossibleValues();
	
}
