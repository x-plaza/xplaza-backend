/*
 * Copyright (c) 2026 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LogSanitizerTest {

  @Test
  void nullValueIsRenderedAsPlaceholder() {
    assertThat(LogSanitizer.forLog(null)).isEqualTo("<null>");
  }

  @Test
  void plainAsciiPassesThrough() {
    assertThat(LogSanitizer.forLog("hello world 42!")).isEqualTo("hello world 42!");
  }

  @Test
  void crlfIsScrubbedToDefeatLogForging() {
    String forged = "harmless\r\n[INFO] forged entry";
    assertThat(LogSanitizer.forLog(forged))
        .doesNotContain("\n")
        .doesNotContain("\r")
        .isEqualTo("harmless__[INFO] forged entry");
  }

  @Test
  void otherControlCharsAreScrubbed() {
    String s = "tab\there\u0007\u001b[31m";
    String out = LogSanitizer.forLog(s);
    assertThat(out).isEqualTo("tab here__[31m");
  }

  @Test
  void overlongValuesAreTruncated() {
    String big = "a".repeat(500);
    String out = LogSanitizer.forLog(big);
    assertThat(out).hasSize(256 + "...<truncated>".length());
    assertThat(out).endsWith("...<truncated>");
  }

  @Test
  void nonStringValuesUseToString() {
    assertThat(LogSanitizer.forLog(42L)).isEqualTo("42");
  }
}
