package comodo2.queries;

import comodo2.engine.Config;
import comodo2.queries.QPackage;
import comodo2.queries.QStereotype;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class QInterface {
  @Inject
  @Extension
  private QStereotype _qStereotype;
  
  @Inject
  @Extension
  private QPackage _qPackage;
  
  public org.eclipse.uml2.uml.Package getContainerPackage(final Interface i) {
    return i.getNearestPackage();
  }
  
  public boolean isToBeGenerated(final Interface i) {
    boolean _isComodoInterface = this._qStereotype.isComodoInterface(((Element) i));
    boolean _equals = (_isComodoInterface == false);
    if (_equals) {
      return false;
    }
    return this._qPackage.isParentComodoModule(i.getNearestPackage(), Config.getInstance().getCurrentModule());
  }
  
  public boolean hasRequests(final Interface i) {
    EList<Reception> _ownedReceptions = i.getOwnedReceptions();
    for (final Reception r : _ownedReceptions) {
      Signal _signal = r.getSignal();
      boolean _isComodoCommand = this._qStereotype.isComodoCommand(((Element) _signal));
      if (_isComodoCommand) {
        return true;
      }
    }
    return false;
  }
}
