package com.jef.jefmining;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by ettienne on 2018/01/17.
 */

public class CustomPairAdapter extends ArrayAdapter<Pairing> {

    private List<Pairing> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView currency;
        TextView exchange1;
        TextView exchange2;
        TextView exchangeToBuy;
        TextView spread;
    }

    public CustomPairAdapter(List<Pairing> data, Context context) {
        super(context, R.layout.pair_row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Pairing dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.pair_row_item, parent, false);
            viewHolder.currency = (TextView) convertView.findViewById(R.id.currency);
            viewHolder.exchange1 = (TextView) convertView.findViewById(R.id.exchange1);
            viewHolder.exchange2 = (TextView) convertView.findViewById(R.id.exchange2);
            viewHolder.exchangeToBuy = (TextView) convertView.findViewById(R.id.exchangeToBuy);
            viewHolder.spread = (TextView) convertView.findViewById(R.id.spread);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.currency.setText(dataModel.getCurrency());
        viewHolder.exchange1.setText(String.valueOf(dataModel.getPrice1()));
        viewHolder.exchange2.setText(String.valueOf(dataModel.getPrice2()));
        viewHolder.exchangeToBuy.setText(dataModel.getBuyAtExchange());
        viewHolder.spread.setText(String.format(Locale.ENGLISH, "%.2f", dataModel.getSpread()));

        // Return the completed view to render on screen
        return convertView;
    }
}

