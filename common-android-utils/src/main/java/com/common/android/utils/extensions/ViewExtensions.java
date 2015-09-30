package common.android.utils.extensions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.adviqo.app.MainActivity;
import com.squareup.picasso.Callback;
import common.android.utils.localization.Localization;
import com.adviqo.app.misc.FontCache;
import common.android.utils.ui.PicassoBigCache;
import de.questico.app.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static common.android.utils.extensions.BitmapExtensions.getBitmap;
import static common.android.utils.extensions.MathExtensions.convertDpToPixel;
import static common.android.utils.extensions.MathExtensions.roundToInt;
import static io.fabric.sdk.android.services.common.CommonUtils.hideKeyboard;

/**
 * Created by Jan Rabe on 24/09/15.
 */
public class ViewExtensions {

    private ViewExtensions() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static boolean isVisible(@NotNull final View view) {
        return view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE;
    }

    public static void toggleViewsVisibilityGone(@NotNull final View... views) {
        for (final View view : views)
            view.setVisibility(view.getVisibility() == View.GONE
                    ? View.VISIBLE
                    : View.GONE);
    }

    public static void toggleViewsVisibility(@NotNull final View... views) {
        for (final View view : views)
            view.setVisibility(view.getVisibility() == View.INVISIBLE
                    ? View.VISIBLE
                    : View.INVISIBLE);
    }


    public static void loadInto(final String imageURL, final ImageView imageView) {
        PicassoBigCache
                .INSTANCE
                .getPicassoBigCache(MainActivity.currentMainActivity())
                .load(imageURL)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    public static void loadIntoWithPlaceholder(final String imageURL, final ImageView imageView) {
        PicassoBigCache
                .INSTANCE
                .getPicassoBigCache(MainActivity.currentMainActivity())
                .load(imageURL)
                .placeholder(R.drawable.expert_image_placeholder)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    public static void loadInto(@DrawableRes final int resourceId, final ImageView imageView) {
        PicassoBigCache
                .INSTANCE
                .getPicassoBigCache(MainActivity.currentMainActivity())
                .load(resourceId).into(imageView);
    }

    public static void loadInto(final String imageUrl, @NotNull final ImageView imageView, @DrawableRes final int placeholder, final Callback callback) {
        PicassoBigCache
                .INSTANCE
                .getPicassoBigCache(MainActivity.currentMainActivity())
                .load(imageUrl).placeholder(placeholder)
                .into(imageView, callback);
    }


    public static void setOneTimeGlobalLayoutListener(@NotNull final View v, @NotNull final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (SDK_INT >= 16)
                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    v.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                onGlobalLayoutListener.onGlobalLayout();

            }
        });
    }

    public static void flipHorizontically(@NotNull final ImageView imageView) {
        imageView.setImageBitmap(BitmapExtensions.flipHorizontically(getBitmap(imageView)));
    }

    public static void setLayoutHeight(@NotNull final View v, final int height) {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.height = height;
        v.setLayoutParams(params);
    }

    public static void setTextSwitcherAnimation(@NotNull final Context context, @NotNull final TextSwitcher v) {
        v.setFactory(new ViewSwitcher.ViewFactory() {

            @NotNull
            @Override
            public View makeView() {
                final LayoutInflater inflater = LayoutInflater.from(context);
                final TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
                textView.setTypeface(FontCache.RosarioBold.getFont());
                return textView;
            }
        });
        final Animation in = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
        final Animation out = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);
        v.setInAnimation(in);
        v.setOutAnimation(out);
    }

    public static void getLocationOnScreen(@Nullable final View view, @Nullable final Rect src, @Nullable Rect dst) {
        if (view == null || src == null)
            return;

        if (dst == null)
            dst = new Rect();

        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        final int offsetX = location[0];
        final int offsetY = location[1];

        dst.set(src.left + offsetX, src.top + offsetY, src.right + offsetX,
                src.bottom + offsetY);
    }


    public static void setFont(@NotNull final Typeface font, @NotNull final ArrayList<View> views) {

        final List<TextView> textViews = new ArrayList<>();
        for (final View view : views) {
            if (view instanceof TextView)
                textViews.add((TextView) view);
        }

        for (final TextView v : textViews)
            v.setTypeface(font);
    }

    public static void showView(@Nullable final View v, final boolean isShown) {
        if (v == null)
            return;

        v.setVisibility(isShown
                ? View.VISIBLE
                : View.GONE);
    }

    public static void showViews(@NotNull final View... views) {
        for (final View view : views)
            view.setVisibility(View.VISIBLE);
    }

    public static void showViews(@NotNull final List<View> views) {
        for (final View view : views)
            view.setVisibility(View.VISIBLE);
    }

    public static void hideViews(@NotNull final View... views) {
        for (final View view : views)
            view.setVisibility(View.INVISIBLE);
    }

    public static void hideViews(@NotNull final List<View> views) {
        for (final View view : views)
            view.setVisibility(View.INVISIBLE);
    }

    public static void hideViewsCompletely(@NotNull final View... views) {
        for (final View view : views)
            view.setVisibility(View.GONE);
    }

    public static void hideViewsCompletely(@NotNull final List<View> views) {
        for (final View view : views)
            view.setVisibility(View.GONE);
    }

    @NotNull
    public static int[] getScreenLocation(@NotNull final View view) {
        final int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        return locations;
    }

    public static void moveViewRelatively(@NotNull final View view, final int left, final int top) {
        final int[] location = getScreenLocation(view);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(left + location[0], top + location[1], 0, 0);
        final ViewGroup.LayoutParams p = view.getLayoutParams();
        params.width = p.width;
        params.height = p.height;
        view.setLayoutParams(params);
    }

    public static void moveView(@NotNull final View view, final int left, final int top) {
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, 0, 0);
        final ViewGroup.LayoutParams p = view.getLayoutParams();
        params.width = p.width;
        params.height = p.height;
        view.setLayoutParams(params);
    }

    public static void setFont(@NotNull final Typeface font, @NotNull final TextView... views) {
        for (final TextView v : views)
            v.setTypeface(font);
    }

    public static void addMargin(@NotNull final View view, final int left, final int top, final int right, final int bottom) {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(left, top, right, bottom);
        view.setLayoutParams(layoutParams);
    }

    @NotNull
    public static Drawable getScaledDrawable(final int resourceId, final int scaleInDp) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        final DisplayMetrics metrics = MainActivity.currentMainActivity().getApplicationContext().getResources().getDisplayMetrics();
        options.inScreenDensity = metrics.densityDpi;
        options.inTargetDensity = metrics.densityDpi;
        options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
        final int px = roundToInt(convertDpToPixel(scaleInDp));
        final Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.currentMainActivity().getResources(), resourceId, options);
        final BitmapDrawable drawable = new BitmapDrawable(MainActivity.currentMainActivity().getResources(), Bitmap.createScaledBitmap(bitmap, px, px, true));
        Log.v("Scaling", "Scaling image [" + scaleInDp + "dp to " + px + "px] => [" + bitmap.getWidth() + "x" + bitmap.getHeight() + " px] to [" + drawable.getIntrinsicWidth() + "x" + drawable.getIntrinsicHeight() + " px]");
        bitmap.recycle();
        return drawable;
    }

    public static View getContentRoot() {
        return MainActivity.currentMainActivity()
                .getWindow()
                .getDecorView()
                .findViewById(android.R.id.content);
    }

    public static void addHtmlContentToTextView(final int resourceId, @NotNull final TextView view) {
        final InputStream inputStream = MainActivity.currentMainActivity().getResources().openRawResource(resourceId);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuffer body = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            reader.close();
        } catch (@NotNull final IOException e) {
        }
        view.setText(Html.fromHtml(body.toString()));
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static Rect getLocationOnScreen(@NotNull final View textView) {
        final Rect rect = new Rect();
        final int[] location = new int[2];

        textView.getLocationOnScreen(location);

        rect.left = location[0];
        rect.top = location[1];
        rect.right = location[0] + textView.getWidth();
        rect.bottom = location[1] + textView.getHeight();

        return rect;
    }

    public static void hideOnLostFocus(@NotNull final Context context, @NotNull final MotionEvent event, @Nullable final View... views) {

        if (views == null)
            return;

        boolean hit = false;

        for (final View view : views)
            hit |= getLocationOnScreen(view).contains((int) event.getX(), (int) event.getY());

        if (event.getAction() == MotionEvent.ACTION_DOWN && !hit)
            hideKeyboard(context, views[0]);
    }

    public static void sendEmail(@NotNull final Context context, final String address, final String subject, final Spanned body, final int requestCode) {
        final Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null));
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            ((Activity) context).startActivityForResult(Intent.createChooser(i, Localization.getString(R.string.kKontaktSharePopupTitle)), requestCode);
        } catch (@NotNull final android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void showMarket(@NotNull final Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        final String packageName = context.getApplicationContext().getPackageName();
        intent.setData(Uri.parse("market://details?id=" + packageName));
        context.startActivity(intent);
    }
}
