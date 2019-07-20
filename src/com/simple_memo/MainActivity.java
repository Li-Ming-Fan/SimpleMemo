package com.simple_memo;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends FragmentActivity
{	
	//
	protected void onPause()
	{
		Common.writeMemoAllToFile();
		Common.SystemFinish();
		//
		super.onPause();
	}  
	public void onResume()
	{
		super.onResume();	    		
	}
	//
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  // no ActionBar
		setContentView(R.layout.activity_main);
		//
		
		// set variables
		File sd = Environment.getExternalStorageDirectory();
		String PreBase = sd.getPath();
		//
		Common.SystemInitialize(PreBase);
		//
		// load data
		//
		// this.loadData();
		//

		//
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		FragmentList fragment = new FragmentList();
		fragmentTransaction.replace(R.id.id_content, fragment);
		fragmentTransaction.commit();

	}//onCreate
	//
	
	//
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    //
    // 处理返回键
    // private long exitTime = 0;
 	public boolean onKeyDown(int keyCode, KeyEvent event)
 	{
 		if (keyCode == KeyEvent.KEYCODE_BACK)
 		{
 			processKeyEventBack();
 			return false;
 		}
 		else if (keyCode == KeyEvent.KEYCODE_MENU)
 		{
 			return true;  // 表示需要处理
 		}
 		//
 		return super.onKeyDown(keyCode, event);
 	}
 	//
 	public void processKeyEventBack()
    {
 		FragmentManager fragmentManager = getSupportFragmentManager();
 		Fragment current = fragmentManager.findFragmentById(R.id.id_content);
 		//
 		if (current != null && current instanceof FragmentEdit)
 		{
 			boolean flag_save = ((FragmentEdit) current).saveTextEdited();
 			//
 			if (flag_save)
 			{
 				Common.writeMemoAllToFile();
 				Common.SystemFinish();
 			}
 	 		//
 		}
 		// 		
		fragmentManager.popBackStack();
		//
    }
	//
 	
}
