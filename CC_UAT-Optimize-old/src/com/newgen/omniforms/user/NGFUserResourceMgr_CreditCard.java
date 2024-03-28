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

import java.util.*;

public class NGFUserResourceMgr_CreditCard
{	
	public static final ResourceBundle ccValidations = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.CC_Validation");
	public static final ResourceBundle ccIfConditionValues = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.CC_IfConditionValues");
	
	private NGFUserResourceMgr_CreditCard() {
	    CreditCard.mLogger.info("Inside private constructor of NGFUserResourceMgr class");
	  }
	
	public static String getResourceString_ccValidations(String str)
	{		
		return ccValidations.getString(str);
    }
	
	public static String getResourceString_ccIfConditions(String str)
	{		
		return ccIfConditionValues.getString(str);
    }
}
