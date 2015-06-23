
package com.creativemd.ingameconfigmanager.api.common.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;

import com.creativemd.ingameconfigmanager.api.tab.core.InGameConfigManager;

public class MouseEvents extends Event
{
	private static List<Class> mouseControlClassListnerList = new ArrayList<Class>();
	private static Logger log = InGameConfigManager.logger;
	private static Mouse mouse;
	private static int superMousePosX;
	private static int superLastMousePosX;
	private static int superMousePosY;
	private static int superLastMousePosY;
	private static int superWheelScroll;
	private static boolean superIsScrollingUp;
	private static boolean superIsScrollingDown;
	//left = 0, middle = 2, right = 1
	
	/**
	 * Call this method if you want your class to listen to this eventHandler.
	 * The method in question need to have the "@HWInputHandler" Annotation.
	 * @param clazz - The instance of the Class in question.
	 */
	public void addMouseListner(Class clazz)
	{
		mouseControlClassListnerList.add(clazz);
	}
	
	
	
	
	private static void callEvents(Object eventType)
	{
		superMousePosX = mouse.getX();
		superMousePosY = mouse.getY();
		superIsScrollingUp = superWheelScroll > 0 ? true : false;
		superIsScrollingUp = superWheelScroll < 0 ? true : false;
		
		for(int i = 0; i < mouseControlClassListnerList.size(); i++)
		{
			Class clazz = mouseControlClassListnerList.get(i);
			Method[] method = clazz.getMethods();
			
			for(int j = 0; j < method.length; i++)
			{
				Method currentMethod = method[j];
				
				if(currentMethod.isAnnotationPresent(HWInputHandler.class))
				{
					if(currentMethod.getParameterTypes().length != 1 || !(currentMethod.getGenericParameterTypes()[0] instanceof Event))
						throw new IllegalArgumentException("Couldn't resolve parameters of:" + currentMethod.getDeclaringClass() + ";" + currentMethod.getName());
					
					if(currentMethod.getGenericParameterTypes()[0].getClass() == eventType.getClass())
					{
						try
						{
							currentMethod.invoke(clazz, eventType.getClass());
						}
						catch(Exception e)
						{
							log.catching(e);
						}
					}
				}
			}
		}
		
		superLastMousePosX = superMousePosX;
		superLastMousePosY = superMousePosY;
	}

	static class onLeftClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;

		public static void callActionEvents()
		{
			callEvents(new onLeftClickEvent());
		}	
	}
	
	static class onLeftMouseButtonReleaseEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		public static void callActionEvents()
		{
			callEvents(new onLeftMouseButtonReleaseEvent());
		}	
	}
	
	static class onDoubleLeftClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		public static void callActionEvents()
		{
			callEvents(new onDoubleLeftClickEvent());
		}	
	}
	
	static class onRightClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;

		public static void callActionEvents()
		{
			callEvents(new onRightClickEvent());
		}
	}
	
	static class onRightMouseButtonReleaseEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		public static void callActionEvents()
		{
			callEvents(new onRightMouseButtonReleaseEvent());
		}
	}
	
	static class onDoubleRightClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		public static void callActionEvents()
		{
			callEvents(new onDoubleRightClickEvent());
		}
	}
	
	static class onButtonPressEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int buttonID;
		
		public static void callActionEvents(int buttonid)
		{
			buttonID = buttonid;
			callEvents(new onButtonPressEvent());
		}
	}
	
	static class onDoubleButtonClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int buttonID;
		
		public static void callActionEvents(int buttonid)
		{
			buttonID = buttonid;
			callEvents(new onDoubleButtonClickEvent());
		}
	}
	
	static class onButtonReleaseEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int buttonID;
		
		public static void callActionEvents(int buttonid)
		{
			buttonID = buttonid;
			callEvents(new onButtonReleaseEvent());
		}
	}
	
	static class onWheelClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		public static void callActionEvents()
		{
			callEvents(new onWheelClickEvent());
		}
	}
	
	static class onWheelReleaseEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		public static void callActionEvents()
		{
			callEvents(new onWheelReleaseEvent());
		}
	}
	
	static class onScrollEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int wheelScroll = superWheelScroll;
		
		public static void callActionEvents(int wheel)
		{
			superWheelScroll = wheel;
			callEvents(new onScrollEvent());
		}
	}
	
	static class onMouseMoveEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int lastMousePosX = superLastMousePosX;
		public static int lastMousePosY = superLastMousePosY;
		public static boolean isScrollingup = superIsScrollingUp;
		public static boolean isScrollingdown = superIsScrollingDown;
		
		public static void callActionEvents()
		{
			callEvents(new onMouseMoveEvent());
		}
	}
	
	static class onleftClickDragEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int lastMousePosX = superLastMousePosX;
		public static int lastMousePosY = superLastMousePosY;
		public static int wheelScroll = superWheelScroll;
		public static boolean isScrollingup = superIsScrollingUp;
		public static boolean isScrollingdown = superIsScrollingDown;
		
		public static void callActionEvents()
		{
			callEvents(new onleftClickDragEvent());
		}
	}
	
	static class onRightClickDragEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int lastMousePosX = superLastMousePosX;
		public static int lastMousePosY = superLastMousePosY;
		public static int wheelScroll = superWheelScroll;
		public static boolean isScrollingup = superIsScrollingUp;
		public static boolean isScrollingdown = superIsScrollingDown;
		
		public static void callActionEvents()
		{
			callEvents(new onRightClickDragEvent());
		}
	}
}