package com.dmv.footballheadz.season;

import java.util.List;
import java.util.Optional;


public interface IService<T> {

    Optional<T> read(String id);

    Optional<T> create(T t);

    Optional<T> replace(T newData);

    Optional<T> update(T newData);

    boolean delete(String key);

    List<T> list();

    List<T> listByYear(String key);
}
