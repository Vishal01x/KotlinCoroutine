package com.exa.android.kotlincoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// JOB SCHEDULING
class MainActivity2 : AppCompatActivity() {

    private val tag = "JObSch"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            execute()
        }
    }

    /*private suspend  fun jobscheduling() {
        val parentjob = CoroutineScope(Dispatchers.Main).launch {
            Log.d(tag, "parent start") // 1
            val childjob = launch {//-> it directly use the context provided in parent if you need another then define as launch(Dispatchers.any)
                 Log.d(tag , "Child start") // 2
                delay(5000)
                Log.d(tag, "child end")//6
            }
            delay(2000)
            Log.d(tag, "parent end") //4
        }
        delay(500)
        Log.d(tag,"before parent delay")//3
        delay(3000)
        parentjob.cancel()
        Log.d(tag, "after 3000")//5
        parentjob.join()
        Log.d(tag,"after join")//7
    }
}*/

    private suspend fun execute() {
        val job = CoroutineScope(Dispatchers.Main).launch {// -> here we use global scope is an Kotlincoroutine , described below
            for (i in 1..1000) {// here default dispatchers is used, we can use io/ main also as per our need
                if(isActive) {
                    doLongRunningTask()
                    Log.d(tag, i.toString())
                }
            }
        }
        delay(100)
        Log.d(tag, "cancelled")
        job.cancel()
        job.join()
        Log.d(tag,"completed")
    }

    private fun doLongRunningTask() {
        for(i in 1..10000){}

    }
}


/*
 Job Scheduling -> when we use hirarchy of jobs then such is used but it can be used separate too then why hirarchy
 here each child is connected with child

 when we write parent-job.join it means after parent and all its child execution is completed i.e.
 when parent is cancelled all child is cancelled also means in every manner parent and child are corelated.
 But it can that child can be cancelled and .join is applied on child alsoas per our requirement

 in above code i have included a lot log to understand how threads works and numbering show its position of display ie the number at which it is completed

how cancellation works - assume that we open a page and after 2sec we terminate in such cases all the child threads
also terminated.
Here, catch block get the cancellation exception and find which thread is cancelled and terminate it
.cancel with job is used to terminate

but always termination doesn't work properly when the thread captures in executing long time task
like an example provided above - here we use isActive check to check is the coroutine is cancelled or not because due to
long running task the thread gets stuck into and forget that it is cancelled

GLOBALCOROUTINE ->
GlobalScope is a top-level scope that is available throughout the application.
Coroutines launched in the GlobalScope are not tied to the lifecycle of any particular component, such as an activity or a fragment.
Coroutines launched in the GlobalScope continue to run until they complete or until the application terminates.
While convenient, using GlobalScope for coroutines is generally discouraged in Android development because it can lead to memory leaks and other issues if coroutines are not properly managed.


 //withcontext - it used when we need different dispatchers to run our program at different instance
 similar behaviour to launch and async at some extent
 it pauses and prevent from running further next instrucion untill our corouting is not complete
 eg -  I want to run some programe using io and then io request is taken, after it i want tp use main/ ui context
 so here it is used


 runBlocking is used in 'coroutines to untill all our coroutines is not executed
 */