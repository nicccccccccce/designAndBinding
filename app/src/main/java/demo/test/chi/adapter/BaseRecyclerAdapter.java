package demo.test.chi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import demo.test.chi.BaseApplication;
import demo.test.chi.test.R;

/**
 * Created by eve on 2015/8/24.
 */
public class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    ActivityOptionsCompat Option;
    //    private static final int TYPE_FOOTER = 1;
    Context context;
    LayoutInflater Inflater;
    private List<String> lists;

    public BaseRecyclerAdapter(Context context, List<String> lists) {
        this.context = context;
        this.lists = lists;
        Inflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View item = Inflater.inflate(R.layout.item, null);
            item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ItemViewHolder(item);
        }
//        else if(viewType==TYPE_FOOTER){
//            View   foot =Inflater.inflate(R.layout.foot, null);
//            foot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT));
//            return new FootViewHolder(foot);
//        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.tv.setText(lists.get(position) + "");
            String url = "";

            if (position % 2 == 0) {
                url = "http://pic4.nipic.com/20090803/2618170_095921092_2.jpg";
            } else {
                url = "http://pic2.ooopic.com/01/26/61/83bOOOPIC72.jpg";
            }
            ImageLoader.getInstance().displayImage(url, itemHolder.imageView
                    , BaseApplication.getInstance().getOptions(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    Palette palette = Palette.from(bitmap).generate();
                    if (palette.getDarkVibrantSwatch() != null) {
                        itemHolder.cardview.setCardBackgroundColor(palette.getDarkVibrantSwatch().getRgb());
                    }
                    Option = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, bitmap, 0, 0);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else
//        {
        return TYPE_ITEM;
//        }
//        return super.getItemViewType(position);
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_item)
        TextView tv;
        @InjectView(R.id.imageview)
        ImageView imageView;
        @InjectView(R.id.cardview)
        CardView cardview;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
