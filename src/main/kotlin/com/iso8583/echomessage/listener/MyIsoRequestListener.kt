package com.iso8583.echomessage.listener

import com.iso8583.echomessage.IsoUtil.logISOMsg
import org.jpos.iso.BaseChannel
import org.jpos.iso.ISOMsg
import org.jpos.iso.ISORequestListener
import org.jpos.iso.ISOSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MyIsoRequestListener : ISORequestListener {
    private val logger: Logger = LoggerFactory.getLogger(MyIsoRequestListener::class.java)

    override fun process(isoSource: ISOSource?, isoMsg: ISOMsg?): Boolean {
        logger.info("process is called")
        kotlin.runCatching {
            logger.info("Incoming message source :: ${(isoSource as BaseChannel).socket.inetAddress.hostAddress}")

            processReceivedMessage(isoSource, isoMsg)
        }.onFailure {
            it.printStackTrace()
        }
        return true
    }

    /**
     * this method receives an iso message from a source
     * process it and returns response via the same source
     */
    private fun processReceivedMessage(isoSource: ISOSource?, isoMsg: ISOMsg?) {
        logger.info("Incoming message :: {}", isoMsg)
        //Log incoming message
        isoMsg?.let { logISOMsg(it) }

        //Can decide to send message a remote host like NIBSS

        //echo message back to the sender with updated MTI and response code

        val response = isoMsg?.clone() as ISOMsg

        response.mti = "0810"
        response.set(39, "00")

        isoSource?.send(response)
    }
}