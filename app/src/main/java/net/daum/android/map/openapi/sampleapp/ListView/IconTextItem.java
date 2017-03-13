package net.daum.android.map.openapi.sampleapp.ListView;

import android.graphics.drawable.Drawable;

/**
 * Created by EunA on 2016-10-20.
 */
// 데이터를 담고 있을 아이템 정의
public class IconTextItem {
	private Drawable mIcon;
	private String[] mData;
	private boolean mSelectable = true;

	// 인자 타입 1
	public IconTextItem(Drawable icon, String[] obj) {
		mIcon = icon;
		mData = obj;
	}

	// 인자 타입 2
	public IconTextItem(Drawable icon, String obj01, String obj02, String obj03) {
		mIcon = icon;

		mData = new String[4];
		mData[0] = obj01;
		mData[1] = obj02;
		mData[2] = obj03;
	}

	// 인자 타입 3
	public IconTextItem(String obj01, String obj02, String obj03) {
		mData = new String[4];
		mData[0] = obj01;
		mData[1] = obj02;
		mData[2] = obj03;
	}


	/**
	 * True if this item is selectable
	 */
	public boolean isSelectable() {
		return mSelectable;
	}

	/**
	 * Set selectable flag
	 */
	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}

	/**
	 * Get data array
	 *
	 * @return
	 */
	public String[] getData() {
		return mData;
	}

	/**
	 * Get data
	 */
	public String getData(int index) {
		if (mData == null || index >= mData.length) {
			return null;
		}

		return mData[index];
	}

	/**
	 * Set data array
	 *
	 * @param obj
	 */
	public void setData(String[] obj) {
		mData = obj;
	}

	/**
	 * Set icon
	 *
	 * @param icon
	 */
	public void setIcon(Drawable icon) {
		mIcon = icon;
	}

	/**
	 * Get icon
	 *
	 * @return
	 */
	public Drawable getIcon() {
		return mIcon;
	}

	/**
	 * Compare with the input object
	 *
	 * @param other
	 * @return
	 */
	public int compareTo(IconTextItem other) {
		if (mData != null) {
			String[] otherData = other.getData();
			if (mData.length == otherData.length) {
				for (int i = 0; i < mData.length; i++) {
					if (!mData[i].equals(otherData[i])) {
						return -1;
					}
				}
			} else {
				return -1;
			}
		} else {
			throw new IllegalArgumentException();
		}

		return 0;
	}

}
