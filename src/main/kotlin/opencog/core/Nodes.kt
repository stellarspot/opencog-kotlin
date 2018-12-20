package opencog.core

data class ConceptNode(override val name: String) : Node()

data class NumberNode(val value: Double) : Node() {
    override val name = value.toString()
}

data class VariableNode(override val name: String) : Node()