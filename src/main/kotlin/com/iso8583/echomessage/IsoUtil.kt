package com.iso8583.echomessage

import org.jpos.iso.ISOException
import org.jpos.iso.ISOMsg

object IsoUtil {
    fun logISOMsg(msg: ISOMsg) {
        println("----ISO MESSAGE-----")
        try {
            println("  MTI : " + msg.mti)
            for (i in 1..msg.maxField) {
                if (msg.hasField(i)) {
                    println(
                        "    Field-" + i + " : "
                                + msg.getString(i)
                    )
                }
            }
        } catch (e: ISOException) {
            e.printStackTrace()
        } finally {
            println("--------------------")
        }
    }
}