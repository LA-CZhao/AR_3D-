package com.xingyun.surfaceviewdemo1;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private SurfaceView surfaceView = null;
	private SurfaceHolder holder1 = null;
	private Canvas canvas = null;
	private Camera cam = null;
	private boolean previewRunning = true;
	private ImageView iv;
	private ImageView iv2;
	
	 /** 传感器管理器 */  
    private SensorManager manager;  
  
    private SensorListener listener = new SensorListener();  


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 //请求窗口特性：无标题  
//        requestWindowFeature(Window.FEATURE_NO_TITLE);  
//        //添加窗口特性：全屏  
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_main);
		
		surfaceView = (SurfaceView) findViewById(R.id.surface1);
		
		holder1 = surfaceView.getHolder();
		
		holder1.addCallback(new MySurfaceViewCallback());
		
		iv = (ImageView) findViewById(R.id.imageView);
		
		iv.setImageResource(R.drawable.circle);
		
		iv2 = (ImageView) findViewById(R.id.imageView2);
		
		iv2.setImageResource(R.drawable.ar_drawable_compass);
		
		iv2.setKeepScreenOn(true);//屏幕高亮
		manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);  
		
	}

	
	private class MySurfaceViewCallback implements SurfaceHolder.Callback{

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			cam = Camera.open(); // 取得第一个摄像头
			
//			cam.setDisplayOrientation(90); // 纠正摄像头自动旋转，纠正角度，如果引用，则摄像角度偏差90度
			
			try {
				cam.setPreviewDisplay(holder);
			} catch (IOException e) {
			}

			cam.startPreview(); // 进行预览
			previewRunning = true; // 已经开始预览
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (cam != null) {
				if (previewRunning) {
					cam.stopPreview(); // 停止预览
					previewRunning = false;
				}
				cam.release();
			}
			
		}
		
	}
	
	
	
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
	      /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
		
		/** 
         *  获取方向传感器 
         *  通过SensorManager对象获取相应的Sensor类型的对象 
         */  
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);  
        //应用在前台时候注册监听器  
        manager.registerListener(listener, sensor,  
                SensorManager.SENSOR_DELAY_GAME);  
        
  
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		//应用不在前台时候销毁掉监听器  
        manager.unregisterListener(listener);  
		super.onPause();
	}
	
	
	
	 private final class SensorListener implements SensorEventListener {  
		  
	        private float predegree = 0;  
	  
	        @Override  
	        public void onSensorChanged(SensorEvent event) {  
	        	 iv2.setRotationX((-event.values[1]));
		         iv2.setRotationY(event.values[2]);
	            /** 
	             *  values[0]: x-axis 方向加速度 
	            　　 values[1]: y-axis 方向加速度 
	            　　 values[2]: z-axis 方向加速度 
	             */  
	            float degree = event.values[0];// 存放了方向值  
	            /**动画效果*/  
	            RotateAnimation animation = new RotateAnimation(predegree, degree,  
	                    Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
	            animation.setDuration(200);  
	            iv2.startAnimation(animation);  
	            predegree=-degree;  
	     
	        }  
	  
	        @Override  
	        public void onAccuracyChanged(Sensor sensor, int accuracy) {  
	  
	        }  
	  
	 }   
	 
	
}
