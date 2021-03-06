/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler;

import bdsm.scheduler.exception.UncaughtExceptions;
import bdsm.scheduler.util.BdsmLogger;
import java.util.Calendar;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author 00030663
 */
final public class BdsmBackThread extends Thread {
    private BdsmScheduler parent;
    private boolean terminated;
    private long sleep;
    private boolean stale;
    private boolean assigned;
    private Map context;
    private IBdsmWorker worker;
    
    public BdsmBackThread(BdsmScheduler parent, long sleep) {
        this.parent = parent;
        this.terminated = false;
        this.sleep = sleep;
        this.stale = false;
        this.assigned = false;
        this.context = null;
        this.worker = null;
        getLogger().info("BACKTREAD BEFORE UNCAUGHT");
        this.setUncaughtExceptionHandler(new UncaughtExceptions());
    }
    
    @Override
    public final void run() {
        try {
            while (!terminated) {
                if (assigned) {
                    getLogger().info("Start execute");
                    long start = Calendar.getInstance().getTimeInMillis();
                    long end, elapse;
                    try {
                        execute();
                    } catch (Throwable e) {
                        getLogger().fatal("Execute", e);
                    } finally {
                        end = Calendar.getInstance().getTimeInMillis();
                        elapse = end - start;
                        getLogger().info("End execute (" + elapse + " ms)");
                        assigned = false;
                        parent.unassigned(this);
                    }
                }

                try {
                    if (sleep > 0) Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    getLogger().fatal("sleep", e);
                }
            }
        } catch (Throwable e) {
            stale = true;
            getLogger().fatal("Stale", e);
        }
    }

    private BdsmLogger getLogger() {
        return (BdsmLogger)BdsmLogger.getLogger(this.getClass().getName());
    }

    protected void execute() {
        if (worker != null) {
            worker.execute(context);
        }
        worker = null;
    }

    public final void terminate() {
        terminated = true;
    }

    public final void assign(Map context, IBdsmWorker worker) {
        assigned = true;
        this.context = context;
        this.worker = worker;
    }
    
}