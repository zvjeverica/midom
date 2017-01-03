package hr.fer.zari.midom.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import hr.fer.zari.midom.R;
import hr.fer.zari.midom.picture.ImageBitmap;
import hr.fer.zari.midom.picture.TouchImageView;
import hr.fer.zari.midom.utils.ImageException;

/**
 * Created by Ana on 2.1.2017..
 */

public class DialogDecompressImage {

    private LayoutInflater layoutInflater;

    private Context context;

    private AlertDialog.Builder builder;

    private AlertDialog alertDialog;

    private View rootView;

    private Button dismissButton;

    private ImageView imageView;

    private TextView textView;

    private File file;


    public DialogDecompressImage(Context context, File file) {
        this.context = context;
        this.file = file;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.builder = new AlertDialog.Builder(this.context);
        this.rootView = this.layoutInflater.inflate(R.layout.image_dialog, null);
        this.imageView = (ImageView)this.rootView.findViewById(R.id.image_dialog_imageview);
        this.textView = (TextView)this.rootView.findViewById(R.id.image_dialog_title);
        this.dismissButton = (Button)this.rootView.findViewById(R.id.image_dialog_dismissbutton);
        this.dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        this.textView.setText(this.file.getName());

        //todo set image to imageview

        //TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.pgmImage);

        if (this.file.getName().toLowerCase().endsWith(".pgm")){
            ImageBitmap bitmap = new ImageBitmap(this.file.getAbsolutePath());
            try {
                bitmap.loadBitmap();
                Drawable draw = new BitmapDrawable(context.getResources(), bitmap.getBitmap());
                this.imageView.setImageDrawable(draw);
            } catch (ImageException e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.wrong_picture_format, Toast.LENGTH_LONG).show();
            }
        }

        if (this.file.getName().toLowerCase().endsWith(".cbp")){
            ImageBitmap bitmap = new ImageBitmap(this.file.getAbsolutePath());
            try {
                bitmap.loadBitmap();
                Drawable draw = new BitmapDrawable(context.getResources(), bitmap.getBitmap());
                this.imageView.setImageDrawable(draw);
            } catch (ImageException e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.wrong_picture_format, Toast.LENGTH_LONG).show();
            }
        }





        //this.imageView.setImageDrawable();
//        if(this.file.getName().contains("pgm")){
//            this.imageView.setImageURI(Uri.fromFile(this.file));
//        }


        this.builder.setView(this.rootView);
        this.alertDialog = this.builder.create();
        this.alertDialog.show();
    }


}
