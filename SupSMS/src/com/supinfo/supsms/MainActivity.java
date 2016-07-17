package com.supinfo.supsms;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button) findViewById(R.id.login);  
        button.setOnClickListener(myOnClickListener); 
        
        //
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        //
	}

	private OnClickListener myOnClickListener = new OnClickListener() {  
		@Override
		public void onClick(View v) {
			
			try {
				URI uri=new URI("http://91.121.105.200/API/index.php");
			HttpPost request = new HttpPost(); 
			request.setURI(uri);
            List <NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("action","login"));
            params.add(new BasicNameValuePair("login",((EditText)findViewById(R.id.editText1)).getText().toString()));
            params.add(new BasicNameValuePair("password", ((EditText)findViewById(R.id.editText2)).getText().toString()));
            request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));  
			HttpResponse httpResponse = new DefaultHttpClient().execute(request);  
			String retSrc = EntityUtils.toString(httpResponse.getEntity());  
			JSONObject result = new JSONObject( retSrc);  
			/*try {
				URI uri=new URI("http://91.121.105.200/API/index.php");
			HttpPost request = new HttpPost(); 
			request.setURI(uri);
			JSONObject param = new JSONObject();  
				param.put("action","login");
			param.put("login",((EditText)findViewById(R.id.editText1)).getText().toString());  
			param.put("password",((EditText)findViewById(R.id.editText2)).getText().toString());  
			StringEntity se = new StringEntity(param.toString()); 
			request.setEntity(se);   
			HttpResponse httpResponse = new DefaultHttpClient().execute(request);  
		
			new AlertDialog.Builder(MainActivity.this)    
            .setTitle("标题")  
            .setMessage(param.toString())  
            .setPositiveButton("确定", null)  
            .show(); 
			
			String retSrc = EntityUtils.toString(httpResponse.getEntity());  
			*/
			
			
			
			
			/* new AlertDialog.Builder(MainActivity.this)    
             .setTitle("标题")  
             .setMessage(retSrc)  
             .setPositiveButton("确定", null)  
             .show(); 
			*/
			/*
			
			*/
			String success=result.getString("success");
			/*
			 new AlertDialog.Builder(MainActivity.this)    
             .setTitle("标题")  
             .setMessage("zhunbei")  
             .setPositiveButton("确定", null)  
             .show();
             */ 
			if(success.equals("true"))
			{
				String user = result.getString("user");
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,MenuActivity.class);
				intent.putExtra("user", user);
				startActivity(intent);
			}
			else
			{
				
				 new AlertDialog.Builder(MainActivity.this)    
                 .setTitle("Prompt")  
                 .setMessage("No such user or password is wrong!")  
                 .setPositiveButton("OK", null)  
                 .show();
                  
			}
			}catch(Exception e)
			{
				new AlertDialog.Builder(MainActivity.this)    
	             .setTitle("Error")  
	             .setMessage("Something wrong in the system, try again later!")  
	             .setPositiveButton("OK", null)  
	             .show();
			}
			
			
		}         
	   };  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
