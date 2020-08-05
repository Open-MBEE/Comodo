package comodo2.templates.elt.xml;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.queries.QInterface;
import comodo2.queries.QSignal;
import comodo2.queries.QStereotype;
import comodo2.utils.FilesHelper;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

public class Mal implements IGenerator {
	@Inject
	@Extension
	private QStereotype mQStereotype;

	@Inject
	@Extension
	private QInterface mQInterface;

	@Inject
	@Extension
	private QSignal mQSignal;

	@Inject
	@Extension
	private Types mTypes;

	@Inject
	@Extension
	private FilesHelper mFilesHelper;

	/**
	 * Transform a cmdoInterface to a MAL ICD XML.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<Interface> _filter = Iterables.<Interface>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), Interface.class);
		for (final Interface e : _filter) {
			if ((mQInterface.isToBeGenerated(e) && mQInterface.hasRequests(e))) {
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toXmlFilePath(mQInterface.getContainerPackage(e).getName())));
				fsa.generateFile(mFilesHelper.toXmlFilePath(mQInterface.getContainerPackage(e).getName()), this.generate(e));
				return;
			}
		}
	}

	public CharSequence generate(final Interface i) {
		StringConcatenation str = new StringConcatenation();
		str.append(printXmlStart());
		str.append("\t" + printPackageStart(i));		
		str.append(exploreEnumerations(mQInterface.getContainerPackage(i)), "\t");		
		str.append(exploreStructures(mQInterface.getContainerPackage(i)), "\t");
		str.append(exploreExceptions(mQInterface.getContainerPackage(i)), "\t");
		str.append(exploreUnions(mQInterface.getContainerPackage(i)), "\t");	
		str.append(exploreInterfaces(i), "\t");	
		str.append(printPackageEnd(i));	
		str.append(printXmlEnd());
		return str;
	}

	public CharSequence exploreEnumerations(final org.eclipse.uml2.uml.Package p) {
		StringConcatenation _builder = new StringConcatenation();
		{
			final Function1<Enumeration, Boolean> _function = (Enumeration e1) -> {
				return Boolean.valueOf(mQStereotype.isComodoEnumeration(e1));
			};
			Iterable<Enumeration> _filter = IterableExtensions.<Enumeration>filter(Iterables.<Enumeration>filter(p.allOwnedElements(), Enumeration.class), _function);
			for(final Enumeration e : _filter) {
				_builder.append("\t");
				_builder.newLine();
				CharSequence _printEnumeration = this.printEnumeration(e);
				_builder.append(_printEnumeration);
				_builder.newLineIfNotEmpty();
			}
		}
		_builder.append("\t\t");
		_builder.newLine();
		return _builder;
	}

	public CharSequence exploreStructures(final org.eclipse.uml2.uml.Package p) {
		StringConcatenation str = new StringConcatenation();
		final Function1<DataType, Boolean> _function = (DataType e1) -> {
			return Boolean.valueOf(mQStereotype.isComodoStructure(e1));
		};
		Iterable<DataType> _filter = IterableExtensions.<DataType>filter(Iterables.<DataType>filter(p.allOwnedElements(), DataType.class), _function);
		for(final DataType e : _filter) {
			str.append(printStructure(e));
		}
		return str;
	}

	public CharSequence exploreExceptions(final org.eclipse.uml2.uml.Package p) {
		StringConcatenation str = new StringConcatenation();
		final Function1<DataType, Boolean> _function = (DataType e1) -> {
			return Boolean.valueOf(mQStereotype.isComodoException(e1));
		};
		Iterable<DataType> _filter = IterableExtensions.<DataType>filter(Iterables.<DataType>filter(p.allOwnedElements(), DataType.class), _function);
		for(final DataType e : _filter) {
			str.append(printException(e));
		}
		return str;
	}

	public CharSequence exploreUnions(final org.eclipse.uml2.uml.Package p) {
		StringConcatenation str = new StringConcatenation();
		final Function1<DataType, Boolean> _function = (DataType e1) -> {
			return Boolean.valueOf(mQStereotype.isComodoUnion(e1));
		};
		Iterable<DataType> _filter = IterableExtensions.<DataType>filter(Iterables.<DataType>filter(p.allOwnedElements(), DataType.class), _function);
		for(final DataType e : _filter) {
			str.append(printUnion(e));
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
		return str;
	}

	public String getExceptionDataTypeName(final Reception r) {
		//Type _head = IterableExtensions.<Type>head(r.getRaisedExceptions());
		//boolean _tripleNotEquals = (_head != null);
		if (IterableExtensions.<Type>head(r.getRaisedExceptions()) != null) {
			return mTypes.typeName(IterableExtensions.<Type>head(r.getRaisedExceptions()));
		}
		return "";
	}

	/**
	 * Formatters
	 */

	public CharSequence printPackageStart(final Interface i) {
		StringConcatenation str = new StringConcatenation();
		str.append("<package name=\"" + mQInterface.getContainerPackage(i).getName() + "\">" + StringConcatenation.DEFAULT_LINE_DELIMITER);		
		return str;
	}

	public CharSequence printPackageEnd(final Interface i) {
		StringConcatenation str = new StringConcatenation();
		str.append("</package>" +  StringConcatenation.DEFAULT_LINE_DELIMITER);		
		return str;
	}

	public CharSequence printEnumeration(final Enumeration e) {
		StringConcatenation str = new StringConcatenation();
		str.newLine();
		str.append("<enum name=\"" + e.getName() + "\">" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		//EList<EnumerationLiteral> _ownedLiterals = e.getOwnedLiterals();
		for (final EnumerationLiteral i : e.getOwnedLiterals()) {
			str.append("\t<enumerator name=\"" + i.getName() + "\" />" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		}
		str.append("</enum>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printStructure(final DataType d) {
		StringConcatenation str = new StringConcatenation();
		str.append("<struct name=\"" + d.getName() + "\">");
		final Function1<Property, String> _function = (Property e) -> {
			return e.getName();
		};
		List<Property> _sortBy = IterableExtensions.<Property, String>sortBy(d.getOwnedAttributes(), _function);
		for(final Property a : _sortBy) {
			str.append("\t" + printAttribute(a), "\t");
		}
		str.append(StringConcatenation.DEFAULT_LINE_DELIMITER + "</struct>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printException(final DataType d) {
		StringConcatenation str = new StringConcatenation();
		str.newLine();
		str.append("<exception name=\"" + d.getName() + "\">");
		final Function1<Property, String> _function = (Property e) -> {
			return e.getName();
		};
		List<Property> _sortBy = IterableExtensions.<Property, String>sortBy(d.getOwnedAttributes(), _function);
		for(final Property a : _sortBy) {
			str.append("\t" + printAttribute(a), "\t");
		}
		str.append(StringConcatenation.DEFAULT_LINE_DELIMITER + "</exception>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printUnion(final DataType d) {
		StringConcatenation _builder = new StringConcatenation();
		_builder.append("<union name=\"");
		String _name = d.getName();
		_builder.append(_name);
		_builder.append("\">");
		_builder.newLineIfNotEmpty();
		_builder.append("  ");
		_builder.append("<discriminator type=\"int8_t\" />");
		_builder.newLine();
		_builder.append("  ");
		int i = 1;
		_builder.newLineIfNotEmpty();
		{
			final Function1<Property, String> _function = (Property e) -> {
				return e.getName();
			};
			List<Property> _sortBy = IterableExtensions.<Property, String>sortBy(d.getOwnedAttributes(), _function);
			for(final Property a : _sortBy) {
				_builder.append("  ");
				_builder.append("<case>");
				_builder.newLine();
				_builder.append("  ");
				_builder.append("  ");
				_builder.append("<caseDiscriminator value=\"");
				int _plusPlus = i++;
				_builder.append(_plusPlus, "    ");
				_builder.append("\" />\t  ");
				_builder.newLineIfNotEmpty();
				_builder.append("  ");
				_builder.append("  ");
				CharSequence _printAttribute = this.printAttribute(a);
				_builder.append(_printAttribute, "    ");
				_builder.newLineIfNotEmpty();
				_builder.append("  ");
				_builder.append("</case>");
				_builder.newLine();
			}
		}
		_builder.append("</union>\t");
		_builder.newLine();
		return _builder;
	}

	public CharSequence printAttribute(final Property p) {
		StringConcatenation str = new StringConcatenation();
		str.newLine();
		str.append("<member name=\"" + p.getName() + "\" " + printAttributeType(p));
		if (p.getUpper() > 1) {
			str.append(" arrayDimensions=\"(" + p.getUpper() + ")\"");
		} 
		str.append("/>");
		return str;
	}

	public CharSequence printAttributeType(final Property p) {
		StringConcatenation str = new StringConcatenation();
		if (mTypes.isPrimitiveType(p)) {
			str.append("type=\"" + mTypes.typeName(p) + "\"");
		} else {
			str.append("type=\"nonBasic\" nonBasicTypeName=\"" + mTypes.typeName(p) +"\"");
		}
		return str;
	}

	public CharSequence printInterfaceMethod(final Signal s, final String replyTypeName, final boolean replyTypeIsPrimitive, final String exceptionName) {
		StringConcatenation str = new StringConcatenation();
		str.append("\t" + printInterfaceMethodHeader(s, replyTypeName, replyTypeIsPrimitive, exceptionName));
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
		if (flag) {
			str.newLine();
		}
		str.append("\t" + "</method>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printInterfaceMethodHeader(final Signal s, final String replyTypeName, final boolean replyTypeIsPrimitive, final String exceptionName) {
		String _nameWithoutPrefix = mQSignal.nameWithoutPrefix(s);
		String _plus = ("<method name=\"" + _nameWithoutPrefix);
		String str = (_plus + "\" returnType=\"");
		if (replyTypeIsPrimitive) {
			String _str = str;
			str = (_str + (replyTypeName + "\""));
		} else {
			String _str_1 = str;
			str = (_str_1 + ((("nonBasic" + "\" nonBasicReturnTypeName=\"") + replyTypeName) + "\""));
		}
		if (!Objects.equal(exceptionName, "")) {
			String _str_2 = str;
			str = (_str_2 + ((" throws=\"" + exceptionName) + "\""));
		}
		String _str_3 = str;
		str = (_str_3 + ">");
		return str;
	}

	public CharSequence printInterfaceArgument(final Property a) {
		StringConcatenation str = new StringConcatenation();
		if (mTypes.isPrimitiveType(a)) {
			str.append("\t\t<argument name=\"" + a.getName() + "\" type=\"" + mTypes.typeName(a) + "\"/>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		} else {
			str.append("\t\t<argument name=\"" + a.getName() + "\" type=\"nonBasic\" nonBasicTypeName=\"" + mTypes.typeName(a) + "\"/>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		}
		return str;
	}

	public CharSequence printXmlStart() {
		StringConcatenation str = new StringConcatenation();
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("<types xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"schemas/icd_type_definition.xsd\">" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}

	public CharSequence printXmlEnd() {
		StringConcatenation str = new StringConcatenation();
		str.append("</types>" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}
}
