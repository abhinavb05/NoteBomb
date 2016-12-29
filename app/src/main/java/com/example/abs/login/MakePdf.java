package com.example.abs.login;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MakePdf extends AppCompatActivity {
    long pname;
    pdf p = new pdf();
    String [] IMAGES;
    ImageView pdfic;
    Button upld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pdf);
        pname = System.currentTimeMillis();
        pdfic = (ImageView)findViewById(R.id.pdfic);
        upld=(Button)findViewById(R.id.upload);
        p.execute();
        Toast.makeText(MakePdf.this, Environment.getExternalStorageDirectory().getAbsolutePath()+"/NoteBombPdf/"+pname+".pdf",Toast.LENGTH_LONG).show();
        upld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MakePdf.this,pdfupload.class);
                startActivity(i);
                finish();
            }
        });
        pdfic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/NoteBombPdf/"+pname+".pdf");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }
    public class pdf extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            File dir = new File(Environment.getExternalStorageDirectory()+"/NoteBomb");
            if (dir.isDirectory())
            {
                IMAGES = dir.list();
                Arrays.sort(IMAGES);
                for (int i = 0; i < IMAGES.length; i++)
                {
                    IMAGES[i]="/sdcard/NoteBomb/"+IMAGES[i];
                }
                try {
                    createPdf("/sdcard/NoteBombPdf/"+pname+".pdf");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        Image img = Image.getInstance(IMAGES[0]);
        Document document = new Document(img);
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        for (final String image : IMAGES) {
            if (!getFileExt(image).equals("pdf")) {
                img = Image.getInstance(image);
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / img.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                img.scalePercent(scaler);
                img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                document.add(img);
            }
        }
        document.close();
    }
    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < IMAGES.length; i++)
        {
            if (!getFileExt(IMAGES[i]).equals("pdf"))
            {
                File file = new File(IMAGES[i]);
                boolean deleted = file.delete();
            }
        }
        Arrays.fill(IMAGES, null);
    }
}
