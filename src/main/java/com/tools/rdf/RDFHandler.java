package com.tools.rdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;


/**
 *
 *
 */
public class RDFHandler {
	private static final Logger log = LoggerFactory.getLogger(RDFHandler.class);
	private static long ID_GENERATOR = System.currentTimeMillis();
	private static XMLInputFactory factory = null;
	private XMLEventReader parser = null;
	private URI base = null;
	public static final String NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	static {
		log.info("## static block in RDF handler start");
		factory = XMLInputFactory.newInstance();
		factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
		factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
		log.info("## static block in RDF handler end");
	}

	/**
	 * constructor initialize the XMLInputFactory
	 */
	public RDFHandler() {
		log.info("## step 1.1 initialization");
	}

	public static class RDFEvent {
		URI subject = null;
		URI predicate = null;
		Object value = null;
		URI valueType = null;
		String lang = null;
		int listIndex = -1;

	}

	protected void found(RDFEvent event) throws XMLStreamException {
		try {
			found(event.subject, event.predicate, event.value, event.valueType,
					event.lang, event.listIndex);
		} catch (IOException err) {
			throw new XMLStreamException(err);
		}
	}

	public void found(URI subject, URI predicate, Object value, URI dataType,
			String lang, int index) throws IOException {

	}

	public void setBase(URI base) {
		this.base = base;
	}

	/** return XMLEventReader */
	protected XMLEventReader getReader() {
		return this.parser;
	}

	/** do the parsing */
	private void read(Reader in) throws XMLStreamException {
		try {
			/** create a XML Event parser */
			this.parser = factory.createXMLEventReader(in);

			/** loop until we find a rdf:RDF element */
			while (this.parser.hasNext()) {
				XMLEvent event = getReader().nextEvent();
				if (event.isStartElement()) {
					StartElement start = (StartElement) event;
					if (name2string(start).equals(NS + "RDF")) {
						Debug.debug("found RDF");
						parseRDF();
					}
				}
			}
		} catch (URISyntaxException e) {
			this.parser = null;
			throw new XMLStreamException(e);
		}
		this.parser = null;
	}

	private void parseRDF() throws XMLStreamException, URISyntaxException {
		Debug.debug("In RDF ROOT: loop over each rdf:Description");
		while (getReader().hasNext()) {
			XMLEvent event = getReader().nextEvent();

			if (event.isEndElement()) {
				return;
			} else if (event.isStartElement()) {
				parseDescription(event.asStartElement());
			} else if (event.isProcessingInstruction()) {
				throw new XMLStreamException(
						"Found Processing Instruction in RDF ???");
			} else if (event.isCharacters()
					&& event.asCharacters().getData().trim().length() > 0) {
				throw new XMLStreamException("Found text in RDF ???");
			}
		}
	}

	/**
	 * Parse description of a Resource
	 *
	 * @param description
	 * @return
	 * @throws URISyntaxException
	 * @throws XMLStreamException
	 */
	private URI parseDescription(StartElement description)
			throws URISyntaxException, XMLStreamException {
		Debug.debug("Found a new  rdf:Description " + description.getName());
		URI descriptionURI = null;
		Attribute att = description.getAttributeByName(new QName(NS, "about"));
		if (att != null)
			descriptionURI = createURI(att.getValue());

		if (descriptionURI == null) {
			descriptionURI = createAnonymousURI();
		}

		Debug.debug("Description uri=\"" + descriptionURI + "\"");

		QName qn = description.getName();
		log.debug("description.getName()" + description.getName()); // Description
		if (!(qn.getNamespaceURI().equals(NS) && qn.getLocalPart().equals("Description"))) {
			RDFEvent evt = new RDFEvent();
			evt.subject = descriptionURI;
			evt.predicate = createURI(NS + "type");
			evt.value = name2uri(qn);
			found(evt);
		}

		/** loop over attributes */
		for (Iterator<?> i = description.getAttributes(); i.hasNext();) {
			att = (Attribute) i.next();
			qn = att.getName();
			String local = qn.getLocalPart();
			if (qn.getNamespaceURI().equals(NS) && (local.equals("about"))) {
				continue;
			}
			Debug.debug("found Attribute;" + att);
			RDFEvent evt = new RDFEvent();
			evt.subject = descriptionURI;
			evt.predicate = name2uri(qn);
			evt.value = att.getValue();
			Debug.debug("subject is:" + evt.subject
					+ " and predicate is:" + evt.predicate + " and value is "
					+ evt.value);
			found(evt);
			Debug.debug("++++++++loop over attributes");
		}

		while (getReader().hasNext()) {
			XMLEvent event = getReader().nextEvent();

			if (event.isEndElement()) {
				return descriptionURI;
			} else if (event.isStartElement()) {
				parsePredicate(descriptionURI, event.asStartElement());
			} else if (event.isProcessingInstruction()) {
				throw new XMLStreamException(
						"Found Processing Instruction in RDF ???");
			} else if (event.isCharacters()
					&& event.asCharacters().getData().trim().length() > 0) {
				throw new XMLStreamException("Found text in RDF ??? \""
						+ event.asCharacters().getData() + "\"");
			}
		}

		return descriptionURI;
	}

	/**
	 * parse predicate
	 *
	 * @param descriptionURI
	 * @param predicate
	 * @throws URISyntaxException
	 * @throws XMLStreamException
	 */
	private void parsePredicate(URI descriptionURI, StartElement predicate) throws URISyntaxException, XMLStreamException {
		String parseType = null;
		String lang = null;
		URI datatype = null;
		Attribute att;
		QName qn = null;
		URI resource = null;

		URI predicateURI = name2uri(predicate.getName());
		Debug.debug("parse rdf:description=\"" + descriptionURI + "\" predicate:" + predicateURI);

		/** collect attributes */
		for (int loop = 0; loop < 2; ++loop) {
			for (Iterator<?> i = predicate.getAttributes(); i.hasNext();) {
				att = (Attribute) i.next();
				qn = att.getName();
				String local = qn.getLocalPart();
				if (qn.getPrefix().equals("xml") && local.equals("lang")) {
					if (loop == 0)
						lang = att.getValue();
					continue;
				} else if (qn.getNamespaceURI().equals(NS)) {
					if (local.equals("parseType")) {
						if (loop == 0)
							parseType = att.getValue();
						Debug.debug("parseType:" + parseType);
						continue;
					} else if (local.equals("datatype")) {
						if (loop == 0)
							/**
							 *       <rdfs:label rdf:datatype="http://www.w3.org/2000/01/rdf-schema#Literal">null</rdfs:label>
							      <subscription:hasCatalogType rdf:datatype="http://www.w3.org/2000/01/rdf-schema#Literal">FLEET</subscription:hasCatalogType>
							      <rdfs:label rdf:datatype="http://www.w3.org/2000/01/rdf-schema#Literal">John Deere</rdfs:label>
							 */
							datatype = createURI(att.getValue());
						Debug.debug("dataType=" + datatype);
						continue;
					} else if (local.equals("resource")) {
						if (loop == 0)
							resource = createURI(att.getValue());
						Debug.debug("rdf:resource=" + resource);
						continue;
					}
				}

				if (loop == 1) {
					if (resource != null) {
						Debug.debug(resource);
						RDFEvent evt = new RDFEvent();
						evt.subject = resource;
						evt.predicate = name2uri(att.getName());
						evt.value = att.getValue();
						found(evt);
					} else {
						throw new XMLStreamException("Cannot handle attribute "
								+ att);
					}
				}
			}
		}

		if (resource != null) {
			RDFEvent evt = new RDFEvent();
			evt.subject = descriptionURI;
			evt.predicate = predicateURI;
			evt.value = resource;
			found(evt);
			XMLEvent event = getReader().peek();
			if (event != null && event.isEndElement()) {
				getReader().nextEvent();
				return;
			}

			throw new XMLStreamException(
					"Expected a EndElement for this element");
		}

		if (parseType == null)
			parseType = "default";

		boolean foundResourceAsChild = false;
		StringBuilder b = new StringBuilder();
		while (getReader().hasNext()) {
			XMLEvent event = getReader().nextEvent();
			if (event.isStartElement()) {
				if (b.toString().trim().length() != 0) {
					throw new XMLStreamException("Bad text \"" + b
							+ "\" before " + event.asStartElement().getName());
				}
				URI childURI = parseDescription(event.asStartElement());
				RDFEvent evt = new RDFEvent();
				evt.subject = descriptionURI;
				evt.predicate = predicateURI;
				evt.value = childURI;
				found(evt);
				b.setLength(0);
				foundResourceAsChild = true;
			} else if (event.isCharacters()) {
				b.append(event.asCharacters().getData());
			} else if (event.isEndElement()) {
				if (!foundResourceAsChild) {
					RDFEvent evt = new RDFEvent();
					evt.subject = descriptionURI;
					evt.predicate = predicateURI;
					evt.value = b.toString();
					evt.lang = lang;
					evt.valueType = datatype;
					found(evt);
				} else {
					if (b.toString().trim().length() != 0)
						throw new XMLStreamException("Found bad text " + b);
				}
				return;

			}
		}// while

	}

	private URI resolveBase(String ID) throws URISyntaxException {
		if (this.base == null)
			return createURI(ID);
		return this.base.resolve(ID);
	}

	protected URI createAnonymousURI() throws URISyntaxException {
		return createURI("_" + (++ID_GENERATOR));
	}

	public void parse(InputStream in) throws XMLStreamException {
		read(new InputStreamReader(in));
	}

	public void parse(Reader in) throws XMLStreamException {
		read(in);
	}

	public void parse(File in) throws XMLStreamException {
		Debug.debug("Parsing file " + in);
		try {
			FileReader fin = new FileReader(in);
			read(fin);
			fin.close();
			Debug.debug("End Parsing file " + in);
		} catch (IOException e) {
			throw new XMLStreamException(e);
		}
	}

	public void parse(URL in) throws XMLStreamException {
		Debug.debug("parsing URL " + in);
		try {
			InputStream fin = in.openStream();
			read(new InputStreamReader(fin));
			fin.close();
		} catch (IOException e) {
			log.error("## IO exception from URL " + in.getPath()
					+ " exception is " + e.getMessage());
			throw new XMLStreamException(e);
		}
	}

	public void parse(URLConnection in) throws XMLStreamException {
		Debug.debug("parsing URL " + in);
		try {
			InputStream fin = in.getInputStream();
			read(new InputStreamReader(fin));
			fin.close();
		} catch (IOException e) {
			log.error("## IO exception from URL " + in.getLastModified()
					+ " exception is " + e.getMessage());
			throw new XMLStreamException(e);
		}
	}

	private String name2string(StartElement e) {
		return name2string(e.getName());
	}

	private String name2string(QName name) {
		return name.getNamespaceURI() + name.getLocalPart();
	}

	private URI name2uri(QName name) throws URISyntaxException {
		return createURI(name2string(name));
	}

	private URI createURI(String uriAsString) throws URISyntaxException {
		return new URI(uriAsString);
	}

	private static String escapeXML(String s) {
		return escape(s);
	}

	public static String escape(CharSequence s) {
		if (s == null)
			throw new NullPointerException("XML.escape(null)");
		int needed = -1;
		for (int i = 0; i < s.length(); ++i) {
			switch (s.charAt(i)) {
			case '\'':
			case '\"':
			case '&':
			case '<':
			case '>':
				needed = i;
				break;

			default:
				break;
			}
			if (needed != -1)
				break;
		}
		if (needed == -1)
			return s.toString();
		StringBuilder buffer = new StringBuilder(s.subSequence(0, needed));
		for (int i = needed; i < s.length(); ++i) {
			switch (s.charAt(i)) {
			case '\'':
				buffer.append("&apos;");
				break;
			case '\"':
				buffer.append("&quot;");
				break;
			case '&':
				buffer.append("&amp;");
				break;
			case '<':
				buffer.append("&lt;");
				break;
			case '>':
				buffer.append("&gt;");
				break;
			default:
				buffer.append(s.charAt(i));
				break;
			}
		}
		return buffer.toString();
	}


	/**
	 * testing inner static class -- internal debug class
	 *
	 * @author tianshi,bu
	 *
	 */
	public static class Debug {
		private static PrintStream out = System.err;
		private static boolean debugging = false;

		protected static String getStackTraceElement(int depth) {
			try {
				throw new Exception();
			} catch (Exception e) {
				StackTraceElement t = e.getStackTrace()[depth];

				return (t.getFileName() == null ? "" : t.getFileName() + ":")
						+ (t.getLineNumber() == -1 ? "" : t.getLineNumber()
								+ ":") + t.getMethodName();
			}
		}

		public static void setDebugging(boolean debugging) {
			Debug.debugging = debugging;
		}

		public static boolean isDebugging() {
			return debugging;
		}

		public static void debug(Object o) {
			if (!isDebugging())
				return;
			synchronized (out) {
				out.print("[DEBUG]" + getStackTraceElement(2) + " : ");
				if (o instanceof Throwable) {
					Throwable.class.cast(o).printStackTrace(out);
				} else {
					out.print(o);
				}
				out.println();
				out.flush();
			}
		}
	}// static class debug

}
