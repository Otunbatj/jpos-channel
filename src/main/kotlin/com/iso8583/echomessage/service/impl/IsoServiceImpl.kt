package com.iso8583.echomessage.service.impl

import com.iso8583.echomessage.IsoUtil
import com.iso8583.echomessage.enums.MessageFormat
import com.iso8583.echomessage.service.IsoService
import ng.tegritech.jposclient.packager.PostBridgePackager
import org.jpos.iso.BaseChannel
import org.jpos.iso.ISOMsg
import org.jpos.iso.ISOPackager
import org.jpos.iso.ISOUtil
import org.jpos.iso.channel.ASCIIChannel
import org.jpos.iso.channel.XMLChannel
import org.jpos.iso.packager.XMLPackager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class IsoServiceImpl : IsoService {
    private val logger: Logger = LoggerFactory.getLogger(IsoServiceImpl::class.java)

    override fun processIsoMsg(messageFormat: MessageFormat): ISOMsg {
        return when (messageFormat) {
            MessageFormat.ASCII -> sendAsciiMessage()
            MessageFormat.XML -> sendXMLMessage()
        }
    }

    private fun sendXMLMessage(): ISOMsg {
        val isoPackager = XMLPackager()
        val request = buildIsoRequest(isoPackager)
        val channel = XMLChannel("localhost", 10000, isoPackager)
        return sendMessage(request, channel)
    }

    private fun sendAsciiMessage(): ISOMsg {
        val isoPackager = PostBridgePackager()
        val request = buildIsoRequest(isoPackager)
        val channel = ASCIIChannel("localhost", 10002, isoPackager)
        return sendMessage(request, channel)
    }

    private fun sendMessage(request: ISOMsg, channel: BaseChannel): ISOMsg {
        IsoUtil.logISOMsg(request)
        logger.info(ISOUtil.hexdump(request.pack()))

        channel.connect()
        if (channel.isConnected) {
            channel.send(request)
            val response = channel.receive()
            logger.info("****************Response *********************")
            IsoUtil.logISOMsg(response)
            return response
        } else {
            logger.error("Error :: Socket is not connected")
            throw Exception("Error :: Socket is not connected")
        }
    }

    private fun buildIsoRequest(isoPackager: ISOPackager): ISOMsg {
        val request = ISOMsg()
        request.packager = isoPackager
        request.mti = "0800"
        request.set(3, "9A0000")
        request.set(7, "1124094621")
        request.set(11, "094621")
        request.set(12, "094621")
        request.set(13, "1124")
        request.set(41, "203516FH")
        return request
    }
}