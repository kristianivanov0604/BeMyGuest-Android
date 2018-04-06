package com.be.my.guest.api.domain;

import java.util.ArrayList;
import java.util.Calendar;

import android.location.Location;

import com.be.my.guest.R;
import com.be.my.guest.api.domain.PARTY.PHONE;
import com.be.my.guest.api.domain.PARTY.PICTURE;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * Place model
 * 
 * @author Abraham Sandbak
 * 
 */
public class PLACE {
	/**
	 * PLACE id
	 */
	@SerializedName("_id")
	public String id;

	/**
	 * Id from partner that owns the place
	 */
	@SerializedName("partnerId")
	public String partnerId;

	/**
	 * Name
	 */
	@SerializedName("name")
	public String name;

	/**
	 * ObjectId of city where party is going to happen
	 */
	@SerializedName("cityId")
	public String cityId;

	/**
	 * Date
	 */
	@SerializedName("date")
	public String date;
    public Calendar dateCal;

	/**
	 * Description
	 */
	@SerializedName("description")
	public String description;

	/**
	 * Location
	 */
	@SerializedName("location")
	public LOCATION location;

	/**
	 * Phone
	 */
	@SerializedName("phone")
	public PHONE phone;

	/**
	 * Pictures
	 */
	@SerializedName("pictures")
	public PICTURE pictures;

	/**
	 * true if can checkin on place
	 */
	@SerializedName("canCheckin")
	public boolean canCheckin;

	/**
	 * Reward for each checkin done
	 */
	@SerializedName("checkinReward")
	public int checkinReward;

	/**
	 * Weight for listing
	 */
	@SerializedName("weight")
	public Number weight;

	/**
	 * 1 for club, 2 for bar
	 */
	@SerializedName("type")
	public int type;

	public String getId() {
		return id;
	}

	public String getImageURL() {
		if (this.pictures != null && this.pictures.photos != null
				&& this.pictures.photos.size() > 0) {
			return this.pictures.photos.get(0);
		} else {
			return null;
		}
	}
	
	public Calendar getDate() {
		if (this.dateCal == null) {
			if (this.date != null && this.date.length() > 0) {
				this.dateCal = BmgUtils.stringToCalendar(this.date, R.string.ISO8601_DATE_FORMAT);
			}
			return this.dateCal;
		} else {
			return this.dateCal;
		}
	}
	
	public static PLACE fromJson(JsonElement json) {
		Gson gson = new Gson();
		PLACE place = null;
		try {
			place = gson.fromJson(json, PLACE.class);
		} catch (Exception e) {
			place = null;
		}
		return place;
	}

	public float getDistance(Location from) {
		Location to = location.getLocation();
		float distance = from.distanceTo(to);
		return distance / 1000;
	}

}
