package com.demo.rdf;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.demo.rdf.RDFHandler.RDFEvent;


public class RDFHelper {

	public static void main(String[] arg){
		try {
			parseRdfEngineByFile();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<RDFEvent> rdfEventList = new ArrayList<RDFEvent>();
	/**
	 *  local test class
	 *
	 */
	public static List parseRdfEngineByFile() throws MalformedURLException, XMLStreamException {
		rdfEventList = new ArrayList<RDFEvent>();


		try {
			RDFHandler h = new RDFHandler() {
				@Override
				public void found(RDFEvent event) {
					System.out.println("## step 1.N : Add RDF into List");
					rdfEventList.add(event);
				}
			};
			System.out.println("## step 2 : initialize RDFhandler");

			// dummy url

			// generate a rdftree list
			File file = new File("C:/Development/Eclipse352/eclipse/workspace-efp/dreambox/src/main/resources/rdf/rdf-1.xml");
			h.parse(file); // traverse the tree and populate static list
			System.out.println("## step 3 : Parsing is done ");

			for (int i = 0; i < rdfEventList.size();) {
				RDFEvent value = (RDFEvent) rdfEventList.get(i++);
				RDFEvent text = (RDFEvent) rdfEventList.get(i++);
				RDFEvent valueType = (RDFEvent) rdfEventList.get(i++);
				System.out.println("### Tree, text is " + text.value.toString()+ " <<--->>    value is " + value.value);
//				rdfList.add(rt);
			}

		} catch (Exception e) {
			System.out.println("## Exception Initialize the RDFHandler  msg: " + e.getMessage());
		}
		return null;
	}
}

