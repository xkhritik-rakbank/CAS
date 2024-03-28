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

import com.newgen.omniforms.util.CommonUtilityMethods;

public class NGFUserResourceMgr_RLOS extends CommonUtilityMethods implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1180038945716132392L;
	public static final ResourceBundle rlosValidations = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.RLOS_Validation");
	public static final ResourceBundle rlosIfConditionValues = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.RLOS_IfConditionValues");
	
	private NGFUserResourceMgr_RLOS() {
	    RLOS.mLogger.info("Inside private constructor of NGFUserResourceMgr class");
	  }
	
	public static String getResourceString_rlosValidations(String str)
	{		
		return rlosValidations.getString(str);
    }
	
	public static String getResourceString_rlosIfConditions(String str)
	{		
		return rlosIfConditionValues.getString(str);
    }
}
