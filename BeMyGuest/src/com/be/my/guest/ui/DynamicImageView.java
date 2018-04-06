package com.be.my.guest.ui;

import com.be.my.guest.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by nine3_marks on 3/27/2015.
 */
public class DynamicImageView extends ImageView {

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadRoundedImage(String imageUrl) {
        Picasso.with(getContext()
                .getApplicationContext())
                .load(imageUrl)
                .fit().centerCrop()
                .placeholder(R.drawable.avatar_edge)
                .transform(new RoundedTransformation(20, 0)).into(this);
    }

    public void loadImage(String imageUrl) {
        Picasso.with(getContext()
                .getApplicationContext())
                .load(imageUrl)
                .fit().centerCrop().into(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public class RoundedTransformation implements
            com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin; // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = 200;//R.dimen.avatar_size
            this.margin = 0;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP));

            try {
                Bitmap output = Bitmap.createBitmap(source.getWidth(),
                        source.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
                        - margin, source.getHeight() - margin), radius, radius, paint);

                if (source != output) {
                    source.recycle();
                }

                return output;
            } catch (Exception e) {
                e.printStackTrace();
                return source;
            }
        }

        @Override
        public String key() {
            return "rounded";
        }
    }
}
