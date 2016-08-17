package com.gmail.hexragonat.clockGadget

infix fun Any.equalsAny(array : Array<Any?>) : Boolean
{
    array.forEach { if (this == it) return true }
    return false
}