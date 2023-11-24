package com.iso8583.echomessage.service

import com.iso8583.echomessage.enums.MessageFormat
import org.jpos.iso.ISOMsg

interface IsoService {
   fun processIsoMsg(messageFormat: MessageFormat) : ISOMsg
}