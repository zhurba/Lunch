package org.zhurba.lunch;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;

public class MainActivity extends FragmentActivity 
	implements 
	NewItemDialogFragment.NewItemDialogListener,
	ActionBar.TabListener,
	MultiChoiceModeListener
{

	final static int SECTION_PLACES = 0;
	final static int SECTION_LUNCHES = 1;
	
	ActionMode mActionMode = null;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	MainListFragment[]  mFragmentsArray = new MainListFragment[2];
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Устанавливаем режим навигации action bar.
		final ActionBar actionBar = getActionBar();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter  = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this)
			);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/*****************************************************************
	 *    Options Menu
	 ************`*****************************************************/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
        case R.id.action_newitem:
            showNewItemDialog();
            return true;
        default:
            return super.onOptionsItemSelected(item);
	    }
	}
	
	/*****************************************************************
	 *    ActionMode (MultiChoiceModeListener)
	 *****************************************************************/
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// Respond to clicks on the actions in the CAB
        switch (item.getItemId()) {
            case R.id.action_deleteitem:
            	mFragmentsArray[mViewPager.getCurrentItem()].deleteSelectedItems();
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		// Inflate the menu for the CAB
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_item, menu);
        mActionMode = mode;
        return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// Here you can make any necessary updates to the activity when
        // the CAB is removed. By default, selected items are deselected/unchecked.
		mActionMode = null;
		
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// Here you can perform updates to the CAB due to
        // an invalidate() request
		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode,
			int position, long id, boolean checked) {
		// Here you can do something when items are selected/de-selected,
        // such as update the title in the CAB
		
		//*
		ListView listView = mFragmentsArray[mViewPager.getCurrentItem()].getListView();
		SparseBooleanArray arrCheckedPos = listView.getCheckedItemPositions();

		int selectedCount = 0;
		for (int i = 0; i < arrCheckedPos.size(); i++) {
			int key = arrCheckedPos.keyAt(i);
			if (arrCheckedPos.get(key)) selectedCount++;
		}
		
		if(selectedCount>0) mode.setTitle(String.valueOf(selectedCount)+getString(R.string.cab_selected));
		//*/
	}

	/*****************************************************************
	 *    ActionBar.TabListener 
	 *****************************************************************/
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		if(mActionMode != null)
			mActionMode.finish();
	}
	
	/*****************************************************************
	 *    FragmentPagerAdapter
	 *****************************************************************/
	
	@SuppressLint("DefaultLocale")
	class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a LunchesFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			MainListFragment listFragment;
			switch(position) {
			case SECTION_PLACES:
				listFragment = new PlacesFragment();
				break;
			case SECTION_LUNCHES:
				listFragment = new LunchesFragment();
				break;
			default:
				return null;
			}
			mFragmentsArray[position] = listFragment;
			return listFragment;
		}

		@Override
		public int getCount() {
			return 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.tabtitle_places).toUpperCase();
			case 1:
				return getString(R.string.tabtitle_lunches).toUpperCase();
			}
			return null;
		}
		
	}

	/*****************************************************************
	 *    Handle dialog for add DB's item
	 *****************************************************************/
	
	@Override
	public void onDialogPositiveClick(String aItemName) {
		mFragmentsArray[mViewPager.getCurrentItem()].addItem(aItemName);
	}

	void showNewItemDialog() {
		DialogFragment dialog = new NewItemDialogFragment();
        dialog.show(getFragmentManager(), "NewItemDialogFragment");
	}

}

