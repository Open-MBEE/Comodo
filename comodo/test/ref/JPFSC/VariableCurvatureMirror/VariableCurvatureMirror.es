#
# This is the guidance script file that can be used to specify
# events to be triggered.
# (see http://babelfish.arc.nasa.gov/trac/jpf/wiki/GuidanceScripts)
#
# Grammar:
#
#  script ::= {section | sequence}.
#  section ::= 'SECTION' ID {',' ID} '{' {sequence} '}'.
#  sequence ::= iteration | selection | event.
#  iteration ::= 'REPEAT' [NUM] '{' {sequence} '}'.
#  selection ::= 'ANY' '{' {event} '}'.
#  event ::= ID ['(' [parameter {',' parameter}] ')'].
#  parameter ::= STRING
#
#  keyword ANY preceedes a set of set of state names or an "*" means 
#  the model checker will try all choices.
#
# Example:
#  e("one"|"two", [12]) 
# is expanded into 
#  e("one",1), e("one",2), e("two",1), e("two",2)
#
# Example:
#  SECTION s1 {
#    a
#    b
#  }  
#  SECTION s2 {
#    ANY {e, f(true|false), g}
#  }
# is expanded into:
#  if (activeState == s1) => a(), b() 
#  if (activeState == s2) => {e(), f(true), f(false), g()}
#
