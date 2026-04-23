/*
 * Copyright (c) 2026 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.util;

/**
 * Helpers for safely embedding user-controlled values inside log messages.
 *
 * <p>
 * Loggers in this codebase write to plain text and JSON sinks that delimit
 * records with newlines. Forwarding raw user input therefore lets an attacker
 * forge entries by smuggling {@code \n[INFO] ...} into a request and is the
 * core of CWE-117 (log injection). This helper strips control characters and
 * caps length so every external string is safe to drop straight into an SLF4J
 * placeholder.
 *
 * <p>
 * Always wrap any value originating from an HTTP request, header, path segment,
 * query parameter, JSON body, file name or third-party callback before logging
 * it.
 */
public final class LogSanitizer {

  private static final int MAX_LOG_LENGTH = 256;

  private static final String NULL_PLACEHOLDER = "<null>";

  private static final String TRUNCATED_SUFFIX = "...<truncated>";

  private LogSanitizer() {
  }

  public static String forLog(Object value) {
    if (value == null) {
      return NULL_PLACEHOLDER;
    }
    String s = value.toString();
    int len = s.length();
    StringBuilder sb = new StringBuilder(Math.min(len, MAX_LOG_LENGTH));
    int limit = Math.min(len, MAX_LOG_LENGTH);
    for (int i = 0; i < limit; i++) {
      char c = s.charAt(i);
      if (c == '\t') {
        sb.append(' ');
      } else if (c < 0x20 || c == 0x7f) {
        sb.append('_');
      } else {
        sb.append(c);
      }
    }
    if (len > MAX_LOG_LENGTH) {
      sb.append(TRUNCATED_SUFFIX);
    }
    return sb.toString();
  }
}
