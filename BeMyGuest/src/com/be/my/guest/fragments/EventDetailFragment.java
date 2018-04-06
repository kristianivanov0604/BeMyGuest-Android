package com.be.my.guest.fragments;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.be.my.guest.R;
import com.be.my.guest.activities.MainActivity;
import com.be.my.guest.activities.base.BaseActivity.CUSTOM_ANIMATIONS;
import com.be.my.guest.adapters.DescriptionListAdapter;
import com.be.my.guest.api.APIClient;
import com.be.my.guest.api.domain.EVENT;
import com.be.my.guest.api.domain.PARTY;
import com.be.my.guest.api.domain.PLACE;
import com.be.my.guest.api.domain.USER;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.ui.BmgDialog;
import com.be.my.guest.ui.DynamicImageView;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.JsonObject;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class EventDetailFragment extends BaseFragment implements
		OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;
	private DETAIL_MODE applyMode = DETAIL_MODE.MODE_EVENT;

	// Data members
	private PARTY mParty;
	private EVENT mEvent;
	private PLACE mPlace;

	// Event Apply Mode
	public static enum DETAIL_MODE {
		MODE_EVENT, MODE_PARTY, MODE_LOCAL, MODE_BENEFIT
	}

	public void setApplyMode(DETAIL_MODE mode, Object object) {
		applyMode = mode;
		switch (mode) {
		case MODE_EVENT:
			mEvent = (EVENT) object;
			mPlace = (PLACE) mEvent.getPlace();
			break;

		case MODE_PARTY:
			mParty = (PARTY) object;
			break;

		case MODE_LOCAL:
			mPlace = (PLACE) object;
			break;

		case MODE_BENEFIT:

			break;

		default:
			break;
		}
	}

	public static EventDetailFragment newInstance() {
		return new EventDetailFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_main_detail, container,
				false);
		BmgUtils.setTAG(this.getClass().getName());
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
		delegate.enableSlideMenu(false);

		// initialize
		initViewAndClassMembers();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;

		// Hiding unused views
		vi.findViewById(R.id.tv_city).setVisibility(View.GONE);

		// Set Custom ActionBar Icons and Handlers
		TextView tvTitle = (TextView) vi.findViewById(R.id.tv_title);

		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);

		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);
		mapBtn.setImageResource(R.drawable.icon_open);

		// Main image
		DynamicImageView backImage = (DynamicImageView) vi
				.findViewById(R.id.iv_event_image);

		// Play button
		ImageView playBtn = (ImageView) vi.findViewById(R.id.iv_play_button);
		playBtn.setOnClickListener(this);

		// Share button
		ImageView shareBtn = (ImageView) vi.findViewById(R.id.iv_share_button);
		shareBtn.setOnClickListener(this);
		shareBtn.setVisibility(View.GONE);

		// Detailed info(Name/Points/Place/Address/Date/Telephone)
		TextView tvEventName = (TextView) vi.findViewById(R.id.tv_event_name);
		TextView tvPoints = (TextView) vi.findViewById(R.id.tv_points);
		TextView tvPlace = (TextView) vi.findViewById(R.id.tv_local);
		TextView tvTime = (TextView) vi.findViewById(R.id.tv_time);
		TextView tvAddress = (TextView) vi.findViewById(R.id.tv_address);
		TextView tvTelephone = (TextView) vi.findViewById(R.id.tv_telephone);
		TextView tvDescription = (TextView) vi
				.findViewById(R.id.tv_detail_description);
		tvPoints.setVisibility(View.GONE);
		tvPlace.setVisibility(View.GONE);
		tvTime.setVisibility(View.GONE);
		tvTelephone.setVisibility(View.GONE);

		// Bottom button
		TextView bottomButton = (TextView) vi.findViewById(R.id.bottom_button);
		bottomButton.setOnClickListener(this);

		// Set content and visibility
		if (applyMode == DETAIL_MODE.MODE_EVENT) {
			// VIP list mode event
			tvTitle.setText(R.string.Event_Info);

			if (mPlace != null) {
				// set image
				backImage.loadImage(mPlace.getImageURL());
				// Place
				tvPlace.setVisibility(View.VISIBLE);
				tvPlace.setText(mPlace.name);
				// Address
				tvAddress.setVisibility(View.VISIBLE);
				tvAddress.setText(mPlace.location.address);
				// Phone
				tvTelephone.setVisibility(View.VISIBLE);
				tvTelephone.setText(mPlace.phone.getNumber());
			}
			// Name
			tvEventName.setText(mEvent.name);
			// Time
			tvTime.setVisibility(View.VISIBLE);
			tvTime.setText(BmgUtils.calendarToString(mEvent.getDate(),
					R.string.FULL_DATE_FORMAT));
			// Description
			tvDescription.setText(Html.fromHtml(mEvent.description));
			// Buttons
			shareBtn.setVisibility(View.VISIBLE);
			bottomButton.setText(R.string.ADD_TO_VIP_LIST);

		} else if (applyMode == DETAIL_MODE.MODE_PARTY) {
			// Ticket mode event
			tvTitle.setText(R.string.Event_Info);
			// set image
			backImage.loadImage(mParty.getImageURL());
			// Name
			tvEventName.setText(mParty.name);
			// Time
			tvTime.setVisibility(View.VISIBLE);
			tvTime.setText(BmgUtils.calendarToString(mParty.getDate(),
					R.string.FULL_DATE_FORMAT));
			// Place
			tvPlace.setVisibility(View.VISIBLE);
			tvPlace.setText(mParty.location.name);
			// Address
			tvAddress.setVisibility(View.VISIBLE);
			tvAddress.setText(mParty.location.address);
			// Phone
			tvTelephone.setVisibility(View.VISIBLE);
			tvTelephone.setText(mParty.phone.getNumber());
			// Description
			tvDescription.setText(Html.fromHtml(mParty.description));
			// Buttons
			shareBtn.setVisibility(View.VISIBLE);
			bottomButton.setText(R.string.BOOK_NOW);
		} else if (applyMode == DETAIL_MODE.MODE_LOCAL) {
			// Place of Local
			tvTitle.setText(R.string.Local);
			// set image
			backImage.loadImage(mPlace.getImageURL());
			// Name
			tvEventName.setText(mPlace.name);
			// Time
			if (mPlace.getDate() != null) {
				tvTime.setVisibility(View.VISIBLE);
				tvTime.setText(BmgUtils.calendarToString(mPlace.getDate(),
						R.string.FULL_DATE_FORMAT));
			}
			// Address
			tvAddress.setVisibility(View.VISIBLE);
			tvAddress.setText(mPlace.location.address);
			// Telephone
			tvTelephone.setVisibility(View.VISIBLE);
			tvTelephone.setText(mPlace.phone.getNumber());
			// Description
			tvDescription.setText(Html.fromHtml(mPlace.description));
			// Buttons
			bottomButton.setText(R.string.CHECK_IN_HERE);
		} else if (applyMode == DETAIL_MODE.MODE_BENEFIT) {
			// Benefit info
			tvTitle.setText(R.string.Product_Info);
			tvEventName.setText(R.string.Tamari_2_Por1);
			tvPoints.setVisibility(View.VISIBLE);
			tvPoints.setText(R.string.Points_32);
			tvPlace.setVisibility(View.VISIBLE);
			tvPlace.setText(R.string.Markis_Place_Barao);
			tvAddress.setText(R.string.sample_event_address);
			tvTelephone.setVisibility(View.VISIBLE);
			tvTelephone.setText(R.string.sample_telephone);

			// Benefit description list view
			ArrayList<String> descriptions = new ArrayList<String>();
			for (int n = 0; n < 3; n++) {
				descriptions.add(getResources().getString(
						R.string.sample_event_description));
			}
			ListView lvDescription = (ListView) vi
					.findViewById(R.id.lv_description_list);
			lvDescription.setVisibility(View.VISIBLE);
			lvDescription.setAdapter(new DescriptionListAdapter(getActivity(),
					DescriptionListAdapter.ADAPTER_MODE.NONDLG, descriptions));

			bottomButton.setText(R.string.EXCHANGE);
		}

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			delegate.onBackPressed();
		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map
			BmgUtils.showMessage("Right button clicked.");

		} else if (v.getId() == R.id.iv_play_button) {
			// Perform play
			BmgUtils.showMessage("Play button clicked.");

		} else if (v.getId() == R.id.iv_share_button) {
			// Perform share
			BmgUtils.showMessage("Share button clicked.");

		} else if (v.getId() == R.id.bottom_button) {
			if (applyMode == DETAIL_MODE.MODE_EVENT) {
				// Perform add to vip list
				if (possibleNameOnList())
					showConfirmListDialog();

			} else if (applyMode == DETAIL_MODE.MODE_PARTY) {
				// Perform booking
				baseActivity.showFragment(R.id.fl_fragment_container,
						TicketFragment.newInstance(), true,
						CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
			} else if (applyMode == DETAIL_MODE.MODE_LOCAL) {
				// Perform check in
				showConfirmCheckinDialog();
			} else if (applyMode == DETAIL_MODE.MODE_BENEFIT) {
				// Perform call
				showConfirmExchangeDialog();
			}
		}

		delegate.hideKeyboard();
	}

	/**
	 * Check can name on RSVP list
	 * 
	 * @return
	 */
	private boolean possibleNameOnList() {
		BmgUtils.printLog("Checking listing possibility");
		if (mEvent.disabled || !mEvent.canRsvp) {
			BmgUtils.showMessage("Unavailable list");
			return false;
		}
		if (mEvent.isListedUser(LocalStorage.userId)) {
			BmgUtils.showMessage("Sent name");
			return false;
		}
		if (mEvent.isClosedList()) {
			BmgUtils.showMessage("Closed list");
			return false;
		}
		return true;
	}

	/*************************************
	 * API callback **********************
	 ************************************/

	/**
	 * Name on VIP list Callback
	 */
	private Callback<JsonObject> nameCallback = new Callback<JsonObject>() {

		@Override
		public void failure(RetrofitError error) {
			// Adding name on VIP list failure
			BmgUtils.hideProgDlg();
			BmgUtils.showMessage("Add to VIP list failure.");
		}

		@Override
		public void success(JsonObject eventJson,
				retrofit.client.Response response) {
			// Adding name on VIP list success
			BmgUtils.hideProgDlg();
			BmgUtils.printLog(eventJson.toString());
			showVIPSuccessDialog();

		}
	};

	/*************************************
	 * Showing custom dialog *************
	 ************************************/

	/**
	 * show common message dialog
	 * 
	 * @param message
	 */
	private void showMessageDialog(String message) {
		final BmgDialog dialog = new BmgDialog();
		dialog.setMessage(message);
		dialog.setPositiveButton(getResources().getString(R.string.OK),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.show(getFragmentManager(), null);
		BmgUtils.printLog(message);
	}

	/**
	 * Confirm adding name to VIP list
	 */
	private void showConfirmListDialog() {
		final BmgDialog dialog = new BmgDialog();
		dialog.setIcon(R.drawable.icon_help);
		dialog.setMessage("Your name should be added to the VIP list.");
		dialog.setPositiveButton(getResources().getString(R.string.OK),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						// perform post name on list with event id
						BmgUtils.showProgDlg();
						APIClient
								.getInstance(delegate)
								.getApiService()
								.nameOnList(LocalStorage.accessToken,
										mEvent.id, nameCallback);
					}
				});
		dialog.setNegativeButton(getResources().getString(R.string.CANCEL),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.show(getFragmentManager(), null);
	}

	/**
	 * VIP listing success
	 */
	private void showVIPSuccessDialog() {
		final BmgDialog dialog = new BmgDialog();
		dialog.setIcon(R.drawable.icon_bold_check);
		dialog.setMessage(getResources().getString(R.string.VIP_Success));
		dialog.setPositiveButton(getResources().getString(R.string.OK),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.show(getFragmentManager(), null);
	}

	/**
	 * Confirm to check-in
	 */
	private void showConfirmCheckinDialog() {
		final BmgDialog dialog = new BmgDialog();
		dialog.setIcon(R.drawable.icon_checkin);
		dialog.setMessage(getResources().getString(
				R.string.sample_event_description));
		dialog.setPositiveButton(getResources().getString(R.string.CHECK_IN),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.setNegativeButton(getResources().getString(R.string.CANCEL),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.show(getFragmentManager(), null);
	}

	/**
	 * Confirm call
	 */
	private void showConfirmCallDialog() {
		final BmgDialog dialog = new BmgDialog();
		dialog.setIcon(R.drawable.icon_call);
		dialog.setMessage(getResources().getString(
				R.string.sample_event_description));
		dialog.setPositiveButton(getResources().getString(R.string.CALL),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.setNegativeButton(getResources().getString(R.string.CANCEL),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.show(getFragmentManager(), null);
	}

	/**
	 * Confirm exchange
	 */
	private void showConfirmExchangeDialog() {
		final BmgDialog dialog = new BmgDialog();
		dialog.setIcon(R.drawable.icon_help);
		dialog.setTitle(getResources().getString(R.string.Confirmation));
		dialog.setMessage(getResources().getString(
				R.string.sample_exchange_message));
		dialog.setPositiveButton(getResources().getString(R.string.EXCHANGE),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						showExchangeSuccessDialog();
					}
				});
		dialog.setNegativeButton(getResources().getString(R.string.CANCEL),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.show(getFragmentManager(), null);
	}

	/**
	 * Exchanging success
	 */
	private void showExchangeSuccessDialog() {
		final BmgDialog dialog = new BmgDialog();
		dialog.setIcon(R.drawable.icon_bold_check);
		dialog.setMessage(getResources()
				.getString(R.string.Exchange_successful));
		dialog.setPositiveButton(getResources().getString(R.string.OK),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});
		dialog.show(getFragmentManager(), null);
	}

}
