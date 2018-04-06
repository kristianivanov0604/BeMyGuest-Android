package com.be.my.guest.api.domain;

import java.util.ArrayList;
import java.util.Calendar;

import com.be.my.guest.R;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * Event model
 * 
 * @author Abraham Sandbak
 * 
 */
public class EVENT {

	/**
	 * EVENT id
	 */
	@SerializedName("_id")
	public String id;

	/**
	 * Place Model
	 */
	@SerializedName("placeId")
	public String placeId;
	private PLACE place;

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
	 * List of user id's that have RSVP'd to event
	 */
	@SerializedName("rsvpList")
	public ArrayList<String> rsvpList;

	/**
	 * true if enabled for receiving rsvp's
	 */
	@SerializedName("canRsvp")
	public boolean canRsvp;

	/**
	 * Number of minutes needed before enabling rsvp list form confirmal
	 */
	@SerializedName("rsvpTimeLimit")
	public Number rsvpTimeLimit;

	/**
	 * Number of points won per rsvp confirmal
	 */
	@SerializedName("rsvpReward")
	public int rsvpReward;

	/**
	 * true if event is disabled for listing
	 */
	@SerializedName("disabled")
	public boolean disabled;

	public Calendar getDate() {
		if (this.dateCal == null) {
			if (this.date != null && this.date.length() > 0) {
				this.dateCal = BmgUtils.stringToCalendar(this.date,
						R.string.ISO8601_DATE_FORMAT);
			}
			return this.dateCal;
		} else {
			return this.dateCal;
		}
	}

	public PLACE getPlace() {
		if (this.place == null && this.placeId != null) {
			this.place = LocalStorage.findPlace(this.placeId);
		}
		return this.place;
	}

	/**
	 * Check the user whose _id is same with id is exist in the RSVP list
	 * 
	 * @param id
	 * @return
	 */
	public boolean isListedUser(String id) {
		if (this.rsvpList != null && this.rsvpList.size() > 0) {
			return this.rsvpList.contains(id);
		}
		return false;
	}

	/**
	 * Check the time limit is possible
	 * @return
	 */
	public boolean isClosedList() {
		Calendar from = Calendar.getInstance();
		Calendar to = this.getDate();
		if (BmgUtils.minutesBetweenDates(from, to) < BmgUtils
				.getIntFromRes(R.integer.LISTING_POSSIBLE_MINUTES))
			return true;

		return false;
	}

	public static EVENT fromJson(JsonElement json) {
		Gson gson = new Gson();
		EVENT event = null;
		try {
			event = gson.fromJson(json, EVENT.class);
		} catch (Exception e) {
			event = null;
		}
		return event;
	}

}
