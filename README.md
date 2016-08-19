# Dexecutor
Execute Dependent tasks in a reliable way

## Example
Lets take a look at an example, here is the content of `DefaultDependentTasksExecutorTest`, which would help you understand the API
   

	@Test
	public void testDependentTaskExecution() {

		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor();

        executor.addDependency(1, 2);
        executor.addDependency(1, 3);
        executor.addDependency(3, 4);
        executor.addDependency(3, 5);
        executor.addDependency(3, 6);
        //executor.addDependency(10, 2); // cycle
        executor.addDependency(2, 7);
        executor.addDependency(2, 9);
        executor.addDependency(2, 8);
        executor.addDependency(9, 10);
        executor.addDependency(12, 13);
        executor.addDependency(13, 4);
        executor.addDependency(13, 14);
        executor.addIndependent(11);

        executor.execute(ExecutionBehavior.RETRY_ONCE_TERMINATING);
	}

	private DefaultDependentTasksExecutor<Integer> newTaskExecutor() {
		return new DefaultDependentTasksExecutor<Integer>(newExecutor(), new SleepyTaskProvider<Integer>());
	}

	private ExecutorService newExecutor() {
		return Executors.newCachedThreadPool();
	}

	private static class SleepyTaskProvider<T> implements TaskProvider<T> {

		public Task provid(T id) {

			return new Task() {

				public void execute() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}		
	}

As can be seen above, `DefaultDependentTasksExecutor` requires two things
1. Instance of `ExecutorService` API would use this Service to schedule tasks
2. Instance of `TaskProvider`, API represents graph using just the basic information (could be task id), it consults the `TaskProvider` to provide the task when it comes to actual execution.

##There are two phases

1. Graph construction: When you say `executor.addDependency(1, 2)` it means tasks `1` should finish before task `2`can start, `executor.addIndependent(11)` means neither task `11` depend on any task nor any other task depend on task `11`.

```
    executor.addDependency(1, 2);
    executor.addDependency(1, 3);
    executor.addDependency(3, 4);
    executor.addDependency(3, 5);
    executor.addDependency(3, 6);
    //executor.addDependency(10, 2); // cycle
    executor.addDependency(2, 7);
    executor.addDependency(2, 9);
    executor.addDependency(2, 8);
    executor.addDependency(9, 10);
    executor.addDependency(12, 13);
    executor.addDependency(13, 4);
    executor.addDependency(13, 14);
    executor.addIndependent(11);
```
Which would generate the following graph

![Dependent Tasks Graph](http://s29.postimg.org/fhnct6wjr/dependent_tasks_graph.png)
2. Tasks execution
    
	executor.execute(ExecutionBehavior.RETRY_ONCE_TERMINATING);
   
## Console Output

    
    21:35:36.198 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 1 node
	21:35:36.298 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 11 node
	21:35:36.299 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 12 node
	21:35:36.300 [pool-1-thread-1] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 1
	21:35:36.300 [pool-1-thread-3] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 12
	21:35:36.301 [pool-1-thread-2] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 11
	21:35:36.800 [pool-1-thread-1] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 1, Execution Done!
	21:35:36.800 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 1 done
	21:35:36.801 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 2 node
	21:35:36.801 [pool-1-thread-3] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 12, Execution Done!
	21:35:36.801 [pool-1-thread-2] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 11, Execution Done!
	21:35:36.801 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 3 node
	21:35:36.801 [pool-1-thread-4] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 2
	21:35:36.801 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 12 done
	21:35:36.801 [pool-1-thread-5] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 3
	21:35:36.802 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 13 node
	21:35:36.802 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 11 done
	21:35:36.802 [pool-1-thread-6] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 13
	21:35:37.301 [pool-1-thread-4] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 2, Execution Done!
	21:35:37.301 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 2 done
	21:35:37.301 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 7 node
	21:35:37.302 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 9 node
	21:35:37.302 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 8 node
	21:35:37.303 [pool-1-thread-5] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 3, Execution Done!
	21:35:37.303 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 3 done
	21:35:37.304 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 113 - node 4 depends on [3, 13]
	21:35:37.304 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 5 node
	21:35:37.304 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 6 node
	21:35:37.304 [pool-1-thread-6] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 13, Execution Done!
	21:35:37.305 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 13 done
	21:35:37.305 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 4 node
	21:35:37.305 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 14 node
	21:35:37.306 [pool-1-thread-7] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 7
	21:35:37.306 [pool-1-thread-9] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 8
	21:35:37.306 [pool-1-thread-10] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 5
	21:35:37.307 [pool-1-thread-11] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 6
	21:35:37.307 [pool-1-thread-13] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 14
	21:35:37.308 [pool-1-thread-8] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 9
	21:35:37.309 [pool-1-thread-12] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 4
	21:35:37.806 [pool-1-thread-9] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 8, Execution Done!
	21:35:37.806 [pool-1-thread-7] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 7, Execution Done!
	21:35:37.806 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 8 done
	21:35:37.806 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 7 done
	21:35:37.807 [pool-1-thread-10] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 5, Execution Done!
	21:35:37.807 [pool-1-thread-13] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 14, Execution Done!
	21:35:37.807 [pool-1-thread-11] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 6, Execution Done!
	21:35:37.807 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 5 done
	21:35:37.807 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 14 done
	21:35:37.808 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 6 done
	21:35:37.809 [pool-1-thread-8] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 9, Execution Done!
	21:35:37.809 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 9 done
	21:35:37.809 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doExecute 110 - Going to schedule 10 node
	21:35:37.809 [pool-1-thread-12] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 4, Execution Done!
	21:35:37.810 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 4 done
	21:35:37.810 [pool-1-thread-14] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 238 - Executing Node # 10
	21:35:38.310 [pool-1-thread-14] DEBUG c.d.e.DefaultDependentTasksExecutor$LoggingTask.execute 241 - Node # 10, Execution Done!
	21:35:38.310 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.doWaitForExecution 135 - Processing of node 10 done
	21:35:38.310 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.execute 93 - Total Time taken to process 14 jobs is 2113 ms.
	21:35:38.310 [main] DEBUG c.d.e.DefaultDependentTasksExecutor.execute 94 - Processed Ndoes Ordering [1, 12, 11, 2, 3, 13, 8, 7, 5, 14, 6, 9, 4, 10]
	
	
	
