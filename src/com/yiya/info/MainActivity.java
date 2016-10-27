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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView admin;
	TextView user;
	ListView list;
	List<List> info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		user = (TextView) findViewById(R.id.user);
		admin = (TextView) findViewById(R.id.admin);
		list = (ListView) findViewById(R.id.list);

		// get package info
		
		info = getPackageInfo();

		MyAdapter adapter = new MyAdapter();
		list.setAdapter(adapter);

	}

	private List<List> getPackageInfo() {
		// ActivityManager am = (ActivityManager)
		// getSystemService(ACTIVITY_SERVICE); 获得正在运行的
		PackageManager pm = getPackageManager();
		List<PackageInfo> info = pm.getInstalledPackages(0);
		// Toast.makeText(this, ""+info.size(), 0).show();
		List<List> list = new ArrayList<List>();
		List<PackageInfoo> user = new ArrayList<PackageInfoo>();
		List<PackageInfoo> admin = new ArrayList<PackageInfoo>();
		for (PackageInfo i : info) {
			PackageInfoo p = new PackageInfoo();

			Drawable icon = i.applicationInfo.loadIcon(pm);
			String name = (String) i.applicationInfo.loadLabel(pm);

			String file = i.applicationInfo.dataDir;
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
			return info.get(0).size()+info.get(1).size()+2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			PackageInfoo p;
			if(position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户应用");
				return tv;
			} if(position > 0 && position < info.get(0).size() + 1 ) {
				//用户应用
				
				//获得info
				p = (PackageInfoo) info.get(0).get(position -1);
				
			} if (position == info.get(0).size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("系统应用");
				return tv;
			} if (position > info.get(0).size() + 1) {
				//系统应用
				p = (PackageInfoo) info.get(1).get(position -2-info.get(0).size());
			}
			
			if(convertView != null && convertView instanceof RelativeLayout) {
				v =  convertView;
			}else {
				v = View.inflate(getApplicationContext(), R.layout.item, null);
				
				ViewHolder holder = new ViewHolder();
				holder.icon = (ImageView) findViewById(R.id.ivl);
				holder.loc = (ImageView) findViewById(R.id.ivr);
				holder.name	= (TextView) findViewById(R.id.name);
				holder.size	= (TextView) findViewById(R.id.size);
				v.setTag(holder);
				
			}
			
			
			
			
			
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
