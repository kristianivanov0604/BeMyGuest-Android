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
import com.be.my.guest.adapters.BenefitListAdapter;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.utility.BmgUtils;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class CongratulationsFragment extends BaseFragment implements
		OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;

	public static CongratulationsFragment newInstance() {
		return new CongratulationsFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_benefit_list, container,
				false);
		BmgUtils.setTAG(this.getClass().getName());
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
		delegate.enableSlideMenu(true);

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
		tvTitle.setText(R.string.Congratulations);

		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);
		menuBtn.setImageResource(R.drawable.icon_menu);

		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);
		mapBtn.setImageResource(R.drawable.icon_checkin);

		// Initialize Benefit ListView
		ListView benefitList = (ListView) vi.findViewById(R.id.lv_benefit_list);
		final BenefitListAdapter benefitAdapter = new BenefitListAdapter(
				getActivity(), BenefitListAdapter.ADAPTER_MODE.CONGRATULATION,
				null);
		benefitList.setAdapter(benefitAdapter);
		benefitList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// Perform select an benefit
				benefitAdapter.setSelected(position);
				benefitAdapter.notifyDataSetChanged();
				baseActivity.showFragment(R.id.fl_fragment_container,
						QRCodeFragment.newInstance(), true, false,
						CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);

			}
		});

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			
			// Open Menu
			delegate.toggleSlideMenu();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map
			
		}
	}
}
