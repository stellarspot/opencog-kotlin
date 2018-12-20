package opencog.core

open class Atom

open abstract class Node : Atom() {

    abstract val type: String
    abstract val name: String
}

open class Link(vararg val out: Atom) : Atom()