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

package com.slc.medialoader.bean.i;

import java.io.Serializable;

/**
 * Created by Taurus on 16/8/28.
 */
public interface IBaseItem extends Serializable {

    int getId();

    void setId(int id);

    String getDisplayName();

    void setDisplayName(String displayName);

    String getPath();

    void setPath(String path);

    long getSize();

    void setSize(long size);

    long getModified();

    void setModified(long modified);

    String getExtension();

    void setExtension(String extension);
}
