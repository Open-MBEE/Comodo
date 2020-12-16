| *Settings*    | *Value*            |
| Documentation | Tests to verify the Model-to-Text transformations for the ELT platform. |
| Resource      | utilities.txt  |
| Default Tags  | TestHello TestHello2 |
| Test Timeout  | 5 minutes          |
#| Test Setup    | StartAllMalApps    | 
#| Test Teardown | StopAllMalApps     |


| *Test Case*      | *Action*                  | *Argument*          |


| Generate Hello | 
| | [Documentation] | Generate hellomal, hellomalif modules. |
| | [Tags]          | TestHello |
| | Cleanup         | ../gen/   |
| | Comodo          | ../model/hello/EELT_ICS_ApplicationFramework.uml | ../gen/ | hellomalif hellomal | ELT-RAD | ALL | 
| | Verify          | ../ref/hello/ | ../gen/   |

| Generate Hello2 | 
| | [Documentation] | Generate hellomal2, hellomalif2, externalif2 modules. |
| | [Tags]          | TestHello2 |
| | Cleanup         | ../gen/   |
| | Comodo          | ../model/hello/EELT_ICS_ApplicationFramework.uml | ../gen/ | externalif2 hellomalif2 hellomal2 | ELT-RAD | ALL | 
| | Verify          | ../ref/hello2/ | ../gen/   |

| Generate ccs-prototype | 
| | [Documentation] | Generate TrkLsv module. |
| | [Tags]          | TestTrkLsv |
| | Cleanup         | ../gen/   |
| | Comodo          | ../model/ccs-prototype/ELT_Project.uml | ../gen/ | TrkLsv | SCXML | ALL |  |
| | Verify          | ../ref/ccs-prototype/ | ../gen/   |
