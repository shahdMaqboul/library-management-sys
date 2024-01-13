package com.library.management.mappers;

public interface Mapper<E,D> {

    D mapEntityToDto(E e);

    E mapEntityFromDto(D d);

}