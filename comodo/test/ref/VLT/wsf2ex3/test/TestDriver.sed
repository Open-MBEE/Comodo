s/Environment  : .* StackId  : [ 0-9][ 0-9][ 0-9][ 0-9][ 0-9][ 0-9]/Environment  : -------  StackId  : ------/g
s/Sequence : *[ 0-9][ 0-9]/Sequence : --  /g
s/signal: [ 0-9][ 0-9]/signal: --/g
s/@(#) $Id.*/@(#) $Id/g
s/[lw][pt][le0-9][a-z0-9A-Z]\{2,4\}/-------/g
s/seq_.* (buffer/seq_--- (buffer/
s/[0-9]*-[0-9]*-[0-9]*T[0-9]*:[0-9]*:[0-9]*.[0-9]* //g
s/.C:[0-9]* /.C:---- /g
