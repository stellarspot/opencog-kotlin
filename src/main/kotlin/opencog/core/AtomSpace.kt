package opencog.core

open class Atom {
    val type: String = javaClass.canonicalName
}

abstract class Node : Atom() {

    abstract val name: String
}

open class Link(vararg val out: Atom) : Atom() {

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }

        if (other !is Link) {
            return false
        }

        if (other.type != this.type) {
            return false
        }

        return other.out.contentDeepEquals(this.out)
    }

    override fun hashCode(): Int = out.contentDeepHashCode()
}

interface AtomSpace {
    fun add(atom: Atom): AtomSpace
    fun add(factory: AtomSpace.() -> Atom): AtomSpace
    fun contains(atom: Atom): Boolean

    fun execute(atom: Atom): Atom
    fun execute(f: () -> Atom): Atom
}
