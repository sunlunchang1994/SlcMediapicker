package com.slc.medialoader.callback;

import android.database.Cursor;
import androidx.loader.content.Loader;

import com.slc.medialoader.inter.ILoader;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnLoaderCallBack implements ILoader {

    public abstract void onLoadFinish(Loader<Cursor> loader, Cursor data);

    public void onLoaderReset(){
    }

}
