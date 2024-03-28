package com.newgen.omniforms.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;


public class PL_GeneratePDF {

	Logger mLogger=PersonalLoanS.mLogger;

	public static String PDFTemplate(boolean MainCIFFlag, String WINAME, String ActivityName, String IntegrationCall, String DocName, HashMap<String, String> GridDataMap,FormReference formObject)
	{
		PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", inside PDFTemplate function for IntegrationCall : "+IntegrationCall);

		String RetValue = "";
		try{
			if(IntegrationCall.equals("COMPLIANCE_CHECK"))
			{
				PersonalLoanS.mLogger.info("inside Fircosoft PDF");
				//PDF Generation****************************************
				try{
					String Xmlout="";

					String CustName = "";
					String Inputcustdob = "";
					String Inputcustnationality = "";							
					String CIF_NUMBER = "";							
					String EXPIRYDATE = "";						
					String GENDER = "";							
					String RESIDENCEADDRCOUNTRY = "";							
					String PASSPORT_NUMBER = "";

					String ReqBusinessUnit = "BUSINESSFINANCESME";						
					String PassportIssuingCountry = "AE";

					if(MainCIFFlag)
					{
						CustName = GridDataMap.get("COMPANY_NAME");
						Inputcustdob = GridDataMap.get("DATEOFINCORPORATION");
						Inputcustnationality = GridDataMap.get("ENTRY_NATIONALITY");							
						CIF_NUMBER = GridDataMap.get("CIF_NUMBER");							
						GENDER = "";							
						RESIDENCEADDRCOUNTRY = GridDataMap.get("COUNTRYOFINCORPORATION");							
						PASSPORT_NUMBER = "";
					}
					else
					{

						String CompayFlag = GridDataMap.get("COMPANYFLAG");

						if("Y".equalsIgnoreCase(CompayFlag))
						{
							CustName = GridDataMap.get("NAME_OF_SISTER_COMPANY").trim();
							Inputcustdob = GridDataMap.get("DATEOFINCORPORATION");
							RESIDENCEADDRCOUNTRY = GridDataMap.get("COUNTRY");
						}

						if("N".equalsIgnoreCase(CompayFlag))
						{
							String FirstName = GridDataMap.get("FIRSTNAME").trim();
							String MiddleName = GridDataMap.get("MIDDLENAME").trim();
							String LastName = GridDataMap.get("LASTNAME").trim();

							CustName = FirstName + " " + LastName;
							if(!"".equalsIgnoreCase(MiddleName))
								CustName = FirstName+" "+ MiddleName + " " + LastName;

							GENDER = GridDataMap.get("GENDER");
							if((GENDER).equalsIgnoreCase("F"))
								GENDER = "Female";
							if((GENDER).equalsIgnoreCase("M"))
								GENDER = "Male";

							Inputcustdob = GridDataMap.get("DATEOFBIRTH");
							RESIDENCEADDRCOUNTRY = GridDataMap.get("COUNTRYOFRESIDENCE");
							PASSPORT_NUMBER = GridDataMap.get("PASSPORTNUMBER");
						}

						Inputcustnationality = GridDataMap.get("NATIONALITY");							
						CIF_NUMBER = GridDataMap.get("CIF");							


					}

					String path = System.getProperty("user.dir");
					String pdfTemplatePath = "";
					String generatedPdfPath = "";

					String imgPath = "";
					String generatedimgPath = "";			


					//Reading path from property file doc
					/*Properties properties = new Properties();
							properties.load(new FileInputStream(System.getProperty("user.dir")+ System.getProperty("file.separator")+ "ConfigFiles" + System.getProperty("file.separator")+ "iRBL_SysCheckIntegration_Config.properties"));
					 */	
					PersonalLoanS.mLogger.info("CustName : "+CustName+", GENDER: "+GENDER+", Inputcustdob :" + Inputcustdob+", RESIDENCEADDRCOUNTRY :" +RESIDENCEADDRCOUNTRY+", PASSPORT_NUMBER :" +PASSPORT_NUMBER+", Inputcustnationality :" +Inputcustnationality+", CIF_NUMBER :" +CIF_NUMBER);

					String pdfName ="Wolrd_Check_Result";

					String dynamicPdfName =  WINAME+ pdfName + ".pdf";

					pdfTemplatePath = path + pdfTemplatePath;//Getting complete path of the pdf tempplate
					generatedPdfPath = System.getProperty("file.separator")+ "CustomConfig" + System.getProperty("file.separator")+ "Firco_Doc"+ System.getProperty("file.separator")+ "generated"+ System.getProperty("file.separator");//Get the loaction of the path where generated template will be saved
					generatedPdfPath += dynamicPdfName;
					generatedPdfPath = path + generatedPdfPath;//Complete path of generated PDF
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \nDedup GeneratedPdfPath :" + generatedPdfPath);

					String fileToBeaddPath = generatedPdfPath;
					PersonalLoanS.mLogger.info("fileToBeaddPath : " + fileToBeaddPath);
					FileOutputStream fileOutputStream = new FileOutputStream(generatedPdfPath);
					com.itextpdf.text.Document doc = new com.itextpdf.text.Document(PageSize.A4.rotate());
					PdfWriter writer = PdfWriter.getInstance(doc, fileOutputStream);
					writer.open();
					doc.open();
					Font bold = new Font(FontFamily.HELVETICA, 12, Font.BOLD);

					String dynamicimgName = "bank-logo.gif";
					generatedimgPath = System.getProperty("file.separator")+ "CustomConfig" + System.getProperty("file.separator")+ "Firco_Doc"+ System.getProperty("file.separator")+ "template"+ System.getProperty("file.separator")+ "Images"+ System.getProperty("file.separator");//Get the loaction of the path where generated template will be saved
					generatedimgPath += dynamicimgName;
					generatedimgPath = path + generatedimgPath;
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \nDedup generatedimgPath :" + generatedimgPath);								

					Paragraph preface = new Paragraph();
					//generatedimgPath=generatedimgPath.replace("/","//");
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \nDedup generatedimgPath aftr replace:" + generatedimgPath);
					Image img = Image.getInstance(generatedimgPath);

					img.setAlignment(Image.ALIGN_RIGHT);  
					img.scaleAbsolute(60f, 40f);

					preface.add(img);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After image");
					doc.add(preface);

					// Start - Header Fields formatted on 23112020 by Angad - Working format 1
					Font fontRed = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD,new BaseColor(230, 0, 0));

					preface = new Paragraph("Customer Details", fontRed);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);

					preface=new Paragraph("   ",bold);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);


					// Start - Header Fields formatted on 29112020 by Sowmya - Working format 2
					PdfPTable pdf1 = new PdfPTable(5);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable:");
					pdf1.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					int[] columnWidths1 ={15,30,5,15,30};
					pdf1.setWidths(columnWidths1);
					pdf1.setWidthPercentage(100);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 1:");

					Font fbld11 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h11 = new PdfPCell(new Phrase("CIF No",fbld11));
					h11.setBackgroundColor(new BaseColor(235, 235, 224));
					h11.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);													
					PdfPCell c11 = new PdfPCell(new Phrase(CIF_NUMBER,fbld11));
					c11.setBackgroundColor(BaseColor.WHITE);
					c11.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);													
					PdfPCell space11 = new PdfPCell(new Phrase("  ",fbld11));
					space11.setBackgroundColor(BaseColor.WHITE);
					space11.disableBorderSide(Rectangle.OUT_RIGHT);
					space11.disableBorderSide(Rectangle.OUT_TOP);
					space11.setBorderColor(BaseColor.WHITE);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 2:");
					Font fbld12 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h12 = new PdfPCell(new Phrase("Name",fbld12));
					h12.setBackgroundColor(new BaseColor(235, 235, 224));
					h12.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c12 = new PdfPCell(new Phrase(CustName,fbld12));
					c12.setBackgroundColor(BaseColor.WHITE);
					c12.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					pdf1.addCell(h11);
					pdf1.addCell(c11);
					pdf1.addCell(space11);
					pdf1.addCell(h12);
					pdf1.addCell(c12);
					doc.add(pdf1);
					preface=new Paragraph("   ",bold);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);


					PdfPTable pdf11 = new PdfPTable(5);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 3:");
					pdf11.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					int[] columnWidths11 = {15,30,5,15,30};
					pdf11.setWidths(columnWidths11);
					pdf11.setWidthPercentage(100);
					Font fbld13 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h13 = new PdfPCell(new Phrase("Expiry Date",fbld13));
					h13.setBackgroundColor(new BaseColor(235, 235, 224));
					h13.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c13 = new PdfPCell(new Phrase(EXPIRYDATE,fbld13));
					c13.setBackgroundColor(BaseColor.WHITE);
					c13.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell space13 = new PdfPCell(new Phrase("  ",fbld13));
					space13.setBackgroundColor(BaseColor.WHITE);
					space13.disableBorderSide(Rectangle.OUT_RIGHT);
					space13.disableBorderSide(Rectangle.OUT_TOP);
					space13.setBorderColor(BaseColor.WHITE);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 4:");
					Font fbld14 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h14 = new PdfPCell(new Phrase("Nationality",fbld14));
					h14.setBackgroundColor(new BaseColor(235, 235, 224));
					h14.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c14 = new PdfPCell(new Phrase(Inputcustnationality,fbld14));
					c14.setBackgroundColor(BaseColor.WHITE);
					c14.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);

					pdf11.addCell(h13);
					pdf11.addCell(c13);
					pdf11.addCell(space13);
					pdf11.addCell(h14);
					pdf11.addCell(c14);
					doc.add(pdf11);

					preface=new Paragraph("   ",bold);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);

					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 5:");
					PdfPTable pdf12 = new PdfPTable(5);
					pdf12.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					int[] columnWidths12 = {15,30,5,15,30};
					pdf12.setWidths(columnWidths12);
					pdf12.setWidthPercentage(100);
					Font fbld15 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h15 = new PdfPCell(new Phrase("Gender",fbld15));
					h15.setBackgroundColor(new BaseColor(235, 235, 224));
					h15.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c15 = new PdfPCell(new Phrase(GENDER,fbld15));
					c15.setBackgroundColor(BaseColor.WHITE);
					c15.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell space15 = new PdfPCell(new Phrase("  ",fbld15));
					space15.setBackgroundColor(BaseColor.WHITE);
					space15.disableBorderSide(Rectangle.OUT_RIGHT);
					space15.disableBorderSide(Rectangle.OUT_TOP);
					space15.setBorderColor(BaseColor.WHITE);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 6:");
					Font fbld16 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h16 = new PdfPCell(new Phrase("Residence Country",fbld16));
					h16.setBackgroundColor(new BaseColor(235, 235, 224));
					h16.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c16 = new PdfPCell(new Phrase(RESIDENCEADDRCOUNTRY,fbld16));
					c16.setBackgroundColor(BaseColor.WHITE);
					c16.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					pdf12.addCell(h15);
					pdf12.addCell(c15);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 16:");  
					pdf12.addCell(space15);
					pdf12.addCell(h16);
					pdf12.addCell(c16);
					doc.add(pdf12);

					preface=new Paragraph("   ",bold);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);


					PdfPTable pdf13 = new PdfPTable(5);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable:");
					pdf13.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					int[] columnWidths13 = {20,25,5,20,25};
					pdf13.setWidths(columnWidths13);
					pdf13.setWidthPercentage(100);
					Font fbld17 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h17 = new PdfPCell(new Phrase("Omniflow Reference No",fbld17));
					h17.setBackgroundColor(new BaseColor(235, 235, 224));
					h17.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c17 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridREFERENCENO.get(0),fbld17));
					c17.setBackgroundColor(BaseColor.WHITE);
					c17.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell space17 = new PdfPCell(new Phrase("  ",fbld17));
					space17.setBackgroundColor(BaseColor.WHITE);
					space17.disableBorderSide(Rectangle.OUT_RIGHT);
					space17.disableBorderSide(Rectangle.OUT_TOP);
					space17.setBorderColor(BaseColor.WHITE);

					Font fbld18 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h18 = new PdfPCell(new Phrase("Passport Issuing Country",fbld18));
					h18.setBackgroundColor(new BaseColor(235, 235, 224));
					h18.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c18 = new PdfPCell(new Phrase(PassportIssuingCountry,fbld18));
					c18.setBackgroundColor(BaseColor.WHITE);
					c18.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);

					pdf13.addCell(h17);
					pdf13.addCell(c17);
					pdf13.addCell(space17);
					pdf13.addCell(h18);
					pdf13.addCell(c18);
					doc.add(pdf13);

					preface=new Paragraph("   ",bold);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);

					PdfPTable pdf2 = new PdfPTable(8);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable:");
					pdf2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					int[] columnWidths2 = {22,13,5,25,16,5,12,12};
					pdf2.setWidths(columnWidths2);
					pdf2.setWidthPercentage(100);
					Font fbld19 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h19 = new PdfPCell(new Phrase("Requesting Business Unit",fbld19));
					h19.setBackgroundColor(new BaseColor(235, 235, 224));
					h19.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c19 = new PdfPCell(new Phrase(ReqBusinessUnit,fbld19));
					c19.setBackgroundColor(BaseColor.WHITE);
					c19.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell space19 = new PdfPCell(new Phrase("  ",fbld19));
					space19.setBackgroundColor(BaseColor.WHITE);
					space19.disableBorderSide(Rectangle.OUT_RIGHT);
					space19.setBorderColor(BaseColor.WHITE);

					Font fbld20 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h20 = new PdfPCell(new Phrase("Passport/Trading License No",fbld20));
					h20.setBackgroundColor(new BaseColor(235, 235, 224));
					h20.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c20 = new PdfPCell(new Phrase(PASSPORT_NUMBER,fbld20));
					c20.setBackgroundColor(BaseColor.WHITE);
					c20.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell space20 = new PdfPCell(new Phrase("  ",fbld20));
					space20.setBackgroundColor(BaseColor.WHITE);
					space20.disableBorderSide(Rectangle.OUT_RIGHT);
					space20.setBorderColor(BaseColor.WHITE);

					Font fbld21 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h21 = new PdfPCell(new Phrase("Date Of Birth",fbld21));
					h21.setBackgroundColor(new BaseColor(235, 235, 224));
					h21.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c21 = new PdfPCell(new Phrase(Inputcustdob,fbld21));
					c21.setBackgroundColor(BaseColor.WHITE);
					c21.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);


					pdf2.addCell(h19);
					pdf2.addCell(c19);
					pdf2.addCell(space19);
					pdf2.addCell(h20);
					pdf2.addCell(c20);
					pdf2.addCell(space20);
					pdf2.addCell(h21);
					pdf2.addCell(c21);
					doc.add(pdf2);

					preface=new Paragraph("   ",bold);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);

					String StatusDesc = "Pending";
					String Status = "";

					PdfPTable pdf3 = new PdfPTable(5);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable:");
					pdf3.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					int[] columnWidths3 = {15,30,5,20,30};
					pdf3.setWidths(columnWidths3);
					pdf3.setWidthPercentage(100);
					Font fbld22 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h22 = new PdfPCell(new Phrase("Status",fbld22));
					h22.setBackgroundColor(new BaseColor(235, 235, 224));
					h22.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c22 = new PdfPCell(new Phrase(Status,fbld22));
					c22.setBackgroundColor(BaseColor.WHITE);
					c22.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell space22 = new PdfPCell(new Phrase("  ",fbld22));
					space22.setBackgroundColor(BaseColor.WHITE);
					space22.disableBorderSide(Rectangle.OUT_RIGHT);
					space22.disableBorderSide(Rectangle.OUT_TOP);
					space22.setBorderColor(BaseColor.WHITE);

					Font fbld23 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);
					PdfPCell h23 = new PdfPCell(new Phrase("Status Description",fbld23));
					h23.setBackgroundColor(new BaseColor(235, 235, 224));
					h23.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					PdfPCell c23 = new PdfPCell(new Phrase(StatusDesc,fbld23));
					c23.setBackgroundColor(BaseColor.WHITE);
					c23.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);

					pdf3.addCell(h22);
					pdf3.addCell(c22);
					pdf3.addCell(space22);
					pdf3.addCell(h23);
					pdf3.addCell(c23);
					doc.add(pdf3);	

					// End - Header Fields formatted on 29112020 by Sowmya - Working format 2	


					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After preface 6:");

					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After preface 6:");

					preface=new Paragraph("   ",bold);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After preface 7:");
					preface=new Paragraph("Alert Details",fontRed);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After preface 8:");
					preface=new Paragraph("   ",bold);
					preface.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					doc.add(preface);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After preface 9:");
					PdfPTable pdf = new PdfPTable(10);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable :");
					pdf.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
					//int[] columnWidths = {35,50,50,55,40,55,50,50,50,50,95};
					int[] columnWidths = {8,8,8,8,8,8,8,8,8,25};
					pdf.setWidths(columnWidths);

					pdf.setWidthPercentage(100);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 1:");
					/*Font fbld1 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));

								PdfPCell h1 = new PdfPCell(new Phrase("SR No",fbld1));
								//System.out.println("Prepared");
								h1.setBackgroundColor(new BaseColor(153, 0, 51));
								h1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);*/

					//PdfPCell c1 = new PdfPCell(new Phrase("CIFID"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 2:");

					Font fbold1 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c1 = new PdfPCell(new Phrase("OFAC ID",fbold1));
					//System.out.println("Prepared");
					c1.setBackgroundColor(new BaseColor(153, 0, 51));
					c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 3:");
					//PdfPCell c1 = new PdfPCell(new Phrase("CIFID"));

					Font fbold2 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c2 = new PdfPCell(new Phrase("Matching Text",fbold2));
					//System.out.println("Prepared");
					c2.setBackgroundColor(new BaseColor(153, 0, 51));
					c2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					//PdfPCell c2 = new PdfPCell(new Phrase("CIFStatus"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 4:");

					Font fbold3 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c3 = new PdfPCell(new Phrase("Name",fbold3));
					//System.out.println("Prepared");
					c3.setBackgroundColor(new BaseColor(153, 0, 51));
					c3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					//PdfPCell c3 = new PdfPCell(new Phrase("FullName"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 5:");


					Font fbold4 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c4 = new PdfPCell(new Phrase("Origin",fbold4));
					//System.out.println("Prepared");
					c4.setBackgroundColor(new BaseColor(153, 0, 51));
					c4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					//PdfPCell c4 = new PdfPCell(new Phrase("EmiratesID"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 6:");

					Font fbold5 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c5 = new PdfPCell(new Phrase("Designation",fbold5));
					//System.out.println("Prepared");
					c5.setBackgroundColor(new BaseColor(153, 0, 51));
					c5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 7:");

					Font fbold6 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c6 = new PdfPCell(new Phrase("Date Of Birth",fbold6));
					//System.out.println("Prepared");
					c6.setBackgroundColor(new BaseColor(153, 0, 51));
					c6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					//PdfPCell c6 = new PdfPCell(new Phrase("Phone"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 8:");


					Font fbold7 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c7 = new PdfPCell(new Phrase("User Data1",fbold7));
					//System.out.println("Prepared");
					c7.setBackgroundColor(new BaseColor(153, 0, 51));
					c7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					//PdfPCell c6 = new PdfPCell(new Phrase("BFlag"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 9:"); 

					Font fbold8 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c8 = new PdfPCell(new Phrase("Nationality",fbold8));
					//System.out.println("Prepared");
					c8.setBackgroundColor(new BaseColor(153, 0, 51));
					c8.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					//PdfPCell c6 = new PdfPCell(new Phrase("BFlag"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 10:");  

					Font fbold9 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c9 = new PdfPCell(new Phrase("Passport",fbold9));
					//System.out.println("Prepared");
					c9.setBackgroundColor(new BaseColor(153, 0, 51));
					c9.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					//PdfPCell c6 = new PdfPCell(new Phrase("BFlag"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 11:");  

					Font fbold10 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD,new BaseColor(255,255,255));
					PdfPCell c10 = new PdfPCell(new Phrase("Additional Info",fbold10));
					//System.out.println("Prepared");
					c10.setBackgroundColor(new BaseColor(153, 0, 51));
					c10.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					//PdfPCell c6 = new PdfPCell(new Phrase("BFlag"));
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 12:");  

					try
					{
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append:");  
						//pdf.addCell(h1);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 1:");  
						pdf.addCell(c1);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 2:");  
						pdf.addCell(c2);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 3 :");  
						pdf.addCell(c3);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 4:");  
						pdf.addCell(c4);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 5:");  
						pdf.addCell(c5);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 6:");  
						pdf.addCell(c6);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 7:");  
						pdf.addCell(c7);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 8:");  
						pdf.addCell(c8);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 9:");
						pdf.addCell(c9);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 10:");
						pdf.addCell(c10);
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable append 11:");  
					}
					catch(Exception e)
					{
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", In catch After image : "+e.getStackTrace());
					}

					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", After PdfPTable 10:");
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", CIF_ID.size() "+PersonalLoanSCommonCode.FircoGridSRNo.size());
					for (int j = 0; j < PersonalLoanSCommonCode.FircoGridSRNo.size(); j++) {

						/*h1 = new PdfPCell(new Phrase(iRBLIntegration.FircoGridSRNo.get(j)));
									PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", SRNo "+iRBLIntegration.FircoGridSRNo.get(j));
									h1.setBackgroundColor(new BaseColor(255,251,240));
									pdf.addCell(h1);*/

						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", before adding OFACID:"+PersonalLoanSCommonCode.FircoGridOFACID.get(j));

						c1 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridOFACID.get(j)));
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", Aftr OFAC_ID for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridOFACID.get(j));
						c1.setBackgroundColor(new BaseColor(255,251,240));
						c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c1);								


						c2 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridMatchingText.get(j)));
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", Aftr MatchingText for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridMatchingText.get(j));
						c2.setBackgroundColor(new BaseColor(255,251,240));
						c2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c2);

						c3 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridName.get(j)));
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", Aftr Name for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridName.get(j));
						c3.setBackgroundColor(new BaseColor(255,251,240));
						c3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c3);

						try {
							c4 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridOrigin.get(j)));
							//System.out.println("Aftr EmiratesIDarray");
							PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n Origin value for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridOrigin.get(j));
							c4.setBackgroundColor(new BaseColor(255,251,240));
							c4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
							pdf.addCell(c4);
						} catch (Exception e){

						}

						c5 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridDestination.get(j)));
						//System.out.println("Aftr PassportNumberarray");
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n Designation value for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridDestination.get(j));
						c5.setBackgroundColor(new BaseColor(255,251,240));
						c5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c5);

						c6 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridDOB.get(j)));
						//System.out.println("aftr Nationalityarray");
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n Date_Of_Birth value for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridDOB.get(j));
						c6.setBackgroundColor(new BaseColor(255,251,240));
						c6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c6);



						c7 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridUserData1.get(j)));
						//System.out.println("aftr Phonearray");
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n UserData1 value for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridUserData1.get(j));
						c7.setBackgroundColor(new BaseColor(255,251,240));
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n PDF Generated check for WINAME "+WINAME);
						c7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c7);

						c8 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridNationality.get(j)));
						//System.out.println("aftr Phonearray");
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n Nationality value for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridNationality.get(j));
						c8.setBackgroundColor(new BaseColor(255,251,240));
						c8.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c8);

						c9 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridPassport.get(j)));
						//System.out.println("aftr Phonearray");
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n Passport value for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridPassport.get(j));
						c9.setBackgroundColor(new BaseColor(255,251,240));
						c9.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c9);

						c10 = new PdfPCell(new Phrase(PersonalLoanSCommonCode.FircoGridAdditionalInfo.get(j)));
						//System.out.println("aftr Phonearray");
						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n AdditionalInfo value for WINAME "+WINAME+" : "+PersonalLoanSCommonCode.FircoGridAdditionalInfo.get(j));
						c10.setBackgroundColor(new BaseColor(255,251,240));
						c10.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
						pdf.addCell(c10);	

						PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n PDF Generated check 1 for WINAME "+WINAME);           
						//doc.add(pdf);
					}

					doc.add(pdf);								
					doc.close();

					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n PDF Generated Successfully in target location for WINAME "+WINAME);

					String jtsIP = FormContext.getCurrentInstance().getFormConfig().getConfigElement("JTSIP");
					String jtsPort = "3333"; 
					String cabinetName=FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName"); 
					String volumeID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("VolumeId"); 
					String Name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
					String password = ""; 
					String sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
					String serverPort = "2809";
					String status=PersonalLoanSCommonCode.attachDocument(fileToBeaddPath,WINAME,DocName,jtsIP,jtsPort,cabinetName,volumeID,Name,password,sessionID,serverPort);
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n Response "+status);
					String[] statusAttachArr =status.split("~"); 
					String docStatus="";String outputDoc="";String docIndex="";String strDocISIndex="";
					PersonalLoanS.mLogger.info("check for docname by jahnavi"+DocName);
					for(int k=0;k<statusAttachArr.length;k++)
					{
						if(k==0)
							docStatus=statusAttachArr[k];
						if(k==1)
							outputDoc=statusAttachArr[k];
						if(k==2)
							docIndex=statusAttachArr[k];
						if(k==3)
							strDocISIndex=statusAttachArr[k];
					}
					
					PersonalLoanS.mLogger.info("check for docname by jahnavi docStatus: "+docStatus+" outputDoc: "+outputDoc+" docIndex: "+docIndex+" strDocISIndex: "+strDocISIndex);
					
					
					if(docStatus.equalsIgnoreCase("Y") ){
						formObject.setNGValue("Doc_waiver",outputDoc);
						// docIndex=formObject.getNGValue("cmplx_emp_ver_sp2_docsize")+","+docIndex;
						formObject.setNGValue("Doc_deferral",docIndex );
					}
					//***********DeleteFile*************
					File file_pdf = new File(generatedPdfPath);
					file_pdf.delete();
					//String strStatus = CommonMethods.DeleteFile(generatedPdfPath);
					//PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", \n strStatus of deleting the file in firco : "+strStatus);
					//**************************************
					RetValue = status;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//sMappOutPutXML="Exception"+e;
					PersonalLoanS.mLogger.info("WINAME : "+WINAME+", WSNAME: "+ActivityName+", in catch of FircosoftGeneratePDF Exception is: "+e);
					RetValue = "in catch of FircosoftGeneratePDF Exception is: "+e;
				}
				//*****************************
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured inside PDFTemplate: "+ e.getMessage());
		}

		return RetValue;
	}

}