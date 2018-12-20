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
}

fun Atom.unknownAtom(): Nothing = throw IllegalArgumentException("Unknown atom: $this")

open class BaseAtomSpace : AtomSpace {

    // Map<Type<Map<Name, Node>>
    private val nodeMap = mutableMapOf<String, MutableMap<String, Node>>()

    override fun add(atom: Atom): AtomSpace {
        when (atom) {
            is Node -> addNode(atom)
            is Link -> TODO()
            else -> atom.unknownAtom()
        }
        return this
    }

    protected fun addNode(node: Node) {
        nodeMap
                .getOrPut(node.type) { mutableMapOf() }
                .putIfAbsent(node.name, node)
    }

    override fun add(factory: AtomSpace.() -> Atom): AtomSpace {
        add(this.factory())
        return this
    }

    override fun contains(atom: Atom): Boolean = when (atom) {
        is Node -> nodeMap.get(atom.type)?.contains(atom.name) ?: false
        is Link -> TODO()
        else -> atom.unknownAtom()
    }
}