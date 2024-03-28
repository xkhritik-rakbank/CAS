
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;


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

		
	             
	 public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		        SKLogger_CC.writeLog("Inside CC_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		        
		        
		        
		        switch (pEvent.getType()) {
		        
		        
		            
		            case MOUSE_CLICKED:
		            	/*if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Button1"))
		            					loadInGrid();*/
		            	
		            	if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button7"))
		            	{
		            		Date dNow = new Date( );
		            		
		  			      SimpleDateFormat ft = new SimpleDateFormat ("E  hh:mm:ss a zzz");
		  		        formObject.setNGValue("NotepadDetails_Text5",ft.format(dNow));
		            	}
		            	break;
		     
		            case FRAME_EXPANDED: 
						SKLogger_CC.writeLog(" In CC Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().FrameExpandEvent(pEvent);						

							break;
							
														
		            case FRAGMENT_LOADED:
						SKLogger_CC.writeLog(" In CC Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						 if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			                	loadPicklist1();
						 } 	
			            						  
						 if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
			                	formObject.setLocked("Compliance_Frame1",true);
			                	formObject.setLocked("ComplianceRemarks",false);
			                	formObject.setLocked("ComplianceRemarks2",false);
						 } 	
			        break;
			       
			        
		          case VALUE_CHANGED:
						SKLogger_CC.writeLog(" In CC Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
							 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
							 {
								 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
								SKLogger_CC.writeLog(" In CC Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

							 }
							 
							 else{
								 
								formObject.setNGValue("Decision", formObject.getNGValue("Decision_Combo2"));
								SKLogger_CC.writeLog(" In CC Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
							 }
					 }						
						
					
		             default:			break;
					}
		        }
		    

    

	
	public void formLoaded(FormEvent arg0) {
		// TODO Auto-generated method stub
		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//formObject.setNGValue("Text1",formObject.getWFProcessName()+formObject.getWFActivityName());
	}
	
	
	 
	 
	 
	public void formPopulated(FormEvent pEvent) {
		SKLogger_CC.writeLog("Inside CC_Initiation","Inside formPopulated()"+FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName"));
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Text7",formObject.getWFWorkitemName());
		formObject.setNGValue("Text1",formObject.getWFProcessName()+formObject.getWFActivityName());
		
		
	}

	
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	
	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
				
		
		SKLogger_CC.writeLog("Inside CC_Initiation","Inside submitFormStarted()" + pEvent.getSource());
  
        s.append(FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName")+"#");
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();

        Iterator<String> it = m.keySet().iterator();
        
       
        while(it.hasNext())
        {
        	String ss = it.next(); 
        	SKLogger_CC.writeLog("Inside CC_Initiation","Inside submitFormStarted() LOOP str is ");
        	
        		s.append(ss+",");        		
        	
        }
        String str = s.toString();
     
       
    	formObject.setNGValue("myString", str);
    	//formObject.setNGValue("Decision","SUBMIT");
    	SKLogger_CC.writeLog("Inside CC_Initiation","Inside submitFormStarted() str is " + str);
    	formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
    	saveIndecisionGrid();
    	
    	     	    
    	

	}
}