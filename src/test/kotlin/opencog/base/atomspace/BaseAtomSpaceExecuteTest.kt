package opencog.core

import opencog.base.BaseAtomSpace
import org.junit.Test
import kotlin.test.assertEquals

class BaseAtomSpaceExecuteTest {

    @Test
    fun testSum() {

        val atomSpace = BaseAtomSpace()

        assertEquals(NumberNode(3.0), atomSpace.execute {
            SumLink(NumberNode(1.0), NumberNode(2.0))
        })

        assertEquals(NumberNode(9.0), atomSpace.execute {
            SumLink(
                    SumLink(NumberNode(2.0), NumberNode(3.0)),
                    NumberNode(4.0))
        })
    }

    @Test
    fun testTimes() {

        val atomSpace = BaseAtomSpace()

        assertEquals(NumberNode(21.0), atomSpace.execute {
            TimesLink(NumberNode(3.0), NumberNode(7.0))
        })

        assertEquals(NumberNode(24.0), atomSpace.execute {
            TimesLink(
                    TimesLink(NumberNode(2.0), NumberNode(3.0)),
                    NumberNode(4.0))
        })
    }

    @Test
    fun testSumTimes() {

        val atomSpace = BaseAtomSpace()

        assertEquals(NumberNode(14.0), atomSpace.execute {
            SumLink(NumberNode(2.0),
                    TimesLink(NumberNode(3.0), NumberNode(4.0)))
        })
    }
}

