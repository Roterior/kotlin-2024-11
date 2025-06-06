package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = GsStCommand.UPDATE

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}
