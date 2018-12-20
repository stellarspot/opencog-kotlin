package opencog.base

import opencog.core.*

fun Atom.unknownAtom(): Nothing =
        throw IllegalArgumentException("Unknown atom: $this")

fun Atom.toNumberNode(): NumberNode =
        if (this !is NumberNode) throw IllegalArgumentException("") else this

fun reduceNumbers(op: (Double, Double) -> Double): (Atom, Atom) -> Atom {
    return { a1, a2 -> NumberNode(op(a1.toNumberNode().value, a2.toNumberNode().value)) }
}

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

    override fun execute(atom: Atom): Atom = when (atom) {
        is Node -> atom
        is SumLink -> atom.reduce(reduceNumbers(Double::plus))
        is TimesLink -> atom.reduce(reduceNumbers(Double::times))
        else -> throw IllegalArgumentException("Unknown atom: $atom")
    }

    override fun execute(f: () -> Atom): Atom = execute(f())

    protected fun Link.reduce(reducer: (Atom, Atom) -> Atom): Atom = out
            .map { execute(it) }
            .reduce(reducer)

}
