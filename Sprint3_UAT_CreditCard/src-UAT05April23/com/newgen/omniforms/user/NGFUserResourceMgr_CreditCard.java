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
	public static final ResourceBundle mastermanagement = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.MasterManagementCC");
	public static final ResourceBundle mastermanagementsecond = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.MasterManagementSecond");
	
	private NGFUserResourceMgr_CreditCard() {
	    CreditCard.mLogger.info("Inside private constructor of NGFUserResourceMgr class");
	  }
	
	public static String getAlert(String str)
	{		
		return ccValidations.getString(str);
    }
	
	public static String getGlobalVar(String str)
	{		
		return ccIfConditionValues.getString(str);
    }
	public static String getMasterManager(String str)
	{		
		CreditCard.mLogger.info("Inside private constructor of NGFUserResourceMgr class"+mastermanagement.getString(str));
		return mastermanagement.getString(str);
    }
	public static String getMasterManagerSecond(String str)
	{		
		return mastermanagementsecond.getString(str);
    }
}
