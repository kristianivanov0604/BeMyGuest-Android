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
public class BmgDialog extends DialogFragment {

	private Dialog dialog;
	private OnClickListener okButtonListener = null;
	private OnClickListener cancelButtonListener = null;
	private int iconContent = 0;
	private String titleContent = null;
	private String messageContent = null;
	private String descriptionContent = null;
	private ArrayList<String> descriptionList = null;
	private String okButtonTitle = null;
	private String cancelButtonTitle = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);

		initViewAndClassMembers();

		return dialog;
	}

	public void setIcon(int icon) {
		iconContent = icon;
	}

	public void setTitle(String title) {
		titleContent = title;
	}

	public void setMessage(String message) {
		messageContent = message;
	}

	public void setDescription(String description) {
		descriptionContent = description;
	}

	public void setDescriptionList(ArrayList<String> descriptions) {
		descriptionList = descriptions;
	}

	public void setPositiveButton(String title, OnClickListener listener) {
		okButtonTitle = title;
		okButtonListener = listener;
	}

	public void setNegativeButton(String title, OnClickListener listener) {
		cancelButtonTitle = title;
		cancelButtonListener = listener;
	}

	private void initViewAndClassMembers() {

		// Set Icon
		ImageView ivIcon = (ImageView) dialog.findViewById(R.id.dlg_icon);
		if (iconContent == 0) {
			ivIcon.setVisibility(View.GONE);
		} else {
			ivIcon.setVisibility(View.VISIBLE);
			ivIcon.setImageResource(iconContent);
		}

		// Set Title
		TextView tvTitle = (TextView) dialog.findViewById(R.id.dlg_title);
		if (titleContent == null) {
			tvTitle.setVisibility(View.GONE);
		} else {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(titleContent);
		}

		// Set Message
		TextView tvMessage = (TextView) dialog.findViewById(R.id.dlg_message);
		if (messageContent == null) {
			tvMessage.setVisibility(View.GONE);
		} else {
			tvMessage.setVisibility(View.VISIBLE);
			tvMessage.setText(messageContent);
		}

		// Set Description
		View viSeperator = (View) dialog.findViewById(R.id.dlg_seperator);
		TextView tvDescription = (TextView) dialog
				.findViewById(R.id.dlg_description);
		if (descriptionContent == null) {
			viSeperator.setVisibility(View.GONE);
			tvDescription.setVisibility(View.GONE);
		} else {
			viSeperator.setVisibility(View.VISIBLE);
			tvDescription.setVisibility(View.VISIBLE);
			tvDescription.setText(descriptionContent);
		}

		// Set Description ListView
		ListView lvDescription = (ListView) dialog
				.findViewById(R.id.description_list);
		if (descriptionList == null) {
			lvDescription.setVisibility(View.GONE);
		} else {
			lvDescription.setVisibility(View.VISIBLE);
			lvDescription
					.setAdapter(new DescriptionListAdapter(dialog.getContext(),
							DescriptionListAdapter.ADAPTER_MODE.DIALOG,
							descriptionList));
		}

		// Set Positive Button
		TextView okButton = (TextView) dialog.findViewById(R.id.ok_button);

		if (okButtonListener == null) {
			okButton.setVisibility(View.GONE);
		} else {
			okButton.setVisibility(View.VISIBLE);
			okButton.setText(okButtonTitle);
			okButton.setOnClickListener(okButtonListener);
		}

		// Set Negative Button
		TextView cancelButton = (TextView) dialog
				.findViewById(R.id.cancel_button);

		if (cancelButtonListener == null) {
			cancelButton.setVisibility(View.GONE);
		} else {
			cancelButton.setVisibility(View.VISIBLE);
			cancelButton.setText(cancelButtonTitle);
			cancelButton.setOnClickListener(cancelButtonListener);
		}
	}
}