package com.jay.weatherapp.common.data.api.mappers

interface ApiMapper<E, D> {
    fun mapToDomain(entity: E): D
}