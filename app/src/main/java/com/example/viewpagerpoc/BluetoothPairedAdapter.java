package com.example.viewpagerpoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class BluetoothPairedAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;

    ArrayList<String> arrayListPaired;
    Button btnUnPair;
    Button btnConnect;
    TextView txtPaired;
    int currentPosition;

    public BluetoothPairedAdapter(Context context, ArrayList<String> arrayListPaired) {
        this.context = context;
        this.arrayListPaired = arrayListPaired;
    }

    @Override
    public int getCount() {
        return arrayListPaired.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListPaired.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        currentPosition = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.layout_bluetooth_paired_list_row_items, parent, false);
            txtPaired = (TextView) convertView.findViewById(R.id.txtPaired);
            btnUnPair = (Button) convertView.findViewById(R.id.btnUnPair);
            btnConnect = (Button) convertView.findViewById(R.id.btnConnect);

        }
        txtPaired.setText(arrayListPaired.get(position));
        btnUnPair.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUnPair:


                break;
            case R.id.btnConnect:
                break;

        }

    }
}
