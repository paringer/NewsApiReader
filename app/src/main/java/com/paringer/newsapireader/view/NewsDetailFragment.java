package com.paringer.newsapireader.view;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.paringer.newsapireader.R;

/**
 * A fragment representing a single News detail screen.
 * This fragment is either contained in a {@link NewsListActivity}
 * in two-pane mode (on tablets) or a {@link NewsDetailActivity}
 * on handsets.
 */
public class NewsDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_TOPIC_LINK = "item_topic_link";
    public static final String ARG_TOPIC_GUID = "item_topic_guid";
    public static final String TAG_NEWS_DETAIL = "TAG_NEWS_DETAIL";

    private String mLink;
    private String mGuid;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsDetailFragment() {
    }

    /**
     * called on creation
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
                if (mLink != null)
                    appBarLayout.setTitle(mLink.replaceAll("^https?\\:\\/\\/", "").replaceAll("\\/.*$", ""));
            }
        }
    }

    /**
     * called on root view creation
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_detail, container, false);

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.news_detail)).setText(mItem.details);
//        }


        return rootView;
    }

    /**
     * called after root view creation
     *
     * @param rootView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        if (getActivity().getIntent().hasExtra(NewsDetailFragment.ARG_TOPIC_LINK)) {
            mLink = getActivity().getIntent().getStringExtra(NewsDetailFragment.ARG_TOPIC_LINK);
        }
        if (getActivity().getIntent().hasExtra(NewsDetailFragment.ARG_TOPIC_GUID)) {
            mGuid = getActivity().getIntent().getStringExtra(NewsDetailFragment.ARG_TOPIC_GUID);
        }
        if (getArguments() != null) {
            mLink = getArguments().getString(NewsDetailFragment.ARG_TOPIC_LINK, null);
            mGuid = getArguments().getString(NewsDetailFragment.ARG_TOPIC_GUID, null);
        }
        WebView webView = (WebView) rootView.findViewById(R.id.newsItemDetailWebView);

//        mWebView.getSettings().setJavaScriptEnabled(false);
        if (webView != null)
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
//                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
        if (mLink != null) {
            webView.loadUrl(mLink);
        } else {
            webView.loadUrl("about:blank");
        }

        Log.d("FRAGMENT DETAIL ://", mLink == null ? "null" : mLink);
        Log.d("FRAGMENT DETAIL WV", webView == null ? "null" : webView.toString());

    }
}
