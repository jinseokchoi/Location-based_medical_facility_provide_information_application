package net.daum.android.map.openapi.sampleapp.ListView;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.daum.android.map.openapi.sampleapp.R;

/**
 * Created by EunA on 2016-10-20.
 */
// 아이템으로 보여줄 뷰 정의
public class IconTextView extends LinearLayout {
	private ImageView mIcon;
	private TextView mText01;
	private TextView mText02;
	private TextView mText03;

	public IconTextView(Context context, int tab, IconTextItem aItem) {
		super(context);

		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (tab == 0)		// 별점 페이지
			inflater.inflate(R.layout.tab_starrating_listview_items, this, true);
		else if(tab == 1)	// 예약 페이지
			inflater.inflate(R.layout.tab_reserve_listview_items, this, true);

		// Set Icon
		//mIcon = (ImageView) findViewById(R.id.iconItem);
		//mIcon.setImageDrawable(aItem.getIcon());

		// Set Text 01
		mText01 = (TextView) findViewById(R.id.dataItem01);
		mText01.setText(aItem.getData(0));

		// Set Text 02
		mText02 = (TextView) findViewById(R.id.dataItem02);
		mText02.setText(aItem.getData(1));

		// Set Text 03
		mText03 = (TextView) findViewById(R.id.dataItem03);
		mText03.setText(aItem.getData(2));
	}

	/**
	 * set Text
	 *
	 * @param index
	 * @param data
	 */
	public void setText(int index, String data) {
		if (index == 0) {
			mText01.setText(data);
		} else if (index == 1) {
			mText02.setText(data);
		} else if (index == 2) {
			mText03.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * set Icon
	 *
	 * @param icon
	 */
	public void setIcon(Drawable icon) {
		mIcon.setImageDrawable(icon);
	}

}
