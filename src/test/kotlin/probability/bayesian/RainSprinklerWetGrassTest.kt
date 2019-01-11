package probability.bayesian

import org.junit.Test
import probability.bayesian.BayesianNetwork.*
import kotlin.test.assertEquals
import util.assertEquals

import probability.bayesian.RainSprinklerWetGrassTest.TruthValue.*

class RainSprinklerWetGrassTest {

    enum class TruthValue(override val value: String) : Value {
        False("false"),
        True("true")
    }

    val rain = Event("rain", TruthValue.values())
    val sprinkler = Event("sprinkler", TruthValue.values())
    val grass = Event("grass-is-wet", TruthValue.values())

    val rainNode = Node(rain) {
        when (it) {
            setOf(rain.withValue(False)) -> 0.8
            setOf(rain.withValue(True)) -> 0.2
            else -> throw IllegalArgumentException("Incorrect argument: $it")
        }
    }

    val sprinklerNode = Node(sprinkler, rainNode) {
        when (it) {
            setOf(rain.withValue(False), sprinkler.withValue(False)) -> 0.6
            setOf(rain.withValue(False), sprinkler.withValue(True)) -> 0.4
            setOf(rain.withValue(True), sprinkler.withValue(False)) -> 0.99
            setOf(rain.withValue(True), sprinkler.withValue(True)) -> 0.01
            else -> throw IllegalArgumentException("Incorrect argument: $it")
        }
    }

    val grassNode = Node(grass, rainNode, sprinklerNode) {
        when (it) {

            setOf(rain.withValue(False), sprinkler.withValue(False), grass.withValue(False)) -> 1.0
            setOf(rain.withValue(False), sprinkler.withValue(False), grass.withValue(True)) -> 0.0

            setOf(rain.withValue(False), sprinkler.withValue(True), grass.withValue(False)) -> 0.1
            setOf(rain.withValue(False), sprinkler.withValue(True), grass.withValue(True)) -> 0.9

            setOf(rain.withValue(True), sprinkler.withValue(False), grass.withValue(False)) -> 0.2
            setOf(rain.withValue(True), sprinkler.withValue(False), grass.withValue(True)) -> 0.8

            setOf(rain.withValue(True), sprinkler.withValue(True), grass.withValue(False)) -> 0.01
            setOf(rain.withValue(True), sprinkler.withValue(True), grass.withValue(True)) -> 0.99
            else -> throw IllegalArgumentException("Incorrect argument: $it")
        }
    }

    val network = BayesianNetwork(rainNode, sprinklerNode, grassNode)

    @Test
    fun testEventsWithAllValues() {

        assertEquals(setOf(), network.eventsWithAllValues(setOf()))
        assertEquals(setOf(
                setOf(EventWithValue(rain, TruthValue.False)),
                setOf(EventWithValue(rain, TruthValue.True))
        ), network.eventsWithAllValues(setOf(rain)))
        assertEquals(setOf(
                setOf(EventWithValue(rain, TruthValue.False), EventWithValue(sprinkler, TruthValue.False)),
                setOf(EventWithValue(rain, TruthValue.False), EventWithValue(sprinkler, TruthValue.True)),
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.False)),
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.True))
        ), network.eventsWithAllValues(setOf(rain, sprinkler)))
    }

    @Test
    fun testAddMissedValues() {
        assertEquals(setOf(), network.eventsWithAllValues(setOf()))
        assertEquals(setOf(
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.False), EventWithValue(grass, TruthValue.True))
        ), network.addMissedValues(setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.False), EventWithValue(grass, TruthValue.True))))
        assertEquals(setOf(
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.False), EventWithValue(grass, TruthValue.False)),
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.False), EventWithValue(grass, TruthValue.True))
        ), network.addMissedValues(setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.False))))

        assertEquals(setOf(
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.False), EventWithValue(grass, TruthValue.False)),
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.False), EventWithValue(grass, TruthValue.True)),
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.True), EventWithValue(grass, TruthValue.False)),
                setOf(EventWithValue(rain, TruthValue.True), EventWithValue(sprinkler, TruthValue.True), EventWithValue(grass, TruthValue.True))
        ), network.addMissedValues(setOf(EventWithValue(rain, TruthValue.True))))
    }

    @Test
    fun testProbabilities() {

        assertEquals(0.2, network.probability(rain.toValueSet(True)))
        assertEquals(0.8, network.probability(rain.toValueSet(False)))

        assertEquals(0.00198, network.probability(setOf(grass.withValue(True), sprinkler.withValue(True), rain.withValue(True))))
        assertEquals(0.1584, network.probability(setOf(grass.withValue(True), sprinkler.withValue(False), rain.withValue(True))))
        assertEquals(0.288, network.probability(setOf(grass.withValue(True), sprinkler.withValue(True), rain.withValue(False))))
        assertEquals(0.0, network.probability(setOf(grass.withValue(True), sprinkler.withValue(False), rain.withValue(False))))

        // P(Rain=true|Grass=true)
        assertEquals(0.928, network.probability(rain.toValueSet(False), grass.toValueSet(False)))
        assertEquals(0.071, network.probability(rain.toValueSet(True), grass.toValueSet(False)))
        assertEquals(0.642, network.probability(rain.toValueSet(False), grass.toValueSet(True)))
        assertEquals(0.357, network.probability(rain.toValueSet(True), grass.toValueSet(True)))
    }
}