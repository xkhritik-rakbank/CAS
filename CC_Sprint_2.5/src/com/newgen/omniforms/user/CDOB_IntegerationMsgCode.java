package com.newgen.omniforms.user;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;

public class CDOB_IntegerationMsgCode {
	static FormReference formObject = FormContext.getCurrentInstance().getFormReference();

	/*public static String Common_IntegrationMsg(String CallName,String ReturnCode)
	{
		String ReturnMsg="";
		if("000"==CallName || "0000"==CallName)
		{
			ReturnMsg="success";
		}
		else
		{
		ReturnMsg=formObject.getNGDataFromDataCache("SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE Integration_call='"+CallName+"' AND error_code='"+ReturnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  Integration_call='"+CallName+"' AND error_code='DEFAULT'))").get(0).get(0);
		}
		return ReturnMsg;
	}*/
	}