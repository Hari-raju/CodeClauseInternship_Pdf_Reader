package com.raju.pdfreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.pdfreader.databinding.PdfItemsBinding;

import java.io.File;
import java.util.Date;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfAdapterHolder> {
    private Context context;
    private List<File> fileList;
    private FileSelectListener listener;

    public PdfAdapter(Context context,List<File> lists,FileSelectListener listener){
        this.context=context;
        this.fileList=lists;
        this.listener=listener;
    }


    @NonNull
    @Override
    public PdfAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PdfItemsBinding pdfItemsBinding = PdfItemsBinding.inflate(LayoutInflater.from(context),parent,false);
        return new PdfAdapterHolder(pdfItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfAdapterHolder holder, int position) {
        holder.setData(position);
        holder.binding.pdfCard.setOnClickListener(v->{
            listener.onPdfSelected(fileList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class PdfAdapterHolder extends RecyclerView.ViewHolder{
        private PdfItemsBinding binding;
        public PdfAdapterHolder(@NonNull PdfItemsBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }

        private void setData(int position){
            binding.textDocumentName.setSelected(true);
            binding.textDocumentName.setText(fileList.get(position).getName());
            File file = new File(fileList.get(position).toURI());
            Date date= new Date(file.lastModified());
            binding.textDocumentDate.setText(date.toString());
            binding.textDocumentName.setSelected(true);
        }
    }
}
