package com.redorange.motutv1.adapter;

import java.util.ArrayList;

import com.extremeiptv.buzziptv.R;
import com.redorange.motutv1.LivePlayActivity;
import com.redorange.motutv1.model.Channel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchMenuAdapter extends BaseAdapter {

	private ArrayList<Channel> items;
    private int width;
    private int height;
    
    public SearchMenuAdapter() {
        super();
        items = new ArrayList<Channel>();
    }
    
    public SearchMenuAdapter(int w, int h) {
        super();
        items = new ArrayList<Channel>();
        width = w;
        height = h;
    }
   
    public void addNewItem(Channel newitem)
    {
    	  items.add(newitem);
    }
    
    public void setNewList(ArrayList<Channel> list){
  	  items = list;
  	  notifyDataSetChanged();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                android.view.LayoutInflater vi = (android.view.LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.searchmenu_item, null);
            }
            Channel p = items.get(position);
            if (p != null) {
          	  LinearLayout item = (LinearLayout)v.findViewById(R.id.search_channel_item_layout);
          	  LinearLayout.LayoutParams m = (LinearLayout.LayoutParams)item.getLayoutParams();
      	      m.height = (int)((float)(height-90)/10);
	      	      item.setLayoutParams(m);
                TextView tt = (TextView) v.findViewById(R.id.search_channel_name);
                String s = LivePlayActivity.IntToThreedigits(p.getSort());
                tt.setText(s+"   "+p.getName());
            }
            return v;
    }

		@Override
		public int getCount() {
			if(items != null)
				return items.size();
			return 0;
		}
	
		@Override
		public Object getItem(int arg0) {
			return items.get(arg0);
		}
	
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
}
