import java.util.concurrent.BlockingDeque
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingDeque

class ActionQueue(private val name: String) {
    private val actQ: BlockingDeque<Runnable> = LinkedBlockingDeque()
    private var stop = false
    fun destroy() {
        actQ.put {stop = true}
        actQ.put {}
    }

    fun <T> run(act: () -> T): T {
        println("run queue")
        if (stop) throw ActionQueueAlreadyDestroyException()
        val cF = CompletableFuture<T>()
        actQ.put {
            cF.complete(act())
        }
        return cF.get()
    }

    init {
        Thread({
            try {
                while (!stop) {
                    val cv = actQ.take()
                    cv.run()
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                throw e
            } catch (e: Exception) {
                throw e
            }
        }, "$name-queue-thread").start()
    }
}