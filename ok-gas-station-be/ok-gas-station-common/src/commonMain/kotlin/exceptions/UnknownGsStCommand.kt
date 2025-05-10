package ru.otus.otuskotlin.gasstation.common.exceptions

import ru.otus.otuskotlin.gasstation.common.models.GsStCommand


class UnknownGsStCommand(command: GsStCommand) : Throwable("Wrong command $command at mapping toTransport stage")
