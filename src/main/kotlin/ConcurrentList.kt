import java.util.*

class ConcurrentList<T> : MutableList<T> {
    private val list = mutableListOf<T>()
    private val actionQueue = ActionQueue("concurrent-list-${UUID.randomUUID().toString()}-thread")

    override val size: Int
        get() = actionQueue.run { list.size }

    fun destroy() {
        actionQueue.destroy()
    }

    override fun clear() {
        actionQueue.run { list.clear() }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return actionQueue.run { list.addAll(elements) }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return actionQueue.run { list.addAll(index, elements) }
    }

    override fun add(index: Int, element: T) {
        actionQueue.run { list.add(index, element) }
    }

    override fun add(element: T): Boolean {
        return actionQueue.run { list.add(element) }
    }

    override fun get(index: Int): T {
        return actionQueue.run { list[index] }
    }

    override fun isEmpty(): Boolean {
        return actionQueue.run { list.isEmpty() }
    }

    override fun iterator(): MutableIterator<T> {
        return actionQueue.run { list.iterator() }
    }

    override fun listIterator(): MutableListIterator<T> {
        return actionQueue.run { list.listIterator() }
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return actionQueue.run { list.listIterator(index) }
    }

    override fun removeAt(index: Int): T {
        return actionQueue.run { list.removeAt(index) }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return actionQueue.run { list.subList(fromIndex, toIndex) }
    }

    override fun set(index: Int, element: T): T {
        return actionQueue.run { list.set(index, element) }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return actionQueue.run { list.retainAll(elements) }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return actionQueue.run { list.removeAll(elements) }
    }

    override fun remove(element: T): Boolean {
        return actionQueue.run { list.remove(element) }
    }

    override fun lastIndexOf(element: T): Int {
        return actionQueue.run { list.lastIndexOf(element) }
    }

    override fun indexOf(element: T): Int {
        return actionQueue.run { list.indexOf(element) }
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return actionQueue.run { list.containsAll(elements) }
    }

    override fun contains(element: T): Boolean {
        return actionQueue.run { list.contains(element) }
    }

    companion object {
        fun <T> of(vararg value: T): ConcurrentList<T> {
            val cl = ConcurrentList<T>()
            cl.addAll(value)
            return cl
        }
    }
}