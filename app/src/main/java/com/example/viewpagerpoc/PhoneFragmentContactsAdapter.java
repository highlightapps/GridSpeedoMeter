package com.example.viewpagerpoc;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vcard.VCardEntry;

import java.util.ArrayList;

public class PhoneFragmentContactsAdapter extends RecyclerView.Adapter<PhoneFragmentContactsAdapter.ContactsViewHolder> {

    public static ArrayList<VCardEntry> contacts;
    public static AdapterOnItemClickListener adapterOnItemClickListener;

    public PhoneFragmentContactsAdapter(ArrayList<VCardEntry> contacts, AdapterOnItemClickListener adapterOnItemClickListener){
        this.contacts = contacts;
        this.adapterOnItemClickListener = adapterOnItemClickListener;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_contacts_list_item, parent, false);
        ContactsViewHolder myViewHolder = new ContactsViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder myViewHolder, int position) {
        myViewHolder.textViewContactName.setText(contacts.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    public void setContacts(ArrayList<VCardEntry> contacts){
        this.contacts = contacts;
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContactName;
        public ContactsViewHolder(View v) {
            super(v);
            textViewContactName = v.findViewById(R.id.tvContactName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(adapterOnItemClickListener != null){
                        adapterOnItemClickListener.onItemClickListener(getAdapterPosition());
                    }
                }
            });
        }
    }
}
