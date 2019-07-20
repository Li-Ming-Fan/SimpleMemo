package com.simple_memo;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

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
    private long exitTime = 0;
 	public boolean onKeyDown(int keyCode, KeyEvent event)
 	{
 		if (keyCode == KeyEvent.KEYCODE_BACK)
 		{
 			backToHome();
 			return false;
 		}
 		else if (keyCode == KeyEvent.KEYCODE_MENU)
 		{
 			return true;  // 表示处理过了
 		}
 		//
 		return super.onKeyDown(keyCode, event);
 	}
 	//
 	public void backToHome()
    {
    	if ((System.currentTimeMillis() - exitTime) > 2000)
    	{
    		Toast.makeText(getApplicationContext(),
    				"再按一次返回桌面",
    				Toast.LENGTH_SHORT).show();
    		exitTime = System.currentTimeMillis();
    	}
    	else
    	{
    		//
     		Common.writeMemoAllToFile();
     		Common.SystemFinish();
     		//
     		// 返回home
     		Intent home = new Intent(Intent.ACTION_MAIN);
     		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            //
    		//finish();
    		//System.exit(0);
    		//
    	}
    }
	//
 	
}
