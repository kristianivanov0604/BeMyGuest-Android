package com.be.my.guest.api.domain;

import java.util.ArrayList;
import java.util.Calendar;

import com.be.my.guest.R;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * Party model
 * 
 * @author Abraham Sandbak
 * 
 */
public class PARTY {
	/**
	 * PARTY id
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
	 * true if event is disabled for listing
	 */
	@SerializedName("disabled")
	public boolean disabled;

	/**
	 * true if party is selling tickets
	 */
	@SerializedName("sellingTickets")
	public boolean sellingTickets;

	/**
	 * true if party is listed on both app & site
	 */
	@SerializedName("listed")
	public boolean listed;

	/**
	 * Maximum number of tickets per single order
	 */
	@SerializedName("maxTicketsPerOrder")
	public Number maxTicketsPerOrder;
	
	public int rewordPoints;

	public static PARTY fromJson(JsonElement json) {
		Gson gson = new Gson();
		PARTY party = null;
		try {
			party = gson.fromJson(json, PARTY.class);
		} catch (Exception e) {
			party = null;
		}
		party.rewordPoints = BmgUtils.getIntFromRes(R.integer.PARTY_REWARD_POINTS);
		return party;
	}
	
	public Calendar getDate() {
		if (this.date != null && this.date.length() > 0) {
			return BmgUtils.stringToCalendar(this.date, R.string.ISO8601_DATE_FORMAT);
		} else {
			return null;
		}
	}

	public String getImageURL() {
		if (this.pictures != null && this.pictures.photos != null
				&& this.pictures.photos.size() > 0) {
			return this.pictures.photos.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Party Phone class
	 */
	public class PHONE {
		// Country Code
		@SerializedName("countryCode")
		public int countryCode;

		// Area code
		@SerializedName("areaCode")
		public int areaCode;

		// Number
		@SerializedName("number")
		public int number;
		
		public String getNumber() {
			return String.format("(%d)%d", areaCode, number);
		}
	}

	/**
	 * Party Pictures class
	 */
	public class PICTURE {
		// Cover photos of party
		@SerializedName("photos")
		public ArrayList<String> photos;

		// Logo of organization/place/etc
		@SerializedName("logo")
		public String logo;
	}

}
