package com.simple_memo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MemoItem
{
	String date_create;
	String date_update;
	String content_full;
	String content_short;
	
	Boolean flag_new;
	
	
	public MemoItem()
	{
		date_create = "";
		date_update = "";
		content_full = "\n\n\n\n\n\n\n\n\n\n";
		content_short = "";
		
		flag_new = true;
	}
	
	//
	public static String getShortFromFull(String str_full)
	{
		String str_short;
		
		if (str_full.length() < 15)
		{
			str_short = str_full;
		}
		else
		{
			str_short = str_full.substring(0, 13) + "...";
		}
		str_short = str_short.replaceAll("[\r\n]", " ");
		
		return str_short;
	}
	//
	public String getFilenameForDetails()
	{
		String filename = date_create.replace(' ', '-').replace(':', '-');
		return filename + ".txt";
	}
	//
	
	//
	public void writeToFile(String filepath)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
			
			bw.write("memo_item_begin:");
			bw.newLine();
			
			bw.write(this.date_update);
			bw.newLine();
			
			bw.write(this.date_create);
			bw.newLine();
			
			bw.write(this.content_short);
			bw.newLine();
			
			bw.write(this.content_full);
			bw.newLine();
			
			bw.write("memo_item_end");
			bw.newLine();

			//
			bw.flush();
			bw.close();	
		}
		catch (IOException ioe)
		{
			System.out.println("Error, in writeToFile() in Class MemoItem");
		}
		
	}
	//
	public void loadFromFile(String filepath)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(filepath));
			//
			String line = br.readLine(); // item_begin
			//
			line = br.readLine();
			this.date_update = line.trim();
			//
			line = br.readLine();
			this.date_create = line.trim();
			//
			line = br.readLine();
			this.content_short = line.trim();
			//
			
			//
			StringBuilder str_full = new StringBuilder();
			//
			while ((line = br.readLine()) != null)
			{
				if (line.trim().equals("memo_item_end")) break;
				//
				str_full.append(line);
				str_full.append("\n");				
			}
			this.content_full = Common.trimSpecified(str_full.toString(), "\n\r ");
			//
			
			/*
			 * 如果写成：
			 * line = br.readLine();
			 * while (line != null)
			 * {
			 *    //
			 *    line = br.readLine();
			 * }
			 * 在加载的时候，APP会崩溃。
			 * 
			 */
			
			//
			br.close();	
		}
		catch (IOException ioe)
		{
			System.out.println("Error, in loadFromFile() in Class MemoItem");
		}
		
	}
	//

}
