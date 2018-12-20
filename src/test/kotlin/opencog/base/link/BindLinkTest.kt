package opencog.base.link

import opencog.base.BaseAtomSpace
import opencog.core.ConceptNode
import opencog.core.InheritanceLink
import org.junit.Ignore
import org.junit.Test

class BindLinkTest {

    @Ignore
    @Test
    fun test() {
        val atomSpace = BaseAtomSpace()
        atomSpace
                .add(InheritanceLink(ConceptNode("Jane"), ConceptNode("John")))
                .add(InheritanceLink(ConceptNode("John"), ConceptNode("Jack")))
    }
}