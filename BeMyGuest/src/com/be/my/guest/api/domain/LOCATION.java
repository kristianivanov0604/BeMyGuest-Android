package com.be.my.guest.api.domain;

import android.location.Location;

import com.google.gson.annotations.SerializedName;
/**
 * LOCATION model
 * @author Abraham Sandbak
 *
 */
/**
 * Party location class
 */
public class LOCATION {
	// Latitude
	@SerializedName("lat")
	public double lat;

	// Longitude
	@SerializedName("lng")
	public double lng;

	// Address
	@SerializedName("address")
	public String address;

	// Name
	@SerializedName("name")
	public String name;

    
    public Location getLocation () {
    	Location location = new Location("");
    	location.setLatitude(lat);
    	location.setLongitude(lng);
    	return location;
    }

}


