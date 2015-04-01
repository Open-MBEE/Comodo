/**
 * Java PathFinder Statechart (jpf-statechart)
 */

import gov.nasa.jpf.sc.*;

public class ThreeStates extends State {

	//
	// State Machine: ThreeStates
	//

	final SM_ThreeStates sm_ThreeStates = makeInitial(new SM_ThreeStates());

	public class SM_ThreeStates extends State {

		int intValue = 42;
		double realValue = 3.14;
		boolean boolValue = true;
		int[] intArray = new int[25];
		String strValue = "test";
		int intVal2;
		boolean e1AlreadyReceived = false;
		final S_A s_A = makeInitial(new S_A());

		public class S_A extends State {

			public void entryAction() {
				log("Executing entry action: myOpaqueBehavior");

				/* MARS
					int i = 0;
				i++;
				 */
			}

			public void doAction() {
				log("Executing doActivity: muOpaqueBehavior2");

				/* MARS
					int j = 2;
				j++;
				 */
			}
			public void exitAction() {
				log("Executing exit action: exitBehavior");

				/* MARS
					int q = 5;
				q*=5;
				// my comment
				 */
			}

			public void e1() {
				/* MARS:
				if (boolValue == true) {
				 */

				log("Executing action on transition: transBehavior");

				/* MARS
					e1AlreadyReceived = true;
				 */
				setNextState(s_B);
				return;
				// MARS: }
				/* MARS:
				if (boolValue == false) {
				 */
				setNextState(s_C);
				return;
				// MARS: }
			}
			public void e6() {

				log("Executing action on transition: Assert");

				/* MARS
					assert (e1AlreadyReceived==true) : "e1 should be executed before e6";
				 */
				setNextState(s_C);
				return;
			}

		}

		final S_B s_B = new S_B();

		public class S_B extends State {

			public void e3() {
				setNextState(s_C);
				return;
			}
			public void e2() {
				setNextState(s_A);
				return;
			}

		}

		final S_C s_C = new S_C();

		public class S_C extends State {

			public void f() {
				setEndState();
				return;
			}
			public void e4() {
				setNextState(s_B);
				return;
			}
			public void e5() {
				setNextState(s_A);
				return;
			}

		}

	}
}
