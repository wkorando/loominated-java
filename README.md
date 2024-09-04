# Loominated Java

The purpose of this project is to introduce and demonstrate the three central features of [Project Loom](https://openjdk.org/projects/loom/); [Virtual Threads](#virtual-threads), [Structured Concurrency](#structured-concurrency), and [Scoped Values](#scoped-values). Primarily the project is demonstrating using Structured Concurrency to break down an unit of work and execute it as concurrent tasks. 

You can view the accompanying presentation for this project here: https://wkorando.github.io/presentations/loominated-java/

## Virtual Threads

The central feature of Project Loom, virtual threads separate the concept of threads into two distinct parts. The Platform Thread, which is functionally similar to legacy Threads in, which have a one-to-one relationship to OS threads. And Virtual Threads, which exist in memory and run on top of platform threads. For a high-level overview of virtual threads, see this video: [https://www.youtube.com/watch?v=bOnIYy3Y5OA](https://www.youtube.com/watch?v=bOnIYy3Y5OA). For a more in-depth explanation on virtual threads be sure to read the [JEP 444](https://openjdk.org/jeps/444). 

## Structured Concurrency

Structured Concurrency, currently in preview as of JDK 23, is designed to allow developers to break a unit of work into multiple tasks that can be executed simultaneously. Structured concurrency introduces a new programming model to Java greatly simplifying the writing (and reading) of concurrent blocks of code, as well as error handling and debugging. Both of which will be covered in this project. 

## Scoped Values

With the introduction of virtual threads, and the possibility of tens of thousands, or more, concurrent threads being handled by a JVM, comes new issues. Historically in Java contextual information when Scoped Values were intended to 


https://openjdk.org/jeps/8338456

## About the Project

### Running the Project

This project is using the latest changes from the Loom EA builds, they are available for download here: [https://jdk.java.net/loom/](https://jdk.java.net/loom/)

### The "Task"

The purpose of this project is to demonstrate running tasks concurrently, so isn't particularly concerned with the look of the task(s) being executed. The task, as designed, is simply wrapping a passed in value in a simple JSON message only to provide a thin veneer of similarity to real-world tasks. The task however is designed to be easily configurable in how long it takes to execute and/or if an error occurs during. 

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

The project is divided in five steps, roughly representing "maturity" regarding concurrent concepts.

* [Step 1](#step-1-linear-programming) - Tasks are executed in serial, because the effort of writing the code concurrently was too difficult/not worth the effort.

* [Step 2](#step-2-concurrent-programming-pre-virtual-threads) - Tasks executed concurrently, but without using virtual threads. 

* [Step 3](#step-3-concurrent-programming-pre-structured-concurrency) - Tasks executed concurrently, but using virtual threads. Examples also go into the difficulty of error handling and cancel propagation in a pre-structured concurrency world. 

* [Step 4](#step-4-concurrent-programming-with-structured-concurrency) - Various examples and scenarios demonstrating how to use structured concurrency to execute tasks concurrently.

* [Step 5](#step-5-working-with-scoped-values) - Simple examples using Scoped Values. 

### Step 1 - Linear Programming

The mindshare and difficulty of concurrent programming; configuring and managing threadpools, error handling and rollback when a task fails, the change in programming model, and disincentivized re-writing tasks from executing concurrently. Though meant that the time to execute tasks was the combined time to execute all the tasks, like in this example. This might often result in slow response times for clients. Like in this example. 

### Step 2 - Concurrent Programming Pre-Virtual Threads

Prior to virtual threads, when considering splitting a unit of work to be executed in concurrent tasks. This would making a choice between the best way of handling 

### Step 3 - Concurrent Programming Pre-Structured Concurrency

Splitting up a unit of work and executing it as concurrent tasks, wasn't always trivial, as we will review in these code examples. 

#### Cancel Propagation

Canceling other concurrent tasks when either a success of failure condition is met was difficult prior to structured concurrency. 

#### Debugging

When using [ ], the relationship between the tasks and subtasks wasn't tracked. This could make debugging difficult for applications receiving many requests, as it would be difficult to determine the parent thread that started the execution of the subtask. 

### Step 4 - Concurrent Programming with Structured Concurrency

#### Configuring Joiner Policies

#### Custom Cancellation Policies


### Step 5 - Working with Scoped Values


### Current State

Various ways of implementing a solution that can be broken into sub tasks executed concurrently. 

1. [SerialSolution](src/main/java/com/fly/us/SerialSolution.java) - Demonstrates calling multiple tasks serially. Easy to implement, understand, and debug, but slow, as the time to execute is the sum of all the tasks. 

2. [FuturesSolutionPreVirtualThreads](src/main/java/com/fly/us/FuturesSolutionPreVirtualThreads.java) - Pre Virtual Threads, there are many different executors to choose from with their own relative strengths and weaknesses. Knowing which to use was difficult.

3. [FuturesSolutionWithVirtualThreads](src/main/java/com/fly/us/FuturesSolutionWithVirtualThreads.java) - Demonstrates the new `Executors.newVirtualThreadPerTaskExecutor()`. Which should be used in nearly all cases. Just let the JDK handle mounting/unmounting virtual threads, they are cheap!

4. [FuturesSolutionShutdownOnError](src/main/java/com/fly/us/FuturesSolutionShutdownOnError.java) - The trouble with concurrency is failures happen! This is an example of the difficulty of handling that use case in current state. Primarily the canceling the execution of other tasks when a failure is detected. 

4. [FuturesSolutionShutdownOnSuccess](src/main/java/com/fly/us/FuturesSolutionShutdownOnSuccess.java) - Alternatively, maybe you want to get the first successful result. This example demonstrates that behavior. 

### Error Handling

### Debugging


