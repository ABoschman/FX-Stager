/*
 * Copyright (C) 2015 Arjan Boschman
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package io.boschman.fxstager.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

/**
 * Aides in constructing and executing tasks using a thread pool.
 *
 * @author Arjan Boschman
 */
public class TaskLauncher {

    //TODO: low prio: name threads.
    private final ExecutorService threadpool;

    /**
     * Creates a new TaskLauncher using a new cached thread pool. All threads
     * created are Daemon threads.
     *
     */
    public TaskLauncher() {
        this(Executors.newCachedThreadPool((Runnable runnable) -> {
            final Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            thread.setName("TaskLauncherThread" + thread.getId());//Todo: multiple threadpools.
            return thread;
        }));
    }

    /**
     * Creates a new TaskLauncher using the given thread pool.
     *
     * @param threadpool The ExecutorService that is to execute the Tasks
     * created by this launcher.
     */
    public TaskLauncher(ExecutorService threadpool) {
        this.threadpool = threadpool;
    }

    /**
     * Schedule a Task for execution.
     *
     * @param task The Task to be executed.
     */
    public void launchTask(Task<?> task) {
        assert Platform.isFxApplicationThread() : "Attempted to call "
                + "Tasklauncher.launchTask() from a thread that is not the "
                + "JavaFX Application Thread.";
        this.threadpool.execute(task);
    }

    /**
     * Creates and runs a Task that produces a value and then consumes that
     * value upon completion. Any unchecked exceptions will be logged, but
     * otherwise disregarded. This method may only be called from the JavaFX
     * Application thread.
     *
     * @param <T> The type parameter of the Task.
     * @param work Whatever work needs to be done. This will be executed in a
     * separate thread, and will produce a certain result of type T.
     * @param uponCompletion This will be executed upon Task completion, and
     * will consume the value returned by the Task.
     */
    public <T> void makeAndLaunchTask(Supplier<T> work, Consumer<T> uponCompletion) {
        launchTask(makeTask(work, uponCompletion));
    }

    /**
     * Creates and runs a Task that produces no value. Does nothing upon
     * completion. Any unchecked exceptions will be logged, but otherwise
     * disregarded. This method may only be called from the JavaFX Application
     * thread.
     *
     * @param work Whatever work needs to be done. This will be executed in a
     * separate thread.
     */
    public void makeAndLaunchVoidTask(Supplier<Void> work) {
        launchTask(makeTask(work, (Void v) -> {
            //Do nothing upon completion.
        }));
    }

    /**
     * Creates a Task that produces a value and then consumes that value upon
     * completion. Any unchecked exceptions will be logged, but otherwise
     * disregarded.
     *
     * @param <T> The type parameter of the Task.
     * @param work Whatever work needs to be done. This will be executed in a
     * separate thread, and will produce a certain result of type T.
     * @param uponSucceeded This will be executed upon Task completion, and will
     * consume the value returned by the Task.
     * @return The constructed Task.
     */
    public <T> Task<T> makeTask(Supplier<T> work, Consumer<T> uponSucceeded) {
        final Task<T> task = new Task<T>() {
            @Override
            protected T call() {
                return work.get();
            }
        };
        task.setOnSucceeded((WorkerStateEvent event) -> {
            try {
                uponSucceeded.accept(task.get());
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(TaskLauncher.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            final Throwable throwable = task.getException();
            if (throwable != null) {
                Logger.getLogger(TaskLauncher.class.getName()).log(Level.SEVERE, null, throwable);
            }
        });
        return task;
    }

    /**
     * Schedule a Runnable for execution.
     *
     * @param runnable
     */
    public void executeRunnable(Runnable runnable) {
        this.threadpool.execute(runnable);
    }

    /**
     * Checks if this is the Application thread. If so, it just runs this
     * runnable on this thread. If not, it schedules it for execution on the
     * Application thread. Hence, this method can make no guarantees about when
     * the runnable will be executed.
     *
     * @param runnable The runnable that is ensured to be executed on the JavaFX
     * Application thread.
     */
    public void runOnApplicationThread(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(() -> runnable.run());
        }
    }

}
