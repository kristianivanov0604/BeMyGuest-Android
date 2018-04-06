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
public class QRCodeFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;

	public static QRCodeFragment newInstance() {
		return new QRCodeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_qr_code, container,
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
		tvTitle.setText(R.string.Ticket_QR_Code);

		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);

		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);

		// Owner Name
		TextView tvOwner = (TextView)vi.findViewById(R.id.owner_name).findViewById(R.id.tv_item);
		tvOwner.setText(R.string.Owner_Name);
		TextView tvOwnerName = (TextView)vi.findViewById(R.id.owner_name).findViewById(R.id.tv_content);
		tvOwnerName.setText(R.string.sample_owner_name);
		
		// ID Number
		TextView tvId = (TextView)vi.findViewById(R.id.id_number).findViewById(R.id.tv_item);
		tvId.setText(R.string.ID_Number);
		TextView tvIdNumber = (TextView)vi.findViewById(R.id.id_number).findViewById(R.id.tv_content);
		tvIdNumber.setText(R.string.sample_id_number);
		

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			// Return to event list fragment
			delegate.onBackPressed();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map
			BmgUtils.showMessage("Right button clicked.");
			
		}
		
		delegate.hideKeyboard();
	}
}
