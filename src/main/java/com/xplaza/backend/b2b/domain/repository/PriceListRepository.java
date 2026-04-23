/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.b2b.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.b2b.domain.entity.PriceList;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Long> {

  Optional<PriceList> findByCode(String code);

  @Query("select pl from PriceList pl where pl.active = true and "
      + "(pl.customerGroupId = :groupId or pl.customerGroupId is null)")
  List<PriceList> findApplicableForGroup(Long groupId);
}
