# Loominated Java

The purpose of this project is to introduce and demonstrate the three central features of [Project Loom](https://openjdk.org/projects/loom/); [Virtual Threads](#virtual-threads), [Structured Concurrency](#structured-concurrency), and [Scoped Values](#scoped-values). Primarily the project is demonstrating using Structured Concurrency to break down a unit of work and execute it as concurrent tasks. 

You can view the accompanying presentation for this project here: https://wkorando.github.io/presentations/loominated-java/

## Virtual Threads

The central feature of Project Loom, virtual threads separate the concept of threads into two distinct parts. The Platform Thread, which is functionally similar to legacy Threads in, which they have a one-to-one relationship to OS threads. And Virtual Threads, which exist in memory and run on top of platform threads. For a high-level overview of virtual threads, see this video: [https://www.youtube.com/watch?v=bOnIYy3Y5OA](https://www.youtube.com/watch?v=bOnIYy3Y5OA). For a more in-depth explanation on virtual threads be sure to read the [JEP 444](https://openjdk.org/jeps/444). 

## Structured Concurrency

Structured Concurrency, currently in preview as of JDK 24, is designed to improve the experience of writing concurrent code. Structured concurrency introduces a new programming model to Java greatly simplifying the writing (and reading) of concurrent blocks of code, as well as error handling and debugging. Both of which will be covered in this project. 

## Scoped Values

With the introduction of virtual threads, and the possibility of tens of thousands, or more, concurrent threads being handled by a JVM, comes new issues. Historically in Java contextual information when Scoped Values were intended to 


https://openjdk.org/jeps/8338456

## About the Project

### Running the Project

This project is using the latest changes from the Loom EA builds, they are available for download here: [https://jdk.java.net/loom/](https://jdk.java.net/loom/)

### The "Task"

The purpose of this project is to demonstrate running tasks concurrently, so isn't particularly concerned with the look of the task(s) being executed. The task, as designed, is simply wrapping a passed in value in a simple JSON message only to provide a thin veneer of similarity to real-world tasks. The task however is designed to be easily configurable for how long it takes to execute and if an error should occur. 

```java
public static String task(String value, long executionTime, boolean throwException) {
	executionTime(executionTime);
	if (throwException) {
		throw new RuntimeException(String.format("""
				Task failed!
				value: %s
				executionTime: %d
				throwException: %b
				""", value, executionTime, throwException));
	}
	String result = String.format("""
			{
				"value" : "%s"
			}
			""", value);
	System.out.println("Result of taks: " + result);
	return result;
}
```

### Working with the Project

The project is divided in four steps, roughly representing "maturity" regarding concurrent concepts.

* [Step 1](#step-1-concurrent-programming-pre-virtual-threads) - Tasks executed concurrently, but without using virtual threads. 

* [Step 2](#step-2-concurrent-programming-pre-structured-concurrency) - Tasks executed concurrently, but using virtual threads. Examples also go into the difficulty of error handling and cancel propagation in a pre-structured concurrency world. 

* [Step 3](#step-3-concurrent-programming-with-structured-concurrency) - Various examples and scenarios demonstrating how to use structured concurrency to execute tasks concurrently.

* [Step 4](#step-4-using-scoped-values) - Simple examples using Scoped Values. 

### Step 1 - Concurrent Programming Pre-Virtual Threads

Prior to virtual threads, when considering splitting a unit of work to be executed in concurrent tasks. This would making a choice between the best way of handling 

### Step 2 - Concurrent Programming Pre-Structured Concurrency

Splitting up a unit of work and executing it as concurrent tasks, wasn't always trivial, as we will review in these code examples.

#### Error Handling

#### Timeout

#### Cancel Propagation

When executing tasks concurrently, it might often be preferable to cancel tasks, when one, or more, of the tasks succeeds, fails, or some other condition is met. Support for cancellation propagation is limited or non-existant in many currently supported options for executing con current tasks. Like in this [example](src/main/java/step2/ConcurrencyWithTaskError.java), where one task fails, but the other tasks continue to execute. 

Best case scenario this merely wastes system resource executing tasks whose results will simply be discarded. In many cases the early cancellation of a task might prevent the need to perform a rollback or avoiding some of transaction cost. 

Cancel propagation is possible in the currently possible, but often requires considerable code to handle properly, [like in this example](src/main/java/step2/ConcurrencyShutdownOnError.java).

#### Debugging

When writing concurrent code in Java, one of the difficulties is the lack of a relationship between the parent thread that spawns tasks. When an error occurs, often a necessary step in determining root cause analysis is following the call stack. In an application that might processing dozens or more transactions a second, without any identification linking between a parent task and a subtask, traversing the call stack to perform root cause analysis becomes very difficult. This can be seen in this [thread dump](src/main/java/step2/pre-structured-concurrency-thread-dump-multi.json). Where in this example of the execution of task, there isn't a straightforward to relate it a parent thread.

```
{
"container": "java.util.concurrent.ThreadPerTaskExecutor@38b81773",
"parent": "<root>",
"owner": null,
"threads": [
 {
   "tid": "50",
   "name": "",
   "stack": [
      "java.base\/java.lang.VirtualThread.parkNanos(VirtualThread.java:839)",
      "java.base\/java.lang.VirtualThread.sleepNanos(VirtualThread.java:1063)",
      "java.base\/java.lang.Thread.sleepNanos(Thread.java:506)",
      "java.base\/java.lang.Thread.sleep(Thread.java:576)",
      "java.base\/java.util.concurrent.TimeUnit.sleep(TimeUnit.java:446)",
      "common.CommonUtils.executionTime(CommonUtils.java:72)",
      "common.CommonUtils.task(CommonUtils.java:46)",
      "step3.MultiExecutionOfConcurrencyTasks.lambda$callWebServices$5(MultiExecutionOfConcurrencyTasks.java:45)",
      "java.base\/java.util.concurrent.FutureTask.run(FutureTask.java:326)",
      "java.base\/java.lang.VirtualThread.run(VirtualThread.java:456)"
   ]
 }
 ```
         

### Step 3 - Concurrent Programming with Structured Concurrency

#### Default Joiner Policies

Several Joiner policies are already provided, covering many of the common needs when executing tasks concurrently. They are:

* [allSuccessfulOrThrow](src/main/java/step3/AllSuccessfulOrThrow.java) - waits for all Subtasks to complete successfully, or, if any fail, throws and exception and interrupts the execution of any still running tasks.
* [anySuccessfulOrThrow](src/main/java/step3/AnySuccessfulOrThrow.java) - waits for the first successful task to complete, and returns the result. Cancels the execution of tasks that do not complete.
* [awaitAllSuccessfulOrThrow](src/main/java/step3/AwaitAllSuccessfulOrThrow.java) - 
* [awaitAll](src/main/java/step3/AwaitAll.java) -

There is also the `allUntil(Predicate)` which allows for customization of when a execution should be terminated, this is covered in more detail under the [Custom Cancellation Policies](#custom-cancellation-policies)

#### Configuring the Joiner

#### Custom Cancellation Policies




### Step 4 - Using Scoped Values


### Error Handling

### Debugging


