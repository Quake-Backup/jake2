/*
 * Swap.java
 * Copyright (C) 2003
 * 
 * $Id: Swap.java,v 1.2 2003-11-18 08:48:05 rst Exp $
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
package jake2.game;

import jake2.Globals;

/**
 * Swap
 */
public final class Swap {

	public static void Init() {
		// set the byte swapping variables in a portable manner
		// TODO test endianess 
		if (Globals.bigendien) {
			Globals.endian= new BigEndianHandler();
		} else {
			Globals.endian= new LittleEndianHandler();
		}
	}

}