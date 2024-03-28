/*
---------------------------------------------------------------------------------------------------------
                  NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                   : Application - Projects
Project/Product			: SM (V1.0) 
Application				: SM
Module					: WorkItem Introduction in SM From AO  
File Name				: Validation.java
Author 					: Ajay Kumar
Date (DD/MM/YYYY)		: 29/06/2009
Description 			: Contains the basic Validation methods.
---------------------------------------------------------------------------------------------------------
                 	CHANGE HISTORY
---------------------------------------------------------------------------------------------------------

Problem No/CR No        Change Date           Changed By             Change Description
---------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------
*/

 
package com.newgen.mq;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>Title: SM</p>
 * <p>Description: Contains the basic Validation methods.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Newgen Software Technologies Ltd.</p>
 * @author Ajay Kumar
 * @version 1.0
 */
public class MapXML
{
	public static String getTagValue(String xml,String tag) throws ParserConfigurationException, SAXException, IOException  
	{
		Document doc=getDocument(xml);
		NodeList nodeList = doc.getElementsByTagName(tag);
		
		doc=null;
		int length = nodeList.getLength();
		
		if (length > 0) 
		{
			Node node =  nodeList.item(0);
			nodeList=null;
			// System.out.println("Node : " + node);
			if (node.getNodeType() == Node.ELEMENT_NODE) 
			{
				NodeList childNodes = node.getChildNodes();
				String value = "";
				int count = childNodes.getLength();
				for (int i = 0; i < count; i++) 
				{
					Node item = childNodes.item(i);
					if (item.getNodeType() == Node.TEXT_NODE) 
					{
						value += item.getNodeValue();
					}
					item=null;
				}
				childNodes=null;
				return value;
			} 
			else if (node.getNodeType() == Node.TEXT_NODE) 
			{
				return node.getNodeValue();
			}
			node=null;
		}
		return "";
	}	
	
	public static String getTagValueusingSubstring(String xml,String tag)  
	{
		try
		{
			return xml.substring(xml.indexOf("<"+tag+">")+tag.length()+2,xml.indexOf("</"+tag+">"));
					}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public static Document getDocument(String xml) throws ParserConfigurationException, SAXException, IOException  
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(xml)));
		return doc;
	}
	public static NodeList getNodeListFromDocument(Document doc,String identifier)
	{
		NodeList records = doc.getElementsByTagName(identifier);
		return records;
	}
}