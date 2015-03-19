package com.samuel.imooc_weichat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {

	private FragmentPagerAdapter fragmentPagerAdapter;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private String[] mTitles = new String[] { "First Fragment !",
			"Second Fragment !", "Third Fragment !", "Fourth Fragment !" };

	private ViewPager viewpager;

	private List<ChangeColorIconTextView> tabs = new ArrayList<ChangeColorIconTextView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOverflowButtonAlways();
		getActionBar().setDisplayShowHomeEnabled(false);
		initView();
		initDatas();
		viewpager.setAdapter(fragmentPagerAdapter);
		initEvents();
		tabs.get(0).setAlpha(1.0f);
		viewpager.setCurrentItem(0);
	}

	private void initEvents() {

		viewpager.setOnPageChangeListener(this);

	}

	private void initDatas() {

		for (String title : mTitles) {
			TabFragment tabFragment = new TabFragment();
			Bundle bundle = new Bundle();
			bundle.putString(TabFragment.TITLE, title);
			tabFragment.setArguments(bundle);
			mTabs.add(tabFragment);
		}
		fragmentPagerAdapter = new FragmentPagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int i) {
				return mTabs.get(i);
			}
		};
	}

	private void initView() {
		viewpager = (ViewPager) this.findViewById(R.id.id_viewpager);
		ChangeColorIconTextView id_weichat = (ChangeColorIconTextView) this
				.findViewById(R.id.id_weichat);
		id_weichat.setOnClickListener(this);
		tabs.add(id_weichat);
		ChangeColorIconTextView id_contact = (ChangeColorIconTextView) this
				.findViewById(R.id.id_contact);
		id_contact.setOnClickListener(this);
		tabs.add(id_contact);
		ChangeColorIconTextView id_found = (ChangeColorIconTextView) this
				.findViewById(R.id.id_found);
		id_found.setOnClickListener(this);
		tabs.add(id_found);
		ChangeColorIconTextView id_me = (ChangeColorIconTextView) this
				.findViewById(R.id.id_me);
		id_me.setOnClickListener(this);
		tabs.add(id_me);

	}

	@Override
	public void onClick(View v) {
		dispacherClick(v);
	}

	/**
	 * 
	 * @param v
	 */
	private void dispacherClick(View v) {
		resetAllTabs();
		switch (v.getId()) {
		case R.id.id_weichat:
			tabs.get(0).setAlpha(1.0f);
			viewpager.setCurrentItem(0,true);
			break;
		case R.id.id_contact:
			tabs.get(1).setAlpha(1.0f);
			viewpager.setCurrentItem(1,true);
			break;
		case R.id.id_found:
			tabs.get(2).setAlpha(1.0f);
			viewpager.setCurrentItem(2,true);
			break;
		case R.id.id_me:
			tabs.get(3).setAlpha(1.0f);
			viewpager.setCurrentItem(3,true);
			break;
		}
	}

	private void resetAllTabs() {
		for (ChangeColorIconTextView changeColorIconTextView : tabs) {
			changeColorIconTextView.setAlpha(0);
		}
	}

	private void setOverflowButtonAlways() {
		try {
			ViewConfiguration viewconf = ViewConfiguration.get(this);
			Field field = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			field.setAccessible(true);
			field.setBoolean(viewconf, Boolean.FALSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (positionOffset > 0) {
			ChangeColorIconTextView left = tabs.get(position);
			ChangeColorIconTextView right = tabs.get(position + 1);
			left.setAlpha(1 - positionOffset);
			right.setAlpha(positionOffset);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageSelected(int arg0) {

	}

}
