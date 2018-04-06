package com.be.my.guest.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.be.my.guest.activities.base.BaseActivity;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.ui.ProgDialog;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Define static methods for Application
 * 
 * @author Abraham Sandbak
 * 
 */
public class BmgUtils {

	/**
	 * Print Log
	 */

	public static String TAG = BaseFragment.class.getName();

	public static void setTAG(String tag) {
		TAG = tag;
	}

	public static void printLog(String log) {
		Log.e(TAG, log);
	}

	public static void printLog(int log) {
		Log.e(TAG, getStringFromRes(log));
	}

	/**
	 * Show toast view
	 */
	public static void showMessage(String message) {
		if (message != null) {
			Toast.makeText(BaseActivity.getContext(), message,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void showMessage(int message) {
		printLog(message);
		Toast.makeText(BaseActivity.getContext(), getStringFromRes(message),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Convert String to java.util.Date
	 */
	public static Date dateFromString(String dateString) {
		Date convertedDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			convertedDate = formatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedDate;
	}

	/**
	 * Convert String to Calendar
	 */
	public static Calendar stringToCalendar(String dateString,
			String formatString) {
		Calendar convertedDate = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			convertedDate.setTime(sdf.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			printLog(e.toString());
			e.printStackTrace();
		}
		return convertedDate;
	}

	public static Calendar stringToCalendar(String dateString, int formatString) {
		Calendar convertedDate = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					getStringFromRes(formatString));
			convertedDate.setTime(sdf.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			printLog(e.toString());
			e.printStackTrace();
		}
		return convertedDate;
	}

	/**
	 * Convert String to Calendar
	 */
	public static String calendarToString(Calendar date, String formatString) {
		String dateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		dateString = sdf.format(date.getTime());
		return dateString;
	}

	public static String calendarToString(Calendar date, int formatString) {
		String dateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat(
				getStringFromRes(formatString));
		dateString = sdf.format(date.getTime());
		return dateString;
	}
	
	public static int minutesBetweenDates(Calendar from, Calendar to) {
		long ms = to.getTimeInMillis() - from.getTimeInMillis();
		int minutes = (int)(ms/60000);
		return minutes;
	}

	/**
	 * Get String from id defined in string.xml
	 */
	public static String getStringFromRes(int resId) {
		return BaseActivity.getContext().getResources().getString(resId);
	}

	/**
	 * Get int from id defined in string.xml
	 */
	public static int getIntFromRes(int resId) {
		return BaseActivity.getContext().getResources().getInteger(resId);
	}

	/** Validate email address **/
	public static boolean validateEmail(String email) {
		Pattern p = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/** Validate telephone number **/
	public static boolean validatePhone(String phone, int length) {
		Pattern p = Pattern.compile(String.format("[0-9]{%d}", length),
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * Progress bar
	 */
	public static ProgDialog progDlg = null;

	public static void showProgDlg() {
		if (progDlg == null) {
			printLog("Showing progress indicator.");
			progDlg = new ProgDialog();
			progDlg.setCancelable(false);
			progDlg.show(BaseFragment.getBaseFragmentMagager(), null);
		} else {
			printLog("Progress Indicator is already shown.");
		}
	}

	public static void hideProgDlg() {
		if (progDlg != null) {
			printLog("Hiding progress indicator.");
			progDlg.dismiss();
			progDlg = null;
		}
	}

}