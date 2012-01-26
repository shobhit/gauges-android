package com.github.mobile.gauges.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.mobile.gauges.R.layout;
import com.github.mobile.gauges.core.Gauge;
import com.github.mobile.gauges.core.GaugesService;
import com.github.mobile.gauges.core.PageContent;
import com.madgag.android.listviews.ReflectiveHolderFactory;
import com.madgag.android.listviews.ViewHoldingListAdapter;
import com.madgag.android.listviews.ViewInflator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Fragment to load page content information for a {@link Gauge}
 */
public class ContentListFragment extends ListLoadingFragment<PageContent> {

	/**
	 * Create content list fragment
	 */
	public ContentListFragment() {
	}

	public Loader<List<PageContent>> onCreateLoader(int id, Bundle args) {
		return new AsyncLoader<List<PageContent>>(getActivity()) {

			public List<PageContent> loadInBackground() {
				GaugesService service = new GaugesService(null, null);
				try {
					return service.getContent(getArguments().getString(
							"gaugeId"));
				} catch (IOException e) {
					Log.d(getClass().getName(),
							"Exception getting page content", e);
					return Collections.emptyList();
				}
			}
		};
	}

	protected ListAdapter adapterFor(List<PageContent> items) {
		return new ViewHoldingListAdapter<PageContent>(items,
				ViewInflator.viewInflatorFor(getActivity(),
						layout.content_list_item),
				ReflectiveHolderFactory
						.reflectiveFactoryFor(ContentViewHolder.class));
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		String url = ((PageContent) l.getItemAtPosition(position)).getUrl();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
}