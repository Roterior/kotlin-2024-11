package ru.otus.otuskotlin.gasstation.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to GsStContext")
