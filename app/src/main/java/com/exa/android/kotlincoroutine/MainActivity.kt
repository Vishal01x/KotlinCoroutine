package com.exa.android.kotlincoroutine

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class MainActivity : AppCompatActivity() {

    private val TAG : String =  "VISHAL"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch{
            printnum()
        }
    }

    private suspend fun printnum() {
        var follow = 0;
        val job = CoroutineScope(Dispatchers.Main).launch {
           follow =  getFb()
        }
        job.join()// look at end for each body description
       val job2 = CoroutineScope(Dispatchers.Main).async {
           getInsta()
       }
        Log.d(TAG,"Fb : $follow, Insta : $job2.await()")
    }

    private suspend fun getInsta(): Int {
        yield()
        delay(1000)
        // any one of them is used
        return 113
    }

    private suspend  fun getFb(): Int {
        delay(1000)
       return 54
    }
}

/* Concept of Kotlin Coroutine

 Suspend - it is used to suspend thread whenever API  request(IO operation) is called
 it can be accessed only from coroutine or from a suspend function
 to suspend thread we use :
1. yield - it suspend
2. delay - it requires time in millisec

Coroutine - works on top of thread , like a lightweight thread but not thread
it requires some dependencies to be include for using it -
dependencies {
    // Kotlin Coroutines Core
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0"

    // Kotlin Coroutines Android Support
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0"

    // Optional: Coroutines Test
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0"
}
it has two things
1 coroutineScope - timelimit
2. CoroutineContext - thread on which it needs to apply

Dispatcher - it is context on which thread process is allocated
three types - Dispatcher.IO, Dispatcher.Main, Dispatcher.Default
with there task

launch - to run process return a job
in this code we want to execute log after getFb(is a fun that respond after 1sec,
try to act it like an API) is completed. But in coroutine when thread get suspend or wait for response
then the thread is allocated to next instruction
before applying join the thread firstly execute log before completion of getFb
therefore when we want to actually wait for completion of task either it waits then while using
"launch - .join is used"
here the result of .launch is job


Another way to  handle same scenario we have "async"
it is generic type that decides its datatype based on last coroutine statement
it returns a defferd type i.e. its result will be resolved after
here to achive same scenario we use ".await" with the children of async
job.await()


Which to use
launch - when we don't care about result, it is of fire and forget
when we have many instruction with coroutine then launch run them one by one

async - when we are  expecting any result
when we have many instruction with coroutine then launch run them parallely by
allocating another thread to that task

CoroutineScope(Dispatcher.Main).launch{
   fb = getFb()
   inst = getInsta()
   Log.d($fb, $insta)
}-> using same fun take 2 unit of time to run , firstly run fb after its completion
run insta then Log.d


CoroutineScope(Dispatcher.Main){
   fb = async{getFb()}
   inst = async{getInsta()}
   Log.d($fb, $insta)
}
async is used then parellely both process run and take 1 sec and then log.d is execute


JOB SCHEDULING  ->  we can create a job within a job like parent - child
if helps whenever we want to wait untill all child is not executed
and if parent coroutine is cancelled then child is also cancelled see in another activity

lifecyclescope - activity and fragment
viewmodelscope - view models
*/