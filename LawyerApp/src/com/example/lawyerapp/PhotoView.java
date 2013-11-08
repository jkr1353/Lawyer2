package com.example.lawyerapp;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoView extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.viewphoto);
        
        String path = this.getIntent().getExtras().getString("path");
        String name = this.getIntent().getExtras().getString("name");
        
        File imgFile = new File(path);
        
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            TextView myText = (TextView) findViewById(R.id.imgName);
            myText.setText(name);
            
            ImageView myImage = (ImageView) findViewById(R.id.imgView);
            myImage.setImageBitmap(myBitmap);

        }
	}

}
