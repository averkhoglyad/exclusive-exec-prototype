package io.averkhoglyad.proto.exclusive

import java.util.*

data class Key(val bean: Any, val signature: String, val args: Array<Any?>) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val (bean1, signature1, args1) = o as Key
        return bean == bean1 && signature == signature1 && args.contentEquals(args1)
    }

    override fun hashCode(): Int {
        var result = Objects.hash(bean, signature)
        result = 31 * result + args.contentHashCode()
        return result
    }
}
