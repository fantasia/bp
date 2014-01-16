package net.ohapps.bibleperuse;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MarkerDataStore {
	private static SharedPreferences pref;
	static final String KEY_START_DATE = "date";
	static final String KEY_DAY_CHECKED = "check_";

	private static Editor getEditor(Activity act) {
		if (null == pref) {
			pref = act.getPreferences(Context.MODE_PRIVATE);
		}
		return pref.edit();
	}

	private static SharedPreferences getPref(Activity act) {
		if (null == pref) {
			pref = act.getPreferences(Context.MODE_PRIVATE);
		}
		return pref;
	}

	public static void setStartDate(Activity act, Date date) {
		Editor edt = getEditor(act);
		edt.putLong(KEY_START_DATE, date.getTime());
		edt.commit();
	}

	public static Date getStartDate(Activity act) {
		return new Date(getPref(act).getLong(KEY_START_DATE, System.currentTimeMillis()));
	}

	public static void setChecked(Activity act, String day, boolean flag) {
		Editor edt = getEditor(act);
		edt.putBoolean(KEY_DAY_CHECKED + day, flag);
		edt.commit();
	}

	public static boolean getChecked(Activity act, String day) {
		return getPref(act).getBoolean(KEY_DAY_CHECKED + day, false);
	}
}
