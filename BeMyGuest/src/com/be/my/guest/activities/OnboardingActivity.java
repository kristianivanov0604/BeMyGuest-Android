package com.be.my.guest.activities;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.be.my.guest.R;
import com.be.my.guest.activities.base.BaseActivity;
import com.be.my.guest.api.APIClient;
import com.be.my.guest.api.domain.USER;
import com.be.my.guest.fragments.StartupFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.utility.BmgUtils;
import com.be.my.guest.utility.GPSTracker;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.gson.JsonObject;

/**
 * Custom Activity class which performs login/register
 * 
 */
public class OnboardingActivity extends BaseActivity {

	private UiLifecycleHelper uiHelper = null;
	private boolean waitFBSession = false;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_onboarding);

		showFragment(R.id.fl_fragment_container, StartupFragment.newInstance(),
				CUSTOM_ANIMATIONS.FADE_IN);

		if (LocalStorage.loadSavedAccessToken() != null) {
			// User is already logged in
			BmgUtils.showProgDlg();
			APIClient
					.getInstance(this)
					.getApiService()
					.getUser(LocalStorage.accessToken, LocalStorage.userId,
							getUserCallback);

		} else {
			// User is signed out or not registered yet
			uiHelper = new UiLifecycleHelper(this, fbSessionCallback);
			uiHelper.onCreate(bundle);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (uiHelper != null)
			uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (uiHelper != null)
			uiHelper.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (uiHelper != null)
			uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (uiHelper != null)
			uiHelper.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (uiHelper != null)
			uiHelper.onDestroy();
	}

	/**
	 * Open Session With FaceBook
	 */
	public void loginWithFacebook() {
		// Showing progress indicator
		BmgUtils.showProgDlg();
		BmgUtils.printLog("Login with Facebook.");

		// Start waiting FaceBook session
		waitFBSession = true;
		Session session = Session.getActiveSession();

		if (!session.isOpened() && !session.isClosed()) {

			BmgUtils.printLog("Open FBSession for read.");
			session.openForRead(new Session.OpenRequest(this).setPermissions(
					Arrays.asList("public_profile", "email", "user_birthday",
							"user_about_me")).setCallback(fbSessionCallback));
		} else {
			BmgUtils.printLog("Open active FBSession.");
			Session.openActiveSession(this, true, fbSessionCallback);
		}
	}

	/**
	 * Perform Authorize with FaceBook User
	 * 
	 * @param user
	 */
	private void authWithFaceBook(GraphUser user) {
		String email = (String) user.getProperty("email");
		String name = user.getName();
		String gender = (String) user.getProperty("gender");
		String birthday = (String) user.getBirthday();
		String facebookId = user.getId();

		JsonObject fbUser = new JsonObject();
		fbUser.addProperty("email", email);

		JsonObject profile = new JsonObject();
		profile.addProperty("name", name);
		profile.addProperty("gender", gender);
		profile.addProperty("birthday", birthday);
		fbUser.add("profile", profile);

		JsonObject services = new JsonObject();
		JsonObject facebook = new JsonObject();
		facebook.addProperty("id", facebookId);
		services.add("facebook", facebook);
		fbUser.add("services", services);

		BmgUtils.printLog("Authorize with Facebook account info.");

		APIClient.getInstance(this).getApiService()
				.loginWithFacebook(fbUser, fbAuthCallback);

	}

	/**
	 * Load Application Data
	 */
	public void loadAppData() {

		// Load application data in background after login success
		// while the progress indicator is showing
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					// Initialize user location 
					LocalStorage.setUserLocation(-22.60640442, -46.91265106);
					
					// Get All Cities
					JsonObject citiesJson = APIClient
							.getInstance(OnboardingActivity.this)
							.getApiService()
							.getAllCity(LocalStorage.accessToken);
					if (citiesJson != null) {
						LocalStorage.setCities(citiesJson);
					} else {
						BmgUtils.printLog("Get All Cities Failure.");
						return null;
					}

					// Get All Events with first City
					JsonObject eventsJson = APIClient
							.getInstance(OnboardingActivity.this)
							.getApiService()
							.getEventsWithCity(
									LocalStorage.accessToken,
									LocalStorage.citiesArray
											.get(LocalStorage.nCurrentCity).id);
					if (eventsJson != null) {
						LocalStorage.setEvents(eventsJson);
					} else {
						BmgUtils.printLog("Get All Events Failure.");
						return null;
					}

					// Get All Parties with first City
					JsonObject partiesJson = APIClient
							.getInstance(OnboardingActivity.this)
							.getApiService()
							.getPartiesWithCity(
									LocalStorage.accessToken,
									LocalStorage.citiesArray
											.get(LocalStorage.nCurrentCity).id);
					if (partiesJson != null) {
						LocalStorage.setParties(partiesJson);
					} else {
						BmgUtils.printLog("Get All Parties Failure.");
						return null;
					}

					// Get All Places with first City
					JsonObject placesJson = APIClient
							.getInstance(OnboardingActivity.this)
							.getApiService()
							.getPlacesWithCity(
									LocalStorage.accessToken,
									LocalStorage.citiesArray
											.get(LocalStorage.nCurrentCity).id);
					if (placesJson != null) {
						LocalStorage.setPlaces(placesJson);
					} else {
						BmgUtils.printLog("Get All Places Failure.");
						return null;
					}

				} catch (RetrofitError e) {
					// Load Data Failure
					if (e.getResponse().getReason() != null)
						BmgUtils.printLog(e.getResponse().getReason()
								.toString());
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				BmgUtils.hideProgDlg();
				if (LocalStorage.citiesArray != null) {
					navigateToMain();
				} else {
					BmgUtils.showMessage("App data loading failure.");
				}
			};
		}.execute();

	}

	/**
	 * Navigate to main activity to start application
	 */
	public void navigateToMain() {
		Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/****************************
	 * CALLBACK functions
	 ***************************/

	/**
	 * FaceBook Session Callback
	 */
	private Session.StatusCallback fbSessionCallback = new Session.StatusCallback() {

		// Callback which is to catch session state change
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (waitFBSession) {
				if (session.isOpened()) {
					BmgUtils.printLog("Send request getting FB Graph user info.");
					Request.newMeRequest(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									BmgUtils.printLog("Getting profile request completed.");
									if (user != null) {
										authWithFaceBook(user);
									} else {
										BmgUtils.hideProgDlg();
										BmgUtils.showMessage("Failed to get FB graph info.");
									}
									waitFBSession = false;
								}
							}).executeAsync();

				} else {
					BmgUtils.printLog(session.toString());
					BmgUtils.hideProgDlg();
					BmgUtils.showMessage("Failed to open FB session.");
					waitFBSession = false;
				}
			}
		}
	};

	/**
	 * Login with FaceBook Callback {@link USER} Authorized User model
	 */
	private Callback<JsonObject> fbAuthCallback = new Callback<JsonObject>() {

		@Override
		public void failure(RetrofitError error) {
			// Authorization with FaceBook failure
			BmgUtils.hideProgDlg();
			BmgUtils.showMessage(R.string.Facebook_Login_Failure);

		}

		@Override
		public void success(JsonObject userJson,
				retrofit.client.Response response) {
			// Authorization with FaceBook success
			BmgUtils.printLog(R.string.Facebook_Login_Success);
			BmgUtils.printLog(userJson.toString());

			// Save authorized user info and access token
			USER user = USER.fromJson(userJson.get("user"));
			LocalStorage.saveAccessToken(user.accessToken.token, user.id, null);
			LocalStorage.authUser = user;

			// Load Application Data
			loadAppData();
		}
	};

	/**
	 * Get user with id Callback {@link USER} Authorized User model
	 */
	private Callback<JsonObject> getUserCallback = new Callback<JsonObject>() {

		@Override
		public void failure(RetrofitError error) {
			// Authorization with FaceBook failure
			BmgUtils.hideProgDlg();
			BmgUtils.showMessage(R.string.Login_Failure);
		}

		@Override
		public void success(JsonObject userJson,
				retrofit.client.Response response) {
			// Authorization with FaceBook success
			BmgUtils.printLog(R.string.Login_Success);
			BmgUtils.printLog(userJson.toString());

			// Save authorized user info and access token
			USER user = USER.fromJson(userJson.get("user"));
			user.password = LocalStorage.userPass;
			LocalStorage.authUser = user;

			// Load Application Data
			loadAppData();
		}
	};

}
