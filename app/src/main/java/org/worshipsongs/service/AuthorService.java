package org.worshipsongs.service;

import android.content.Context;
import android.database.Cursor;

import org.worshipsongs.domain.Author;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Madasamy
 * Version : x.x.x
 */

public class AuthorService extends  GenericService
{
    public static final String TABLE_NAME_AUTHOR = "authors";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_DISPLAY_NAME = "display_name";
    private String[] allColumns = {COLUMN_ID, COLUMN_FIRST_NAME,
            COLUMN_LAST_NAME, COLUMN_DISPLAY_NAME};
    public static final String COLUMN_AUTHOR_ID = "author_id";
    private String[] columns = {COLUMN_AUTHOR_ID};

    public AuthorService(Context context) {
        super(context);
    }

    public List<Author> findAll() {
        List<Author> authors = new ArrayList<Author>();
        Cursor cursor = getDatabase().query(true, TABLE_NAME_AUTHOR,
                allColumns, null, null, null, null, COLUMN_DISPLAY_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Author author = cursorToAuthor(cursor);
            authors.add(author);
            cursor.moveToNext();
        }
        cursor.close();
        return authors;
    }

    private Author cursorToAuthor(Cursor cursor) {
        Author author = new Author();
        author.setId(cursor.getInt(0));
        author.setFirstName(cursor.getString(1));
        author.setLastName(cursor.getString(2));
        author.setName(cursor.getString(3));
        return author;
    }
}
