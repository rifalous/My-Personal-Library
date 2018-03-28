package com.a4visionmedia.personallibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rifal on 28/03/2018.
 */

class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
{
    TextView txtTitle,txtAuthor,txtPublisher,txtCategory,txtISBN;
    ImageView cover;
    private ItemClickListener itemClickListener;

    @SuppressLint("CutPasteId")
    BookViewHolder(View itemView) {
        super(itemView);

        txtTitle = (TextView)itemView.findViewById(R.id.titleBook);
        txtAuthor = (TextView)itemView.findViewById(R.id.authorBook);
        cover = (ImageView)itemView.findViewById(R.id.coverBook);

        // Set Event Listener
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
    private Book book;
    private Context mContext;
    private LayoutInflater inflater;

    public BookAdapter(Book book, Context mContext) {
        this.book = book;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.book_item,parent,false);
        return new BookViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {

        holder.txtTitle.setText(book.getTitle());
        holder.txtAuthor.setText(book.getAuthor());
        if (book.getCover().isEmpty()) {
            holder.cover.setImageResource(R.drawable.no_image);
        } else {
            Picasso.with(mContext).load(book.getCover()).into(holder.cover);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick)
                {
                    Intent intent;
                    intent = new Intent(mContext, BookDetailActivity.class);
                    /*intent.putExtra("ARTICLE_URL", rssObject.getItems().get(position).getLink());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
