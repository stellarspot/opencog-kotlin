package opencog.core

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
}