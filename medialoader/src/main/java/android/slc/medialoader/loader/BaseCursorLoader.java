/*
 * Copyright 2016 jiajunhui
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package android.slc.medialoader.loader;

import android.content.Context;

import androidx.loader.content.CursorLoader;

import android.slc.medialoader.inter.ILoader;

/**
 * Created by Taurus on 16/8/28.
 */
public class BaseCursorLoader extends CursorLoader {

    public BaseCursorLoader(Context context, ILoader iLoader) {
        super(context);
        setProjection(iLoader.getSelectProjection());
        setUri(iLoader.getQueryUri());
        setSelection(iLoader.getSelections());
        setSelectionArgs(iLoader.getSelectionsArgs());
        setSortOrder(iLoader.getSortOrderSql());
    }
}
