
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Initiation

File Name                                                         : CC_Initiation

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


//@Override
public class CC_Initiation extends CC_Common implements FormListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<String,String>m = new HashMap<String,String>(); 
	StringBuilder s=new StringBuilder();
	String old_field=null,new_field=null,value_old=null;
    String value_new=null;
    boolean IsFragLoaded=false;
    String queryData_load="";
	
	
    
	
	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}

		
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	 public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		        CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		        
		        
		        
		        switch (pEvent.getType()) {
		        
		        
		            
		            case MOUSE_CLICKED:
		            	new CC_CommonCode().mouse_clicked(pEvent);
		            	break;
		     
		            case FRAME_EXPANDED: 
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().FrameExpandEvent(pEvent);						

							break;
							
														
		            case FRAGMENT_LOADED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						fragment_loaded(pEvent,formObject);
						
						
			        break;
			       
			        
		          case VALUE_CHANGED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().value_changed(pEvent);
						break;
						
					
		             default:			break;
					}
		        }
		    

    

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : To Make Sheet Visible in DDVT Maker(8,9,11,12,13,15,16,17,18)              

	 ***********************************************************************************  */
	public void formLoaded(FormEvent arg0) {
		// TODO Auto-generated method stub
		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//formObject.setNGValue("Text1",formObject.getWFProcessName()+formObject.getWFActivityName());
	}
	
	
	 
	 
	 
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For setting the form header             

	 ***********************************************************************************  */
	public void formPopulated(FormEvent pEvent) {
		CreditCard.mLogger.info("Inside formPopulated()"+FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName"));
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Text7",formObject.getWFWorkitemName());
		formObject.setNGValue("Text1",formObject.getWFProcessName()+formObject.getWFActivityName());
		
		
	}

	
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
				
		
		CreditCard.mLogger.info("Inside submitFormStarted()" + pEvent.getSource());
  
        s.append(FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName")+"#");
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();

        Iterator<String> it = m.keySet().iterator();
        
       
        while(it.hasNext())
        {
        	String ss = it.next(); 
        	CreditCard.mLogger.info("Inside submitFormStarted() LOOP str is ");
        	
        		s.append(ss+",");        		
        	
        }
        String str = s.toString();
     
       
    	formObject.setNGValue("myString", str);
    	//formObject.setNGValue("Decision","SUBMIT");
    	CreditCard.mLogger.info("Inside submitFormStarted() str is " + str);
    	formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
    	saveIndecisionGrid();
    	
    	     	    
    	

	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		 if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
         	loadPicklist1();
		 } 	
     						  
		 if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
         	formObject.setLocked("Compliance_Frame1",true);
         	formObject.setLocked("ComplianceRemarks",false);
         	formObject.setLocked("ComplianceRemarks2",false);
		 } 	
	}
	
	
}