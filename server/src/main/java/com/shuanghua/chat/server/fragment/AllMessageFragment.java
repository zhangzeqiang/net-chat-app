/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shuanghua.chat.server.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.shuanghua.chat.server.R;
import com.shuanghua.chat.server.activity.ChatActivity;
import com.shuanghua.chat.server.widge.MessageListAdapter;
import java.util.ArrayList;

public class AllMessageFragment extends Fragment{
	private ListView listView=null;
	private ArrayList<String> mItemNameArray;
	private ArrayList<String> mItemContentArray;
	private ArrayList<String> mImgUrlArray;
	private MessageListAdapter adapter=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_all_message_, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initUI();
	}

	private void initData() {
		mItemNameArray=new ArrayList<String>();
		mItemNameArray.add("张三");
		mItemNameArray.add("李四");
		mItemNameArray.add("王五");
		mItemNameArray.add("赵六");
		mItemNameArray.add("梁八");
		mItemContentArray=new ArrayList<String>();
		mItemContentArray.add("你好！");
		mItemContentArray.add("在么？");
		mItemContentArray.add("有事想问你？");
		mItemContentArray.add("有事想问你？");
		mItemContentArray.add("有事想问你？");
		mImgUrlArray=new ArrayList<String>();
		mImgUrlArray.add("图一");
		mImgUrlArray.add("图二");
		mImgUrlArray.add("图五");
		mImgUrlArray.add("图五");
		mImgUrlArray.add("图五");

	}

	private void initUI() {
		listView=(ListView)getActivity().findViewById(R.id.all_item_list);
		adapter=new MessageListAdapter(this.getActivity(), mItemNameArray,mItemContentArray, mImgUrlArray);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				startActivity(new Intent(AllMessageFragment.this.getActivity(), ChatActivity.class));
			}
		});
	}
}
