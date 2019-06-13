/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.prajwal.smarthelmet1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link HelmetAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link Helmets} objects.
 */
public class HelmetAdapter extends ArrayAdapter<Helmets> {

    /** Resource ID for the background color for this list of words */
    private boolean type;

    /**
     * Create a new {@link HelmetAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param words is the list of {@link Helmets}s to be displayed.
     * @param type is the resource ID for the type of bluetoth device (paired or not)
     */
    public HelmetAdapter(Context context, ArrayList<Helmets> words, boolean type) {
        super(context, 0, words);
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Helmets currentHelmet = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID miwok_text_view.
        TextView name = (TextView) listItemView.findViewById(R.id.name);
        // Get the Miwok translation from the currentWord object and set this text on
        // the Miwok TextView.
        name.setText(currentHelmet.getName());

        // Find the TextView in the list_item.xml layout with the ID default_text_view.
        TextView mac = (TextView) listItemView.findViewById(R.id.mac);
        // Get the default translation from the currentWord object and set this text on
        // the default TextView.
        mac.setText("MAC address : "+currentHelmet.getMAC());

        Button set=(Button)listItemView.findViewById(R.id.set_default);

        // Find the ImageView in the list_item.xml layout with the ID image.

        if (currentHelmet.getType()) {
            // If an image is available, display the provided image based on the resource ID
            set.setText("Pair");
            // Make sure the view is visible
            } else {
            // Otherwise hide the ImageView (set visibility to GONE)
            set.setText("Set Default");
        }

        return listItemView;
    }
}