
package com.simple_memo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentEdit extends Fragment
{
	TextView tv_back_symbol, tv_back, tv_datetime;
	EditText edit_text;
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        //
        if (! Common.memo_to_edit.flag_new)
        {
        	String filepath = Common.PathDirDetail + "/" + Common.memo_to_edit.getFilenameForDetails();
    		Common.memo_to_edit.loadFromFile(filepath);
        }
        //
        
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
    	View mView = inflater.inflate(R.layout.fragment_edit, container, false);
    	
    	tv_back_symbol = (TextView) mView.findViewById(R.id.edit_tv_back_symbol);
    	tv_back_symbol.setText("<");
    	// tv_back_symbol.setOnClickListener(new ClickEvent());
    	
    	tv_back = (TextView) mView.findViewById(R.id.edit_tv_back);
    	tv_back.setText("SimpleMemo");
    	tv_back.setOnClickListener(new ClickEvent());
    	    	
    	tv_datetime = (TextView) mView.findViewById(R.id.edit_tv_datetime);
    	tv_datetime.setText(Common.memo_to_edit.date_update);
    		
		//
    	edit_text = (EditText) mView.findViewById(R.id.edit_edittext);
    	edit_text.setText(Common.memo_to_edit.content_full + "\n\n\n\n\n\n\n\n\n\n");
    	

        return mView;
        
    }

    
    //
	class ClickEvent implements View.OnClickListener
	{
		 public void onClick(View v)
		 {
			 if (v == tv_back)
			 {
				 saveTextEdited();
				 
				 //
				 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				 fragmentManager.popBackStack();
				 //			 
			 }
		 }// onClick
	}
	//ClickEvent
	
	//
	public boolean saveTextEdited()
	{
		String str_content = Common.trimSpecified(edit_text.getText().toString(), "\n\r ");
		String str_full = Common.trimSpecified(Common.memo_to_edit.content_full, "\n\r ");
		 
		if (! str_full.equals(str_content))
		{
			Common.memo_to_edit.content_full = str_content;
			Common.memo_to_edit.content_short = MemoItem.getShortFromFull(str_content);
			Common.memo_to_edit.date_update = Common.getCurrentDateTime();				 
			String filepath = Common.PathDirDetail + "/" + Common.memo_to_edit.getFilenameForDetails();
			
			if (! Common.memo_to_edit.flag_new)
			{
				Common.list_memo_all.remove(Common.posi_in_list_memo_all);
			
			    if (str_content.length() > 0)
			    {
			    	Common.list_memo_all.add(Common.memo_to_edit);							 
					Common.memo_to_edit.writeToFile(filepath);
				}
				else
				{
					Common.removeFile(filepath);
			    }
			}
			else if (str_content.length() > 0)
			{
				Common.list_memo_all.add(Common.memo_to_edit);
				Common.memo_to_edit.writeToFile(filepath);
			}
			
			// not same, saved
			return true;
		}
		
		// same, need not to save
		return false;
	}
	//
	
}

