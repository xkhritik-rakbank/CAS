//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: NGFUserResourceMgr.java
//Author					: Prateek Garg
//Date written (DD/MM/YYYY)	: 06/10/2017
//Description				: to read from the resource file the corresponding localized string
//----------------------------------------------------------------------------------------------------

package com.newgen.omniforms.user;

import java.io.FileReader;
import java.io.Serializable;
import java.util.*;

import com.newgen.omniforms.util.CommonUtilityMethods;

public class NGFUserResourceMgr_RLOS extends CommonUtilityMethods implements Serializable
{	
	private static final long serialVersionUID = 1L;
	public static final ResourceBundle rlosValidations = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.RLOS_Validation");
	public static final ResourceBundle rlosIfConditionValues = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.RLOS_IfConditionValues");
	public static final ResourceBundle mastermanagement = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.MasterManagementRLOS");
	public static final ResourceBundle mastermanagementsecond = PropertyResourceBundle.getBundle("com.newgen.omniforms.user.MasterManagementSecond");
	
	private NGFUserResourceMgr_RLOS() {
	  //  RLOS.mLogger.info("Inside private constructor of NGFUserResourceMgr class");
	  }
	
	public static String getAlert(String str)
	{		
		return rlosValidations.getString(str);
    }
	
	public static String getGlobalVar(String str)
	{		
		return rlosIfConditionValues.getString(str);
    }
	public static String getMasterManager(String str)
	{		
		RLOS.mLogger.info("Inside private constructor of NGFUserResourceMgr class"+mastermanagement.getString(str));
		return mastermanagement.getString(str);
    }
	public static String getMasterManagerSecond(String str)
	{		
		return mastermanagementsecond.getString(str);
    }
}
