package com.creativemd.ingameconfigmanager.api.common.event;

import java.awt.Toolkit;
import java.util.ArrayList;

import net.minecraftforge.client.event.TextureStitchEvent;

import org.lwjgl.input.Mouse;

import com.creativemd.ingameconfigmanager.api.client.representative.RepresentativeIcon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ConfigEventHandler
{
	public static final Integer timerinterval = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval")/50; 
	public static boolean EnableMouseHandling;
	private static boolean isMoving;
	private static boolean[] buttonReleased = new boolean[9];
	private static boolean[] doubleClicked = new boolean[9];
	private static int[] buttonTimer = new int[11];
	private static Mouse mouse;
	private static int mouseX;
	private static int mouseY;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre event)
	{
		ArrayList<RepresentativeIcon> iconList = RepresentativeIcon.getIconList();
		
		for(int i = 0; i < iconList.size(); i++)
		{
			String iconPath = iconList.get(i).iconPath;
			if((iconList.get(i).isItem && event.map.getTextureType() == 1) || (!iconList.get(i).isItem && event.map.getTextureType() == 0))
			{
				event.map.registerIcon(iconPath);
			}
		}
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(!EnableMouseHandling)
		{
			if (mouseX != mouse.getX() || mouseY != mouse.getY())
			{
				MouseEvents.onMouseMoveEvent.callActionEvents();
				isMoving = true;
			}
			else isMoving = false;
			mouseX = mouse.getX();
			mouseY = mouse.getY();
			
			int wheel = mouse.getDWheel();
			if (wheel != 0)
				MouseEvents.onScrollEvent.callActionEvents(wheel);
			
			for(int i = 0; i <= 8; i++)
			{
				if(mouse.isButtonDown(i))
				{
					if (buttonTimer[i] < timerinterval && buttonReleased[i] == true)
						doubleClicked[i] = true;
					else MouseEvents.onButtonPressEvent.callActionEvents(i);
					
					if(isMoving)
					{
						if(i == 0)
							MouseEvents.onleftClickDragEvent.callActionEvents();
						if(i == 1)
							MouseEvents.onRightClickDragEvent.callActionEvents();
					}
					buttonReleased[i] = false;
					buttonTimer[i] = 0;
				}
				else
				{
					buttonTimer[i] += 1;
					buttonReleased[i] = true;
					
					switch(i)
					{
					case 0:
						if(doubleClicked[i])
						{
							MouseEvents.onDoubleLeftClickEvent.callActionEvents();
							MouseEvents.onDoubleButtonClickEvent.callActionEvents(i);
						}
						else MouseEvents.onLeftClickEvent.callActionEvents();
						MouseEvents.onLeftMouseButtonReleaseEvent.callActionEvents();
						MouseEvents.onButtonReleaseEvent.callActionEvents(i);
						break;
					case 1:
						if(doubleClicked[i])
						{
							MouseEvents.onDoubleRightClickEvent.callActionEvents();
							MouseEvents.onDoubleButtonClickEvent.callActionEvents(i);
						}
						else MouseEvents.onRightClickEvent.callActionEvents();
						MouseEvents.onRightMouseButtonReleaseEvent.callActionEvents();
						MouseEvents.onButtonReleaseEvent.callActionEvents(i);
						break;
					case 2:
						if(doubleClicked[i])
							MouseEvents.onDoubleButtonClickEvent.callActionEvents(i);
						else MouseEvents.onWheelClickEvent.callActionEvents();
						MouseEvents.onWheelReleaseEvent.callActionEvents();
						MouseEvents.onButtonReleaseEvent.callActionEvents(i);
						break;
					default:
						if(doubleClicked[i])
							MouseEvents.onDoubleButtonClickEvent.callActionEvents(i);
						MouseEvents.onButtonReleaseEvent.callActionEvents(i);
					}
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
}
