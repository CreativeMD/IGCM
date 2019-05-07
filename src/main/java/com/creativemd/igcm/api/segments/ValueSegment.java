package com.creativemd.igcm.api.segments;

import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.command.CommandParser;

import net.minecraft.command.CommandException;

public abstract class ValueSegment<T> extends ConfigSegment implements ICommandSupport {
	
	public T defaultValue;
	
	public T value;
	
	public ValueSegment(String title, T defaultValue) {
		super(title);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public void initDefault() {
		value = defaultValue;
		super.initDefault();
	}
	
	@Override
	public boolean contains(String search) {
		if (title != null && title.toLowerCase().contains(search))
			return true;
		if (value != null && value.toString().toLowerCase().contains(search))
			return true;
		return getKey().toLowerCase().contains(search);
	}
	
	public abstract void set(T newValue);
	
	@Override
	public boolean hasCommandSupport() {
		return CommandParser.canParseObject(value.getClass());
	}
	
	@Override
	public void parseValue(String arg) throws CommandException {
		set((T) CommandParser.parseObject(arg, value.getClass()));
	}
	
	@Override
	public String printValue() {
		return CommandParser.printObject(value);
	}
	
	@Override
	public String printDefaultValue() {
		return CommandParser.printObject(defaultValue);
	}
	
	@Override
	public String[] getPossibleValues() {
		if (value instanceof Boolean)
			return new String[] { "true", "false" };
		return null;
	}
}
