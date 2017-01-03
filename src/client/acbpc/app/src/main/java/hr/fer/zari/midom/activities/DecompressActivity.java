package hr.fer.zari.midom.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.ListViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zari.midom.R;
import hr.fer.zari.midom.adapters.FilesListViewAdapter;

public class DecompressActivity extends ActionBarActivity {

    private ListView listView;
    private FilesListViewAdapter fileslistViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decompress);
        this.listView = (ListView) DecompressActivity.this.findViewById(R.id.files_list_view);
        /*final LinearLayoutManager layoutManager = new LinearLayoutManager(DecompressActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);*/
        this.fileslistViewAdapter = new FilesListViewAdapter(this.getFiles(), DecompressActivity.this);
        this.listView.setAdapter(this.fileslistViewAdapter);
    }

    private List<File> getFiles(){
        List<File> result = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString()+"/MidomData";
        //Log.d("Files", "Path: " + path);
        File directory = new File(path);
        if (! directory.exists()){
            directory.mkdir();
        }
        File[] files = directory.listFiles();
        //Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            result.add(files[i]);
            //Log.d("Files", "FileName:" + files[i].getName());
        }
        return result;
    }
}
