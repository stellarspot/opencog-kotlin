package opencog.core

import opencog.base.BaseAtomSpace
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BaseAtomSpaceTest {

    @Test
    fun testContainsNode() {
        val atomSpace = BaseAtomSpace()
        assertFalse(atomSpace.contains(ConceptNode("TEST")))
        assertFalse(atomSpace.contains(ConceptNode("TEST")))
        assertTrue {
            atomSpace
                    .add(ConceptNode("TEST"))
                    .contains(ConceptNode("TEST"))
        }
        assertTrue(atomSpace.contains(ConceptNode("TEST")))
    }

    @Test
    fun testContainsLink() {

        val atomSpace = BaseAtomSpace()

        assertFalse {
            atomSpace.contains(
                    InheritanceLink(ConceptNode("Jane"), ConceptNode("John")))
        }

        assertFalse {
            atomSpace.contains(
                    InheritanceLink(ConceptNode("Jane"), ConceptNode("John")))
        }

        assertTrue {
            atomSpace
                    .add(InheritanceLink(ConceptNode("Jane"), ConceptNode("John")))
                    .contains(InheritanceLink(ConceptNode("Jane"), ConceptNode("John")))
        }

        assertTrue {
            atomSpace.contains(
                    InheritanceLink(ConceptNode("Jane"), ConceptNode("John")))
        }

        assertFalse {
            atomSpace.contains(
                    InheritanceLink(ConceptNode("John"), ConceptNode("Jane")))
        }
    }
}
