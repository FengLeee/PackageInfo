package com.yiya.info;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yiya.info.R.drawable;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	TextView user;
	ListView list;
	List<List> info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		user = (TextView) findViewById(R.id.user);
		
		user.setVisibility(View.INVISIBLE);
		
		list = (ListView) findViewById(R.id.list);

		// get package info

		info = getPackageInfo();
		
		new Thread() {
			public void run() {
				MyAdapter adapter = new MyAdapter();
				list.setAdapter(adapter);
				setOnScrollListener();
			};
		}.start();

	}

	protected void setOnScrollListener() {
		list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {	
				if(  firstVisibleItem > 0 && firstVisibleItem< info.get(0).size()) {
					user.setText("用户应用");
					user.setVisibility(View.VISIBLE);
				}else if(firstVisibleItem > info.get(0).size() + 1) {
					user.setText("系统应用");
					user.setVisibility(View.VISIBLE);
				}
			}
		});
		
	}

	private List<List> getPackageInfo() {
		
		PackageManager pm = getPackageManager();
		List<PackageInfo> info = pm.getInstalledPackages(0);
		
		List<List> list = new ArrayList<List>();
		List<PackageInfoo> user = new ArrayList<PackageInfoo>();
		List<PackageInfoo> admin = new ArrayList<PackageInfoo>();
		for (PackageInfo i : info) {
			PackageInfoo p = new PackageInfoo();

			Drawable icon = i.applicationInfo.loadIcon(pm);
			String name = (String) i.applicationInfo.loadLabel(pm);

			String file = i.applicationInfo.sourceDir;
			File f = new File(file);
			long l = f.length();
			String size = Formatter.formatFileSize(this, l);

			p.setIcon(icon);
			p.setName(name);
			p.setSize(size);
			if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				// 系统应用
				p.setLoc(drawable.memory);
				admin.add(p);

			} else {
				p.setLoc(drawable.sd);
				user.add(p);
			}

		}
		list.add(user);
		list.add(admin);
		return list;

	}

	static class ViewHolder {
		 ImageView icon;
		 ImageView loc;
		 TextView name;
		 TextView size;
	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return info.get(0).size() + info.get(1).size() + 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View v = null;
			PackageInfoo p = null;
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户应用");
				return tv;
			}
			if (position > 0 && position < info.get(0).size() + 1 ) {
				// 用户应用
				
				// 获得info
				p = (PackageInfoo) info.get(0).get(position - 1);

			}
			if (position == info.get(0).size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("系统应用");
				return tv;
			}
			if (position > info.get(0).size() + 1) {
				// 系统应用
				p = (PackageInfoo) info.get(1).get(
						position - 2 - info.get(0).size());
			}

			if (convertView != null && convertView instanceof RelativeLayout) {

				holder = (ViewHolder) convertView.getTag();

				v = convertView;

			} else {
				v = View.inflate(getApplicationContext(), R.layout.item, null);

				holder = new ViewHolder();

				holder.icon = (ImageView)v.findViewById(R.id.ivl);
				holder.loc = (ImageView)v.findViewById(R.id.ivr);
				holder.name = (TextView)v.findViewById(R.id.name);
				holder.size = (TextView)v.findViewById(R.id.size);
				
				v.setTag(holder);
			}
			
			holder.icon.setImageDrawable(p.getIcon());
			holder.loc.setImageResource(p.getLoc());
			holder.name.setText(p.getName());
			holder.size.setText(p.getSize());

			return v;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

}
