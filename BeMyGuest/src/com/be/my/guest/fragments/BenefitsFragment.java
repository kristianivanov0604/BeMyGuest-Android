package com.be.my.guest.fragments;

import java.util.ArrayList;

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
import com.be.my.guest.adapters.MainListAdapter;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.ui.BmgDialog;
import com.be.my.guest.utility.BmgUtils;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class BenefitsFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;

	public static BenefitsFragment newInstance() {
		return new BenefitsFragment();
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
		tvTitle.setText(R.string.My_Benefit_Store);

		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);
		menuBtn.setImageResource(R.drawable.icon_return);

		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);
		mapBtn.setImageResource(R.drawable.icon_checkin);

		// Initialize Benefit ListView
		ListView benefitList = (ListView) vi
				.findViewById(R.id.lv_benefit_list);
		final BenefitListAdapter benefitAdapter = new BenefitListAdapter(getActivity(),
				BenefitListAdapter.ADAPTER_MODE.BENEFIT, null);
		benefitList.setAdapter(benefitAdapter);
		benefitList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Perform select an benefit
				benefitAdapter.setSelected(position);
				benefitAdapter.notifyDataSetChanged();
				showDescriptionDialog(position);
			}
		});

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			// Open Menu
			delegate.onBackPressed();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map

		}
	}
	
	/**
	 * show the description of selected benefit
	 */
	private void showDescriptionDialog(int position) {
		ArrayList<String> descriptions = new ArrayList<String>();
		for (int n = 0; n < 3; n++) {
			descriptions.add(getResources().getString(R.string.sample_event_description));
		}
		
		final BmgDialog dialog = new BmgDialog();
		dialog.setTitle(getResources().getString(R.string.Tamari_2_Por1));
		dialog.setDescriptionList(descriptions);
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
