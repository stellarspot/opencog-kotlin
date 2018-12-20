package opencog.core

import org.junit.Test
import kotlin.test.assertEquals

class FunctionsTest {

    @Test
    fun testSum() {
        assertEquals(NumberNode(3.0), execute {
            SumLink(NumberNode(1.0), NumberNode(2.0))
        })

        assertEquals(NumberNode(9.0), execute {
            SumLink(
                    SumLink(NumberNode(2.0), NumberNode(3.0)),
                    NumberNode(4.0))
        })
    }

    @Test
    fun testTimes() {
        assertEquals(NumberNode(21.0), execute {
            TimesLink(NumberNode(3.0), NumberNode(7.0))
        })

        assertEquals(NumberNode(24.0), execute {
            TimesLink(
                    TimesLink(NumberNode(2.0), NumberNode(3.0)),
                    NumberNode(4.0))
        })
    }

    @Test
    fun testSumTimes() {
        assertEquals(NumberNode(14.0), execute {
            SumLink(NumberNode(2.0),
                    TimesLink(NumberNode(3.0), NumberNode(4.0)))
        })
    }
}

