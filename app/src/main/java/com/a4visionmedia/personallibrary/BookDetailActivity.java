package com.a4visionmedia.personallibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private String mPengarang;
    private String mPenerbit;
    private String mKategori;
    private String mISBN;
    private String mCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        TextView mTitleBook, mAuthorBook, mPubBook, mCatBook, mNoISBN;
        ImageView mCoverBook;
        Intent intent = getIntent();

        String mJudul = intent.getStringExtra("judul");
        mPengarang = intent.getStringExtra("pengarang");
        mPenerbit = intent.getStringExtra("penerbit");
        mKategori = intent.getStringExtra("kategori");
        mISBN = intent.getStringExtra("isbn");
        mCover = intent.getStringExtra("cover");

        mTitleBook = findViewById(R.id.titleBook);
        mAuthorBook = findViewById(R.id.authorBook);
        mPubBook = findViewById(R.id.pubBook);
        mCatBook = findViewById(R.id.catBook);
        mNoISBN = findViewById(R.id.isbn);
        mCoverBook = findViewById(R.id.coverBook);

        mTitleBook.setText(mJudul);
        mAuthorBook.setText(mPengarang);
        mPubBook.setText(mPenerbit);
        mCatBook.setText(mKategori);
        mNoISBN.setText(mISBN);
        Picasso.with(getApplicationContext()).load(mCover).into(mCoverBook);
    }
}
