package com.kphd.systemd;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface LibSystemd extends Library {
    LibSystemd INSTANCE = Native.load("systemd", LibSystemd.class);

    /**
     * The main function of systemd is notify.
     * @param unset_environment if true, the NOTIFY_SOCKET variable will be removed from the environment (usually set to 0/false)
     * @param state status string (e.g., "READY=1")
     * @return a negative number on error, >0 on success
     */
    int sd_notify(int unset_environment, String state);
}