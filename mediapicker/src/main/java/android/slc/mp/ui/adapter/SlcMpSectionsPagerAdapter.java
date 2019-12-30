package android.slc.mp.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.collection.SparseArrayCompat;
import android.view.ViewGroup;

import android.slc.mp.ui.SlcIMpDelegate;
import android.slc.mp.ui.page.SlcMpPagerBaseFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SlcMpSectionsPagerAdapter extends FragmentPagerAdapter {
    private SparseArrayCompat<Fragment> mFragmentSparseArray = new SparseArrayCompat<>();
    private final FragmentManager mFm;
    private final Context mContext;
    private SlcIMpDelegate mMediaPickerDelegate;

    public SlcMpSectionsPagerAdapter(Context context, FragmentManager fm, SlcIMpDelegate slcIMpDelegate) {
        super(fm);
        mFm = fm;
        mContext = context;
        mMediaPickerDelegate = slcIMpDelegate;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragmentTemp = mFragmentSparseArray.get(position);
        if (fragmentTemp == null) {
            fragmentTemp = SlcMpPagerBaseFragment.newInstance(position,
                    mMediaPickerDelegate.getMediaTypeList().get(position), mMediaPickerDelegate);
            mFragmentSparseArray.put(position, fragmentTemp);
        }
        return fragmentTemp;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mMediaPickerDelegate.getTitleByPosition(position);
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = mFragmentSparseArray.get(position);
        mFm.beginTransaction().hide(fragment).commit();
    }

    @Override
    public int getCount() {
        return mMediaPickerDelegate.getMediaTypeList().size();
    }
}