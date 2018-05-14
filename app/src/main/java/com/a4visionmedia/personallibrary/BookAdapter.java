package com.a4visionmedia.personallibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rifal on 28/03/2018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> implements Filterable{

    private List<Book> books;
    private List<Book> booksFiltered;
    private OnClickListener listener;
    private Context context;

    public BookAdapter(List<Book> books, Context context, OnClickListener listener) {
        this.books = books;
        this.context = context;
        this.listener = listener;
        this.booksFiltered = books;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = booksFiltered.get(position);
        String imgUrl = book.getCover();

        holder.bind(book, listener);
        holder.booksTitle.setText(book.getTitle());
        holder.booksAuthor.setText(book.getAuthor());
        if(imgUrl==null){
            holder.booksImage.setImageResource(R.drawable.no_image);
        }else{
            Picasso.with(context).load(imgUrl).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.booksImage);
        }
        //holder.booksAuthor.setText(feed.getmUrl());
    }

    @Override
    public int getItemCount() {
        try {
            return booksFiltered.size();
        }catch (NullPointerException e){
            Toast.makeText(context,"Error Timed Out",Toast.LENGTH_SHORT);
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    booksFiltered = books;
                } else {
                    List<Book> filteredList = new ArrayList<>();
                    for (Book row : books) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getCategory().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("book",charString + "\n");
                            filteredList.add(row);
                        }
                    }

                    booksFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = booksFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                booksFiltered = (ArrayList<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView booksImage;
        TextView booksTitle;
        TextView booksAuthor;

        public ViewHolder(View itemView) {
            super(itemView);

            booksImage = itemView.findViewById(R.id.coverBook);
            booksTitle = itemView.findViewById(R.id.titleBook);
            booksAuthor = itemView.findViewById(R.id.authorBook);}

        public void bind(final Book feed, final OnClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(feed);
                }
            });
        }
    }


}
