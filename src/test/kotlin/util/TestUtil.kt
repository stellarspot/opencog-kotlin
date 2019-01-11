package util

import org.junit.Assert

fun assertEquals(expected: Double, actual: Double, delta: Double = 1e-2) {
    Assert.assertEquals(expected, actual, delta)
}
