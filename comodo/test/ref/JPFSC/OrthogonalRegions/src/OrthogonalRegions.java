/**
 * Java PathFinder Statechart (jpf-statechart)
 */

import gov.nasa.jpf.sc.*;

public class OrthogonalRegions extends State {

	//
	// State Machine: OrthogonalRegions
	//

	final SM_OrthogonalRegions sm_OrthogonalRegions = makeInitial(new SM_OrthogonalRegions());

	public class SM_OrthogonalRegions extends State {

		final S_STATE s_STATE = makeInitial(new S_STATE());

		public class S_STATE extends State {

			//initial state for orthogonal region 'Reg1': 'A'

			final S_A s_A = makeInitial(new S_A());

			final S_C s_C = new S_C();

			final S_B s_B = new S_B();

			public class S_A extends State {

				public void e1() {
					setNextState(s_B);
					return;
				}
				public void e6() {
					setNextState(s_C);
					return;
				}

			}

			public class S_C extends State {

				public void e4() {
					setNextState(s_B);
					return;
				}
				public void e5() {
					setNextState(s_A);
					return;
				}

			}

			public class S_B extends State {

				public void doAction() {
					log("Executing doActivity: Assert1");

					/* MARS
						assert (getMachine().isInState("sm_OrthogonalRegions.s_STATE.s_F.s_D")==false) : "B and D active together";
					assert (getMachine().isInState("sm_OrthogonalRegions.s_STATE.s_F")==false) : "B and F active together";
					 */
				}
				public void e3() {
					setNextState(s_C);
					return;
				}
				public void e2() {
					setNextState(s_A);
					return;
				}

			}

			//initial state for orthogonal region 'Reg2': 'F'

			final S_F s_F = makeInitial(new S_F());

			public class S_F extends State {

				//initial state for composite state 'F': 'D'
				final S_D s_D = makeInitial(new S_D());
				final S_E s_E = new S_E();

				public class S_D extends State {

					public void doAction() {
						log("Executing doActivity: Assert2");

						/* MARS
							assert(getMachine().isInState("sm_OrthogonalRegions.s_STATE.s_B") == false) : "In D and B at the same time!";
						 */
					}
					public void e1() {
						setNextState(s_E);
						return;
					}

				}

				public class S_E extends State {

					public void e4() {
						setNextState(s_D);
						return;
					}

				}

			}

			public void f() {
				setEndState();
				return;
			}

		}

	}
}
