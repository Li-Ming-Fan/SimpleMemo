
package com.simple_memo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentList extends Fragment
{
	TextView tv_create;
	ListView lv_list;
	List<Map<String,String>> ListData;
	MyAdapter adapter;

	//
	public void refreshListView()
	{
		ListData.clear();
		ListData.addAll(getData());

		//
		adapter.notifyDataSetChanged();
	}

	//		
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		ListData = getData();
		adapter = new MyAdapter(this.getActivity());
		
		//
		View mView = inflater.inflate(R.layout.fragment_list, container, false);
		
		tv_create = (TextView) mView.findViewById(R.id.list_tv_create);
		tv_create.setText("NewMemo");
		tv_create.setOnClickListener(new ClickEvent()); 
		
		//
		lv_list = (ListView) mView.findViewById(R.id.list_listview);
		lv_list.setAdapter(adapter);

		//
		lv_list.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//点击后在标题上显示点击了第几行 setTitle("你点击了第"+arg2+"行");
				Common.posi_in_list_memo_all = Common.list_memo_all.size() - 1 - arg2;
				//
				Common.memo_to_edit = Common.list_memo_all.get(Common.posi_in_list_memo_all);
				Common.memo_to_edit.flag_new = false;
				//
												
				//
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

				FragmentEdit fragment = new FragmentEdit();
				fragmentTransaction.replace(R.id.id_content, fragment);

				fragmentTransaction.addToBackStack(null); 

				//
				fragmentTransaction.commit(); 
			}
		});

		// 注册长按菜单
		getActivity().registerForContextMenu(lv_list); //为ListView添加上下文菜单 

		// 添加长按点击弹出选择菜单
		lv_list.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
		{
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
			{
				//menu.setHeaderTitle("文件操作");
				menu.add(0, 0, 0, "               删除      ");
				//menu.add(0, 1, 0, "                           置顶");                  
			}
		});

		return mView;

	}

	// 长按菜单响应函数 
	public boolean onContextItemSelected(MenuItem item)
	{ 
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		int MID = (int) info.id; //  显示中的位置

		switch (item.getItemId())
		{
		case 0:
			int posi = Common.list_memo_all.size() - 1 - MID;
			MemoItem selected = Common.list_memo_all.get(posi);
			String filepath = Common.PathDirDetail + "/" + selected.getFilenameForDetails();
			//
			Common.list_memo_all.remove(posi);
			Common.removeFile(filepath);
			//
			Toast.makeText(getActivity().getApplicationContext(),
					"　已删除　", Toast.LENGTH_SHORT).show();
			break;
		//case 1:      	
		//	break; 
		default:
			break; 
		} 

		//
		refreshListView();

		//
		return super.onContextItemSelected(item); 
	}
	//

	class ClickEvent implements View.OnClickListener
	{
		public void onClick(View v)
		{
			if (v == tv_create)
			{
				MemoItem item_new = new MemoItem();
				Common.memo_to_edit = item_new;
				Common.memo_to_edit.date_create = Common.getCurrentDateTime();
				Common.memo_to_edit.date_update = Common.memo_to_edit.date_create;

				//
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

				FragmentEdit fragment = new FragmentEdit();
				fragmentTransaction.replace(R.id.id_content, fragment);

				fragmentTransaction.addToBackStack(null); 

				//
				fragmentTransaction.commit(); 
			}
		}// onClick
	}
	//ClickEvent
	
	//
	static class ViewHolder
    {
		public TextView content;
        public TextView datetime;
    }
	class MyAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater = null;

		private MyAdapter(Context context)
        {
			this.mInflater = LayoutInflater.from(context);
        }

		//
		public int getCount()
		{
			return ListData.size();
		}
		public Object getItem(int position)
		{
			return ListData.get(position);
			//return position;
		}
		public long getItemId(int position)
		{
			return position;
		}
		
		//
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.memo_list_item, parent, false);  
                
                holder.content = (TextView)convertView.findViewById(R.id.memo_item_content);
                holder.datetime = (TextView)convertView.findViewById(R.id.memo_item_date);
                
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            
            holder.content.setText(ListData.get(position).get("content"));
            holder.datetime.setText(ListData.get(position).get("datetime"));
            
                             
            //
            return convertView;
		}

	}
	//
	private List<Map<String,String>> getData()
    {
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Map<String,String> map;
        
        int num_items = Common.list_memo_all.size();
        for (int idx = num_items - 1; idx >=0; idx--)
        {
        	MemoItem curr = Common.list_memo_all.get(idx);
			map = new HashMap<String,String>();

			String [] str_arr = curr.date_update.split(" ");
			str_arr = str_arr[0].split("-");
			String datetime = str_arr[0] + str_arr[1] + str_arr[2];
			
			map.put("content", curr.content_short);
			map.put("datetime", datetime);

			//
			list.add(map);          
		} 
		
        return list;
    }
	//
	
}

