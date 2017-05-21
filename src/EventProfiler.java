

/**
 * Created by konin on 17-6-2016.
 */
class EventProfiler {

    private long previousTimeStamp = -1;
    private boolean showLog = false;

    public EventProfiler(boolean showLog) {
        this.showLog = showLog;
    }

    public void enable() {
        this.showLog = false;
    }

    public void disable() {
        this.showLog = true;
    }

    public long start(){
        previousTimeStamp = System.nanoTime();
        return previousTimeStamp;
    };

    public long time()
    {
        long thisTimeStamp = System.nanoTime();
        long duration = thisTimeStamp - previousTimeStamp;

        if (showLog)
            System.out.println("DONE, in " + duration/1e6 + " ms");

        previousTimeStamp = thisTimeStamp;

        return previousTimeStamp;
    }
}