package io.averkhoglyad.proto.exclusive

import java.util.*

data class Key(val bean: Any, val signature: String, val args: Array<Any?>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val (bean1, signature1, args1) = other as Key
        return bean == bean1 && signature == signature1 && args.contentEquals(args1)
    }

    override fun hashCode(): Int {
        var result = Objects.hash(bean, signature)
        result = 31 * result + args.contentHashCode()
        return result
    }
}
