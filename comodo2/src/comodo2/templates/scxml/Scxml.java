package comodo2.templates.scxml;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Main;
import comodo2.queries.QClass;
import comodo2.queries.QRegion;
import comodo2.queries.QState;
import comodo2.queries.QStateMachine;
import comodo2.queries.QTransition;
import comodo2.utils.FilesHelper;
import java.util.List;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class Scxml implements IGenerator {
  @Inject
  @Extension
  private QStateMachine _qStateMachine;
  
  @Inject
  @Extension
  private QRegion _qRegion;
  
  @Inject
  @Extension
  private QState _qState;
  
  @Inject
  @Extension
  private QTransition _qTransition;
  
  @Inject
  @Extension
  private QClass _qClass;
  
  @Inject
  @Extension
  private FilesHelper _filesHelper;
  
  private static final Logger mLogger = Logger.getLogger(Main.class);
  
  /**
   * Transform UML State Machine associated to a class (classifier behavior)
   * into an SCXML document.
   * 
   * The UML Class should:
   * - be inside a UML Package with stereotype cmdoModule
   * - the cmdoModule name should have been provided in the configuration
   * - have stereotype cmdoComponent
   * - have an associated UML State Machine
   */
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
    for (final org.eclipse.uml2.uml.Class e : _filter) {
      if ((this._qClass.isToBeGenerated(e) && this._qClass.hasStateMachines(e))) {
        Iterable<StateMachine> _stateMachines = this._qClass.getStateMachines(e);
        for (final StateMachine sm : _stateMachines) {
          {
            this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toScxmlFilePath(sm.getName())));
            fsa.generateFile(this._filesHelper.toScxmlFilePath(sm.getName()), this.generate(sm));
          }
        }
      }
    }
  }
  
  public CharSequence generate(final StateMachine sm) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _printStateMachineStart = this.printStateMachineStart(sm);
    _builder.append(_printStateMachineStart);
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    CharSequence _exploreTopStates = this.exploreTopStates(sm);
    _builder.append(_exploreTopStates, "  ");
    _builder.newLineIfNotEmpty();
    CharSequence _printStateMachineEnd = this.printStateMachineEnd();
    _builder.append(_printStateMachineEnd);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * Start transformation from top level states.
   */
  public CharSequence exploreTopStates(final StateMachine sm) {
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<State, Boolean> _function = (State e) -> {
        return Boolean.valueOf(this._qState.isTopState(e));
      };
      final Function1<State, String> _function_1 = (State e) -> {
        return e.getName();
      };
      List<State> _sortBy = IterableExtensions.<State, String>sortBy(IterableExtensions.<State>filter(this._qStateMachine.getAllStates(sm), _function), _function_1);
      for(final State s : _sortBy) {
        CharSequence _exploreState = this.exploreState(s);
        _builder.append(_exploreState);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  /**
   * Transform a simple or composite state.
   */
  public CharSequence exploreState(final State s) {
    boolean _isSimple = s.isSimple();
    if (_isSimple) {
      return this.exploreSimpleState(s);
    } else {
      boolean _isComposite = s.isComposite();
      if (_isComposite) {
        return this.exploreCompositeState(s);
      }
    }
    String _name = s.getName();
    String _plus = ("COMODO2 ERROR transforming " + _name);
    return (_plus + " state!");
  }
  
  /**
   * Transform a simple state.
   */
  public CharSequence exploreSimpleState(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isFinal = this._qState.isFinal(s);
      if (_isFinal) {
        CharSequence _printFinalState = this.printFinalState(s);
        _builder.append(_printFinalState);
        _builder.newLineIfNotEmpty();
      } else {
        CharSequence _printStateStart = this.printStateStart(s);
        _builder.append(_printStateStart);
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        CharSequence _exploreActions = this.exploreActions(s);
        _builder.append(_exploreActions, "  ");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        CharSequence _exploreTransitions = this.exploreTransitions(s);
        _builder.append(_exploreTransitions, "  ");
        _builder.newLineIfNotEmpty();
        CharSequence _printStateEnd = this.printStateEnd();
        _builder.append(_printStateEnd);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  /**
   * Transform a composite state.
   * A composite state can:
   * - have orthogonal regions
   * - have substates (including final pseudo-states)
   * - have no substates
   */
  public CharSequence exploreCompositeState(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isOrthogonal = s.isOrthogonal();
      if (_isOrthogonal) {
        CharSequence _exploreOrthogonalState = this.exploreOrthogonalState(s);
        _builder.append(_exploreOrthogonalState);
        _builder.newLineIfNotEmpty();
      } else {
        boolean _isEmpty = IterableExtensions.isEmpty(this._qState.getAllNonFinalSubstates(s));
        boolean _not = (!_isEmpty);
        if (_not) {
          CharSequence _printStateStart = this.printStateStart(s);
          _builder.append(_printStateStart);
          _builder.newLineIfNotEmpty();
          _builder.append("  ");
          CharSequence _printInitial = this.printInitial(this._qState.getInitialSubstateName(s));
          _builder.append(_printInitial, "  ");
          _builder.newLineIfNotEmpty();
          {
            Iterable<State> _compositeSubstates = this._qState.getCompositeSubstates(s);
            for(final State ss : _compositeSubstates) {
              _builder.newLine();
              _builder.append("  ");
              CharSequence _exploreCompositeState = this.exploreCompositeState(ss);
              _builder.append(_exploreCompositeState, "  ");
              _builder.newLineIfNotEmpty();
            }
          }
          {
            Iterable<State> _simpleSubstates = this._qState.getSimpleSubstates(s);
            for(final State ss_1 : _simpleSubstates) {
              _builder.newLine();
              _builder.append("  ");
              CharSequence _exploreSimpleState = this.exploreSimpleState(ss_1);
              _builder.append(_exploreSimpleState, "  ");
              _builder.newLineIfNotEmpty();
            }
          }
          {
            Iterable<State> _finalSubstates = this._qState.getFinalSubstates(s);
            for(final State ss_2 : _finalSubstates) {
              _builder.newLine();
              _builder.append("  ");
              CharSequence _exploreSimpleState_1 = this.exploreSimpleState(ss_2);
              _builder.append(_exploreSimpleState_1, "  ");
              _builder.newLineIfNotEmpty();
            }
          }
          {
            boolean _hasHistory = this._qState.hasHistory(s);
            if (_hasHistory) {
              _builder.newLine();
              _builder.append("  ");
              CharSequence _exploreHistoryState = this.exploreHistoryState(s);
              _builder.append(_exploreHistoryState, "  ");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("  ");
          CharSequence _exploreActions = this.exploreActions(s);
          _builder.append(_exploreActions, "  ");
          _builder.newLineIfNotEmpty();
          _builder.append("  ");
          CharSequence _exploreTransitions = this.exploreTransitions(s);
          _builder.append(_exploreTransitions, "  ");
          _builder.newLineIfNotEmpty();
          CharSequence _printStateEnd = this.printStateEnd();
          _builder.append(_printStateEnd);
          _builder.newLineIfNotEmpty();
        } else {
          CharSequence _exploreSimpleState_2 = this.exploreSimpleState(s);
          _builder.append(_exploreSimpleState_2);
          _builder.newLineIfNotEmpty();
        }
      }
    }
    return _builder;
  }
  
  public CharSequence exploreOrthogonalState(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<parallel id=\"");
    String _stateName = this._qState.getStateName(s);
    _builder.append(_stateName);
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    {
      final Function1<Region, Boolean> _function = (Region e) -> {
        Element _owner = e.getOwner();
        return Boolean.valueOf(Objects.equal(_owner, s));
      };
      Iterable<Region> _filter = IterableExtensions.<Region>filter(Iterables.<Region>filter(s.allOwnedElements(), Region.class), _function);
      for(final Region r : _filter) {
        _builder.append("  ");
        _builder.append("<state id=\"");
        String _regionName = this._qRegion.getRegionName(r);
        _builder.append(_regionName, "  ");
        _builder.append("\">");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        CharSequence _printInitial = this.printInitial(this._qRegion.getInitialStateName(r));
        _builder.append(_printInitial, "    ");
        _builder.newLineIfNotEmpty();
        {
          final Function1<State, Boolean> _function_1 = (State e1) -> {
            Element _owner = e1.getOwner();
            return Boolean.valueOf(Objects.equal(_owner, r));
          };
          Iterable<State> _filter_1 = IterableExtensions.<State>filter(Iterables.<State>filter(r.allOwnedElements(), State.class), _function_1);
          for(final State substate : _filter_1) {
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            CharSequence _exploreState = this.exploreState(substate);
            _builder.append(_exploreState, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
        _builder.append("  ");
        CharSequence _printStateEnd = this.printStateEnd();
        _builder.append(_printStateEnd, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _hasHistory = this._qState.hasHistory(s);
      if (_hasHistory) {
        CharSequence _exploreHistoryState = this.exploreHistoryState(s);
        _builder.append(_exploreHistoryState);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    CharSequence _exploreActions = this.exploreActions(s);
    _builder.append(_exploreActions, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    CharSequence _exploreTransitions = this.exploreTransitions(s);
    _builder.append(_exploreTransitions, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("</parallel>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence exploreHistoryState(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Iterable<Pseudostate> _history = this._qState.getHistory(s);
      for(final Pseudostate hs : _history) {
        _builder.append("<history id=\"");
        String _regionName = this._qRegion.getRegionName(hs.getContainer());
        _builder.append(_regionName);
        _builder.append(":");
        String _name = hs.getName();
        _builder.append(_name);
        _builder.append("\" type=\"");
        String _historyTypeName = this._qState.getHistoryTypeName(hs);
        _builder.append(_historyTypeName);
        _builder.append("\">");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("<transition target=\"");
        String _regionName_1 = this._qRegion.getRegionName(hs.getOutgoings().get(0).getTarget().getContainer());
        _builder.append(_regionName_1, "\t");
        _builder.append(":");
        String _name_1 = hs.getOutgoings().get(0).getTarget().getName();
        _builder.append(_name_1, "\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
        _builder.append("</history>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public CharSequence exploreActions(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((this._qState.hasOnEntryActions(s) || this._qState.hasTimerTransition(s))) {
        CharSequence _printEntryActions = this.printEntryActions(s);
        _builder.append(_printEntryActions);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _hasDoActivities = this._qState.hasDoActivities(s);
      if (_hasDoActivities) {
        CharSequence _printDoActivities = this.printDoActivities(s);
        _builder.append(_printDoActivities);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((this._qState.hasOnExitActions(s) || this._qState.hasTimerTransition(s))) {
        CharSequence _printExitActions = this.printExitActions(s);
        _builder.append(_printExitActions);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence exploreTransitions(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<Transition, String> _function = (Transition e) -> {
        return e.getName();
      };
      List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
      for(final Transition t : _sortBy) {
        {
          boolean _isMalformed = this._qTransition.isMalformed(t);
          if (_isMalformed) {
            String _stateName = this._qState.getStateName(s);
            String _plus = ("Internal transition from state " + _stateName);
            String _plus_1 = (_plus + " has no trigger event and no guard, skipped since could introduce infinite loop!");
            Scxml.mLogger.warn(_plus_1);
            _builder.newLineIfNotEmpty();
          } else {
            CharSequence _printTransitionStart = this.printTransitionStart(this._qTransition.getFirstEventName(t), this._qTransition.getResolvedGuardName(t), this._qTransition.getTargetName(t), this._qTransition.hasAction(t));
            _builder.append(_printTransitionStart);
            _builder.newLineIfNotEmpty();
            {
              boolean _hasAction = this._qTransition.hasAction(t);
              if (_hasAction) {
                _builder.append("  ");
                CharSequence _printAction = this.printAction(this._qTransition.getFirstActionName(t));
                _builder.append(_printAction, "  ");
                _builder.newLineIfNotEmpty();
                CharSequence _printTransitionEnd = this.printTransitionEnd();
                _builder.append(_printTransitionEnd);
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  public CharSequence printInitial(final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<initial>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<transition target=\"");
    _builder.append(name, "  ");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("</initial>\t\t\t\t");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence printTransitionStart(final String eventName, final String guardName, final String targetName, final boolean hasAction) {
    String str = "<transition";
    boolean _notEquals = (!Objects.equal(eventName, ""));
    if (_notEquals) {
      String _str = str;
      str = (_str + ((" event=\"" + eventName) + "\""));
    }
    boolean _notEquals_1 = (!Objects.equal(guardName, ""));
    if (_notEquals_1) {
      String _str_1 = str;
      str = (_str_1 + ((" cond=\"" + guardName) + "\""));
    }
    boolean _notEquals_2 = (!Objects.equal(targetName, ""));
    if (_notEquals_2) {
      String _str_2 = str;
      str = (_str_2 + ((" target=\"" + targetName) + "\""));
    }
    if (hasAction) {
      String _str_3 = str;
      str = (_str_3 + ">");
    } else {
      String _str_4 = str;
      str = (_str_4 + "/>");
    }
    return str;
  }
  
  public CharSequence printTransitionEnd() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("</transition>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence printEntryActions(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<onentry>");
    _builder.newLine();
    _builder.append("  ");
    CharSequence _printAction = this.printAction(s.getEntry().getName());
    _builder.append(_printAction, "  ");
    _builder.newLineIfNotEmpty();
    {
      final Function1<Transition, String> _function = (Transition e) -> {
        return e.getName();
      };
      List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
      for(final Transition t : _sortBy) {
        {
          boolean _isTimerTransition = this._qTransition.isTimerTransition(t);
          if (_isTimerTransition) {
            _builder.append("<send target=\"\" type=\"scxml\" sendid=\"\'");
            String _stateName = this._qState.getStateName(s);
            _builder.append(_stateName);
            _builder.append("_");
            String _eventName = this._qTransition.getEventName(t);
            _builder.append(_eventName);
            _builder.append("\'\" event=\"\'");
            String _eventName_1 = this._qTransition.getEventName(t);
            _builder.append(_eventName_1);
            _builder.append("\'\" delay=\"\'");
            String _timeEventDuration = this._qTransition.getTimeEventDuration(t);
            _builder.append(_timeEventDuration);
            _builder.append("\'\"/>");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("</onentry>\t");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence printExitActions(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<onexit>");
    _builder.newLine();
    _builder.append("  ");
    CharSequence _printAction = this.printAction(s.getExit().getName());
    _builder.append(_printAction, "  ");
    _builder.newLineIfNotEmpty();
    {
      final Function1<Transition, String> _function = (Transition e) -> {
        return e.getName();
      };
      List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
      for(final Transition t : _sortBy) {
        {
          boolean _isTimerTransition = this._qTransition.isTimerTransition(t);
          if (_isTimerTransition) {
            _builder.append("<cancel sendid=\"\'");
            String _stateName = this._qState.getStateName(s);
            _builder.append(_stateName);
            _builder.append("_");
            String _eventName = this._qTransition.getEventName(t);
            _builder.append(_eventName);
            _builder.append("\'\"/>");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("</onexit>\t");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence printAction(final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<customActionDomain:");
    _builder.append(name);
    _builder.append(" name=\"");
    _builder.append(name);
    _builder.append("\"/>\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence printDoActivities(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<invoke id=\"");
    String _name = s.getDoActivity().getName();
    _builder.append(_name);
    _builder.append("\"/>\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence printStateStart(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<state id=\"");
    String _stateName = this._qState.getStateName(s);
    _builder.append(_stateName);
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence printStateEnd() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("</state>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence printFinalState(final State s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<final id=\"");
    String _stateName = this._qState.getStateName(s);
    _builder.append(_stateName);
    _builder.append("\"/>\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence printStateMachineStart(final StateMachine sm) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("<scxml xmlns=\"http://www.w3.org/2005/07/scxml\" xmlns:customActionDomain=\"http://my.custom-actions.domain/CUSTOM\" version=\"1.0\" initial=\"");
    String _initialStateName = this._qStateMachine.getInitialStateName(sm);
    _builder.append(_initialStateName);
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence printStateMachineEnd() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("</scxml>");
    _builder.newLine();
    return _builder;
  }
}
