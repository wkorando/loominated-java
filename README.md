# Loominated Java

The purpose of this project is to demonstrate the three central features of project loom; Virtual Threads, Structured Concurrency, and Scoped Values. Primarily the project is demonstrating Structured Concurrency as it will have the most impact on how Java developers design and write applications that execute tasks concurrently. 

The basic premise of the application is a service for estimating the cost of flying between two locations by polling price estimates from multiple airlines. 

## Virtual Threads

The central feature of Project Loom, virtual threads separate the concept of threads into two distinct parts. The Platform Thread, which is functionally similar to old threads, with a close relationship to underlying OS threads. And Virtual Threads, which exist in memory only and run on top of platform threads. For a high-level overview of virtual threads, see this video: https://www.youtube.com/watch?v=bOnIYy3Y5OA.  

## Structured Concurrency

Structured Concurrency, currently in preview as of JDK 23, is a another feature part of project loom, that's designed to allow developers to break a unit of work into multiple tasks that can be executed simultaneously. The benefit structured concurrency provides, if for shutdown, cancellation, and error handling of the tasks being executed in parallel. 

## Scoped Values


## About the Project

The purpose of the project is demonstrate the "current state" of doing concurrent programming in Java and how Structured Concurrency improves upon it.

The "task":

```java
() -> {
	String result;
	TimeUnit.MILLISECONDS.sleep(1500);
	result = "b";
	System.out.println(result);
	return result;
}
```

## Demonstrate Virtual Threads in Action 

Being suspended, placed in queue, and resumed

What causes a Virtual Thread to resume

The comparative memory costs


## Step 1 - Serial/Linear Programming

The mindshare and difficulty of concurrent programming; configuring and managing threadpools, error handling and rollback when a task fails, the change in programming model, and disincentivized re-writing tasks from executing concurrently. Though meant that the time to execute tasks was the combined time to execute all the tasks, like in this example. This might often result in slow response times for clients. Like in this example. 

## Step 2 - Concurrent Programming Pre-Virtual Threads

Prior to virtual threads, when considering splitting a unit of work to be executed in concurrent tasks. This would making a choice between the best way of handling 

## Step 3 - The Difficulties of Concurrent Programming Pre-Structured Concurrency

Splitting up a unit of work and executing it as concurrent tasks, wasn't always trivial, as we will review in these code examples. 

### Cancel Propagation

### Error Handling

#### Debugging


## Step 4 - Concurrent Programming with Structured Concurrency

### Refactoring

### Configuring Joiner Policies

### Custom Cancellation Policies



## Step 5 - Using Scoped Values


### Current State

Various ways of implementing a solution that can be broken into sub tasks executed concurrently. 

1. [SerialSolution](src/main/java/com/fly/us/SerialSolution.java) - Demonstrates calling multiple tasks serially. Easy to implement, understand, and debug, but slow, as the time to execute is the sum of all the tasks. 

2. [FuturesSolutionPreVirtualThreads](src/main/java/com/fly/us/FuturesSolutionPreVirtualThreads.java) - Pre Virtual Threads, there are many different executors to choose from with their own relative strengths and weaknesses. Knowing which to use was difficult.

3. [FuturesSolutionWithVirtualThreads](src/main/java/com/fly/us/FuturesSolutionWithVirtualThreads.java) - Demonstrates the new `Executors.newVirtualThreadPerTaskExecutor()`. Which should be used in nearly all cases. Just let the JDK handle mounting/unmounting virtual threads, they are cheap!

4. [FuturesSolutionShutdownOnError](src/main/java/com/fly/us/FuturesSolutionShutdownOnError.java) - The trouble with concurrency is failures happen! This is an example of the difficulty of handling that use case in current state. Primarily the canceling the execution of other tasks when a failure is detected. 

4. [FuturesSolutionShutdownOnSuccess](src/main/java/com/fly/us/FuturesSolutionShutdownOnSuccess.java) - Alternatively, maybe you want to get the first successful result. This example demonstrates that behavior. 

### Error Handling

### Debugging


