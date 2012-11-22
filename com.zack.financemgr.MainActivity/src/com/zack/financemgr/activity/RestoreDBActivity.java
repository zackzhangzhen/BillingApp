package com.zack.financemgr.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.zack.data.adapter.DBArrayAdapter;
import com.zack.persistent.entity.DBEntry;
import com.zack.utilities.Utilities;

public class RestoreDBActivity extends BaseActivity {
	

	
	private List<DBEntry> entries;
	private ListView dbListView;
	
	private Button backBtn;
	
	private void initDBList()
	{
		File dbBackupDir = Utilities.getDBBackupDir();
		
		File[] files = dbBackupDir.listFiles();
		
		if(files != null && files.length > 0)
		{
			entries = new ArrayList<DBEntry>();
			for(File f : files)
			{
				entries.add(new DBEntry(f.getName(), f.getAbsolutePath()));
			}
		}
	}
	
	private void initComponents()
	{
		backBtn = (Button)this.findViewById(R.id.backBtn);		
		this.addListenerOnButton(backBtn, this, MainActivity.class);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_db);
        
        initDBList();
        initComponents();
        
        if(Utilities.isNullOrEmptyList(entries))
        {
        	return;
        }

        DBArrayAdapter adapter = new DBArrayAdapter(this, R.layout.db_entry, entries);

        dbListView = (ListView)findViewById(R.id.dbListView);       
        dbListView.setSelector(R.drawable.list_selector);
        dbListView.setAdapter(adapter);
        
    }  
}
