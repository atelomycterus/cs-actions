package io.cloudslang.content.jclouds.entities.aws;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/6/2016.
 */
public enum MonitoringState {
    DISABLED,
    ENABLED;

    public static String getValue(String input) throws Exception {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid monitoringState value: [" + input + "]. Valid values: disabled, enabled.");
        }
    }
}
