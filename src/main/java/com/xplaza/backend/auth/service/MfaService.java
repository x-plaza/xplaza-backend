/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.auth.service;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.util.Utils;

import org.springframework.stereotype.Service;

/** TOTP-based multi-factor authentication. Issues secrets, generates QR-code data URLs, and verifies codes. */
@Service
public class MfaService {

  private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
  private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
  private final CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
  private final CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, new SystemTimeProvider());

  public String generateSecret() {
    return secretGenerator.generate();
  }

  public String generateQrCodeImageDataUri(String email, String secret) {
    var data = new QrData.Builder()
        .label(email)
        .secret(secret)
        .issuer("X-Plaza")
        .algorithm(HashingAlgorithm.SHA1)
        .digits(6)
        .period(30)
        .build();
    try {
      var bytes = qrGenerator.generate(data);
      return Utils.getDataUriForImage(bytes, qrGenerator.getImageMimeType());
    } catch (QrGenerationException e) {
      throw new IllegalStateException("Could not generate QR code", e);
    }
  }

  public boolean verify(String secret, String code) {
    return codeVerifier.isValidCode(secret, code);
  }
}
