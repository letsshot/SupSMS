package com.supinfo.supsms;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MenuActivity extends Activity {
private JSONObject user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menuactivity);
		Bundle extras = getIntent().getExtras(); 
		String userstring=extras.getString("user");
		try {
			user=new JSONObject(userstring);
		} catch (JSONException e) {
		}
		Button button1 = (Button) findViewById(R.id.sent);  
        button1.setOnClickListener(myOnClickListener1);
		Button button2 = (Button) findViewById(R.id.inbox);  
        button2.setOnClickListener(myOnClickListener2); 
        Button button3 = (Button) findViewById(R.id.contacts);  
        button3.setOnClickListener(myOnClickListener3); 
        Button button4 = (Button) findViewById(R.id.logout);  
        button4.setOnClickListener(myOnClickListener4); 
        
        //
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        //
	}

	   private OnClickListener myOnClickListener1 = new OnClickListener() {  
		@Override
		public void onClick(View v) {
			JSONArray SMS=new JSONArray();
			ContentResolver resolver = getBaseContext().getContentResolver();
			Cursor cursor = resolver.query(Uri.parse("content://sms/sent"), new String[] {"body","date","_id","address","thread_id"},null, null, null );
			if(cursor.moveToFirst()) {  
				int bodyIdx = cursor.getColumnIndex("body"); 
				int dateIdx = cursor.getColumnIndex("date"); 
				int _idIdx = cursor.getColumnIndex("_id");  
	            int addrIdx = cursor.getColumnIndex("address");  
	            int thread_idIdx = cursor.getColumnIndex("thread_id");  
	            do {  
	            	String body= cursor.getString(bodyIdx);
	                String date = cursor.getString(dateIdx);    
	                String _id = cursor.getString(_idIdx);
	                String address=cursor.getString(addrIdx);
	                String thread_id=cursor.getString(thread_idIdx);
	                try{
	                JSONObject item=new JSONObject();
	                item.put("body",Base64.encodeToString(body.getBytes(),0));
	                item.put("box", "sent");
	                item.put("date",date);
	                item.put("_id", _id);
	                item.put("address", address);
	                item.put("thread_id", thread_id);
	                SMS.put(item);
	                }catch(Exception e)
	                {  	
	                }
	            } while(cursor.moveToNext());  
	            cursor.close();
	        } 		
			try {
			URI uri=new URI("http://91.121.105.200/API/index.php");
			HttpPost request = new HttpPost(); 
			request.setURI(uri);
            List <NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("action","backupsms"));
            params.add(new BasicNameValuePair("login",user.getString("username")));
            params.add(new BasicNameValuePair("password", user.getString("password")));
            params.add(new BasicNameValuePair("box","sent"));
            params.add(new BasicNameValuePair("sms",SMS.toString()));
            request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));  
			HttpResponse httpResponse = new DefaultHttpClient().execute(request);  
			String retSrc = EntityUtils.toString(httpResponse.getEntity());  
			JSONObject result = new JSONObject( retSrc);  
			if(result.getString("success").equals("true"))
			{
				new AlertDialog.Builder(MenuActivity.this)    
	            .setTitle("Prompt")  
	            .setMessage("Success")  
	            .setPositiveButton("OK", null)  
	            .show(); 	
			}
			else
			{
				new AlertDialog.Builder(MenuActivity.this)    
	            .setTitle("Prompt")  
	            .setMessage("Failed")  
	            .setPositiveButton("OK", null)  
	            .show(); 	
			}
			}catch(Exception e)
			{
			}
		}         
	   }; 
	   private OnClickListener myOnClickListener2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			JSONArray SMS=new JSONArray();
			ContentResolver resolver = getBaseContext().getContentResolver();
			Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"), new String[] {"body","date","_id","address","thread_id"},null, null, null );
			if(cursor.moveToFirst()) {  
				int bodyIdx = cursor.getColumnIndex("body"); 
				int dateIdx = cursor.getColumnIndex("date"); 
				int _idIdx = cursor.getColumnIndex("_id");  
	            int addrIdx = cursor.getColumnIndex("address");  
	            int thread_idIdx = cursor.getColumnIndex("thread_id");  
	            do {  
	            	String body= cursor.getString(bodyIdx);
	                String date = cursor.getString(dateIdx);    
	                String _id = cursor.getString(_idIdx);
	                String address=cursor.getString(addrIdx);
	                String thread_id=cursor.getString(thread_idIdx);
	                try{
	                JSONObject item=new JSONObject();
	                item.put("body",Base64.encodeToString(body.getBytes(),0));
	                item.put("box", "inbox");
	                item.put("date",date);
	                item.put("_id", _id);
	                item.put("address", address);
	                item.put("thread_id", thread_id);
	                
	                SMS.put(item);
	                }catch(Exception e)
	                {  	
	                }
	            } while(cursor.moveToNext());  
	            cursor.close();
	        } 
			try {
			URI uri=new URI("http://91.121.105.200/API/index.php");
			HttpPost request = new HttpPost(); 
			request.setURI(uri);
            List <NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("action","backupsms"));
            params.add(new BasicNameValuePair("login",user.getString("username")));
            params.add(new BasicNameValuePair("password", user.getString("password")));
            params.add(new BasicNameValuePair("box","inbox"));
            params.add(new BasicNameValuePair("sms",SMS.toString()));
            request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));  
			HttpResponse httpResponse = new DefaultHttpClient().execute(request);  
			String retSrc = EntityUtils.toString(httpResponse.getEntity());  
			JSONObject result = new JSONObject( retSrc);  
			if(result.getString("success").equals("true"))
			{
				new AlertDialog.Builder(MenuActivity.this)    
	            .setTitle("Prompt")  
	            .setMessage("Success")  
	            .setPositiveButton("OK", null)  
	            .show(); 	
			}
			else
			{
				new AlertDialog.Builder(MenuActivity.this)    
	            .setTitle("Prompt")  
	            .setMessage("Failed")  
	            .setPositiveButton("OK", null)  
	            .show(); 	
			}
			}catch(Exception e)
			{
				new AlertDialog.Builder(MenuActivity.this)    
	            .setTitle("Prompt")  
	            .setMessage(e.toString())  
	            .setPositiveButton("OK", null)  
	            .show(); 	
			}
			
		}         
	   };  
	   private OnClickListener myOnClickListener3 = new OnClickListener() {  
			@Override
			public void onClick(View v) {	
				JSONArray contacts=new JSONArray();
				Uri uri = Uri.parse("content://com.android.contacts/contacts");
				 ContentResolver resolver = getBaseContext().getContentResolver();
			     Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
			        while (cursor.moveToNext()) {
			            JSONObject item=new JSONObject();
			        	int contractID = cursor.getInt(0);
			        	try {
							item.put("_ID", contractID);
						} catch (JSONException e) {
						}
			            uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
			            Cursor cursor1 = resolver.query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
			            while (cursor1.moveToNext()) {
			                String data1 = cursor1.getString(cursor1.getColumnIndex("data1"));
			                String mimeType = cursor1.getString(cursor1.getColumnIndex("mimetype"));
			                if ("vnd.android.cursor.item/name".equals(mimeType)) { //是姓名
			                    try {
									item.put("DNAME", data1);
								} catch (JSONException e) {
								}
			                } else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) { //邮箱
			                	try {
									item.put("EMAIL", data1);
								} catch (JSONException e) {
								}
			                } else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { //手机
			                	try {
									item.put("PNUM", data1);
								} catch (JSONException e) {
								}
			                }                
			            }
			            cursor1.close();
			            contacts.put(item);
			        }
			        cursor.close();
			        try {
						URI u=new URI("http://91.121.105.200/API/index.php");
						HttpPost request = new HttpPost(); 
						request.setURI(u);
			            List <NameValuePair> params=new ArrayList<NameValuePair>();
			            params.add(new BasicNameValuePair("action","backupcontacts"));
			            params.add(new BasicNameValuePair("login",user.getString("username")));
			            params.add(new BasicNameValuePair("password", user.getString("password")));
			            params.add(new BasicNameValuePair("contacts",contacts.toString()));
			            request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));  
						HttpResponse httpResponse = new DefaultHttpClient().execute(request);  
						String retSrc = EntityUtils.toString(httpResponse.getEntity());  
						JSONObject result = new JSONObject( retSrc);  
						if(result.getString("success").equals("true"))
						{
							new AlertDialog.Builder(MenuActivity.this)    
				            .setTitle("Prompt")  
				            .setMessage("Success")  
				            .setPositiveButton("OK", null)  
				            .show(); 	
						}
						else
						{
							new AlertDialog.Builder(MenuActivity.this)    
				            .setTitle("Prompt")  
				            .setMessage("Failed")  
				            .setPositiveButton("OK", null)  
				            .show(); 	
						}
						}catch(Exception e)
						{
							new AlertDialog.Builder(MenuActivity.this)    
				            .setTitle("Prompt")  
				            .setMessage(e.toString())  
				            .setPositiveButton("OK", null)  
				            .show(); 	
						}
						 
			        
			}         
		   }; 
       private OnClickListener myOnClickListener4 = new OnClickListener() {  
				@Override
				public void onClick(View v) {	
					Intent intent = new Intent();
					intent.setClass(MenuActivity.this,MainActivity.class);
					startActivity(intent);
				}         
			   }; 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
