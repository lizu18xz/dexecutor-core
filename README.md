# dependent-tasks-executor
Execute Dependent tasks in a reliable way

## Example
Lets take a look at an example, here is the content of `DefaultDependentTasksExecutorTest`, which would help you understand the API

   ```

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

        executor.execute();
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
```
As can be seen above, `DefaultDependentTasksExecutor` requires two things
1. Instance of `ExecutorService` API would use this Service to schedule tasks
2. Instance of `TaskProvider`, API represents graph using just the basic information (could be task id), it consults the `TaskProvider` to provide the task when it comes to actual execution.

There are two phases
1. Graph construction 

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

    ``` 
	executor.execute()
   	
   ```
   
## Console Output

    ```
    19:57:43.705 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 1 node
    19:57:43.710 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 11 node
    19:57:43.710 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 12 node
    19:57:44.212 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 1 done
    19:57:44.213 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 2 node
    19:57:44.214 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 3 node
    19:57:44.215 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 11 done
    19:57:44.216 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 12 done
    19:57:44.217 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 13 node
    19:57:44.717 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 2 done
    19:57:44.718 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 7 node
    19:57:44.719 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 9 node
    19:57:44.720 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 8 node
    19:57:44.721 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 3 done
    19:57:44.722 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 83 - node 4 depends on [3, 13]
    19:57:44.724 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 5 node
    19:57:44.726 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 6 node
    19:57:44.728 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 13 done
    19:57:44.729 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 4 node
    19:57:44.730 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 14 node
    19:57:45.219 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 7 done
    19:57:45.221 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 9 done
    19:57:45.223 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doExecute 80 - Going to schedule 10 node
    19:57:45.225 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 8 done
    19:57:45.227 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 5 done
    19:57:45.228 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 6 done
    19:57:45.234 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 4 done
    19:57:45.235 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 14 done
    19:57:45.725 [main] DEBUG c.n.e.DefaultDependentTasksExecutor.doWaitForExecution 105 - Processing of node 10 done
    19:57:45.726 [main] INFO  c.n.e.DefaultDependentTasksExecutor.execute 69 - Total Time taken to process 14 jobs each taking 500 ms is 2022 ms instead of 7000 ms
    19:57:45.728 [main] INFO  c.n.e.DefaultDependentTasksExecutor.execute 70 - [1, 11, 12, 2, 3, 13, 7, 9, 8, 5, 6, 4, 14, 10]
```

