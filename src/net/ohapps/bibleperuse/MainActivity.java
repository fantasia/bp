package net.ohapps.bibleperuse;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private ArrayList<String> sections;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		// Specify a SpinnerAdapter to populate the dropdown list.
		actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, android.R.id.text1, sections), this);
	}

	private void init() {
		sections = new ArrayList<String>();
		sections.add(getString(R.string.title_section_marker));
		sections.add(getString(R.string.title_section_prayer));

		MarkerDataProvider.getInstance().init(this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new MarkerFragment();
			break;
		case 1:
			fragment = new PrayerFragment();
			break;

		default:
			break;
		}

		if (null != fragment) {
			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		}
		return true;
	}
}
