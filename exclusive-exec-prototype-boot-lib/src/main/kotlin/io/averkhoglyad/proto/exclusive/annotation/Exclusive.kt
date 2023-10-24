package io.averkhoglyad.proto.exclusive.annotation

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
)
@Retention(
    AnnotationRetention.RUNTIME
)
@MustBeDocumented
annotation class Exclusive {
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class Key
}
