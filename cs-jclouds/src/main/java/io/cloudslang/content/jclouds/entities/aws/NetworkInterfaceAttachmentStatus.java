package io.cloudslang.content.jclouds.entities.aws;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/8/2016.
 */
public enum NetworkInterfaceAttachmentStatus {
    ATTACHING,
    ATTACHED,
    DETACHING,
    DETACHED;

    public static String getValue(String input) throws Exception {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized networkInterfaceAttachmentStatus value: [" + input + "]. " +
                    "Valid values are: attaching, attached, detaching, detached.");
        }
    }
}
