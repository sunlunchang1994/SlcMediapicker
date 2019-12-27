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

package com.slc.mp.po;

import com.slc.mp.po.i.IBaseFolder;
import com.slc.mp.po.i.IBaseItem;

import java.util.List;

/**
 * Created by Taurus on 2016/8/29.
 */
public abstract class BaseFolder<T extends IBaseItem> extends com.slc.medialoader.bean.BaseFolder<T> implements IBaseFolder<T> {
    public BaseFolder() {
    }

    public BaseFolder(List<T> items) {
        this.items = items;
    }

    public BaseFolder(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
