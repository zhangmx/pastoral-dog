package com.zmx.libkotlin

import org.junit.Assert.*

import org.junit.Test

class MyClassTest {

    @Test
    fun test1() {
        val myClass = MyClass()
        assertEquals(1, myClass.add(1, 0))
    }
}