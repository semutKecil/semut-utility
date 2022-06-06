import java.util.*

class InstantQueueProcessor<T, V>(val processor: (T) -> V) {
    private val actionQueue = ActionQueue("instant-queue-process-${UUID.randomUUID().toString()}-thread")

    fun process(data: T): V {
        return actionQueue.run { processor(data) }
    }

    fun destroy() {
        actionQueue.destroy()
    }
}