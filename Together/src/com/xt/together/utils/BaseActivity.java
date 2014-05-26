package com.xt.together.utils;

import com.xt.together.activity.SendPhotoActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseActivity extends Activity {
	
	public final int CAPTURE_CODE = 1001;
	public final int ALBUM_CODE   = 1002;
	public static Bitmap bitmap;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK) {
			return;
		}
		if(requestCode == CAPTURE_CODE) {
			bitmap = (Bitmap)data.getExtras().get("data");
		} else if(requestCode == ALBUM_CODE) {
			Uri originalUri = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri);
			} catch (Exception e) {
				return;
			}
		}
		
		Intent intent = new Intent(BaseActivity.this,SendPhotoActivity.class);
		startActivity(intent);
	}
	
	public class CameraOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(BaseActivity.this)
				.setTitle("选择图片")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[]{"相机","来自相册"}, 0, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(which == 0) {
									Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(intent, CAPTURE_CODE);
								} else if(which == 1) {
									Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
									intent.setType("image/");
									startActivityForResult(intent, ALBUM_CODE);
								}
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
		}
		
	}
}
