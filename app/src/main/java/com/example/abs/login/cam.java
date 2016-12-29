package com.example.abs.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class cam extends AppCompatActivity {
    ArrayList<String> images;
    TextView take;
    TextView get;
    TextView finish;
    GridView gridview;
    ImageAdapter imageAdapter;
    String gad;
    File file= new File("/sdcard/NoteBomb/");
    int PICK_IMAGE_REQUEST = 2;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        }
        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
        }
        images = new ArrayList<String>();
        imageAdapter =new ImageAdapter(this, images);
        gridview=(GridView)findViewById(R.id.grid);
        gridview.setAdapter(imageAdapter);
        take=(TextView)findViewById(R.id.take);
        get=(TextView)findViewById(R.id.select);
        finish=(TextView)findViewById(R.id.finish);
        try{
            if(file.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            if(new File("/sdcard/NoteBombPdf/").mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String add="/sdcard/NoteBomb/"+System.currentTimeMillis()+".jpg";
                Uri uriSavedImage=Uri.fromFile(new File(add));
                images.add(add);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(takePictureIntent,1);
            }
        });
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cam.this, MakePdf.class);
                startActivity(i);
                finish();
            }
        });
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                //Toast.makeText(MainActivity.this,"Pos:"+ images.get(position),Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(cam.this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Do you really want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                File file = new File(images.get(position));
                                boolean deleted = file.delete();
                                images.remove(position);
                                gridview.setAdapter(imageAdapter);
                                Toast.makeText(cam.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String path = selectedImage.getPath();
            String fname = null;
            String fp = null;
            for (String retval: path.split(":")) {
                path=retval;
            }
            path="/sdcard/"+path;
            for (String retval: path.split("/")) {
                fname=retval;
            }
            path=path.replace(fname,"");
            gad=(copyFile(path,fname,"/sdcard/NoteBomb/"));
            //Toast.makeText(MainActivity.this,""+gad,Toast.LENGTH_SHORT).show();
            images.add(gad);
            gridview.setAdapter(imageAdapter);
        }

    }
    private String copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        File from = new File(outputPath+inputFile);
        File to = new File(outputPath+System.currentTimeMillis()+".jpg");
        from.renameTo(to);
        return (to.getAbsolutePath());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class ImageAdapter extends BaseAdapter {
        private static final int PADDING = 8;
        private static final int WIDTH = 250;
        private static final int HEIGHT = 250;
        private Context mContext;
        private List<String> mThumbIds;

        public ImageAdapter(Context c, List<String> ids){
            mContext = c;
            this.mThumbIds = ids;
        }

        @Override
        public int getCount() {
            return mThumbIds.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView = (ImageView) convertView;

            if (imageView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
                imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mThumbIds.get(position), options);

            options.inSampleSize = 4;


            options.inJustDecodeBounds = false;
            Bitmap myBitmap = BitmapFactory.decodeFile(mThumbIds.get(position), options);

            imageView.setImageBitmap(myBitmap);

            return imageView;
        }
    }
}
