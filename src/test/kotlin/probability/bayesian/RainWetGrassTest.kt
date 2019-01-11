package probability.bayesian

import org.junit.Test
import probability.bayesian.BayesianNetwork.*
import probability.bayesian.RainWetGrassTest.GrassValue.Dry
import probability.bayesian.RainWetGrassTest.GrassValue.Wet
import probability.bayesian.RainWetGrassTest.TruthValue.False
import probability.bayesian.RainWetGrassTest.TruthValue.True
import util.assertEquals

class RainWetGrassTest {

    enum class TruthValue(override val value: String) : Value {
        True("true"),
        False("false")
    }

    enum class GrassValue(override val value: String) : Value {
        Wet("wet"),
        Dry("dry")
    }

    val rain = Event("rain", TruthValue.values())
    val grass = Event("grass-is-wet", GrassValue.values())

    val rainNode = Node(rain) {
        when (it) {
            setOf(rain.withValue(False)) -> 0.8
            setOf(rain.withValue(True)) -> 0.2
            else -> throw IllegalArgumentException("Incorrect argument: $it")
        }
    }

    val grassNode = Node(grass, rainNode) {
        when (it) {
            setOf(rain.withValue(False), grass.withValue(Dry)) -> 0.75
            setOf(rain.withValue(False), grass.withValue(Wet)) -> 0.25
            setOf(rain.withValue(True), grass.withValue(Dry)) -> 0.1
            setOf(rain.withValue(True), grass.withValue(Wet)) -> 0.9
            else -> throw IllegalArgumentException("Incorrect argument: $it")
        }
    }

    val network = BayesianNetwork(rainNode, grassNode)

    @Test
    fun testProbabilities() {

        assertEquals(0.2, network.probability(rain.toValueSet(True)))
        assertEquals(0.8, network.probability(rain.toValueSet(False)))

        assertEquals(0.75 * 0.8 + 0.1 * 0.2, network.probability(grass.toValueSet(Dry)))
        assertEquals(0.25 * 0.8 + 0.9 * 0.2, network.probability(grass.toValueSet(Wet)))

        assertEquals(0.97, network.probability(rain.toValueSet(False), grass.toValueSet(Dry)))
        assertEquals(0.03, network.probability(rain.toValueSet(True), grass.toValueSet(Dry)))
        assertEquals(0.53, network.probability(rain.toValueSet(False), grass.toValueSet(Wet)))
        assertEquals(0.47, network.probability(rain.toValueSet(True), grass.toValueSet(Wet)))
    }
}