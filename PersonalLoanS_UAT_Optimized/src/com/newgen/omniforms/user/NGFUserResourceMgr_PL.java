//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: Phoenix
//Product / Project			: Form Builder
//Module					: FormApplet
//File Name					: NGFUserResourceMgr.java
//Author					: Vikas Tyagi
//Date written (DD/MM/YYYY)	: 06/10/2008
//Description				: to read from the resource file the corresponding localized string
//----------------------------------------------------------------------------------------------------
//			CHANGE HISTORY
// Date                          Change By       Change Description (Bug No. (If Any))
// (DD/MM/YYYY)
//1. 05-02-2015					Disha			//Added for Portuguese emz affiliate

//----------------------------------------------------------------------------------------------------

package com.newgen.omniforms.user;

import java.io.Serializable;
import java.util.*;

public class NGFUserResourceMgr_PL implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle plValidations = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.PL_Validation");
	
	private NGFUserResourceMgr_PL() {
	    PersonalLoanS.mLogger.info("Inside private constructor of NGFUserResourceMgr class");
	  }
	
	public static String getResourceString_plValidations(String str)
	{		
		PersonalLoanS.mLogger.info("::::::properties"+plValidations.getString(str));
		
		return plValidations.getString(str);
    }
	
}
