package com.example.myapplication

/**
 * @property message It can be used to provide additional information through the [message] parameter, which indicates the reason for
 *   writing the annotation or how to remove it.
 */
@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FILE,
    AnnotationTarget.FIELD,
    AnnotationTarget.TYPE,
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Generated(val message: String = "")

/** Alias for the [Generated] annotation, indicating that the associated code should not be considered for test coverage. */
typealias NoCoverage = Generated