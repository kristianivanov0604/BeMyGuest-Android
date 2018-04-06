package com.be.my.guest.api;

import com.be.my.guest.api.domain.CITY;
import com.be.my.guest.api.domain.EVENT;
import com.be.my.guest.api.domain.ORDERCreate;
import com.be.my.guest.api.domain.PARTY;
import com.be.my.guest.api.domain.PLACE;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface APIService {

	/**
	 * USER APIs
	 */

	/**
	 * Login with FaceBook
	 * 
	 * @param user
	 * @param mCallback
	 */
	@POST("/authorize/facebook")
	public void loginWithFacebook(@Body JsonObject user,
			Callback<JsonObject> mCallback);

	/**
	 * LogIn with email
	 * 
	 * @param email
	 * @param password
	 * @param mCallback
	 */
	@FormUrlEncoded
	@POST("/authorize")
	public void loginWithEmail(@Field("email") String email,
			@Field("password") String password, Callback<JsonObject> mCallback);

	/**
	 * SignUp with email
	 * 
	 * @param user
	 */
	@POST("/users/")
	public JsonObject signupWithEmail(@Body JsonObject user);

	/**
	 * LogIn with access token
	 * 
	 * @param mCallback
	 */
	@GET("/users/{id}")
	public void getUser(@Header("Access-Token") String token,
			@Path("id") String id, Callback<JsonObject> mCallback);

	/**
	 * Edit user profile
	 * 
	 * @param user
	 * @param mCallback
	 */
	@PUT("/users/{id}")
	public void editUser(@Header("Access-Token") String token,
			@Path("id") String id, @Body JsonObject user,
			Callback<JsonObject> mCallback);

	/**
	 * CITY APIs
	 */

	/**
	 * Get all cities
	 */
	@GET("/cities")
	public JsonObject getAllCity(@Header("Access-Token") String token);

	/**
	 * Get City by id
	 */
	@GET("/cities/{id}")
	public void getCity(@Header("Access-Token") String token,
			@Path("id") String id, Callback<CITY> mCallback);

	/**
	 * EVENT APIs
	 */

	/**
	 * Get events with city
	 */
	@GET("/events")
	public JsonObject getEventsWithCity(@Header("Access-Token") String token,
			@Query("city") String cityId);

	/**
	 * Get events with city by page
	 */
	@GET("/events")
	public JsonObject getEventsWithCity(@Header("Access-Token") String token,
			@Query("city") String cityId, @Query("per_page") int perPage,
			@Query("page") int page);

	/**
	 * Get event by id
	 * 
	 * @param mCallback
	 */
	@GET("/events/{id}")
	public void getEvent(@Header("Access-Token") String token,
			@Path("id") String id, Callback<EVENT> mCallback);

	/**
	 * Create order
	 * 
	 * @param token
	 * @param order
	 * @param mCallback
	 */
	@POST("/orders")
	public void createOrder(@Header("Access-Token") String token,
			@Body ORDERCreate order, Callback<JsonObject> mCallback);

	/**
	 * PARTY APIs
	 */

	/**
	 * Get parties with city
	 */
	@GET("/parties")
	public JsonObject getPartiesWithCity(@Header("Access-Token") String token,
			@Query("city") String cityId);

	/**
	 * Get parties with city by page
	 */
	@GET("/parties")
	public JsonObject getPartiesWithCity(@Header("Access-Token") String token,
			@Query("city") String cityId, @Query("per_page") int perPage,
			@Query("page") int page);

	/**
	 * Get party by id
	 * 
	 * @param mCallback
	 */
	@GET("/party/{id}")
	public void getParty(@Header("Access-Token") String token,
			@Path("id") String id, Callback<PARTY> mCallback);

	/**
	 * PLACE APIs
	 */

	/**
	 * Get places with city
	 */
	@GET("/places")
	public JsonObject getPlacesWithCity(@Header("Access-Token") String token,
			@Query("city") String cityId);

	/**
	 * Get place by id
	 * 
	 * @param mCallback
	 */
	@GET("/place/{id}")
	public void getPlace(@Header("Access-Token") String token,
			@Path("id") String id, Callback<PLACE> mCallback);

	/**
	 * Get all tickets by partyId
	 * 
	 * @param mCallback
	 */
	@GET("/tickettypes")
	public void getTicketTypes(@Header("Access-Token") String token,
			@Query("partyId") String partyId, Callback<JsonObject> mCallback);

	/**
	 * Post name on the event's RSVP list
	 * 
	 * @param mCallback
	 */
	@FormUrlEncoded
	@POST("/rsvps")
	public void nameOnList(@Header("Access-Token") String token,
			@Field("eventId") String eventId, Callback<JsonObject> mCallback);

}