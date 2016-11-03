package com.tools.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbu on 11/3/2016.
 *
 * I am making use of the DOM parser implementation that comes with the JDK and in my example I am using JDK 7.
 * The DOM Parser loads the complete XML content into a Tree structure.
 * And we iterate through the Node and NodeList to get the content of the XML. The code for XML parsing using DOM parser is given below.
 *
 */
public class DOMParserDemo {

    public static void main(String[] args) throws Exception {
        //Get the DOM Builder Factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //Get the DOM Builder
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Load and Parse the XML document
        //document contains the complete XML as a Tree.
        Document document = builder.parse(ClassLoader.getSystemResourceAsStream("xml/employee.xml"));

        List<Employee> empList = new ArrayList();

        //Iterating through the nodes and extracting the data.
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {

            //We have encountered an <employee> tag.
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Employee emp = new Employee();
                emp.id = node.getAttributes().getNamedItem("id").getNodeValue();
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node cNode = childNodes.item(j);

                    // Identifying the child tag of employee encountered.
                    if (cNode.getNodeType() == Node.ELEMENT_NODE) {
                        String content = cNode.getLastChild().getTextContent().trim();
                        switch (cNode.getNodeName()) {
                            case "firstName":
                                emp.firstName = content;
                                break;
                            case "lastName":
                                emp.lastName = content;
                                break;
                            case "location":
                                emp.location = content;
                                break;
                        }
                    }
                }
                empList.add(emp);
            }

        }

        //Printing the Employee list populated.
        for (Employee emp : empList) {
            System.out.println(emp);
        }

    }
}

class Employee{
    String id;
    String firstName;
    String lastName;
    String location;

    @Override
    public String toString() {
        return firstName+" "+lastName+"("+id+")"+location;
    }
}