package comodo2.queries;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QPackage;
import comodo2.queries.QStereotype;
import javax.inject.Inject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class QClass {
  @Inject
  @Extension
  private QStereotype _qStereotype;
  
  @Inject
  @Extension
  private QPackage _qPackage;
  
  /**
   * This function check whether a class has associated
   * state machine as classifier behavior.
   * 
   * @param c Class with/without classifier behavior.
   * @return true If the given Class has a State Machine, false otherwise.
   */
  public boolean hasStateMachines(final org.eclipse.uml2.uml.Class c) {
    boolean _isEmpty = IterableExtensions.isEmpty(this.getStateMachines(c));
    return (!_isEmpty);
  }
  
  /**
   * This function returns the list of state machines defining
   * a class' classifier behavior.
   * 
   * @param c Class with classifier behavior.
   * @return The list of State Machines associated to the given Class.
   */
  public Iterable<StateMachine> getStateMachines(final org.eclipse.uml2.uml.Class c) {
    return Iterables.<StateMachine>filter(c.allOwnedElements(), StateMachine.class);
  }
  
  /**
   * This function returns the first state machine defining a
   * class' classifier behavior.
   * 
   * @param c Class with classifier behavior.
   * @return The first State Machine associated to the given Class.
   */
  public StateMachine getFirstStateMachine(final org.eclipse.uml2.uml.Class c) {
    return IterableExtensions.<StateMachine>head(this.getStateMachines(c));
  }
  
  public org.eclipse.uml2.uml.Package getContainerPackage(final org.eclipse.uml2.uml.Class c) {
    return c.getNearestPackage();
  }
  
  public String getContainerPackageName(final org.eclipse.uml2.uml.Class c) {
    org.eclipse.uml2.uml.Package _nearestPackage = c.getNearestPackage();
    boolean _tripleEquals = (_nearestPackage == null);
    if (_tripleEquals) {
      return "";
    }
    return c.getNearestPackage().getName();
  }
  
  public boolean isToBeGenerated(final org.eclipse.uml2.uml.Class c) {
    boolean _isComodoComponent = this._qStereotype.isComodoComponent(((Element) c));
    boolean _equals = (_isComodoComponent == false);
    if (_equals) {
      return false;
    }
    return this._qPackage.isParentComodoModule(c.getNearestPackage(), Config.getInstance().getCurrentModule());
  }
}
