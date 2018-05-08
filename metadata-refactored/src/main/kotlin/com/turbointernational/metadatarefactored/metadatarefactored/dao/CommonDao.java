package com.turbointernational.metadatarefactored.metadatarefactored.dao;

import java.util.List;
import java.util.Optional;

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
public interface CommonDao<T> {
    Optional<T> findById(Long object);
    <S extends T> S save(S s);
    List<T> findAll();
}