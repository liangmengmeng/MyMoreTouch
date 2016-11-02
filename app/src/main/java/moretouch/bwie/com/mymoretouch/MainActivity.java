package moretouch.bwie.com.mymoretouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private  SurfaceHolder holder=null;
    private int screenWidth=0;
    private int screenhigh=0;
    private Bitmap bitmap;
    //计算图片的X和Y值
    private int imageX=0;
    private int imageY=0;
    private int imgWidth;
    private int imgHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //获取屏幕的宽和高
        this.screenWidth=super.getWindowManager().getDefaultDisplay().getWidth();
        this.screenhigh=super.getWindowManager().getDefaultDisplay().getHeight();
        //获取图片
        bitmap = BitmapFactory.decodeResource(super.getResources(), R.mipmap.mm23);
        //获取图片的宽和高
        imgWidth = bitmap.getWidth();
        imgHeight = bitmap.getHeight();
        this.imageX=(screenWidth- imgWidth)/2;
        this.imageY=(screenhigh- imgHeight)/2;
        super.setContentView(new MoreTouchView(this));
    }
    public class MoreTouchView extends SurfaceView implements SurfaceHolder.Callback{

        public MoreTouchView(Context context) {
            super(context);
            MainActivity.this.holder=super.getHolder();
            MainActivity.this.holder.addCallback(this);
            //获得焦点 进行触摸事件
            super.setFocusable(true);
        }
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //进行图片的缩放
            MainActivity.this.setImage(1,350,500);
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
    private void setImage(float scale,int width,int height) {
        //获取画布
        Canvas canvas = MainActivity.this.holder.lockCanvas();
        //获取画笔
        Paint paint = new Paint();
        canvas.drawRect(0,0,MainActivity.this.screenWidth,MainActivity.this.screenhigh,paint);
        //定义工具
        Matrix matrix = new Matrix();
        //等量缩放
        matrix.postScale(scale,scale);
        Bitmap target = Bitmap.createBitmap(MainActivity.this.bitmap, 0, 0, width, height, matrix, true);
        this.imgWidth=target.getWidth();
        this.imgHeight=target.getHeight();
        this.imageX=(this.screenWidth-this.imgWidth)/2;
        this.imageY=(this.screenhigh-this.imgHeight)/2;
        //平移到指定位置
        canvas.translate(this.imageX,this.imageY);
        canvas.drawBitmap(this.bitmap,matrix,paint);
        //解锁并提交图像
        MainActivity.this.holder.unlockCanvasAndPost(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        if (pointerCount==2){
            float pointX = event.getX(0);
            float pointY = event.getY(1);
            if (pointX<pointY){
                float temp=pointX;
                pointX=pointY;
                pointY=temp;
            }
            if (!(event.getAction()==MotionEvent.ACTION_UP)){
                float scale=this.getScale(pointX,pointY);
                MainActivity.this.setImage(scale,350,350);

            }

        }
        return true;

    }

    private float getScale(float pointX, float pointY) {
        float scale=pointX/pointY;
        return scale;

    }
}
