package com.rontaxi.constants

enum class BookingStatus(val value: Int) {
    PENDING(0),
    ONTHEWAY(2),
    COMPLETED(3),
    ARRIVED(4),
    STARTED(5),
    CANCELLEDBYCUSTOMER(6),
    CANCELLEDBYDRIVER(7)
}