package opencog.core

data class ConceptNode(override val name: String) : Node() {
    override val type = "ConceptNode"
}

data class NumberNode(val value: Double) : Node() {
    override val type = "NumberNode"
    override val name = value.toString()
}