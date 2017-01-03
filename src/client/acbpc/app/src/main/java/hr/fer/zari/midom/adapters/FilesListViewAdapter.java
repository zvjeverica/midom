package hr.fer.zari.midom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import hr.fer.zari.midom.R;
import hr.fer.zari.midom.dialogs.DialogDecompressImage;

/**
 * Created by Ana on 2.1.2017..
 */

public class FilesListViewAdapter extends BaseAdapter {

    private List<File> files;
    private Context context;

    public FilesListViewAdapter(List<File> files, Context context){
        this.files = files;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(this.files==null){
            return 0;
        }else{
            return this.files.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return this.files.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        final int position = i;
        TextView textView;
        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (View)layoutInflater.inflate(R.layout.activity_decompress_listitem, null);
        }else{
            v = view;
        }
        textView = (TextView)v.findViewById(R.id.info_text);
        textView.setText(this.files.get(i).getName());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked : "+ files.get(position).getName(), Toast.LENGTH_SHORT).show();
                //DialogDecompressImage dialogDecompressImage = new DialogDecompressImage(context, files.get(position));
            }
        });
        return v;
    }
}
