package com.be.my.guest.api.domain;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Create order parameter
 * 
 * @author Abraham Sandbak
 * 
 */
public class ORDERCreate {

	// Order object
	@SerializedName("order")
	public ORDER order;

	/**
	 * Order Class
	 */
	public class ORDER {
		// Payer object
		@SerializedName("payer")
		public PAYER payer;

		// Payment Data
		@SerializedName("payment_data")
		public PAYMENT paymentData;

		// True if payment charge should be done in test (sandbox) mode
		@SerializedName("test")
		public boolean test;

		// Total of tickets in order
		@SerializedName("ticketCount")
		public Number ticketCount;

		// Order total (fee included) (in BRL)
		@SerializedName("total")
		public Number total;

		// BMG Service Fee (%)
		@SerializedName("fee")
		public Number fee;

		// Tickets array
		@SerializedName("tickets")
		public ArrayList<TICKET> tickets;

		/**
		 * Payer Class
		 */
		public class PAYER {
			// Address object
			@SerializedName("address")
			public ADDRESS address;

			// Name
			@SerializedName("name")
			public String name;

			// Phone Number
			@SerializedName("phone")
			public String phone;

			// Phone Prefix (Brazilian DDD)
			@SerializedName("phone_prefix")
			public String phonePrefix;

			// CPF/CNPJ (Brazilian Register Number)
			@SerializedName("cpf_cnpj")
			public String cpfCnpj;

			/**
			 * Address Class
			 */
			public class ADDRESS {
				// Street
				@SerializedName("street")
				public String street;

				// Country
				@SerializedName("country")
				public String country;

				// City
				@SerializedName("city")
				public String city;

				// State
				@SerializedName("state")
				public String state;

			}
		}

		/**
		 * Payment Data Class
		 */
		public class PAYMENT {
			// Credit Card Number
			@SerializedName("number")
			public String number;

			// Credit Card Expiry Month
			@SerializedName("month")
			public Number month;

			// Credit Card Expiry Year
			@SerializedName("year")
			public Number year;

			// Credit Card First Name
			@SerializedName("first_name")
			public String firstName;

			// Credit Card Last Name
			@SerializedName("last_name")
			public String lastName;

		}

		/**
		 * Ticket Class
		 */
		public class TICKET {
			// TicketType ObjectId
			@SerializedName("_id")
			public String _id;

			// Quantity
			@SerializedName("quantity")
			public Number quantity;

			// Tickets Guest Information
			@SerializedName("guestInfos")
			public ArrayList<GUEST> guestInfos;

			/**
			 * Guest Info Class
			 */
			public class GUEST {
				// Guest Name
				@SerializedName("name")
				public String name;

				// Guest ID Number
				@SerializedName("rg")
				public String rg;

			}
		}

	}

}
