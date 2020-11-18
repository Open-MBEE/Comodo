package comodo2.templates.elt.xml;

import comodo2.queries.QInterface;
import comodo2.queries.QSignal;
import comodo2.queries.QStereotype;
import comodo2.utils.FilesHelper;
import comodo2.utils.PropertyComparator;
import comodo2.utils.ReceptionComparator;
import java.util.TreeSet;
import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
//import org.eclipse.xtext.xbase.lib.IterableExtensions;

public class Mal implements IGenerator {

	@Inject
	private QStereotype mQStereotype;

	@Inject
	private QInterface mQInterface;

	@Inject
	private QSignal mQSignal;

	@Inject
	private Types mTypes;

	@Inject
	private FilesHelper mFilesHelper;

	/**
	 * Transform a cmdoInterface to a MAL ICD XML.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		/*
		Iterable<Interface> _filter = Iterables.<Interface>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), Interface.class);
		for (final Interface e : _filter) {
			if ((mQInterface.isToBeGenerated(e) && mQInterface.hasRequests(e))) {
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toXmlFilePath(mQInterface.getContainerPackage(e).getName())));
				fsa.generateFile(mFilesHelper.toXmlFilePath(mQInterface.getContainerPackage(e).getName()), this.generate(e));
				return;
			}
		}
		*/
		final TreeIterator<EObject> allContents = input.getAllContents();
		while (allContents.hasNext()) {
			EObject e = allContents.next();
			if (e instanceof Interface) {
				Interface i = (Interface)e; 
				if ((mQInterface.isToBeGenerated(i) && mQInterface.hasRequests(i))) {
					mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toXmlFilePath(mQInterface.getContainerPackage(i).getName())));
					fsa.generateFile(mFilesHelper.toXmlFilePath(mQInterface.getContainerPackage(i).getName()), this.generate(i));
					return;
				}
			}
		}
	}

	public CharSequence generate(final Interface i) {
		StringConcatenation str = new StringConcatenation();
		str.append(printXmlStart());
		str.append(explorePackageIncludes(mQInterface.getContainerPackage(i)));				
		str.append("    " + printPackageStart(i));		
		str.append(exploreEnumerations(mQInterface.getContainerPackage(i)), "    ");		
		str.append(exploreStructures(mQInterface.getContainerPackage(i)), "    ");
		str.append(exploreExceptions(mQInterface.getContainerPackage(i)), "    ");
		str.append(exploreUnions(mQInterface.getContainerPackage(i)), "    ");	
		str.append(exploreInterfaces(i), "    ");	
		str.append(printPackageEnd(i));	
		str.append(printXmlEnd());
		return str;
	}

	public CharSequence explorePackageIncludes(final org.eclipse.uml2.uml.Package p) {
		String str = "";
		for (PackageImport e : p.getPackageImports()) {
			if (e.getImportedPackage() != null) {
				str += "    <include href=\"" + e.getImportedPackage().getName() + ".xml\"/>\n";
			}
		}
		return str;
	}
	
	public CharSequence exploreEnumerations(final org.eclipse.uml2.uml.Package p) {
		StringConcatenation str = new StringConcatenation();
		/*
		final Function1<Enumeration, Boolean> _function = (Enumeration e1) -> {
			return Boolean.valueOf(mQStereotype.isComodoEnumeration(e1));
		};
		Iterable<Enumeration> _filter = IterableExtensions.<Enumeration>filter(Iterables.<Enumeration>filter(p.allOwnedElements(), Enumeration.class), _function);
		for(final Enumeration e : _filter) {
			str.append("    ");
			str.newLine();
			CharSequence _printEnumeration = this.printEnumeration(e);
			str.append(_printEnumeration);
			str.newLineIfNotEmpty();
		}
		*/
		for (Element e : p.allOwnedElements()) {
			if (mQStereotype.isComodoEnumeration(e)) {
				str.append("    ");
				str.newLine();
				str.append(printEnumeration((Enumeration)e));
				str.newLineIfNotEmpty();					
			}
		}
		str.append("        ");
		str.newLine();
		return str;
	}

	public CharSequence exploreStructures(final org.eclipse.uml2.uml.Package p) {
		StringConcatenation str = new StringConcatenation();
		/*
		final Function1<DataType, Boolean> _function = (DataType e1) -> {
			return Boolean.valueOf(mQStereotype.isComodoStructure(e1));
		};
		Iterable<DataType> _filter = IterableExtensions.<DataType>filter(Iterables.<DataType>filter(p.allOwnedElements(), DataType.class), _function);
		for(final DataType e : _filter) {
			str.append(printStructure(e));
		}
		*/
		for (Element e : p.allOwnedElements()) {
			if (mQStereotype.isComodoStructure(e)) {
				str.append(printStructure((DataType)e));
			}
		}
		return str;
	}

	public CharSequence exploreExceptions(final org.eclipse.uml2.uml.Package p) {
		StringConcatenation str = new StringConcatenation();
		/*
		final Function1<DataType, Boolean> _function = (DataType e1) -> {
			return Boolean.valueOf(mQStereotype.isComodoException(e1));
		};
		Iterable<DataType> _filter = IterableExtensions.<DataType>filter(Iterables.<DataType>filter(p.allOwnedElements(), DataType.class), _function);
		for(final DataType e : _filter) {
			str.append(printException(e));
		}
		*/
		for (Element e : p.allOwnedElements()) {
			if (mQStereotype.isComodoException(e)) {
				str.append(printException((DataType)e));
			}
		}
		return str;
	}

	public CharSequence exploreUnions(final org.eclipse.uml2.uml.Package p) {
		StringConcatenation str = new StringConcatenation();
		/*
		final Function1<DataType, Boolean> _function = (DataType e1) -> {
			return Boolean.valueOf(mQStereotype.isComodoUnion(e1));
		};
		Iterable<DataType> _filter = IterableExtensions.<DataType>filter(Iterables.<DataType>filter(p.allOwnedElements(), DataType.class), _function);
		for(final DataType e : _filter) {
			str.append(printUnion(e));
		}
		*/
		for (Element e : p.allOwnedElements()) {
			if (mQStereotype.isComodoUnion(e)) {
				str.append(printUnion((DataType)e));
			}
		}
		return str;
	}

	public CharSequence exploreInterfaces(final Interface i) {
		StringConcatenation str = new StringConcatenation();
		str.newLine();
		str.append("<interface name=\"" + i.getName() + "\">" + StringConcatenation.DEFAULT_LINE_DELIMITER);		
		str.append(exploreSignals(i));
		str.append("</interface>"  +  StringConcatenation.DEFAULT_LINE_DELIMITER);	
		str.newLine();
		return str;
	}

	public CharSequence exploreSignals(final Interface i) {
		StringConcatenation str = new StringConcatenation();
		/*
		final Function1<Reception, String> _function = (Reception e) -> {
			return e.getName();
		};
		List<Reception> _sortBy = IterableExtensions.<Reception, String>sortBy(i.getOwnedReceptions(), _function);
		for(final Reception r : _sortBy) {
			Signal _signal = r.getSignal();
			if (mQStereotype.isComodoCommand(((Element) _signal))) {
				str.append(printInterfaceMethod(r.getSignal(), mTypes.typeName(mQSignal.getReplyType(r.getSignal())), mQSignal.isReplyTypePrimitive(r.getSignal()), this.getExceptionDataTypeName(r)));
			}
		}
		*/
		TreeSet<Reception> allReceptions = new TreeSet<Reception>(new ReceptionComparator());
		for (final Reception r : i.getOwnedReceptions()) {
			allReceptions.add(r);
		}
		for(final Reception r : allReceptions) {
			Signal s = r.getSignal();
			if (s != null && mQStereotype.isComodoCommand(((Element) s))) {
				str.append(printInterfaceMethod(s, mTypes.typeName(mQSignal.getReplyType(s)), mQSignal.isReplyTypePrimitive(s), getExceptionDataTypeName(r)));
			}
		}

		return str;
	}

	public String getExceptionDataTypeName(final Reception r) {
		EList<Type> raisedExceptions = r.getRaisedExceptions();
		if (raisedExceptions != null && raisedExceptions.size() > 0) {
			Type t = raisedExceptions.get(0);
			return mTypes.typeName(t);
		}
		/*
		if (IterableExtensions.<Type>head(r.getRaisedExceptions()) != null) {
			return mTypes.typeName(IterableExtensions.<Type>head(r.getRaisedExceptions()));
		}
		*/
		return "";
	}

	/**
	 * Formatters
	 */

	public CharSequence printPackageStart(final Interface i) {
		return "<package name=\"" + mQInterface.getContainerPackage(i).getName() + "\">\n";		
	}

	public CharSequence printPackageEnd(final Interface i) {
		return "</package>\n";
	}

	public CharSequence printEnumeration(final Enumeration e) {
		StringConcatenation str = new StringConcatenation();
		str.newLine();
		str.append("<enum name=\"" + e.getName() + "\">" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		//EList<EnumerationLiteral> _ownedLiterals = e.getOwnedLiterals();
		for (final EnumerationLiteral i : e.getOwnedLiterals()) {
			str.append("    <enumerator name=\"" + i.getName() + "\" />" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		}
		str.append("</enum>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printStructure(final DataType d) {
		StringConcatenation str = new StringConcatenation();
		str.append("<struct name=\"" + d.getName() + "\">");
		/*
		final Function1<Property, String> _function = (Property e) -> {
			return e.getName();
		};
		List<Property> _sortBy = IterableExtensions.<Property, String>sortBy(d.getOwnedAttributes(), _function);
		for(final Property a : _sortBy) {
			str.append(printAttribute(a), "    ");
		}
		*/
		TreeSet<Property> allProperties = new TreeSet<Property>(new PropertyComparator());
		for (final Property p : d.getOwnedAttributes()) {
			allProperties.add(p);
		}
		for(final Property p : allProperties) {
			str.append(printAttribute(p), "    ");			
		}
		
		str.append(StringConcatenation.DEFAULT_LINE_DELIMITER + "</struct>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printException(final DataType d) {
		StringConcatenation str = new StringConcatenation();
		str.newLine();
		str.append("<exception name=\"" + d.getName() + "\">");
		/*
		final Function1<Property, String> _function = (Property e) -> {
			return e.getName();
		};
		List<Property> _sortBy = IterableExtensions.<Property, String>sortBy(d.getOwnedAttributes(), _function);
		for(final Property a : _sortBy) {
			str.append(printAttribute(a), "    ");
		}
		*/
		TreeSet<Property> allProperties = new TreeSet<Property>(new PropertyComparator());
		for (final Property p : d.getOwnedAttributes()) {
			allProperties.add(p);
		}
		for(final Property p : allProperties) {
			str.append(printAttribute(p), "    ");			
		}

		str.append(StringConcatenation.DEFAULT_LINE_DELIMITER + "</exception>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printUnion(final DataType d) {
		StringConcatenation str = new StringConcatenation();
		str.append("<union name=\"");
		str.append(d.getName());
		str.append("\">");
		str.newLineIfNotEmpty();
		str.append("  ");
		str.append("<discriminator type=\"int8_t\" />");
		str.newLine();
		str.append("  ");
		int i = 1;
		str.newLineIfNotEmpty();

		TreeSet<Property> allProperties = new TreeSet<Property>(new PropertyComparator());
		for (final Property p : d.getOwnedAttributes()) {
			allProperties.add(p);
		}
/*
		final Function1<Property, String> _function = (Property e) -> {
			return e.getName();
		};
		List<Property> _sortBy = IterableExtensions.<Property, String>sortBy(d.getOwnedAttributes(), _function);
		for(final Property a : _sortBy) {
*/
		for(final Property p : allProperties) {		
			str.append("  ");
			str.append("<case>");
			str.newLine();
			str.append("  ");
			str.append("  ");
			str.append("<caseDiscriminator value=\"");
			str.append(i++, "    ");
			str.append("\" />      ");
			str.newLineIfNotEmpty();
			str.append("  ");
			str.append("  ");
			str.append(printAttribute(p), "    ");
			str.newLineIfNotEmpty();
			str.append("  ");
			str.append("</case>");
			str.newLine();
		}		
		
		str.append("</union>    ");
		str.newLine();
		return str;
	}

	public CharSequence printAttribute(final Property p) {
		String str = "\n"; 
		str += "<member name=\"" + p.getName() + "\" " + printAttributeType(p);
		if (p.getUpper() > 1) {
			str += " arrayDimensions=\"(" + p.getUpper() + ")\"";
		} 
		str += "/>";
		return str;
	}

	public CharSequence printAttributeType(final Property p) {
		String str = "";
		if (mTypes.isPrimitiveType(p)) {
			str = "type=\"" + mTypes.typeName(p) + "\"";
		} else {
			str = "type=\"nonBasic\" nonBasicTypeName=\"" + mTypes.typeName(p) +"\"";
		}
		return str;
	}

	public CharSequence printInterfaceMethod(final Signal s, final String replyTypeName, final boolean replyTypeIsPrimitive, final String exceptionName) {
		StringConcatenation str = new StringConcatenation();
		str.append("    " + printInterfaceMethodHeader(s, replyTypeName, replyTypeIsPrimitive, exceptionName));
		/*
		final Function1<Property, String> _function = (Property e) -> {
			return e.getName();
		};
		List<Property> _sortBy = IterableExtensions.<Property, String>sortBy(s.getOwnedAttributes(), _function);
		boolean flag = true;
		for(final Property a : _sortBy) {			
			if (!Objects.equal(a.getName(), "reply")) {
				str.newLine();
				str.append(printInterfaceArgument(a));
				flag = false;
			}
		}
		*/
		TreeSet<Property> allProperties = new TreeSet<Property>(new PropertyComparator());
		for (final Property p : s.getOwnedAttributes()) {
			allProperties.add(p);
		}
		boolean flag = true;
		for(final Property p : allProperties) {
			if (p.getName().equalsIgnoreCase("reply") == false) {
				str.newLine();
				str.append(printInterfaceArgument(p));
				flag = false;
			}
		}
		
		if (flag) {
			str.newLine();
		}
		str.append("    " + "</method>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printInterfaceMethodHeader(final Signal s, final String replyTypeName, final boolean replyTypeIsPrimitive, final String exceptionName) {
		String str = ("<method name=\"" + mQSignal.nameWithoutPrefix(s) + "\" returnType=\"");
		if (replyTypeIsPrimitive) {
			str += replyTypeName + "\"";
		} else {
			str += "nonBasic" + "\" nonBasicReturnTypeName=\"" + replyTypeName + "\"";
		}
		if (exceptionName.isEmpty() == false) {
			str += " throws=\"" + exceptionName + "\"";
		}
		str += ">";
		return str;
	}

	public CharSequence printInterfaceArgument(final Property a) {
		String str = "        <argument name=\"" + a.getName() + "\" type=\"";
		if (mTypes.isPrimitiveType(a)) {
			str += mTypes.typeName(a) + "\"/>\n";
		} else {
			str += "nonBasic\" nonBasicTypeName=\"" + mTypes.typeName(a) + "\"/>\n";
		}
		return str;
	}

	public CharSequence printXmlStart() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		       "<types xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"schemas/icd_type_definition.xsd\">\n";
	}

	public CharSequence printXmlEnd() {
		return "</types>\n";
	}
}
