package com.shuanghua.chat.server.widge;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuanghua.chat.server.R;

/**
 * 列表容器类
 * <br>Copyright (c) 2015 双华科技
 * @author YHD
 * @version 2015-10-19 下午4:37:10
 */
public class MessageListAdapter extends BaseAdapter {
	private Context mcontext;
	private ArrayList<String> mItemName;
	private ArrayList<String> mItemTime;
	private ArrayList<String> mimgUrl;
	private LayoutInflater mInflater;
    //构造函数
	public MessageListAdapter(Context context, ArrayList<String> itemName ,ArrayList<String> itemTime, ArrayList<String> imgUrl) {
		this.mcontext = context;
		this.mItemName = itemName;
		this.mItemTime = itemTime;
		this.mimgUrl = imgUrl;
		mInflater = LayoutInflater.from(mcontext);
	}
	
	/**
	 * 返回当前文件数目
	 */
	@Override
	public int getCount() {
		return mItemName.size();
	}
	
	/**
	 * 返回列表指定位置item
	 */
	@Override
	public Object getItem(int position) {
		return mItemName.get(position);
	}

	/**
	 * 返回item位置
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 重写getview，让列表每个item都不一样
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StoryItemViewHolder viewHolder;
		if (convertView == null) {
			//每次都新建一个item类，并初始化内容
			viewHolder = new StoryItemViewHolder();
			convertView = mInflater.inflate(R.layout.item_message_item, null);
			convertView.setTag(viewHolder);
			viewHolder.mItemImg = (ImageView) convertView.findViewById(R.id.item_img);
			viewHolder.mItemName = (TextView) convertView.findViewById(R.id.item_name);
			viewHolder.mItemTime = (TextView) convertView.findViewById(R.id.item_time);
		} else {
			viewHolder = (StoryItemViewHolder) convertView.getTag();
		}
		viewHolder.mItemName.setText(mItemName.get(position));
		viewHolder.mItemTime.setText(mItemTime.get(position));
		return convertView;
	}	
	/**
	 * ListView单个item类
	 * <br>Copyright (c) 2015 双华科技
	 * @author YHD
	 * @version 2015-10-19 下午4:54:36
	 */
	public class StoryItemViewHolder {
		public ImageView mItemImg;
		public TextView mItemName;
		public TextView mItemTime;
	}
}
