package com.github.mrharindra.htilesview.web.servlet.config;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


class XmlUtil 
{
	private static XPath sXpath = null;

	static
	{
		sXpath = XPathFactory.newInstance().newXPath();
	}

	public static Document parseXMLFile(String pXmlFilePath) throws Exception 
	{
		InputStream lInputStream = null;
		try
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(false);
			builderFactory.setValidating(false);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			lInputStream = new FileInputStream(pXmlFilePath);
			InputSource lInputSource = new InputSource( lInputStream );			
			Document xmlDocument = builder.parse( lInputSource );
			return xmlDocument;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if( lInputStream != null ) { 
				try{ 
					lInputStream.close(); 
				} 
				catch(Exception e){
				} 
			};
		}
	}


	/**
	 * @param pDocument
	 * @return
	 */
	public static Node getRootNode(Document pDocument)
	{
		NodeList nodeList = pDocument.getChildNodes();
		return nodeList.item(0);
	}


	/**
	 * @param tagName
	 * @param parentNode
	 * @return
	 */
	public static Node parseNode(String tagName, Node parentNode) throws Exception
	{
		return (Node) sXpath.evaluate(tagName, parentNode, XPathConstants.NODE);
	}

	/**
	 * @param pXmlTagName
	 * @param pParentNode
	 * @return
	 */
	public static NodeList parseNodeList(String pXmlTagName, Node pParentNode) throws Exception
	{
		return (NodeList) sXpath.evaluate(pXmlTagName, pParentNode, XPathConstants.NODESET);
	}

	/** Get the value of given node
	 * @param pXmlTagName - node to be parsed as string
	 * @param pParentNode - parent node
	 * @return
	 */
	public static String parseString(String pXmlTagName, Node pParentNode) throws Exception
	{
		return (String) sXpath.evaluate(pXmlTagName+"/text()", pParentNode, XPathConstants.STRING);
	}
	
	/** Get the attribute value
	 * @param pXmlTagName
	 * @param pNode
	 * @return
	 */
	public static String parseAttribute(String attributeName, Node pNode) throws Exception
	{
		return (String) sXpath.evaluate("@"+attributeName, pNode, XPathConstants.STRING);
	}

	public static String escapeForXML(String pStringXML)
	{
		if(pStringXML == null || pStringXML.trim().isEmpty())
		{
			return pStringXML;
		}
		
		return pStringXML.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
	}

}
