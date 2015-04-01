/**
 * Java PathFinder Statechart (jpf-statechart)
 */

import gov.nasa.jpf.sc.*;

public class VariableCurvatureMirror extends State {

//
// State Machine: VariableCurvatureMirror
//

final SM_VariableCurvatureMirror sm_VariableCurvatureMirror = makeInitial(new SM_VariableCurvatureMirror());

public class SM_VariableCurvatureMirror extends State {

					boolean initOk = true;
		final S_VCM s_VCM = makeInitial(new S_VCM());
	
	
	
public class S_VCM extends State {

		//initial state for orthogonal region 'Protocol': 'PROTOCOL'
			
				final S_PROTOCOL s_PROTOCOL = makeInitial(new S_PROTOCOL());
				
	
		
public class S_PROTOCOL extends State {

		//initial state for composite state 'PROTOCOL': 'LOADED'
		  		final S_ERROR_P s_ERROR_P = new S_ERROR_P();
				final S_LOADED s_LOADED = makeInitial(new S_LOADED());
		  		final S_UNINITIALIZED s_UNINITIALIZED = new S_UNINITIALIZED();
		  		final S_INITIALIZED s_INITIALIZED = new S_INITIALIZED();
		  		final S_ONLINE s_ONLINE = new S_ONLINE();
		  		final S_CONTROLLING s_CONTROLLING = new S_CONTROLLING();
		  		final S_STANDBY s_STANDBY = new S_STANDBY();
		
public class S_ERROR_P extends State {

	

		
}
		
public class S_LOADED extends State {

		//initial state for composite state 'LOADED': 'UNINITIALIZED'
				final S_UNINITIALIZED s_UNINITIALIZED = makeInitial(new S_UNINITIALIZED());
		  		final S_INITIALIZED s_INITIALIZED = new S_INITIALIZED();
		
public class S_UNINITIALIZED extends State {

		public void INIT() {
					setNextState(s_INITIALIZED);
					return;
		}
	

		
}
		
public class S_INITIALIZED extends State {

public void entryAction() {
	log("Executing entry action: InitializeSystem");

	/* MARS
		// ... code to initialize the system

/*
 * No need to trigger the event using sendEvent since the model checker
 * tries all possible events!
 */
if (initOk) {				
 // Initialization successfull				 //sendEvent("INITIALIZED");
} else {
 // Initialization not successfull
 //sendEvent("ERROR_P");				}

	*/
}

	

		
}
	

		
}
		
public class S_ONLINE extends State {

		//initial state for composite state 'ONLINE': 'CONTROLLING'
				final S_CONTROLLING s_CONTROLLING = makeInitial(new S_CONTROLLING());
		
public class S_CONTROLLING extends State {

public void doAction() {
	log("Executing doActivity: Controlling");

	/* MARS
		// make sure that if the system is controlling the valves, it is also measuring!
assert(getMachine().isInState("s_STATE.s_MEASURING") == true) : "Controlling without measuring!";
	*/
} 
	

		
}
		public void LOWERLIMIT() {
					
					log("Executing action on transition: StopSysem");

					setNextState(s_STANDBY);
					return;
		}
		public void UPPERLIMIT() {
					
					log("Executing action on transition: StopSysem");

					setNextState(s_STANDBY);
					return;
		}
		public void STOP() {
					
					log("Executing action on transition: StopSystem");

					setNextState(s_STANDBY);
					return;
		}
	

		
}
		
public class S_STANDBY extends State {

	

		
}
		public void ERROR_IV() {
					setNextState(s_ERROR_P);
					return;
		}
		public void STANDBY() {
				/* MARS:
				if (ALH.inState("MEASURING")) {
				*/
					setNextState(s_STANDBY);
					return;
				// MARS: }
		}
		public void ERROR_EV() {
					setNextState(s_ERROR_P);
					return;
		}
		public void ERROR_P() {
					setNextState(s_ERROR_P);
					return;
		}
		public void ERROR_M() {
					setNextState(s_ERROR_P);
					return;
		}
		public void INIT() {
					setNextState(s_INITIALIZED);
					return;
		}
		public void ONLINE() {
				/* MARS:
				if (ALH.inState("STATE::Estimator::MEASURING")) {
				*/
					setNextState(s_ONLINE);
					return;
				// MARS: }
		}
		public void OFF() {
					setNextState(s_UNINITIALIZED);
					return;
		}
	

		
}
		
		
		//initial state for orthogonal region 'Estimator': 'NOT_MEASURING'
			
				final S_NOT_MEASURING s_NOT_MEASURING = makeInitial(new S_NOT_MEASURING());
				
			
		  		final S_MEASURING s_MEASURING = new S_MEASURING();
		  		
			
		  		final S_ERROR_M s_ERROR_M = new S_ERROR_M();
		  		
	
		
public class S_NOT_MEASURING extends State {

		public void INITIALIZED() {
					setNextState(s_MEASURING);
					return;
		}
	

		
}
		
		
	
		
public class S_MEASURING extends State {

public void doAction() {
	log("Executing doActivity: Estimating");

	/* MARS
		// make sure that if the system is not measuring, it is not controlling the valves!
assert(getMachine().isInState("s_STATE.s_PROTOCOL.s_ONLINE.s_CONTROLLING") == false) : "Do Controlling without measuring!";
	*/
} 
		public void ERROR_M() {
					setNextState(s_ERROR_M);
					return;
		}
		public void OFF() {
					setNextState(s_NOT_MEASURING);
					return;
		}
	

		
}
		
		
	
		
public class S_ERROR_M extends State {

		public void INIT() {
					setNextState(s_NOT_MEASURING);
					return;
		}
	

		
}
		
		
		//initial state for orthogonal region 'ExhaustValve': 'EXHAUST'
			
				final S_EXHAUST s_EXHAUST = makeInitial(new S_EXHAUST());
				
			
		  		final S_ERROR_EV s_ERROR_EV = new S_ERROR_EV();
		  		
	
		
public class S_EXHAUST extends State {

		//initial state for composite state 'EXHAUST': 'UNKNOWN_EV'
		  		final S_OPEN_EV s_OPEN_EV = new S_OPEN_EV();
		  		final S_CLOSED_EV s_CLOSED_EV = new S_CLOSED_EV();
				final S_UNKNOWN_EV s_UNKNOWN_EV = makeInitial(new S_UNKNOWN_EV());
		
public class S_OPEN_EV extends State {

public void entryAction() {
	log("Executing entry action: OpenExhaustValve");

}

		public void CLOSEEXHAUST() {
					setNextState(s_CLOSED_EV);
					return;
		}
	

		
}
		
public class S_CLOSED_EV extends State {

public void entryAction() {
	log("Executing entry action: CloseExhaustValve");

}

		public void OPENEXHAUST() {
					setNextState(s_OPEN_EV);
					return;
		}
	

		
}
		
public class S_UNKNOWN_EV extends State {

		public void INITIALIZED() {
					setNextState(s_CLOSED_EV);
					return;
		}
	

		
}
		public void ERROR_EV() {
					setNextState(s_ERROR_EV);
					return;
		}
	

		
}
		
		
	
		
public class S_ERROR_EV extends State {

		public void INIT() {
					setNextState(s_EXHAUST);
					return;
		}
	

		
}
		
		
		//initial state for orthogonal region 'IntakeValve': 'INTAKE'
			
				final S_INTAKE s_INTAKE = makeInitial(new S_INTAKE());
				
			
		  		final S_ERROR_IV s_ERROR_IV = new S_ERROR_IV();
		  		
	
		
public class S_INTAKE extends State {

		//initial state for composite state 'INTAKE': 'UNKNOWN_IV'
		  		final S_CLOSED_IV s_CLOSED_IV = new S_CLOSED_IV();
		  		final S_OPEN_IV s_OPEN_IV = new S_OPEN_IV();
				final S_UNKNOWN_IV s_UNKNOWN_IV = makeInitial(new S_UNKNOWN_IV());
		
public class S_CLOSED_IV extends State {

public void entryAction() {
	log("Executing entry action: CloseIntakeValve");

}

		public void OPENINTAKE() {
					setNextState(s_OPEN_IV);
					return;
		}
	

		
}
		
public class S_OPEN_IV extends State {

public void entryAction() {
	log("Executing entry action: OpenIntakeValve");

}

		public void CLOSEINTAKE() {
					setNextState(s_CLOSED_IV);
					return;
		}
	

		
}
		
public class S_UNKNOWN_IV extends State {

		public void INITIALIZED() {
					setNextState(s_CLOSED_IV);
					return;
		}
	

		
}
		public void ERROR_IV() {
					setNextState(s_ERROR_IV);
					return;
		}
	

		
}
		
		
	
		
public class S_ERROR_IV extends State {

		public void INIT() {
					setNextState(s_INTAKE);
					return;
		}
	

		
}
		
		
		//initial state for orthogonal region 'Pressure': 'PRESSURE'
			
				final S_PRESSURE s_PRESSURE = makeInitial(new S_PRESSURE());
				
	
		
public class S_PRESSURE extends State {

		//initial state for composite state 'PRESSURE': 'UNKNOWN_P'
		  		final S_ON_TARGET s_ON_TARGET = new S_ON_TARGET();
		  		final S_UNDER_PRESSURE s_UNDER_PRESSURE = new S_UNDER_PRESSURE();
		  		final S_OVER_PRESSURE s_OVER_PRESSURE = new S_OVER_PRESSURE();
				final S_UNKNOWN_P s_UNKNOWN_P = makeInitial(new S_UNKNOWN_P());
		
public class S_ON_TARGET extends State {

	

		
}
		
public class S_UNDER_PRESSURE extends State {

	

		
}
		
public class S_OVER_PRESSURE extends State {

	

		
}
		
public class S_UNKNOWN_P extends State {

	

		
}
		public void UNDERPRESSURE() {
					setNextState(s_UNDER_PRESSURE);
					return;
		}
		public void OVERPRESSURE() {
					setNextState(s_OVER_PRESSURE);
					return;
		}
		public void ONTARGET() {
					setNextState(s_ON_TARGET);
					return;
		}
	

		
}
		
		
		//initial state for orthogonal region 'CustomCmdHandler': 'IDLE'
			
				final S_IDLE s_IDLE = makeInitial(new S_IDLE());
				
			
		  		final S_SETTING_PRESSURE s_SETTING_PRESSURE = new S_SETTING_PRESSURE();
		  		
			
		  		final S_SETTING_CURVATURE s_SETTING_CURVATURE = new S_SETTING_CURVATURE();
		  		
	
		
public class S_IDLE extends State {

		public void SETCUR() {
				/* MARS:
				if (ALH.inState("CONTROLLING")) {
				*/
					setNextState(s_SETTING_CURVATURE);
					return;
				// MARS: }
		}
		public void SETPRE() {
				/* MARS:
				if (ALH.inState("CONTROLLING")) {
				*/
					setNextState(s_SETTING_PRESSURE);
					return;
				// MARS: }
		}
	

		
}
		
		
	
		
public class S_SETTING_PRESSURE extends State {

public void entryAction() {
	log("Executing entry action: SetTargetPressure");

}

		public void STOP() {
					
					log("Executing action on transition: ReplyErrToSETPRE");

					setNextState(s_IDLE);
					return;
		}
		public void after_120s() {
					
					log("Executing action on transition: ReplyErrToSETPRE");

					setNextState(s_IDLE);
					return;
		}
		public void ERROR_P() {
					
					log("Executing action on transition: ReplyErrToSETPRE");

					setNextState(s_IDLE);
					return;
		}
		public void SETCUR() {
					
					log("Executing action on transition: SupersedeSETPRE");

					setNextState(s_SETTING_CURVATURE);
					return;
		}
		public void SETPRE() {
					
					log("Executing action on transition: SupersedeSETPRE");

					setNextState(s_SETTING_PRESSURE);
					return;
		}
		public void ONTARGET() {
					
					log("Executing action on transition: ReplyOkToSETPRE");

					setNextState(s_IDLE);
					return;
		}
	
		public void timer120s() {
			setNextState(s_IDLE);
			return;
		}

		
}
		
		
	
		
public class S_SETTING_CURVATURE extends State {

public void entryAction() {
	log("Executing entry action: SetTargetCurvature");

}

		public void STOP() {
					
					log("Executing action on transition: ReplyErrToSETCUR");

					setNextState(s_IDLE);
					return;
		}
		public void after_120s() {
					
					log("Executing action on transition: ReplyErrToSETCUR");

					setNextState(s_IDLE);
					return;
		}
		public void ERROR_P() {
					
					log("Executing action on transition: ReplyErrToSETCUR");

					setNextState(s_IDLE);
					return;
		}
		public void SETCUR() {
					
					log("Executing action on transition: SupersedeSETCUR");

					setNextState(s_SETTING_CURVATURE);
					return;
		}
		public void SETPRE() {
					
					log("Executing action on transition: SupersedeSETCUR");

					setNextState(s_SETTING_PRESSURE);
					return;
		}
		public void ONTARGET() {
					
					log("Executing action on transition: ReplyOkToSETCUR");

					setNextState(s_IDLE);
					return;
		}
	
		public void timer120s() {
			setNextState(s_IDLE);
			return;
		}

		
}
		
		
		public void STOP() {
					
					log("Executing action on transition: StopSystem");

		}
		public void GETCUR() {
		}
		public void GETPRE() {
		}
	

	
}
		
		
}
}
