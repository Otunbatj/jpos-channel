package com.iso8583.echomessage.controller

import com.iso8583.echomessage.enums.MessageFormat
import com.iso8583.echomessage.service.IsoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @Autowired
    lateinit var isoService: IsoService

    /**
     * This method sends a message to the server and expects and echo back
     */
    @GetMapping("echo")
    fun testEcho(): String {
        kotlin.runCatching {
            isoService.processIsoMsg(MessageFormat.XML)
        }.onSuccess {
            return it.toString()
        }.onFailure {
            it.printStackTrace()
        }
        return ""
    }

    /**
     * A second endpoint for testing our ASCII packager
     */
    @GetMapping("echo2")
    fun testEcho2(): String {
        kotlin.runCatching {
            isoService.processIsoMsg(MessageFormat.ASCII)
        }.onSuccess {
            return it.toString()
        }.onFailure {
            it.printStackTrace()
        }
        return ""
    }
}