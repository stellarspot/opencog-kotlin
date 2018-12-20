package opencog.core

import kotlin.IllegalArgumentException

fun execute(f: () -> Atom): Atom = execute(f())

fun execute(atom: Atom): Atom = when (atom) {
    is Node -> atom
    is SumLink -> atom.reduce(reduceNumbers(Double::plus))
    is TimesLink -> atom.reduce(reduceNumbers(Double::times))
    else -> throw IllegalArgumentException("Unknown atom: $atom")
}

fun Atom.toNumberNode(): NumberNode =
        if (this !is NumberNode) throw IllegalArgumentException("") else this

fun Link.reduce(reducer: (Atom, Atom) -> Atom): Atom = out
        .map { execute(it) }
        .reduce(reducer)

fun reduceNumbers(op: (Double, Double) -> Double): (Atom, Atom) -> Atom {
    return { a1, a2 -> NumberNode(op(a1.toNumberNode().value, a2.toNumberNode().value)) }
}