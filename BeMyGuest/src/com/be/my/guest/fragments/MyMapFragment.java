package com.be.my.guest.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.be.my.guest.R;
import com.be.my.guest.activities.MainActivity;
import com.be.my.guest.activities.base.BaseActivity;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.model.CustomMarker;
import com.be.my.guest.utility.BmgUtils;
import com.be.my.guest.utility.GPSTracker;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class MyMapFragment extends BaseFragment implements OnClickListener,
		OnMarkerClickListener, OnCameraChangeListener, OnTouchListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;
	private Context _context;
	private LinearLayout llForground;
	private CustomMarker selectedMarker;
	private boolean waitCameraChange;

	SupportMapFragment mapFragment;
	private GoogleMap googleMap;
	private Marker currentMarker;

	private double latitude;
	private double longitude;

	private GPSTracker gps;

	private final int MAGNIFY_RATE = 10;
	private final int ACCURATE_RATE = 16;
	
	private double[][] _locations = { { -22.60640442, -46.91265106 },
			{ -22.65900711, -46.89411163 }, { -22.64379877, -46.79592133 },
			{ -22.71665678, -46.94149017 }, { -22.72869047, -47.01358795 },
			{ -22.69702061, -47.00878143 }, { -22.66090804, -46.73618317 } };

	private int[] _maptags = { R.drawable.maptag_current,
			R.drawable.maptag_blue, R.drawable.maptag_green,
			R.drawable.maptag_pink, R.drawable.maptag_purple,
			R.drawable.maptag_red, R.drawable.maptag_yellow };

	public static ArrayList<CustomMarker> CUSTOM_MARKERS;

	public static MyMapFragment newInstance() {
		return new MyMapFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		// inflater.inflate(R.layout.fragment_map, container, false);

		inflateViewIfNull(inflater, container, R.layout.fragment_map);
		_context = BaseActivity.getContext();
		this.view = rootView;
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

	@Override
	public void onResume() {
		super.onResume();
		try {
			// Initialize google map
			initializeGoogleMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {

			// Open Menu
			delegate.toggleSlideMenu();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open ...

		} else if (v.getId() == R.id.tv_get_there) {
			// Zoom in Map ...
			llForground.setVisibility(View.GONE);
			if (googleMap.getCameraPosition().zoom < ACCURATE_RATE) {
				navigateToPosition(selectedMarker.getDelegate().getPosition(), ACCURATE_RATE);				
			}
		} else if (v.getId() == R.id.tv_more_info) {
			// Goto Event Info
			BmgUtils.showMessage(delegate.getResources().getString(R.string.MORE_INFORMATION));
			llForground.setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == llForground) {
			Log.e("MAP", "Foreground touched.");
			llForground.setVisibility(View.GONE);
		}
		return false;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		Log.e("Marker", "Marker clicked.");

		if (llForground.getVisibility() == View.VISIBLE) {
			llForground.setVisibility(View.GONE);
		}

		if (!currentMarker.equals(arg0)) {
			selectedMarker.setDelegate(arg0);
			waitCameraChange = true;
		}
		return false;
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		Log.e("Map Camera", "Camera changed.");

		if (waitCameraChange) {
			waitCameraChange = false;
			llForground.setVisibility(View.VISIBLE);
		}
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
		tvTitle.setText(R.string.Map);

		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);
		menuBtn.setImageResource(R.drawable.icon_return);

		ImageView rightBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		rightBtn.setOnClickListener(this);
		rightBtn.setImageResource(R.drawable.icon_open);

		// Initialize custom tag info view
		llForground = (LinearLayout) vi.findViewById(R.id.ll_forground);
		llForground.setVisibility(View.GONE);
		llForground.setOnTouchListener(this);

		vi.findViewById(R.id.tv_get_there).setOnClickListener(this);
		vi.findViewById(R.id.tv_more_info).setOnClickListener(this);
		selectedMarker = new CustomMarker();

		// Initialize Map
		gps = new GPSTracker(_context);
		try {
			initializeMapFragment();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** Initialize Map Fragment **/
	private void initializeMapFragment() {
		if (googleMap == null) {

			FragmentManager fm = getChildFragmentManager();
			mapFragment = (SupportMapFragment) fm
					.findFragmentById(R.id.fragment_map);
			if (mapFragment == null) {
				mapFragment = SupportMapFragment.newInstance();
				fm.beginTransaction().replace(R.id.fragment_map, mapFragment)
						.commit();
			}
		}
	}

	/** Get Google Map object from Map Fragment **/
	private void initializeGoogleMap() {
		if (googleMap == null) {

			googleMap = mapFragment.getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				BmgUtils.showMessage("Sorry! unable to create maps");
				return;
			}

			googleMap.setMyLocationEnabled(false);
			googleMap.getUiSettings().setRotateGesturesEnabled(false);
			googleMap.setOnMarkerClickListener(this);
			googleMap.setOnCameraChangeListener(this);
			showCurrentPositionOnMap();

			CUSTOM_MARKERS = new ArrayList<CustomMarker>();
			for (int i = 1; i < _locations.length; i++) {
				addPartnerMarker(_locations[i][0], _locations[i][1],
						_maptags[i]);
			}
		}

	}

	/** Add position as the new tag **/
	private void addPartnerMarker(double latitude, double longitude, int tag) {
		CustomMarker marker = new CustomMarker();

		MarkerOptions options = new MarkerOptions().position(new LatLng(
				latitude, longitude));
		options.icon(BitmapDescriptorFactory.fromResource(tag));
		Marker delegate = googleMap.addMarker(options);

		marker.setMarkerID(delegate.getId());
		marker.setLatitude(latitude);
		marker.setLongitude(longitude);
		marker.setDelegate(delegate);
	}

	/** Shows user's current position on the map **/
	private void showCurrentPositionOnMap() {
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		} else {
			gps.showSettingsAlert();
		}

		// Set Location as Sp, Sao Paulo Brazil
		latitude = _locations[0][0];
		longitude = _locations[0][1];

		navigateToPosition(new LatLng(latitude, longitude), MAGNIFY_RATE);

		MarkerOptions marker = new MarkerOptions().position(new LatLng(
				latitude, longitude));
		marker.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.maptag_current));
		if (currentMarker == null) {
			currentMarker = googleMap.addMarker(marker);
		} else {
			currentMarker.setPosition(new LatLng(latitude, longitude));
		}
		currentMarker.setVisible(true);

	}

	/** Move the camera to the specified position **/
	private void navigateToPosition(LatLng position, int zoom) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(position).zoom(zoom).build();

		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		SupportMapFragment f = (SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_map);
		if (f != null) {
			getFragmentManager().beginTransaction().remove(f).commit();
		}
	}

}
