/*
 * Copyright 2013 Daniel Logan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.growingstems.scouting;

import java.util.ArrayList;
import java.util.List;
import org.growingstems.scouting.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<ExpandableListItem<String>> groups;

	public TextListAdapter(Context context,
			List<ExpandableListItem<String>> groups) {
		this.context = context;
		this.groups = groups;
	}

	public TextListAdapter(Context context) {
		this.context = context;
		groups = new ArrayList<ExpandableListItem<String>>();
	}

	public void addGroup(ExpandableListItem<String> group) {
		groups.add(group);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getContents().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		List<String> child = groups.get(groupPosition).getRow(childPosition);
		if (convertView == null) {
			LayoutInflater childInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = childInflater.inflate(R.layout.two_text_child_item,
					null);
		}
		TextView tv1 = (TextView) convertView.findViewById(R.id.text_child_one);
		if (!child.isEmpty())
			tv1.setText(child.get(0));
		else
			tv1.setText("");
		TextView tv2 = (TextView) convertView.findViewById(R.id.text_child_two);
		if (child.size() > 1)
			tv2.setText(child.get(1));
		else
			tv2.setText("");

		if (child.size() > 2 && convertView instanceof LinearLayout) {
			tv2.setGravity(Gravity.CENTER_HORIZONTAL);
			ViewGroup.LayoutParams params = tv1.getLayoutParams();
			for (int i = 2; i < child.size(); i++) {
				TextView ch = new TextView(tv1.getContext());
				ch.setLayoutParams(params);
				ch.setText(child.get(i));
				((LinearLayout) convertView).addView(ch);
			}
		}
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getContents().size();
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ExpandableListItem<String> group = groups.get(groupPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.expandable_group_item, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.event_group);
		tv.setText(group.getName());
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return groups.get(groupPosition).isSelectable(childPosition);
	}

}
