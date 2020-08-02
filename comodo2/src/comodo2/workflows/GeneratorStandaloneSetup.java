package comodo2.workflows;

import java.net.URL;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.xtext.ISetup;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GeneratorStandaloneSetup implements ISetup {
	private Injector injector;
	private GeneratorConfig config;
	
	public void setConfig(GeneratorConfig config) {
		this.config = config;
	}
	
	public void setDoInit (boolean init) {
		createInjectorAndDoEMFRegistration();
	}
	

	public GeneratorStandaloneSetup () {
	}
	
	private Module getDynamicModule () {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(GeneratorConfig.class).toInstance(config);
			}
		};
	}

	@Override
	public Injector createInjectorAndDoEMFRegistration() {
		if (injector == null) {
			injector = Guice.createInjector(new UMLGeneratorModule(), getDynamicModule());
			register(injector);
		}
		return injector;
	}

	public void register(Injector injector) {
		org.eclipse.xtext.resource.IResourceFactory resourceFactory = injector.getInstance(org.eclipse.xtext.resource.IResourceFactory.class);
		org.eclipse.xtext.resource.IResourceServiceProvider serviceProvider = injector.getInstance(org.eclipse.xtext.resource.IResourceServiceProvider.class);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", resourceFactory);
		org.eclipse.xtext.resource.IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", serviceProvider);
		
		registerUMLPathMaps();
	}
	
	protected void registerUMLPathMaps () {
		// Compute the base URI for org.eclipse.uml2.uml.resources plugin archive
		String umlResourcesPath = null;
		String p = "metamodels/UML.metamodel.uml";
		URL url = Thread.currentThread().getContextClassLoader().getResource("metamodels/UML.metamodel.uml");
		URI uri = URI.createURI(url.toExternalForm());
		if (uri == null || uri.toString().equals(p)) {
			throw new IllegalStateException("Missing required plugin 'org.eclipse.uml2.uml.resources' in classpath.");
		}
		umlResourcesPath = uri.toString();
		final int mmIndex = umlResourcesPath.lastIndexOf("/metamodels");
		if (mmIndex < 0)
			throw new IllegalStateException("Missing required plugin 'org.eclipse.uml2.uml.resources' in classpath.");

		umlResourcesPath = umlResourcesPath.substring(0, mmIndex);

		// 
		URIConverter.URI_MAP.put(URI.createURI("pathmap://UML_PROFILES/"), URI.createURI(umlResourcesPath+"/profiles/"));
		URIConverter.URI_MAP.put(URI.createURI("pathmap://UML_METAMODELS/"), URI.createURI(umlResourcesPath+"/metamodels/"));
		URIConverter.URI_MAP.put(URI.createURI("pathmap://UML_LIBRARIES/"), URI.createURI(umlResourcesPath+"/libraries/"));
	}
	
}
