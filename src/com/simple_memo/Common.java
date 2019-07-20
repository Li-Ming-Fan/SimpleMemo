package com.simple_memo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Common
{
	public static String PreBase;
	public static String SubdirBase = "/SimpleMemo";
	public static String SubdirDetail = "/SimpleMemo/details";
	
	public static String PathDirBase = null;
	public static String PathDirDetail = null;
	public static String PathFileMemoAll = null;
	
	//
	public static List<MemoItem> list_memo_all;
	public static MemoItem memo_to_edit;
	public static int posi_in_list_memo_all;
		
	//
	public static void SystemInitialize(String pre_base)
	{
		Common.PreBase = pre_base;
		Common.PathDirBase = Common.PreBase + Common.SubdirBase;
		Common.PathDirDetail = Common.PreBase + Common.SubdirDetail;
		Common.PathFileMemoAll = Common.PathDirBase + "/memo_all.txt";
		//
		File dir = new File(Common.PathDirBase);
		if (!dir.exists()) dir.mkdir();
		//
		dir = new File(Common.PathDirDetail);
		if (!dir.exists()) dir.mkdir();
		//
		File file=new File(Common.PathFileMemoAll);
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException ioe) {  }
		}
		//
		list_memo_all = loadListMemoItemFromFile(Common.PathFileMemoAll);
		memo_to_edit = null;
		//		
	}
	//
	public static void SystemFinish()
	{
		
	}
	//
	public static void writeMemoAllToFile()
	{
		try
		{
			BufferedWriter bw=new BufferedWriter(new FileWriter(Common.PathFileMemoAll));
			
			for (MemoItem item : Common.list_memo_all)
			{				
				bw.write(item.date_update);
				bw.newLine();
				
				bw.write(item.date_create);
				bw.newLine();
				
				bw.write(item.content_short);
				bw.newLine();
			}

			//
			bw.flush();
			bw.close();	
		}
		catch (IOException ioe)
		{
			System.out.println("Error, in writeMemoAllToFile()");
		}
	}
	//
	public static List<MemoItem> loadListMemoItemFromFile(String filepath)
	{
		List<MemoItem> list_loaded = new ArrayList<MemoItem>();
		
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(filepath));
			//
			String line = br.readLine();
			while (line != null)
			{
				line = line.trim();
				if (line.length() == 0) break;
				//
				MemoItem item = new MemoItem();
				item.date_update = line;
				//
				line = br.readLine();
				if (line == null) break;
				line = line.trim();
				if (line.length() == 0) break;
				item.date_create = line;
				//
				line = br.readLine();
				if (line == null) break;
				line = line.trim();
				if (line.length() == 0) break;
				item.content_short = line;
				//
				list_loaded.add(item);
				//				
				
				//
				line = br.readLine();
				//
			}
			
			//
			br.close();	
		}
		catch (IOException ioe)
		{
			System.out.println("Error, in loadFromFile() in Class MemoItem");
		}
		
		
		return list_loaded;		
	}
	//
	public static void removeFile(String filepath)
	{
		File file = new File(filepath);
		if (file.exists()) file.delete();		
	}
	//
	
	//
	public static String getCurrentDateTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA); //设置日期格式
		String curr_datetime = df.format(new Date());

		return curr_datetime;
	}
	//
	
	public static String trimSpecified(String text, String trim_chars)
	{
		int posi_left = 0;
		int posi_right = text.length() - 1;
		
		while (posi_left < posi_right)
		{
			if (trim_chars.contains(text.subSequence(posi_left, posi_left + 1)))
			{
				posi_left++;
			}
			else
			{
				break;
			}
		}
		
		while (posi_right > posi_left)
		{
			if (trim_chars.contains(text.subSequence(posi_right, posi_right + 1)))
			{
				posi_right--;	
			}
			else
			{
				break;
			}
		}
		
		return text.substring(posi_left, posi_right + 1);
	
	}

}
