package com.paringer.newsapireader.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Update;
import com.paringer.newsapireader.R;
import com.paringer.newsapireader.model.dao.ArticleTopic;
import com.paringer.newsapireader.view.interfaces.AdapterClickListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Zhenya on 02.06.2017.
 */


/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    private final List<ArticleTopic> mDataSet;

    WeakReference<Context> context;
    AdapterClickListener<ArticleTopic> onClickListener;
    private volatile boolean favoritesOnly;

    public void setOnClickListener(AdapterClickListener<ArticleTopic> onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setFavoritesOnly(boolean favoritesOnly) {
        this.favoritesOnly = favoritesOnly;
    }

    public boolean getFavoritesOnly() {
        return favoritesOnly;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.textView3date)
        TextView textView3date;
        @BindView(R.id.textView4Agency)
        TextView textView4Agency;
        @BindView(R.id.textView2text)
        TextView textView2text;
        @BindView(R.id.imageIconUrl)
        ImageView imageIconUrl;
        @BindView(R.id.textView5Author)
        TextView textView5Author;
        @BindView(R.id.textView1title)
        TextView textView1title;
        @BindView(R.id.imageView8Favor)
        ImageView imageView8Favor;
        private Unbinder unbinder;
        @OnClick(R.id.imageView8Favor)
        public void onClickFavorite(View view) {
            ArticleTopic itemData = mDataSet.get(getAdapterPosition());
            toggleFavorites(view, itemData);
//            MyAdapter.this.notifyItemChanged(getAdapterPosition());
        }


        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
//            v.setOnClickListener(this);
//            imageView8Favor.setOnClickListener(favoriteListener);
            unbinder = ButterKnife.bind(this, v);
        }

        @Override
        protected void finalize(){
            if (unbinder != null) unbinder.unbind();
        }

        @OnClick
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
            AdapterClickListener<ArticleTopic> listener = onClickListener;
            if (listener != null) listener.onItemClick(  getAdapterPosition(), mDataSet.get(getAdapterPosition()), ViewHolder.this);
        }

        public void toggleFavorites(View view, ArticleTopic itemData) {
            itemData.setFavorite(!itemData.getFavorite());
            new Update(ArticleTopic.class).set("favorite = ?", itemData.getFavorite()?1:0).where("url = ?", itemData.getUrl()).execute();
            setupFavoriteStarIcon(itemData);
            view.invalidate();
        }

        public void setText(ArticleTopic articleTopic, Context context) {
            if(favoritesOnly && !articleTopic.getFavorite()){itemView.setVisibility(View.GONE);}
            else itemView.setVisibility(View.VISIBLE);
            textView1title.setText(articleTopic.getTitle());
            textView2text.setText(articleTopic.getDescription());
            textView3date.setText(articleTopic.getPublishedAt());
            textView4Agency.setText(articleTopic.getSource());
            textView5Author.setText(articleTopic.getAuthor());
            Picasso.with(context).load(articleTopic.getUrlToImage()).into(imageIconUrl);
            setupFavoriteStarIcon(articleTopic);
        }

        private void setupFavoriteStarIcon(ArticleTopic articleTopic) {
            if(articleTopic.getFavorite())
                {imageView8Favor.setImageResource(android.R.drawable.btn_star_big_on);}
            else
                {imageView8Favor.setImageResource(android.R.drawable.btn_star_big_off);}
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public MyAdapter(List<ArticleTopic> dataSet, Context context) {
        mDataSet = dataSet;
        this.context = new WeakReference<Context>(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.article_topic_item, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.setText(mDataSet.get(position), context.get());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
//        return 0;
    }

    public void initSwipeToDismiss(final RecyclerView recyclerView) {
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter
                int position = viewHolder.getAdapterPosition();
                MyAdapter adapter = (MyAdapter) recyclerView.getAdapter();
                ((ViewHolder)viewHolder).toggleFavorites(viewHolder.itemView, adapter.getDataAt(position));
                viewHolder.setIsRecyclable(true);
                if(adapter.getFavoritesOnly()) adapter.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void remove(int position) {
        mDataSet.remove(position);
    }
    public ArticleTopic getDataAt(int position) {return mDataSet.get(position);}
}
