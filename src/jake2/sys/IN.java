/*
 * IN.java
 * Copyright (C) 2003
 * 
 * $Id: IN.java,v 1.12 2004-01-18 09:41:57 hoz Exp $
 */
/*
Copyright (C) 1997-2001 Id Software, Inc.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package jake2.sys;

import java.awt.*;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.ImageIcon;

/**
 * IN
 */
public final class IN {
	
	static Component c = null;
	static Cursor emptyCursor = null;
	
	static boolean mouse_avail = true;
	static boolean mouse_active = false;
	static boolean ignorefirst = false;
	
	public static void ActivateMouse() {
//		if (!mouse_avail || c == null) return;
		if (!mouse_active) {
			KBD.mx = KBD.my = 0; // don't spazz
			install_grabs();
			mouse_active = true;
		}
	}
	
	public static void DeactivateMouse() {
//		if (!mouse_avail || c == null) return;
		if (mouse_active) {
			uninstall_grabs();
			mouse_active = false;
		}
	}

	private static void install_grabs() {
		if ( emptyCursor == null ) {
			ImageIcon emptyIcon = new ImageIcon(new byte[0]);
			emptyCursor = c.getToolkit().createCustomCursor(
				emptyIcon.getImage(), new Point(0,0), "emptyCursor");
		}
		c.setCursor(emptyCursor);
		KBD.centerMouse();
		
		ignorefirst = true;
	}
	
	private static void uninstall_grabs() {
		c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public static void toggleMouse() {
		if (mouse_active) DeactivateMouse();
		else ActivateMouse();
	}
		
	public static void Init() {
//		00468         in_joystick     = Cvar_Get ("in_joystick", "0", CVAR_ARCHIVE);		
	}
	
	public static void Shutdown() {
		RW.IN_Shutdown();	
	}
	
	public static void Real_IN_Init() {
//	00473         if (RW_IN_Init_fp)
//	00474                 RW_IN_Init_fp(&in_state);
	}
  
	public static void Commands() {
//	00485         if (RW_IN_Commands_fp)
//	00486                 RW_IN_Commands_fp();
	}
 
//	00489 void IN_Move (usercmd_t *cmd)
//	00490 {
//	00491         if (RW_IN_Move_fp)
//	00492                 RW_IN_Move_fp(cmd);
//	00493 }
 
	public static void Frame() {
//	00497         if (RW_IN_Activate_fp) 
//	00498         {
//	00499                 if ( !cl.refresh_prepped || cls.key_dest == key_console || cls.key_dest == key_menu)
//	00500                         RW_IN_Activate_fp(false);
//	00501                 else
//	00502                         RW_IN_Activate_fp(true);
//	00503         }
//	00504 
//	00505         if (RW_IN_Frame_fp)
//	00506                 RW_IN_Frame_fp();
	}
 
	public static void Activate(boolean active) {
	} 

}
