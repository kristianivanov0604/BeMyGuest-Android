package com.be.my.guest.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import com.be.my.guest.activities.base.BaseActivity;
import com.be.my.guest.api.domain.CITY;
import com.be.my.guest.api.domain.EVENT;
import com.be.my.guest.api.domain.PARTY;
import com.be.my.guest.api.domain.PLACE;
import com.be.my.guest.api.domain.USER;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.location.Location;
import android.preference.PreferenceManager;

public class LocalStorage {

	private static final String USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN";
	private static final String USER_IDENTIFIER = "USER_IDENTIFIER";
	private static final String USER_PASSWORD = "USER_PASSWORD";

	public static String accessToken;
	public static String userId;
	public static String userPass;
	public static USER authUser;
	public static double userLatitude;
	public static double userLongitude;
	public static Location userLocation;

	public static int nCurrentCity;
	public static JsonObject citiesJson;
	public static ArrayList<CITY> citiesArray;
	public static ArrayList<String> cityNames;

	public static JsonObject eventsJson;
	public static ArrayList<EVENT> eventsArray;

	public static JsonObject partiesJson;
	public static ArrayList<PARTY> partiesArray;

	public static JsonObject placesJson;
	public static ArrayList<PLACE> placesArray;

	public static void resetStorage() {
		saveAccessToken(null, null, null);
		authUser = null;
		
		userLatitude = 0;
		userLongitude = 0;

		nCurrentCity = 0;
		citiesJson = null;
		citiesArray = null;

		eventsJson = null;
		eventsArray = null;

		partiesJson = null;
		partiesArray = null;

		placesJson = null;
		placesArray = null;
	}

	public static void saveAccessToken(String token, String id, String password) {
		accessToken = token;
		PreferenceManager
				.getDefaultSharedPreferences(BaseActivity.getContext()).edit()
				.putString(USER_ACCESS_TOKEN, accessToken).commit();

		userId = id;
		PreferenceManager
				.getDefaultSharedPreferences(BaseActivity.getContext()).edit()
				.putString(USER_IDENTIFIER, userId).commit();

		userPass = password;
		PreferenceManager
				.getDefaultSharedPreferences(BaseActivity.getContext()).edit()
				.putString(USER_PASSWORD, userPass).commit();

	}
	
	public static void setUserLocation(double latitude, double longitude) {
		userLatitude = latitude;
		userLongitude = longitude;
		if (userLocation == null)
			userLocation = new Location("");
		userLocation.setLatitude(latitude);
		userLocation.setLongitude(longitude);
	}

	public static String loadSavedAccessToken() {
		accessToken = PreferenceManager.getDefaultSharedPreferences(
				BaseActivity.getContext()).getString(USER_ACCESS_TOKEN, null);

		userId = PreferenceManager.getDefaultSharedPreferences(
				BaseActivity.getContext()).getString(USER_IDENTIFIER, null);

		userPass = PreferenceManager.getDefaultSharedPreferences(
				BaseActivity.getContext()).getString(USER_PASSWORD, null);

		return accessToken;
	}

	public static void setCities(JsonObject cities) {
		citiesJson = cities;
		citiesArray = new ArrayList<CITY>();
		cityNames = new ArrayList<String>();
		JsonArray jArray = (JsonArray) cities.get("cities");
		if (jArray != null) {
			for (int i = 0; i < jArray.size(); i++) {
				CITY city = CITY.fromJson(jArray.get(i));
				citiesArray.add(city);
				cityNames.add(city.name);
				if (authUser.profile.cityId != null
						&& authUser.profile.cityId.equals(city.id)) {
					nCurrentCity = i;
				}
			}
		}

		BmgUtils.printLog(cities.toString());
	}

	public static void setEvents(JsonObject events) {
		eventsJson = events;
		eventsArray = new ArrayList<EVENT>();
		JsonArray jArray = (JsonArray) events.get("events");
		if (jArray != null) {
			for (int i = 0; i < jArray.size(); i++) {
				EVENT event = EVENT.fromJson(jArray.get(i));
				eventsArray.add(event);
			}
		}
		BmgUtils.printLog(events.toString());
	}

	public static void setParties(JsonObject parties) {
		partiesJson = parties;
		partiesArray = new ArrayList<PARTY>();
		JsonArray jArray = (JsonArray) parties.get("parties");
		if (jArray != null) {
			for (int i = 0; i < jArray.size(); i++) {
				PARTY party = PARTY.fromJson(jArray.get(i));
				partiesArray.add(party);
			}
		}
		BmgUtils.printLog(parties.toString());
	}

	public static void setPlaces(JsonObject places) {
		placesJson = places;
		placesArray = new ArrayList<PLACE>();
		JsonArray jArray = (JsonArray) places.get("places");
		if (jArray != null) {
			for (int i = 0; i < jArray.size(); i++) {
				PLACE place = PLACE.fromJson(jArray.get(i));
				placesArray.add(place);
			}
		}
		BmgUtils.printLog(places.toString());
	}

	public static PLACE findPlace(String id) {
		for (PLACE object : placesArray) {
			if (object.id.equals(id)) {
				return object;
			}
		}
		return null;
	}

	public static ArrayList<Object> getEventsByModel(int model) {
		ArrayList<Object> allEvents = new ArrayList<Object>();
		Calendar today = Calendar.getInstance();
		int dayOfYear = today.get(Calendar.DAY_OF_YEAR);

		switch (model) {
		case 0:
			// All
			for (EVENT object : eventsArray) {
				if (object.getDate() != null) {
					allEvents.add(object);
				}
			}
			for (PARTY object : partiesArray) {
				if (object.getDate() != null) {
					allEvents.add(object);
				}
			}
			break;
		case 1:
			// Today
			for (EVENT object : eventsArray) {
				if (object.getDate() != null
						&& object.getDate().get(Calendar.DAY_OF_YEAR) == dayOfYear) {
					allEvents.add(object);
				}
			}
			for (PARTY object : partiesArray) {
				if (object.getDate() != null
						&& object.getDate().get(Calendar.DAY_OF_YEAR) == dayOfYear) {
					allEvents.add(object);
				}
			}
			break;
		case 2:
			// Bars
			for (EVENT object : eventsArray) {
				if (object.getDate() != null
						&& object.getPlace().type == 2) {
					allEvents.add(object);
				}
			}
			break;
		case 3:
			// Clubs
			for (EVENT object : eventsArray) {
				if (object.getDate() != null
						&& object.getPlace().type == 1) {
					allEvents.add(object);
				}
			}
			break;
		case 4:
			// Parties
			for (PARTY object : partiesArray) {
				if (object.getDate() != null && object.getDate().after(today)) {
					allEvents.add(object);
				}
			}
			break;

		default:
			break;
		}

		Collections.sort(allEvents, new Comparator<Object>() {
			@Override
			public int compare(Object obj1, Object obj2) {
				Calendar date1 = (obj1.getClass().getName().equals(EVENT.class
						.getName())) ? ((EVENT) obj1).getDate()
						: ((PARTY) obj1).getDate();
				Calendar date2 = (obj2.getClass().getName().equals(EVENT.class
						.getName())) ? ((EVENT) obj2).getDate()
						: ((PARTY) obj2).getDate();
				return date1.compareTo(date2);
			}
		});
		return allEvents;
	}
	
	public static ArrayList<Object> getPlacesByModel(int model) {
		ArrayList<Object> allPlaces = new ArrayList<Object>();

		switch (model) {
		case 0:
			// All
			for (PLACE object : placesArray) {
				allPlaces.add(object);
			}
			break;
		case 1:
			// Bars
			for (PLACE object : placesArray) {
				if (object.type == 2) {
					allPlaces.add(object);
				}
			}
			break;
		case 2:
			// Clubs
			for (PLACE object : placesArray) {
				if (object.type == 1) {
					allPlaces.add(object);
				}
			}
			break;

		default:
			break;
		}
		
		Collections.sort(allPlaces, new Comparator<Object>() {
			@Override
			public int compare(Object obj1, Object obj2) {
				float distance1 = ((PLACE)obj1).getDistance(userLocation);
				float distance2 = ((PLACE)obj2).getDistance(userLocation);
				return (distance1==distance2)?0:((distance1<distance2)?-1:1);
			}
		});
		
		return allPlaces;
	}
}
