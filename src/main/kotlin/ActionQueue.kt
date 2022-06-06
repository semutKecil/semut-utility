/*
 * MIT License
 *
 * Copyright (c) [2022] [Herlan Septiyan]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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