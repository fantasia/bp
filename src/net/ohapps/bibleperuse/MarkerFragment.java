package net.ohapps.bibleperuse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

public class MarkerFragment extends Fragment {
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E");

	TextView tvStartDate;
	ListView listview;
	ArrayAdapter<String> adapter;

	ArrayList<String> list = new ArrayList<String>();

	public MarkerFragment() {
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_marker, list) {
			@Override
			public int getCount() {
				return MarkerDataProvider.getInstance().size();
			}

			@Override
			public View getView(final int pos, View cv, ViewGroup parent) {
				View v;
				if (cv != null) {
					v = cv;
				} else {
					LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = li.inflate(R.layout.marker_listitem, null);
				}

				Date d = MarkerDataStore.getStartDate(getActivity());
				Calendar cal = new GregorianCalendar();
				cal.setTime(d);
				int initDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
				cal.add(Calendar.HOUR, 24 * pos);
				if (initDayOfWeek + pos > 7) {
					cal.add(Calendar.HOUR, 24 * ((initDayOfWeek + pos) / 7));
				}

				if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
					// 일요일이면 하루 더해준다.
					cal.add(Calendar.HOUR, 24);
				}

				((TextView) v.findViewById(R.id.maker_listitem_index)).setText((pos + 1) + "일차");
				((TextView) v.findViewById(R.id.maker_listitem_date)).setText(sdf.format(cal.getTime()) + " / " + MarkerDataProvider.getInstance().getDuration(pos));
				((TextView) v.findViewById(R.id.maker_listitem_bible)).setText(MarkerDataProvider.getInstance().getContent(pos));
				((TextView) v.findViewById(R.id.maker_listitem_check)).setText(MarkerDataStore.getChecked(getActivity(), pos + 1 + "") ? getString(R.string.marker_set_complete) : "");
				return v;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_marker, container, false);

		tvStartDate = (TextView) view.findViewById(R.id.marker_start_date);
		view.findViewById(R.id.marker_start_date_wrapper).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Date d = MarkerDataStore.getStartDate(getActivity());
				Calendar cal = new GregorianCalendar();
				cal.setTime(d);

				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog dpd = new DatePickerDialog(getActivity(), new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker arg0, int year, int month, int day) {
						MarkerDataStore.setStartDate(getActivity(), new GregorianCalendar(year, month, day).getTime());
						updateUI();
					}
				}, year, month, day);
				dpd.show();
			}
		});

		listview = (ListView) view.findViewById(R.id.listview);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
				String key = pos + 1 + "";
				MarkerDataStore.setChecked(getActivity(), key, !MarkerDataStore.getChecked(getActivity(), key));
				updateUI();
			}
		});

		updateUI();
		return view;
	}

	private void updateUI() {
		Date d = MarkerDataStore.getStartDate(getActivity());
		tvStartDate.setText(sdf.format(d));
		adapter.notifyDataSetChanged();
	}
}
