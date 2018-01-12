package me.smartproxy.ui;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tanck on 11/29/2017.
 * sourced from Tammen Bruccoleri(SharedServices)
 */
public class ProxyLogger {
    private static final String TAG = ProxyLogger.class.getSimpleName();
    private static final int FILE_SIZE_CHECK_INTERVAL = 40 * 60 * 1000;   // 40 mins
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.US);
    private static final File OUTPUT_FILE;
    private static BufferedWriter out;
    private static long lastFileSizeCheck = 0;
    private static final String LOG_FILE_NAME = "proxy.log";

    static {
        OUTPUT_FILE = getOutputFile();
        try {
            out = new BufferedWriter(new FileWriter(getOutputFile(), true));
        } catch (IOException | NullPointerException e) {
            Log.e(TAG, "Error opening Proxy log output file.", e);
        }
    }

    public static void d(String tag, String entry) {
        if (tag != null && entry != null) {
            Log.d(tag, entry);
            writeEntry(tag, entry);
        }
    }

    public static void i(String tag, String entry) {
        if (tag != null && entry != null) {
            Log.i(tag, entry);
            writeEntry(tag, entry);
        }
    }

    public static void w(String tag, String entry) {
        if (tag != null && entry != null) {
            Log.w(tag, entry);
            writeEntry(tag, entry);
        }
    }

    public static void e(String tag, String entry) {
        if (tag != null && entry != null) {
            Log.e(tag, entry);
            writeEntry(tag, entry);
        }
    }

    public static void e(String tag, String entry, Throwable t) {
        if (tag != null && entry != null) {
            Log.e(tag, entry, t);
            writeEntry(tag, entry);
        }
    }

    private static void writeEntry(String tag, String entry) {
        String formatted = DATE_FORMAT.format(new Date()) + tag + "  " + entry + '\n';
        try {
            out.write(formatted);
            out.flush();
        } catch (IOException ignore) {
        }

        long now = System.currentTimeMillis();
        if (now - lastFileSizeCheck > FILE_SIZE_CHECK_INTERVAL) {
            lastFileSizeCheck = now;
            Log.i(TAG, "Proxy Logfile size: " + OUTPUT_FILE.length());

            if (OUTPUT_FILE.getFreeSpace() < 50000000) {  // 50meg low disk
                Log.w(TAG, "Proxy Log file exceeded size limit or low disk space, deleting");
                //noinspection ResultOfMethodCallIgnored
                OUTPUT_FILE.delete();
                try {
                    out = new BufferedWriter(new FileWriter(OUTPUT_FILE));
                } catch (IOException e) {
                    Log.e(TAG, "Error creating new Proxy log file", e);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    static boolean zeroLogContents() {
        try {
            if (out != null)
                out.close();
            out = new BufferedWriter(new FileWriter(getOutputFile(), true));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    private static File getOutputFile() {
        Log.i(TAG, "Checking output file...");
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            throw new IllegalStateException("Can't access external directory.");
        File tabletLogsDir = new File(Environment.getExternalStorageDirectory(), "TabletLogs");
        //noinspection ResultOfMethodCallIgnored
        tabletLogsDir.mkdir();
        if (!tabletLogsDir.isDirectory()) {
            Log.e(TAG, "Error opening log directory");
            return (null);
        }
        return (new File(tabletLogsDir, LOG_FILE_NAME));
    }
}