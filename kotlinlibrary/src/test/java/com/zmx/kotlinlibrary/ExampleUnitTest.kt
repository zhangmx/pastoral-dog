package com.zmx.kotlinlibrary

import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

//        GlobalScope.launch {
//            println("hello")
//            delay(1500)
//            println("hello")
//        }

        runBlocking {
            launch {
                println("hello launch in runBlocking")
                delay(1500)
                println("hello first launch in runBlocking")
            }

            launch {
                println("hello launch in runBlocking")
                delay(1500)
                println("hello second launch in runBlocking")
            }

            println("code in coroutine")
            delay(1501)
            println("code in coroutine")
        }

        Thread.sleep(1000)

        assertEquals(4, 2 + 2)
    }

    @Test
    fun `test coroutine`() {
        val start = System.currentTimeMillis()

        runBlocking {
            repeat(100_000) {
                launch {
                    delay(1000)
                    println("hello")
                }
            }
        }
        val end = System.currentTimeMillis()

        println("cost time: ${end - start}")
    }

    @Test
    fun `test coroutineScope`() {
        runBlocking {
            coroutineScope {
                launch {
                    for (i in 1..10) {
                        println("i: $i")
                        delay(1000)
                    }
                }
                println("launch in coroutineScope")
            }
            println("code after coroutineScope")
        }
        println("runBlocking finish")

//        coroutineScope{
//            launch {
//                for (i in 1..10) {
//                    println("i: $i")
//                    delay(1000)
//                }
//            }
//            println("launch in coroutineScope")
//        }
        println("code after coroutineScope")
    }

    @Test
    fun `coroutine with result`() {
        val result = runBlocking {
            val result = withContext(Dispatchers.Default) {
                delay(1000)
                "hello"
            }
            println("result: $result")
            result
        }
        println("result: $result")
    }

    @Test
    fun `coroutine with result withContext order`() {
        val result = runBlocking {

            val start = System.currentTimeMillis()

            val result1 = withContext(Dispatchers.Default) {
                delay(1000)
                10
            }

            val result2 = withContext(Dispatchers.Default) {
                delay(1000)
                20
            }

            val end = System.currentTimeMillis()
            println("cost time: ${end - start}")
            result1 + result2
        }
        println("result: $result")
    }

    @Test
    fun `coroutine with result async order`() {
        val result = runBlocking {

            val start = System.currentTimeMillis()

            val result1 = async {
                delay(1000)
                10
            }.await()

            val result2 = async {
                delay(1000)
                20
            }.await()

            val end = System.currentTimeMillis()
            println("cost time: ${end - start}")
            result1 + result2
        }
        println("result: $result")
    }

    @Test
    fun `coroutine with result async order await same time`() {
        val result = runBlocking {

            val start = System.currentTimeMillis()

            val result1 = async {
                withContext(Dispatchers.Default) {
                    delay(1000)
                    10
                }
            }

            val result2 = async {
                withContext(Dispatchers.Default) {
                    delay(1000)
                    20
                }
            }

            val result = result1.await() + result2.await()

            val end = System.currentTimeMillis()
            println("cost time: ${end - start}")
            result
        }
        println("result: $result")
    }

}