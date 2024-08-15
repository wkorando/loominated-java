package com.fly.us;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PavelCancelOnError {
	/*
	 * Submits the specified tasks into a newly created executor, and waits for all
	 * the tasks to return a result or any task to throw an exception.
	 *
	 * - If a task throws an exception, all other tasks are cancelled or interrupted
	 * if they have already started - Future.isDone is true for each element of the
	 * returned list - The order of futures returned corresponds to that of tasks
	 * specified
	 */
	public static <T> List<Future<T>> waitForAllSucceedOrFirstFailed(List<Callable<T>> tasks) throws InterruptedException {
		/*
		 * No virtual threads, no completable futures, no auto-closeable executors.
		 * Designed for when all the tasks are known beforehand.
		 */
		for (var task : tasks)
			Objects.requireNonNull(task);
		var done = new Semaphore(0);
		var result = new ArrayList<Future<T>>();
		var executorService = Executors.newCachedThreadPool();
		Exception exc = null;
		try {
			for (var task : tasks)
				result.add(executorService.submit(() -> {
					try {
						var r = task.call();
						done.release(1);
						return r;
					} catch (Throwable t) {
						done.release(tasks.size());
						throw t;
					}
				}));
			done.acquire(tasks.size());
			return result;
		} catch (InterruptedException | RejectedExecutionException e) {
			exc = e;
			return null; // to please the compiler: this return will be overridden in finally
		} finally {
			executorService.shutdownNow();
			InterruptedException interrupted = null;
			while (true) {
				try {
					if (executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS))
						break;
				} catch (InterruptedException e) {
					if (interrupted == null)
						interrupted = e;
				}
			}
			if (exc != null) {
				if (interrupted != null) {
					exc.addSuppressed(interrupted);
				}
				if (exc instanceof InterruptedException ie)
					throw ie;
				else if (exc instanceof RuntimeException re)
					throw re;
			} else if (interrupted != null) {
				throw interrupted;
			}
			// normal return
		}
	}
}
