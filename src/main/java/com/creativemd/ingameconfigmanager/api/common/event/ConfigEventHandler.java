package com.creativemd.ingameconfigmanager.api.common.event;

/*
 * Content
 * - IconRegistryEvents
 * - MouseEvents
 * - KeyboardEvents
 */

import java.awt.Toolkit;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.TextureStitchEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.creativemd.creativecore.client.avatar.AvatarIcon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ConfigEventHandler
{
	//////////////////////////////
	/*
	 * IconRegistryEvents
	 */
	//////////////////////////////
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre event)
	{
		ArrayList<AvatarIcon> iconList = AvatarIcon.getIconList();
		
		for(int i = 0; i < iconList.size(); i++)
		{
			String iconPath = iconList.get(i).iconPath;
			if((iconList.get(i).isItem && event.map.getTextureType() == 1) || (!iconList.get(i).isItem && event.map.getTextureType() == 0))
			{
				event.map.registerIcon(iconPath);
			}
		}
	}
	
	
	
	
	//////////////////////////////
	/*
	 * MouseEvents
	 */
	//////////////////////////////
	public static final Integer timerInterval = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval")/50; 
	public static boolean EnableMouseHandling;
	private static boolean isMoving;
	private static boolean[] mouseButtonReleased = new boolean[9];
	private static boolean[] Clicked = new boolean[9];
	private static int[] mouseButtonTimer = new int[9];
	private static Mouse mouse;
	private static int mouseX;
	private static int mouseY;
	
	@SubscribeEvent
	public void handleMouseEventOnClientTick(TickEvent.ClientTickEvent event)
	{
		if(EnableMouseHandling)
		{
			if (mouseX != mouse.getX() || mouseY != mouse.getY())
			{
				MouseEvents.onMouseMoveEvent.callActionEvents();
				isMoving = true;
			}
			else isMoving = false;
			int dMouseX = mouse.getX() - mouseX;
			int dMouseY = mouse.getY() - mouseY;
			boolean isSignificantMoving = (dMouseX > 3 || dMouseY > 3);
			mouseX = mouse.getX();
			mouseY = mouse.getY();
			
			int wheel = mouse.getDWheel();
			if (wheel != 0)
				MouseEvents.onScrollEvent.callActionEvents(wheel);
			
			for(int i = 0; i <= 8; i++)
			{
				if(mouse.isButtonDown(i))
				{
					if(mouseButtonReleased[i])
					{
						switch(i)
						{
						case 0: MouseEvents.onLeftMouseButtonPressEvent.callActionEvents();
							break;
						case 1: MouseEvents.onRightMouseButtonPressEvent.callActionEvents();
							break;
						case 2: MouseEvents.onWheelPressEvent.callActionEvents();
						}
						MouseEvents.onMouseButtonPressEvent.callActionEvents(i);
						if(mouseButtonTimer[i] == -1)
							mouseButtonTimer[i] = 0;						
						mouseButtonReleased[i] = false;
					}
					switch(i)
					{
					case 0: MouseEvents.onLeftMouseButtonDownEvent.callActionEvents();
						break;
					case 1: MouseEvents.onRightMouseButtonDownEvent.callActionEvents();
						break;
					case 2: MouseEvents.onWheelDownEvent.callActionEvents();
					}
					MouseEvents.onMouseButtonDownEvent.callActionEvents(i);
					if(mouseButtonTimer[i] != -1)
						mouseButtonTimer[i] += 1;
					if(mouseButtonTimer[i] > timerInterval)
						mouseButtonTimer[i] = -1;
				}
				else
				{
					if(!mouseButtonReleased[i])
					{
						if(mouseButtonTimer[i] < timerInterval)
						{
							if(Clicked[i])
							{
								if(i == 0)
									MouseEvents.onDoubleLeftClickEvent.callActionEvents();
								else if( i == 1)
									MouseEvents.onDoubleRightClickEvent.callActionEvents();
								MouseEvents.onDoubleButtonClickEvent.callActionEvents(i);
								Clicked[i] = false;
							}
							else Clicked[i] = true;
						}

						switch (i)
					{
						case 0:
							MouseEvents.onRightClickEvent.callActionEvents();
							MouseEvents.onRightMouseButtonReleaseEvent.callActionEvents();
							break;
						case 1:
							MouseEvents.onLeftClickEvent.callActionEvents();
							MouseEvents.onLeftMouseButtonReleaseEvent.callActionEvents();
							break;
						case 2: MouseEvents.onWheelClickEvent.callActionEvents();
						}
						MouseEvents.onMouseButtonReleaseEvent.callActionEvents(i);
					}
					if(mouseButtonTimer[i] > timerInterval)
						mouseButtonTimer[i] = -1;
				}
			}
		}
	}
	
/*	
	@SubscribeEvent
	public void onclick(KeyInputEvent event)
	{
		System.out.println("type!");
	}
*/
	//////////////////////////////
	/*
	 * KeyboardEvents
	 */
	//////////////////////////////
	public static boolean enableKeyboardHandling;
	private static Keyboard keyboard;
	private boolean[] isKeyReleased = new boolean[keyboard.getKeyCount()];
	private static int keyboardCount = 0;
	
	@SubscribeEvent
	public void handleKeyboardEventsOnClientTick(TickEvent.ClientTickEvent event)
	{
		if(enableKeyboardHandling)
		{
			if(keyboardCount != keyboard.getKeyCount())
			{
				keyboardCount = keyboard.getKeyCount();
				isKeyReleased = new boolean[keyboard.getKeyCount()];
			}
			for(int i = 0; i < keyboardCount; i++)
			{
				if(keyboard.isKeyDown(i))
				{
					if(isKeyReleased[i])
					{
						KeyBoardEvents.onKeyPress.callActionEvents(i);
						isKeyReleased[i] = false;
					}
					KeyBoardEvents.onKeyDown.callActionEvents(i);
				}
				else
				{
					if(!isKeyReleased[i])
					{
						KeyBoardEvents.onKeyRelease.callActionEvents(i);
						isKeyReleased[i] = true;
					}
				}
			}
		}
	}
}
