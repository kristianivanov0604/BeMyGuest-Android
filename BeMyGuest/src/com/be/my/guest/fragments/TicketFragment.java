package com.be.my.guest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.be.my.guest.R;
import com.be.my.guest.activities.MainActivity;
import com.be.my.guest.activities.base.BaseActivity.CUSTOM_ANIMATIONS;
import com.be.my.guest.adapters.MainListAdapter;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.utility.BmgUtils;
import com.google.android.gms.internal.bn;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class TicketFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;

	public static TicketFragment newInstance() {
		return new TicketFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_ticket, container,
				false);
		BmgUtils.setTAG(this.getClass().getName());
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();

		// Disable sliding menu
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
		tvTitle.setText(R.string.Event_Ticket);

		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);

		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);

		// Initialize profile views
		vi.findViewById(R.id.tv_minus_button).setOnClickListener(this);
		vi.findViewById(R.id.tv_plus_button).setOnClickListener(this);
		TextView confirmButton = (TextView) vi.findViewById(R.id.bottom_button);
		confirmButton.setOnClickListener(this);
		confirmButton.setText(R.string.CONFIRM_BOOKING);
		

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			// Return to event list fragment
			delegate.onBackPressed();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map
			BmgUtils.showMessage("Right button clicked.");
			
		} else if (v.getId() == R.id.tv_minus_button) {
			// Perform Minus Count
			BmgUtils.showMessage("Minus button clicked.");

		} else if (v.getId() == R.id.tv_plus_button) {
			// Perform Plus Count
			BmgUtils.showMessage("Plus button clicked.");

		} else if (v.getId() == R.id.bottom_button) {
			// Confirm booking
			CheckoutFragment fragment = CheckoutFragment.newInstance();
			fragment.setCheckoutProcess(CheckoutFragment.CHECKOUT_PROCESS.PROCESS_1_IDENTIFY);
			baseActivity.showFragment(R.id.fl_fragment_container,
					fragment, true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		}
		
		delegate.hideKeyboard();
	}
}
