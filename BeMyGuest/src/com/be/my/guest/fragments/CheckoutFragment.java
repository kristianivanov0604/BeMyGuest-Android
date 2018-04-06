package com.be.my.guest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.be.my.guest.R;
import com.be.my.guest.activities.MainActivity;
import com.be.my.guest.activities.base.BaseActivity.CUSTOM_ANIMATIONS;
import com.be.my.guest.adapters.MainListAdapter;
import com.be.my.guest.adapters.TicketListAdapter;
import com.be.my.guest.fragments.EventDetailFragment.DETAIL_MODE;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.ui.BmgDialog;
import com.be.my.guest.ui.ProgDialog;
import com.be.my.guest.utility.BmgUtils;
import com.google.android.gms.internal.bn;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class CheckoutFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;
	private CHECKOUT_PROCESS checkoutProcess = CHECKOUT_PROCESS.PROCESS_1_IDENTIFY;

	// Event Apply Mode
	public static enum CHECKOUT_PROCESS {
		PROCESS_1_IDENTIFY, PROCESS_2_BILLING, PROCESS_3_CARD, PROCESS_4_TOTAL
	}

	public void setCheckoutProcess(CHECKOUT_PROCESS process) {
		checkoutProcess = process;
	}

	public static CheckoutFragment newInstance() {
		return new CheckoutFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_1_IDENTIFY) {
			this.view = inflater.inflate(R.layout.fragment_checkout_1,
					container, false);
		} else if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_2_BILLING) {
			this.view = inflater.inflate(R.layout.fragment_checkout_2,
					container, false);
		} else if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_3_CARD) {
			this.view = inflater.inflate(R.layout.fragment_checkout_3,
					container, false);
		} else if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_4_TOTAL) {
			this.view = inflater.inflate(R.layout.fragment_checkout_4,
					container, false);
		}
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
		tvTitle.setText(R.string.Checkout);

		// Top left button
		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);

		// Top right button
		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);

		// Bottom button
		TextView nextButton = (TextView) vi.findViewById(R.id.bottom_button);
		nextButton.setOnClickListener(this);

		// Ticket checkout number
		TextView processId = (TextView) vi.findViewById(R.id.tv_process_number);
		TextView processTitle = (TextView) vi
				.findViewById(R.id.tv_process_title);

		if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_1_IDENTIFY) {
			/**
			 * Ticket Identification
			 */
			processId.setText(R.string.number_1);
			processTitle.setText(R.string.TICKET_IDENTIFICATION);
			nextButton.setText(R.string.NEXT_PROCESS);

			// Ticket Identification List
			ListView lvTicketList = (ListView) vi
					.findViewById(R.id.lv_ticket_list);
			lvTicketList.setAdapter(new TicketListAdapter(getActivity(), null));

		} else if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_2_BILLING) {
			/**
			 * Billing Information
			 */
			processId.setText(R.string.number_2);
			processTitle.setText(R.string.BILLING_INFORMATION);
			nextButton.setText(R.string.NEXT_PROCESS);

			// Name
			EditText etName = (EditText) vi.findViewById(R.id.et_name)
					.findViewById(R.id.et_text);
			etName.setHint(R.string.Name);
			etName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

			// CPF
			EditText etCPF = (EditText) vi.findViewById(R.id.et_cpf)
					.findViewById(R.id.et_text);
			etCPF.setHint(R.string.CPF);
			etCPF.setInputType(InputType.TYPE_CLASS_NUMBER);

			// Telephone
			EditText etTelephone = (EditText) vi
					.findViewById(R.id.et_telephone).findViewById(R.id.et_text);
			etTelephone.setHint(R.string.Telephone);
			etTelephone.setInputType(InputType.TYPE_CLASS_PHONE);

			// Address
			EditText etAddress = (EditText) vi.findViewById(R.id.et_address)
					.findViewById(R.id.et_text);
			etAddress.setHint(R.string.Address);
			etAddress.setInputType(InputType.TYPE_CLASS_TEXT);

			// State
			EditText etState = (EditText) vi.findViewById(R.id.et_state)
					.findViewById(R.id.et_text);
			etState.setHint(R.string.State);
			etState.setInputType(InputType.TYPE_CLASS_TEXT);

			// City
			EditText etCity = (EditText) vi.findViewById(R.id.et_city)
					.findViewById(R.id.et_text);
			etCity.setHint(R.string.City);
			etCity.setInputType(InputType.TYPE_CLASS_TEXT);

			// Country
			EditText etCountry = (EditText) vi.findViewById(R.id.et_country)
					.findViewById(R.id.et_text);
			etCountry.setHint(R.string.Country);
			etCountry.setInputType(InputType.TYPE_CLASS_TEXT);

		} else if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_3_CARD) {
			/**
			 * Credit Card Information
			 */
			mapBtn.setImageResource(R.drawable.icon_camera);
			processId.setText(R.string.number_3);
			processTitle.setText(R.string.CREDIT_CARD_INFORMATION);
			nextButton.setText(R.string.NEXT_PROCESS);

			// Credit Card Number
			EditText etCard = (EditText) vi.findViewById(R.id.et_card_number)
					.findViewById(R.id.et_text);
			etCard.setHint(R.string.Card_Number);
			etCard.setInputType(InputType.TYPE_CLASS_NUMBER);

			// Expires Date
			EditText etExpire = (EditText) vi.findViewById(R.id.et_expire_date)
					.findViewById(R.id.et_text);
			etExpire.setHint(R.string.Expires_On);
			etExpire.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);

			// CVV Code
			EditText etCVV = (EditText) vi.findViewById(R.id.et_cvv_code)
					.findViewById(R.id.et_text);
			etCVV.setHint(R.string.CVV_Code);
			etCVV.setInputType(InputType.TYPE_CLASS_NUMBER);

		} else if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_4_TOTAL) {
			/**
			 * Total Overview
			 */
			processId.setText(R.string.number_4);
			processTitle.setText(R.string.YOUR_TOTAL_OVERVIEW);
			nextButton.setText(R.string.COMPLETE);

			// Male
			TextView maleItem = (TextView) vi.findViewById(R.id.item_male)
					.findViewById(R.id.tv_item);
			maleItem.setText(R.string.Male_3_Lot);
			TextView maleContent = (TextView) vi.findViewById(R.id.item_male)
					.findViewById(R.id.tv_content);
			maleContent.setText(R.string.Qty_3);

			// Female
			TextView femaleItem = (TextView) vi.findViewById(R.id.item_female)
					.findViewById(R.id.tv_item);
			femaleItem.setText(R.string.Female_3_Lot);
			TextView femaleContent = (TextView) vi.findViewById(
					R.id.item_female).findViewById(R.id.tv_content);
			femaleContent.setText(R.string.Qty_3);

			// Price
			TextView priceItem = (TextView) vi.findViewById(R.id.item_price)
					.findViewById(R.id.tv_item);
			priceItem.setText(R.string.Price);
			TextView priceContent = (TextView) vi.findViewById(R.id.item_price)
					.findViewById(R.id.tv_content);
			priceContent.setText(R.string.Price_25);

			// Subtotal
			TextView subtotalItem = (TextView) vi.findViewById(
					R.id.item_subtotal).findViewById(R.id.tv_item);
			subtotalItem.setText(R.string.Sub_Total);
			TextView subtotalContent = (TextView) vi.findViewById(
					R.id.item_subtotal).findViewById(R.id.tv_content);
			subtotalContent
					.setTextColor(getResources().getColor(R.color.black));
			subtotalContent.setText(R.string.Price_75);

			// Tax
			TextView taxItem = (TextView) vi.findViewById(R.id.item_tax)
					.findViewById(R.id.tv_item);
			taxItem.setText(R.string.Tax);
			TextView taxContent = (TextView) vi.findViewById(R.id.item_tax)
					.findViewById(R.id.tv_content);
			taxContent.setTextColor(getResources().getColor(R.color.black));
			taxContent.setText(R.string.Price_10);

			// Your Total
			TextView totalItem = (TextView) vi.findViewById(R.id.item_total)
					.findViewById(R.id.tv_item);
			totalItem.setText(R.string.Your_Total);
			TextView totalContent = (TextView) vi.findViewById(R.id.item_total)
					.findViewById(R.id.tv_content);
			totalContent.setText(R.string.Price_85);
		}

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			// Return to event list fragment
			delegate.onBackPressed();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map
			BmgUtils.showMessage("Right button clicked.");

		} else if (v.getId() == R.id.bottom_button) {

			if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_4_TOTAL) {
				// Perform Complete
				showCongratulationDialog();
			} else {
				// Perform Next Process
				CheckoutFragment fragment = CheckoutFragment.newInstance();
				if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_1_IDENTIFY) {
					fragment.setCheckoutProcess(CheckoutFragment.CHECKOUT_PROCESS.PROCESS_2_BILLING);
				} else if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_2_BILLING) {
					fragment.setCheckoutProcess(CheckoutFragment.CHECKOUT_PROCESS.PROCESS_3_CARD);
				} else if (checkoutProcess == CHECKOUT_PROCESS.PROCESS_3_CARD) {
					fragment.setCheckoutProcess(CheckoutFragment.CHECKOUT_PROCESS.PROCESS_4_TOTAL);
				}
				baseActivity.showFragment(R.id.fl_fragment_container, fragment,
						true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
			}
		}
		delegate.hideKeyboard();
	}

	private void showCongratulationDialog() {
		final BmgDialog dialog = new BmgDialog();
		dialog.setIcon(R.drawable.icon_congratulation);
		dialog.setTitle(getResources().getString(R.string.Congratulation));
		dialog.setMessage(getResources().getString(R.string.Great_having));
		dialog.setDescription(getResources().getString(
				R.string.sample_event_description));
		dialog.setPositiveButton(getResources().getString(R.string.OK),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						// Navigate to Congratulations
						delegate.selectMenu(MainActivity.MENUITEM_TICKETS);

					}
				});
		dialog.show(getFragmentManager(), null);
	}
}
