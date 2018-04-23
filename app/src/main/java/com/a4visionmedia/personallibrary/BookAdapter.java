package com.a4visionmedia.personallibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rifal on 28/03/2018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> books;
    private OnClickListener listener;
    private Context context;

    public BookAdapter(List<Book> books, Context context, OnClickListener listener) {
        this.books = books;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = books.get(position);
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
            return books.size();
        }catch (NullPointerException e){
            Toast.makeText(context,"Error Timed Out",Toast.LENGTH_SHORT);
            return 0;
        }
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
