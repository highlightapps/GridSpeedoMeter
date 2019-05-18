package com.example.viewpagerpoc;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.vcard.VCardEntry;

import java.util.ArrayList;
import java.util.List;

public class PhoneFragmentContactsAdapter extends RecyclerView.Adapter<PhoneFragmentContactsAdapter.ContactsViewHolder> implements Filterable {

    public List<VCardEntry> contactList;
    public List<VCardEntry> contactListFiltered;
    public AdapterOnItemClickListener adapterOnItemClickListener;

    public PhoneFragmentContactsAdapter(ArrayList<VCardEntry> contacts, AdapterOnItemClickListener adapterOnItemClickListener){
        this.contactList = contacts;
        this.contactListFiltered = contacts;
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
        myViewHolder.textViewContactName.setText(contactListFiltered.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return contactListFiltered != null ? contactListFiltered.size() : 0;
    }

    public void setContacts(ArrayList<VCardEntry> contacts){
        this.contactList = contacts;
        this.contactListFiltered = contacts;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<VCardEntry> filteredList = new ArrayList<>();
                    for (VCardEntry row : contactList) {

                        // name match condition. this might differ depending on requirement
                        // here we are looking for display name
                        if (row.getDisplayName().toLowerCase().startsWith(charString.toLowerCase())) {
                                //|| row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<VCardEntry>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
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
