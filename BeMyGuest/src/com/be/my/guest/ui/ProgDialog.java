package com.be.my.guest.ui;

import java.util.ArrayList;
import java.util.Collections;

import com.be.my.guest.R;
import com.be.my.guest.adapters.CityListAdapter;
import com.be.my.guest.adapters.DescriptionListAdapter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Define static methods for Application
 * @author Abraham Sandbak
 *
 */

/**
 * Custom Dialog class for Common Dialog in NailsPolished
 * 
 */
public class ProgDialog extends DialogFragment {

	private Dialog dialog;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progress_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);

		return dialog;
	}

}