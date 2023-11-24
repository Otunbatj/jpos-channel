package com.iso8583.echomessage

import com.iso8583.echomessage.listener.MyIsoRequestListener
import ng.tegritech.jposclient.packager.PostBridgePackager
import org.jpos.iso.ISOServer
import org.jpos.iso.channel.ASCIIChannel
import org.jpos.iso.channel.XMLChannel
import org.jpos.iso.packager.XMLPackager
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EchomessageApplication : CommandLineRunner {

    override fun run(vararg args: String?) {
        //Create a simple mock server that can receive request from client and route it through the listener
        createCustomServer()
    }

    private fun createCustomServer() {
        val hostIp = "localhost"
        val hostPort = 10000
        val asciiPort = 10002

        //client should reach the server on the above ip and port

        val xmlChannel = XMLChannel(hostIp, hostPort, XMLPackager())

        val asciiChannel = ASCIIChannel(hostIp, asciiPort, PostBridgePackager())

        val server1 = ISOServer(hostPort, xmlChannel, null)

        val server2 = ISOServer(asciiPort, asciiChannel, null)

        //Add the listener to the created server
        server1.addISORequestListener(MyIsoRequestListener())
        server2.addISORequestListener(MyIsoRequestListener())

        println("Server started")

        Thread(server1).start()
        Thread(server2).start()
    }

}

fun main(args: Array<String>) {
    runApplication<EchomessageApplication>(*args)
}