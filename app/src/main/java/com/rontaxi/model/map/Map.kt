package com.rontaxi.model.map

import java.io.Serializable

class Address:Serializable {

    constructor()

    constructor(
        lat: Double?,
        lng: Double?,
        address: String?,
        city: String?,
        postalCode: String?,
        state: String?,
        country: String?
    ) {
        this.lat = lat
        this.lng = lng
        this.address = address
        this.city = city
        this.postalCode = postalCode
        this.state = state
        this.country = country
    }

    var lat: Double?=null
    var lng: Double?=null
    var address: String?=null
    var city: String?=null
    var postalCode: String?=null
    var state: String?=null
    var country: String?=null


}



class RecentSearch(): Serializable{

    var result= ArrayList<RecentSearchModel>()


}


class  RecentSearchModel{

    var primaryAddress: String?=null
    var secondaryAddress: String?=null
    var placeId: String?=null

    constructor(primaryAddress: String?, secondaryAddress: String?, placeId: String?) {
        this.primaryAddress = primaryAddress
        this.secondaryAddress = secondaryAddress
        this.placeId = placeId
    }
}


