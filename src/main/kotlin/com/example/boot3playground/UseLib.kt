package com.example.boot3playground

import com.xunfos.lib.withDelay

class UseLib {

    suspend fun doSomething() {
        withDelay {
            println("Hellooo!")
        }
    }
}
