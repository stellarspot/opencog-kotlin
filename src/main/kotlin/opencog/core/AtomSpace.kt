package opencog.core

open class Atom

abstract class Node : Atom() {

    abstract val type: String
    abstract val name: String
}

open class Link(vararg val out: Atom) : Atom()

interface AtomSpace {
    fun add(atom: Atom): AtomSpace
    fun add(factory: AtomSpace.() -> Atom): AtomSpace
    fun contains(atom: Atom): Boolean

    fun execute(atom: Atom): Atom
    fun execute(f: () -> Atom): Atom
}
