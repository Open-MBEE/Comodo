from wtools.project import declare_project

def configure(cnf):
    cnf.load('java')
    cnf.env.CLASSPATH_EMF = [ cnf.path.abspath() + '/libs/*']

declare_project('comodo2', '0.1.0-dev',
    requires='java',
    recurse='comodo2 libs wrapper')
