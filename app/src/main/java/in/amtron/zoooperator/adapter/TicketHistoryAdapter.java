package in.amtron.zoooperator.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.amtron.zoooperator.R;
import in.amtron.zoooperator.database.Booking;
import in.amtron.zoooperator.helper.Util;

public class TicketHistoryAdapter extends RecyclerView.Adapter<TicketHistoryAdapter.MyViewHolder>{

    private OnRecyclerViewItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    List<Booking> bookingList;

    public TicketHistoryAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
        notifyDataSetChanged();
    }

    public void addBooking(Booking booking){
        this.bookingList.add(booking);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketNo, tvDate, tvAmount, tvIndian, tvForeigner, tvPayment;
        Button btnUpdatePayment, btnPrintTicket;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTicketNo = itemView.findViewById(R.id.tv_ticket_no);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvIndian = itemView.findViewById(R.id.tv_ind_details);
            tvForeigner = itemView.findViewById(R.id.tv_fore_details);
            tvPayment = itemView.findViewById(R.id.tv_payment);

            btnUpdatePayment = itemView.findViewById(R.id.btn_update_payment);
            btnPrintTicket = itemView.findViewById(R.id.btn_print_ticket);
        }
    }

    @NonNull
    @Override
    public TicketHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ticket_history,parent,false);
        return new TicketHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHistoryAdapter.MyViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvTicketNo.setText(booking.getTicketNo());
        holder.tvDate.setText(booking.getDate());
        holder.tvAmount.setText(Util.moneyFormat(booking.getPayableAmount()));

        holder.tvPayment.setText(booking.getPayment() == 1?"Paid":"Not Paid");

        String indian = "A("+booking.getIndianAdults()+")  C("+booking.getIndianChildrens()+")  S("+booking.getIndianStudents()+")  ";
        indian += "Cam("+booking.getIndianStillCameras()+")  DSLR("+booking.getIndianSlrCameras()+")  V("+booking.getIndianVideoCameras()+")";
        holder.tvIndian.setText(indian);

        String foreigner = "A("+booking.getForeignAdults()+")  C("+booking.getForeignChildrens()+")  S("+booking.getForeignStudents()+")  ";
        foreigner += "Cam("+booking.getForeignStillCameras()+")  DSLR("+booking.getForeignSlrCameras()+")  V("+booking.getForeignVideoCameras()+")";
        holder.tvForeigner.setText(foreigner);

        if(booking.getPayment() == 1){
            holder.btnPrintTicket.setVisibility(View.VISIBLE);
            holder.btnUpdatePayment.setVisibility(View.GONE);
        }else{
            holder.btnPrintTicket.setVisibility(View.GONE);
            holder.btnUpdatePayment.setVisibility(View.VISIBLE);
        }
        holder.btnPrintTicket.setOnClickListener(v->{
            if(mItemClickListener != null){
                mItemClickListener.onItemClickListener(position,"print");
            }
        });

        holder.btnUpdatePayment.setOnClickListener(v->{
            if(mItemClickListener != null){
                mItemClickListener.onItemClickListener(position,"payment");
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookingList==null?0:bookingList.size();
    }


}
