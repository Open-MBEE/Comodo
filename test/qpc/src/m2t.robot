| *Settings*    | *Value*            |
| Documentation | Tests to verify the Model-to-Text transformations for the QPC platform. |
| Resource      | utilities.txt  |
| Default Tags  | QpcTest |
| Test Timeout  | 5 minutes          |


| *Test Case*      | *Action*                  | *Argument*          |


| Generate BlinkyChoice | 
| | [Documentation] | Generate BlinkyChoice module. |
| | [Tags]          | QpcTest |
| | Cleanup         | ../gen/   |
| | Comodo          | ../model/qpc-test-model/qpc-test-model.uml | ../gen/ | BlinkyChoice | QPC-C | ALL |
| | Verify          | ../ref/BlinkyChoice | ../gen/BlinkyChoice   |
