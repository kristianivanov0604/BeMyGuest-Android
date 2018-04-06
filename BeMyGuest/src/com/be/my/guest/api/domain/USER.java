package com.be.my.guest.api.domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * User model
 * 
 * @author Abraham Sandbak
 * 
 */
public class USER {

	/**
	 * User id
	 */
	@SerializedName("_id")
	public String id;

	/**
	 * Email Address
	 */
	@SerializedName("email")
	public String email;

	/**
	 * Password
	 */
	@SerializedName("password")
	public String password;

	/**
	 * Score amount
	 */
	@SerializedName("score")
	public Number score;

	/**
	 * Access level
	 */
	@SerializedName("level")
	public Number level;

	/**
	 * Profile information
	 */
	@SerializedName("profile")
	public PROFILE profile;

	/**
	 * Connected 3rd party services
	 */
	@SerializedName("services")
	public SERVICE services;

	/**
	 * Be My Guest internal access data
	 */
	@SerializedName("accessToken")
	public ACCESSTOKEN accessToken;

	/**
	 * Account creation date
	 */
	@SerializedName("created_at")
	public String createdAt;

	/**
	 * Last access
	 */
	@SerializedName("last_access")
	public String lastAccess;
	
	/**
	 * Privacy
	 */
	@SerializedName("privacy")
	public PRIVACY privacy;
	
	/**
	 * Followers
	 */
	@SerializedName("followers")
	public String[] followers;
	
	/**
	 * Following
	 */
	@SerializedName("following")
	public String[] following;
	
	public boolean isFBUser () {
		if (this.services != null
				&& this.services.facebook != null
				&& this.services.facebook.id != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public static USER fromJson(JsonElement json) {
		Gson gson = new Gson();
		USER user = null;
		try {
			user = gson.fromJson(json, USER.class);
		} catch (Exception e) {
			user = null;
		}
		return user;
	}
	
	/**
	 * Classes 
	 */
	public class PROFILE {

		/**
		 * Full name
		 */
		@SerializedName("name")
		public String name;

		/**
		 * Profile picture
		 */
		@SerializedName("picture")
		public String pictureURL;

		/**
		 * Current city ObjectId
		 */
		@SerializedName("city")
		public String cityId;

		/**
		 * Birthday (Date on ISO Format)
		 */
		@SerializedName("birthday")
		public String birthday;

		/**
		 * Gender
		 */
		@SerializedName("gender")
		public String gender;

		/**
		 * Phone
		 */
		@SerializedName("phone")
		public String phone;

	}

	public class SERVICE {

		/**
		 * Connected FaceBook Account
		 */
	    @SerializedName("facebook")
	    public FACEBOOK facebook;

	    /**
	     * Opt-in for follow ability
	     */
	    @SerializedName("canBeFollowed")
	    public String canBeFollowed;
	    
	    public class FACEBOOK {

	    	/**
	    	 * FaceBook Id
	    	 */
	        @SerializedName("id")
	        public String id;
	    }

	}

	public class ACCESSTOKEN {

		/**
		 * Unique Access Token
		 */
	    @SerializedName("token")
	    public String token;

	    /**
	     * Expiry date for Token
	     */
	    @SerializedName("expiry")
	    public String expiryDate;

	}
	
    public class PRIVACY {

    	/**
    	 * Can be followed
    	 */
        @SerializedName("canBeFollowed")
        public String canBeFallowed;
    }


}
