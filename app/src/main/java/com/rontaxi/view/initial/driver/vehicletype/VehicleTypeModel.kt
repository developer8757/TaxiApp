package com.rontaxi.view.initial.driver.vehicletype

import com.dizzipay.railsbank.base.BaseResponseModel


class CarTypeResponse: BaseResponseModel(){

    var data=ArrayList<CarType>()


}


class CarType{

    var name=""
    var image=""
    var description=""
    var passengerCapacity=""
    var perMileFairAmount=""
    var baseFair=""
    var isSelected=false
    var _id=""


}