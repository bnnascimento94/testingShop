package com.vullpes.testing

object RegistrationUtil {

    private val registeredUsers = listOf("Peter", "Carl")
    fun validateRegistrationInput(
        username:String,
        password:String,
        confirmedPassword:String
    ): Boolean{

        if(username.isEmpty() || password.isEmpty()){
            return false
        }

        if(password != confirmedPassword){
            return false
        }

        if(password.count {it.isDigit()} < 2){
            return false
        }

        if(username in registeredUsers){
            return false
        }

        return true
    }


    fun fib(n:Int): Long {
        if(n ==0 || n ==1){
            return n.toLong()
        }

        var a = 0L
        var b = 1L
        var c = 1L
        (1..n-1).forEach{i ->
            c = a+b
            a = b
            b = c
        }

        return c
    }

    fun checkBraces(string: String): Boolean {

        if(string.indexOf(")") == 0){
           return false
        }

        if(string.lastIndexOf("(") == (string.length - 1)){
            return false
        }


        return string.count { it == '(' } == string.count { it == ')' }
    }
}