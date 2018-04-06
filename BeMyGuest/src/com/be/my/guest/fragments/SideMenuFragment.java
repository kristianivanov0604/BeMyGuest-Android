package com.be.my.guest.fragments;

import java.util.ArrayList;
import java.util.Collections;

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
import com.be.my.guest.adapters.SideMenuAdapter;
import com.be.my.guest.api.domain.USER;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.ui.DynamicImageView;
import com.be.my.guest.utility.BmgUtils;

/**
 * Custom fragment class for sliding side menu
 * 
 * @author Abraham Sandbak
 * 
 */
public class SideMenuFragment extends BaseFragment implements OnClickListener {

	private View view;
	private MainActivity delegate;
	private ListView listSideMenu;
	private DynamicImageView ivAvatar;
	private TextView tvName;
	private TextView tvPoints;
	
	private SideMenuAdapter adaperSideMenu;

	public interface SideMenuItemClickListener {
		public void onMenuItemSelected(int position);
	}

	private SideMenuItemClickListener mCallback;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_side_menu, container,
				false);
		BmgUtils.setTAG(this.getClass().getName());
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
		delegate.setMenuFragment(this);
		mCallback = (SideMenuItemClickListener) delegate;
		
		// initialize members
		initViewAndClassMembers();

	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;
		
		// Profile
		ivAvatar = (DynamicImageView)vi.findViewById(R.id.iv_avatar);
		tvName = (TextView)vi.findViewById(R.id.tv_name);
		tvPoints = (TextView)vi.findViewById(R.id.tv_points);
		
		// Menu
		listSideMenu = (ListView) vi.findViewById(R.id.lv_side_menu);
		String[] items = getActivity().getResources().getStringArray(
				R.array.side_menu_items);
		ArrayList<String> menuItems = new ArrayList<String>();
		Collections.addAll(menuItems, items);
		adaperSideMenu = new SideMenuAdapter(getActivity(), menuItems);
		listSideMenu.setAdapter(adaperSideMenu);
		listSideMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectMenuItem(position);
			}
		});

		// Map view elements to event handlers
		vi.findViewById(R.id.iv_toggle_button).setOnClickListener(this);
		
		setUserProfile();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_toggle_button) {
			delegate.toggleSlideMenu();
		}
	}
	
	public void setMenuSelected(int position) {
		adaperSideMenu.setSelected(position);
		adaperSideMenu.notifyDataSetChanged();
	}
	
	public void setUserProfile() {
		USER user = LocalStorage.authUser;
		
		// show avatar image
		ivAvatar.loadRoundedImage(user.profile.pictureURL);
		
		// show user name
		tvName.setText(user.profile.name);
		
		// show user points
		tvPoints.setText(String.format("%d POINTS", 0, getResources().getString(R.string.POINTS)));
	}
	
	private void selectMenuItem(int position) {
		mCallback.onMenuItemSelected(position);
	}
	
	

}
