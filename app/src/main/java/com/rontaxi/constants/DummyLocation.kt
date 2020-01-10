package com.rontaxi.constants

var dummydata: ArrayList<Pair<Double, Double>> = ArrayList()


fun getDummyRoute(): ArrayList<Pair<Double, Double>> {
    if (dummydata.size == 0) {
        dummydata.add(Pair(30.6757051, 76.7397417))
//        dummydata.add(Pair(30.704320000000003, 76.7397417))
        dummydata.add(Pair(30.675710000000002, 76.73974000000001))
        dummydata.add(Pair(30.676220000000004, 76.73931))
        dummydata.add(Pair(30.67696, 76.73869))
        dummydata.add(Pair(30.67706, 76.73861000000001))
        dummydata.add(Pair(30.677170000000004, 76.73852000000001))
        dummydata.add(Pair(30.67733, 76.73838))
        dummydata.add(Pair(30.6773302, 76.7383846))
        dummydata.add(Pair(30.677830000000004, 76.73919000000001))
        dummydata.add(Pair(30.678250000000002, 76.73994))
        dummydata.add(Pair(30.678252, 76.7399384))
        dummydata.add(Pair(30.678250000000002, 76.73994))
        dummydata.add(Pair(30.679890000000004, 76.73857000000001))
    }
    return dummydata
}