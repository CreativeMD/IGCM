package com.creativemd.igcm.command;

import java.util.HashMap;

import akka.io.Tcp.Command;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;

public abstract class CommandParser<T> {
	
	private static HashMap<Class, CommandParser> parsers = new HashMap<>();
	
	public static <T> void registerParser(Class<T> clazz, CommandParser<T> parser)
	{
		parsers.put(clazz, parser);
	}
	
	public static boolean canParseObject(Class clazz)
	{
		return parsers.containsKey(clazz);
	}
	
	public static <T> String printObject(T input)
	{
		CommandParser parser = parsers.get(input.getClass());
		if(parser != null)
			return parser.print(input);
		return "";
	}
	
	public static <T> T parseObject(String input, Class<T> clazz) throws CommandException
	{
		CommandParser parser = parsers.get(clazz);
		if(parser != null)
			return (T) parser.parse(input);
		throw new CommandException("No parser found!", new Object[]{clazz});
	}
	
	static {
		registerParser(Boolean.class, new CommandParser<Boolean>() {
			
			@Override
			public String print(Boolean input) {
				if(input)
					return "true";
				return "false";
			}
			
			@Override
			public Boolean parse(String input) throws CommandException {
				return CommandBase.parseBoolean(input);
			}
		});
		registerParser(Integer.class, new CommandParser<Integer>(){

			@Override
			public Integer parse(String input) throws CommandException {
				return CommandBase.parseInt(input);
			}

			@Override
			public String print(Integer input) {
				return input.toString();
			}
		});
		registerParser(Float.class, new CommandParser<Float>(){

			@Override
			public Float parse(String input) throws CommandException {
				return (float) CommandBase.parseDouble(input);
			}

			@Override
			public String print(Float input) {
				return input.toString();
			}
		});
		registerParser(Double.class, new CommandParser<Double>(){

			@Override
			public Double parse(String input) throws CommandException {
				return CommandBase.parseDouble(input);
			}

			@Override
			public String print(Double input) {
				return input.toString();
			}
		});
		registerParser(String.class, new CommandParser<String>(){

			@Override
			public String parse(String input) throws CommandException {
				return input;
			}

			@Override
			public String print(String input) {
				return input;
			}
		});
	}
	
	public abstract T parse(String input) throws CommandException;
	
	public abstract String print(T input);
	
}
