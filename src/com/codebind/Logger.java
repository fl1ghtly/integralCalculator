package com.codebind;

public class Logger {
    private boolean showLog;

    public Logger(boolean showLog)
    {
        this.showLog = showLog;
    }

    public void log(String message)
    {
        if (showLog)
        {
            System.out.println(message);
        }
    }

    public void log(String message, boolean overrideShowLog)
    {
        if (overrideShowLog)
        {
            System.out.println(message);
        }
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }
}
