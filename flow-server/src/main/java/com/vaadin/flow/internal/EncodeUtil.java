/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.internal;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utilities related to various encoding schemes.
 * <p>
 * For internal use only. May be renamed or removed in a future release.
 *
 * @author Vaadin Ltd
 * @since 1.0
 */
public final class EncodeUtil {

    private EncodeUtil() {
        // Static utils only
    }

    /**
     * Encodes the given string to UTF-8 <code>value-chars</code> as defined in
     * RFC5987 for use in e.g. the <code>Content-Disposition</code> HTTP header.
     *
     * @param value
     *            the string to encode, not <code>null</code>
     * @return the encoded string
     */
    public static String rfc5987Encode(String value) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        while (i < value.length()) {
            int cp = value.codePointAt(i);
            if (cp < 127 && (Character.isLetterOrDigit(cp) || cp == '.')) {
                builder.append((char) cp);
            } else {
                // Create string from a single code point
                String cpAsString = new String(new int[] { cp }, 0, 1);

                appendHexBytes(builder, cpAsString.getBytes(UTF_8));
            }

            // Advance to the next code point
            i += Character.charCount(cp);
        }

        return builder.toString();
    }

    private static void appendHexBytes(StringBuilder builder, byte[] bytes) {
        for (byte byteValue : bytes) {
            // mask with 0xFF to compensate for "negative" values
            int intValue = byteValue & 0xFF;
            String hexCode = Integer.toString(intValue, 16);
            builder.append('%').append(hexCode);
        }
    }

}
