package com.ivygames.morskoiboi.bluetooth;

import org.commons.logger.Ln;

import java.io.Closeable;
import java.io.IOException;

public class BluetoothUtils {

    private BluetoothUtils() {

    }

    /**
     * Get the device MAC address, which is the last 17 chars in the info
     */
    public static String extractMacAddress(String info) {
        return info.substring(info.length() - 17);
    }

    static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                Ln.w(e, "error closing");
            }
        }
    }
}
