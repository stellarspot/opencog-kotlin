package probability.bayesian

class BayesianNetwork(vararg val nodes: Node) {

    interface Value {
        val value: String
    }

    // TBD: override equals() and hashcode()
    data class Event(val name: String, val values: Array<out Value>) {
        override fun toString() = name
    }

    data class EventWithValue(val name: Event, val value: Value) {
        override fun toString() = "$name=$value"
    }

    class Node(val event: Event, vararg val parents: Node, val f: (Set<EventWithValue>) -> Double)


    fun probability(events: Set<EventWithValue>, conditions: Set<EventWithValue> = setOf()): Double {
        return calcProbability(events + conditions) / calcProbability(conditions)
    }

    private fun calcProbability(eventsWithValues: Set<EventWithValue>): Double {

        if (eventsWithValues.isEmpty()) {
            return 1.0
        }

        val allEventsWithValues = addMissedValues(eventsWithValues)

        return 1.0
    }

    private fun <T> print(msg: String, eventsWithValues: Set<T>) {
        println("$msg:\n${eventsWithValues.joinToString("\n")}\n")

    }

    internal fun addMissedValues(eventsWithValues: Set<EventWithValue>): Set<Set<EventWithValue>> {

        val missedEvents = nodes.map { it.event }.toSet() - eventsWithValues.map { it.name }

        if (missedEvents.isEmpty()) {
            return setOf(eventsWithValues)
        }

        val missedEventsWithValues = eventsWithAllValues(missedEvents)

        return missedEventsWithValues.map { it + eventsWithValues }.toSet()
    }

    internal fun eventsWithAllValues(events: Set<Event>): Set<Set<EventWithValue>> {

        if (events.isEmpty()) {
            return setOf()
        }

        val event = events.first()
        val values = event.values.toSet()

        fun eventAndValue(value: Value) = EventWithValue(event, value)
        if (events.size == 1) {
            return values.map { setOf(eventAndValue(it)) }.toSet()
        }

        val tail = eventsWithAllValues(events.drop(1).toSet())

        return tail.flatMap { set ->
            values.map { set + eventAndValue(it) }
        }.toSet()
    }

}