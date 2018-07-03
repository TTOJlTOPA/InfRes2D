package com.poltora.infres2d;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ResourceManager {
    public static final ScheduledExecutorService INPUT_EXEC_SERVICE = Executors.newSingleThreadScheduledExecutor();

    private ResourceManager() {

    }
}
