/**
 * This Class contains mainly methods to link through the mouseInput in Minecraft.
 * Because the given ForgeEvents aren't usable outside of an loaded world,
 * the MouseInput is monitored twice a tick, and linked to a custom MouseInputEventHandler.
 * Which is decent enough for guiControl, but isn't recommended for in-game use.
 * For in-game mouseEventHandling, use the ForgeEvents instead!
 */
package com.creativemd.ingameconfigmanager.api.common.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.creativemd.ingameconfigmanager.api.tab.core.InGameConfigManager;

public class MouseEvents extends Event
{
	public static final MouseEvents instance = new MouseEvents();
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
	
	
    private static boolean isCtrlKeyDown()
    {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    private static boolean isShiftKeyDown()
    {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
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

	public static class onLeftClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;

		protected static void callActionEvents()
		{
			callEvents(new onLeftClickEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onLeftMouseButtonPressEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onLeftMouseButtonPressEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onLeftMouseButtonDownEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onLeftMouseButtonDownEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onLeftMouseButtonReleaseEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onLeftMouseButtonReleaseEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onDoubleLeftClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onDoubleLeftClickEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onRightClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;

		protected static void callActionEvents()
		{
			callEvents(new onRightClickEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onRightMouseButtonPressEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onRightMouseButtonPressEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onRightMouseButtonDownEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onRightMouseButtonDownEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onRightMouseButtonReleaseEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onRightMouseButtonReleaseEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onDoubleRightClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onDoubleRightClickEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onMouseButtonPressEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int mouseButtonID;
		
		protected static void callActionEvents(int mousebuttonID)
		{
			mouseButtonID = mousebuttonID;
			callEvents(new onMouseButtonPressEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onMouseButtonDownEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int mouseButtonID;
		
		protected static void callActionEvents(int mousebuttonID)
		{
			mouseButtonID = mousebuttonID;
			callEvents(new onMouseButtonDownEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onMouseButtonReleaseEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int mouseButtonID;
		
		protected static void callActionEvents(int mousebuttonID)
		{
			mouseButtonID = mousebuttonID;
			callEvents(new onMouseButtonReleaseEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onDoubleButtonClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int mouseButtonID;
		
		protected static void callActionEvents(int mousebuttonID)
		{
			mouseButtonID = mousebuttonID;
			callEvents(new onDoubleButtonClickEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onWheelClickEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onWheelClickEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onWheelPressEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onWheelPressEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onWheelDownEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onWheelDownEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onWheelReleaseEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		
		protected static void callActionEvents()
		{
			callEvents(new onWheelReleaseEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onScrollEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int wheelScroll = superWheelScroll;
		
		protected static void callActionEvents(int wheel)
		{
			superWheelScroll = wheel;
			callEvents(new onScrollEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onMouseMoveEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int lastMousePosX = superLastMousePosX;
		public static int lastMousePosY = superLastMousePosY;
		public static boolean isScrollingup = superIsScrollingUp;
		public static boolean isScrollingdown = superIsScrollingDown;
		
		protected static void callActionEvents()
		{
			callEvents(new onMouseMoveEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onleftClickDragEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int lastMousePosX = superLastMousePosX;
		public static int lastMousePosY = superLastMousePosY;
		public static int wheelScroll = superWheelScroll;
		public static boolean isScrollingup = superIsScrollingUp;
		public static boolean isScrollingdown = superIsScrollingDown;
		
		protected static void callActionEvents()
		{
			callEvents(new onleftClickDragEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
	
	public static class onRightClickDragEvent
	{
		public static int mousePosX = superMousePosX;
		public static int mousePosY = superMousePosY;
		public static int lastMousePosX = superLastMousePosX;
		public static int lastMousePosY = superLastMousePosY;
		public static int wheelScroll = superWheelScroll;
		public static boolean isScrollingup = superIsScrollingUp;
		public static boolean isScrollingdown = superIsScrollingDown;
		
		protected static void callActionEvents()
		{
			callEvents(new onRightClickDragEvent());
		}
		
		/**
		 * Returns true if either shift key is down
		 */
		public boolean isShiftPressed()
		{
			return isShiftKeyDown();
		}
		
		/**
		 * Returns true if either windows ctrl key is down or if either mac meta key is down
		 */
		public boolean isCtrlPressed()
		{
			return isCtrlKeyDown();
		}
	}
}