package state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class StateLogger {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(RuntimeClassFactory.of(Object.class))
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .setPrettyPrinting()
            .create();

    private static final String LOGGER_OUTPUT_FILE = "StateLogger.outputFile";
    private static boolean ACTIVE = Boolean.TRUE;

    public static String getStateAsJson(Object... o) {
        if (!ACTIVE) {
            return null;
        }

        int expand = 3;
        Object[] newArray = new Object[o.length + expand];
        System.arraycopy(o, 0, newArray, expand, o.length);

        newArray[0] = "Time: " + DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(LocalDateTime.now());
        newArray[1] = "Thread: " + Thread.currentThread().getName();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement[] subTrace = new StackTraceElement[stackTrace.length - 2];
        System.arraycopy(stackTrace, 2, subTrace, 0, subTrace.length);
        newArray[2] = "StackTrace: " + Arrays.stream(subTrace).map(StackTraceElement::toString)
                .collect(Collectors.joining(", "));
        return GSON.toJson(newArray);
    }

    public static void log(Object... o) {
        if (!ACTIVE) {
            return;
        }

        String path = System.getProperty(LOGGER_OUTPUT_FILE);
        File outputFile = StringUtils.isNotBlank(path) ? new File(path) : null;

        try {

            if (outputFile != null && outputFile.exists()) {
                try {
                    outputFile.createNewFile();
                } catch (IOException e) {
                    System.err.println(StateLogger.class.getSimpleName() +
                            ".log(Object ...o): Error creating file" + outputFile.getAbsolutePath());
                    outputFile = null;
                }
            }

            if (outputFile != null) {
                outputFile.createNewFile();
                try (Writer writer = new FileWriter(outputFile, true)) {
                    GSON.toJson(getStateAsJson(o), writer);
                }
            } else {
                System.out.println(getStateAsJson(o));
            }

        } catch (IOException e) {
            System.err.println(StateLogger.class.getSimpleName() +
                    ".log(Object ...o): Error logging state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void enable() {
        ACTIVE = Boolean.TRUE;
    }

    public static void disable() {
        ACTIVE = Boolean.FALSE;
    }

    private StateLogger() {
    }
}