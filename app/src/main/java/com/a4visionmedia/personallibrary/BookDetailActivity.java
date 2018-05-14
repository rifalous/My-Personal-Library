package com.a4visionmedia.personallibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private String mPengarang;
    private String mPenerbit;
    private String mKategori;
    private String mISBN;
    private String mCover;
    private String mJudul;
    private String mUsername;
    private LinearLayout mEditButton;
    private TextView mTitleBook, mAuthorBook, mPubBook, mCatBook, mNoISBN;
    private ImageView mCoverBook;
    private Intent getIntentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initialize();

        getIntentData = getIntent();

        mJudul = getIntentData.getStringExtra("judul");
        mPengarang = getIntentData.getStringExtra("pengarang");
        mPenerbit = getIntentData.getStringExtra("penerbit");
        mKategori = getIntentData.getStringExtra("kategori");
        mISBN = getIntentData.getStringExtra("isbn");
        mCover = getIntentData.getStringExtra("cover");
        mUsername = getIntentData.getStringExtra("username");

        mTitleBook.setText(mJudul);
        mAuthorBook.setText(mPengarang);
        mPubBook.setText(mPenerbit);
        mCatBook.setText(mKategori);
        mNoISBN.setText(mISBN);
        Picasso.with(getApplicationContext()).load(mCover).into(mCoverBook);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailActivity.this, InputBookActivity.class);
                intent.putExtra("FLAG", "EDIT");
                intent.putExtra("judul", mJudul);
                intent.putExtra("pengarang", mPengarang);
                intent.putExtra("penerbit", mPenerbit);
                intent.putExtra("kategori", mKategori);
                intent.putExtra("isbn", mISBN);
                intent.putExtra("cover", mCover);
                intent.putExtra("username", mUsername);
                startActivity(intent);
            }
        });

    }

    public void initialize(){
        mTitleBook = findViewById(R.id.titleBook);
        mAuthorBook = findViewById(R.id.authorBook);
        mPubBook = findViewById(R.id.pubBook);
        mCatBook = findViewById(R.id.catBook);
        mNoISBN = findViewById(R.id.isbn);
        mCoverBook = findViewById(R.id.coverBook);
        mEditButton = findViewById(R.id.edit_buku);
    }

}
