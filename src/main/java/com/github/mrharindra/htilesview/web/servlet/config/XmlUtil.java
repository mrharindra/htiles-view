/*
MIT License

Copyright (c) 2025 Harindra Chaudhary

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.github.mrharindra.htilesview.web.servlet.config;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
			
			disableXXE(builderFactory);
			
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

	private static void disableXXE(DocumentBuilderFactory pDocumentBuilderFactory) throws ParserConfigurationException
	{	
		String features = "http://apache.org/xml/features/disallow-doctype-decl";
		pDocumentBuilderFactory.setFeature(features, true);
		
		features = "http://xml.org/sax/features/external-general-entities";
		pDocumentBuilderFactory.setFeature(features, false);

		features = "http://xml.org/sax/features/external-parameter-entities";
		pDocumentBuilderFactory.setFeature(features, false);

		// Disable external DTDs as well
		features = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
		pDocumentBuilderFactory.setFeature(features, false);

		pDocumentBuilderFactory.setXIncludeAware(false);
		pDocumentBuilderFactory.setExpandEntityReferences(false);
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
