package com.pischik.nikita.rentalmanager;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Class that represent adapter to forming RecyclerView data
 */
public class RVAddressesListAdapter extends
        RecyclerView.Adapter<RVAddressesListAdapter.AddressesViewHolder> {

    static OnItemClickListener mItemClickListener;

    /**
     * class realized ViewHolder pattern
     */
    static class AddressesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView address;
        TextView rent;
        TextView landLord;
        TextView date;

        public AddressesViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            address = (TextView) itemView.findViewById(R.id.address_field);
            rent = (TextView) itemView.findViewById(R.id.rent_field);
            landLord = (TextView) itemView.findViewById(R.id.landlord_field);
            date = (TextView) itemView.findViewById(R.id.date_field);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        RVAddressesListAdapter.mItemClickListener = mItemClickListener;
    }


    List<AddressesItem> addresses;

    RVAddressesListAdapter(List<AddressesItem> addresses){
        this.addresses = addresses;
    }

    @Override
    public AddressesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_addresses_element
                , parent, false);
        AddressesViewHolder avh = new AddressesViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(AddressesViewHolder holder, int position) {

        /**
         * formatting date and address in the required form
         */
        StringBuilder date = new StringBuilder();
        date.append(addresses.get(position).getDateIn());
        date.append(" to ");
        date.append(addresses.get(position).getDateOut());
        StringBuilder address = new StringBuilder();
        address.append(addresses.get(position).getStreet());
        address.append(", ");
        address.append(addresses.get(position).getCity());
        address.append(", ");
        address.append(addresses.get(position).getState());

        holder.address.setText(address);
        holder.rent.setText(addresses.get(position).getRent());
        holder.landLord.setText(addresses.get(position).getLandLord());
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}
