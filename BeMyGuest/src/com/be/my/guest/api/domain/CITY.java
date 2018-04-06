package com.be.my.guest.api.domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
/**
 * City model
 * @author Abraham Sandbak
 *
 */
public class CITY {

	/**
	 * City id
	 */
    @SerializedName("_id")
    public String id;

	/**
	 * Country
	 */
    @SerializedName("country")
    public String country;

	/**
	 * State 
	 */
    @SerializedName("state")
    public String state;

	/**
	 * City name
	 */
    @SerializedName("name")
    public String name;

    /**
     * True if the city is enabled for listing
     */
    @SerializedName("enabled")
    public boolean listingEnabled;

    /**
     * City location
     */
    @SerializedName("location")
    public LOCATION location;
    
	public static CITY fromJson(JsonElement json) {
		Gson gson = new Gson();
		CITY city = null;
		try {
			city = gson.fromJson(json, CITY.class);
		} catch (Exception e) {
			city = null;
		}
		return city;
	}

}
