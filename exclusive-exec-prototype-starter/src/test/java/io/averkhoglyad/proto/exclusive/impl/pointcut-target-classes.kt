//@file:Suppress("pack")
package io.averkhoglyad.proto.exclusive.impl.test.pointcut.target

open class Child {

}

open class Parent : Child() {

}

class GrandParent : Parent() {

}