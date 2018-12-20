package opencog.core

class SumLink(vararg out: Atom) : Link(*out)
class TimesLink(vararg out: Atom) : Link(*out)

class InheritanceLink(child: Atom, parent: Atom) : Link(child, parent)

class BindLink(variable: Atom, pattern: Atom, result: Atom) : Link(variable, pattern, result)