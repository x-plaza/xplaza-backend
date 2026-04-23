/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * One-shot bootstrap that reconciles environments which were deployed before
 * the V1/V2/V3 -> single-V1 consolidation.
 *
 * The old flyway_schema_history on those environments still lists V2 and V3
 * with their pre-consolidation checksums, so Flyway validate() will refuse to
 * run the new, checksum-different single V1. Since there is no production data
 * to preserve yet, we detect this exact situation and drop the public schema so
 * Flyway can apply the new baseline cleanly.
 *
 * Behaviour matrix: - Fresh database (no flyway_schema_history) -> skip,
 * migrate normally - History only has version=1 (post-consolidation) -> skip,
 * migrate is a no-op - History has any row with version in (2, 3) ->
 * drop+recreate public, migrate fresh
 *
 * This is safe to leave in the codebase indefinitely: once every environment
 * has been reconciled it becomes a cheap check-and-skip on startup.
 */
@Configuration
public class FlywayBootstrapConfig {

  private static final Logger log = LoggerFactory.getLogger(FlywayBootstrapConfig.class);

  @Bean
  public FlywayMigrationStrategy consolidationAwareMigrationStrategy(DataSource dataSource) {
    return flyway -> {
      if (hasStaleConsolidationHistory(dataSource)) {
        log.warn(
            "Detected pre-consolidation Flyway history (V2/V3 rows present). "
                + "Dropping public schema so the consolidated V1 baseline can be applied cleanly. "
                + "This is a one-shot reconciliation; future startups will skip it.");
        resetPublicSchema(dataSource);
      }
      flyway.migrate();
    };
  }

  private boolean hasStaleConsolidationHistory(DataSource dataSource) {
    try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
      try (ResultSet rs = s.executeQuery(
          "SELECT EXISTS (SELECT 1 FROM information_schema.tables "
              + "WHERE table_schema = current_schema() AND table_name = 'flyway_schema_history')")) {
        if (!rs.next() || !rs.getBoolean(1)) {
          return false;
        }
      }
      try (ResultSet rs = s.executeQuery(
          "SELECT COUNT(*) FROM flyway_schema_history WHERE version IN ('2', '3')")) {
        return rs.next() && rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      log.warn("Could not inspect flyway_schema_history for consolidation check; "
          + "proceeding with plain migrate. cause={}", e.getMessage());
      return false;
    }
  }

  private void resetPublicSchema(DataSource dataSource) {
    try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
      s.execute("DROP SCHEMA public CASCADE");
      s.execute("CREATE SCHEMA public");
      s.execute("GRANT ALL ON SCHEMA public TO public");
      log.info("public schema reset complete");
    } catch (SQLException e) {
      throw new IllegalStateException(
          "Failed to reset public schema for Flyway consolidation bootstrap", e);
    }
  }
}
