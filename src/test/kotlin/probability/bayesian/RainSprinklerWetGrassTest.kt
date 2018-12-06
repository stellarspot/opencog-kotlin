package probability.bayesian

import org.junit.Test
import probability.bayesian.BayesianNetwork.*
import kotlin.test.assertEquals

class RainSprinklerWetGrassTest {


    enum class TruthValue(override val value: String) : Value {
        T("true"),
        F("false")
    }

    val Rain = Event("rain", TruthValue.values())
    val Sprinkler = Event("sprinkler", TruthValue.values())
    val GrassIsWet = Event("grass-is-wet", TruthValue.values())

    val rainNode = Node(Rain) {
        when (it) {
            setOf(EventWithValue(Rain, TruthValue.F)) -> 0.8
            setOf(EventWithValue(Rain, TruthValue.T)) -> 0.2
            else -> throw IllegalArgumentException("Incorrect argument: $it")
        }
    }

    val sprinklerNode = Node(Sprinkler, rainNode) {
        when (it) {
            setOf(
                    EventWithValue(Rain, TruthValue.F),
                    EventWithValue(Sprinkler, TruthValue.F)
            ) -> 0.6
            setOf(
                    EventWithValue(Rain, TruthValue.F),
                    EventWithValue(Sprinkler, TruthValue.T)
            ) -> 0.4
            setOf(
                    EventWithValue(Rain, TruthValue.T),
                    EventWithValue(Sprinkler, TruthValue.F)
            ) -> 0.09
            setOf(
                    EventWithValue(Rain, TruthValue.T),
                    EventWithValue(Sprinkler, TruthValue.T)
            ) -> 0.01

            else -> throw IllegalArgumentException("Incorrect argument: $it")
        }
    }

    val grassNode = Node(GrassIsWet, rainNode, sprinklerNode) {
        when (it) {
            listOf("F", "F", "F") -> 1.0
            listOf("F", "F", "T") -> 0.0
            listOf("F", "T", "F") -> 0.2
            listOf("F", "T", "T") -> 0.8

            listOf("T", "F", "F") -> 0.1
            listOf("T", "F", "T") -> 0.9
            listOf("T", "T", "F") -> 0.01
            listOf("T", "T", "T") -> 0.99

            else -> throw IllegalArgumentException("Incorrect argument: $it")
        }
    }

    val network = BayesianNetwork(rainNode, sprinklerNode, grassNode)

    @Test
    fun testEventsWithAllValues() {

        assertEquals(setOf(), network.eventsWithAllValues(setOf()))
        assertEquals(setOf(
                setOf(EventWithValue(Rain, TruthValue.F)),
                setOf(EventWithValue(Rain, TruthValue.T))
        ), network.eventsWithAllValues(setOf(Rain)))
        assertEquals(setOf(
                setOf(EventWithValue(Rain, TruthValue.F), EventWithValue(Sprinkler, TruthValue.F)),
                setOf(EventWithValue(Rain, TruthValue.F), EventWithValue(Sprinkler, TruthValue.T)),
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.F)),
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.T))
        ), network.eventsWithAllValues(setOf(Rain, Sprinkler)))
    }

    @Test
    fun testAddMissedValues() {
        assertEquals(setOf(), network.eventsWithAllValues(setOf()))
        assertEquals(setOf(
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.F), EventWithValue(GrassIsWet, TruthValue.T))
        ), network.addMissedValues(setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.F), EventWithValue(GrassIsWet, TruthValue.T))))
        assertEquals(setOf(
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.F), EventWithValue(GrassIsWet, TruthValue.F)),
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.F), EventWithValue(GrassIsWet, TruthValue.T))
        ), network.addMissedValues(setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.F))))

        assertEquals(setOf(
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.F), EventWithValue(GrassIsWet, TruthValue.F)),
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.F), EventWithValue(GrassIsWet, TruthValue.T)),
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.T), EventWithValue(GrassIsWet, TruthValue.F)),
                setOf(EventWithValue(Rain, TruthValue.T), EventWithValue(Sprinkler, TruthValue.T), EventWithValue(GrassIsWet, TruthValue.T))
        ), network.addMissedValues(setOf(EventWithValue(Rain, TruthValue.T))))
    }
}