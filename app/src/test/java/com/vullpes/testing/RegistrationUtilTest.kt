package com.vullpes.testing


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest{

    @Test
    fun `empty username returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "123",
            "123"
        )

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `valid username and correctly repeated password returns true`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "John",
            "1234",
            "1234"
        )

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `username alterady exists returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "1234",
            "1234"
        )

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `empty password returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "",
            "1234"
        )

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `password repeated incorrectly returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "12345",
            "1234"
        )

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `password contains less then 2 digits returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "12",
            "12"
        )

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `test next fibonacci when input is zero`(){
        val result = RegistrationUtil.fib(0)

        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `test next fibonacci when input is one`(){
        val result = RegistrationUtil.fib(1)

        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `text next fibonacci when input is three`(){
        val result = RegistrationUtil.fib(3)

        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `test if checkBracets returns false when are missing closing bracets `(){
        val result = RegistrationUtil.checkBraces("((())")

        assertThat(result).isFalse()
    }

    @Test
    fun `test if checkBracets returns true when bracets are set correctly`(){
        val result = RegistrationUtil.checkBraces("((()))")

        assertThat(result).isTrue()
    }

    @Test
    fun `test if checkBracets returns false when closing braces is at first and open at last `(){
        val result = RegistrationUtil.checkBraces(")(())(")

        assertThat(result).isFalse()
    }
}