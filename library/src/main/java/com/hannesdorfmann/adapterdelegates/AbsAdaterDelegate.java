/*
 * Copyright (c) 2015 Hannes Dorfmann.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hannesdorfmann.adapterdelegates;


/**
 * A simple {@link AdapterDelegate} implementation that already implements {@link
 * #getItemViewType()}
 *
 * @author Hannes Dorfmann
 */
public abstract class AbsAdaterDelegate<T> implements AdapterDelegate<T> {

  /**
   * The item view type
   */
  protected int viewType;

  public AbsAdaterDelegate(int viewType) {
    this.viewType = viewType;
  }

  @Override public int getItemViewType() {
    return viewType;
  }
}
