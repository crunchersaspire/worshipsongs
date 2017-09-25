package org.worshipsongs.dao;

import android.content.Context;
import android.database.Cursor;

import org.worshipsongs.WorshipSongApplication;
import org.worshipsongs.domain.Song;
import org.worshipsongs.parser.ISongParser;
import org.worshipsongs.parser.SongParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Madasamy
 * @Version : 1.0
 */
public class SongDao extends AbstractDao
{
    public static final String TABLE_NAME = "songs";
    private ISongParser songParser = new SongParser();

    public SongDao()
    {
        super();
    }

    public SongDao(Context context)
    {
        super(context);
    }

    public List<Song> findAll()
    {
        List<Song> songs = new ArrayList<Song>();
        Cursor cursor = getDatabase().query(TABLE_NAME,
                new String[]{"title", "lyrics", "verse_order", "search_title", "search_lyrics", "comments"}, null, null, null, null, "title");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Song song = cursorToSong(cursor);
            songs.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        return songs;
    }

    public List<Song> findByTopicId(int topicId)
    {
        List<Song> songs = new ArrayList<Song>();
        String query = "select title,lyrics,verse_order,search_title,search_lyrics,comments " +
                "from songs as s inner join songs_topics as st on st.song_id = s.id inner join " +
                "topics as t on st.topic_id = t.id where t.id= ?";
        Cursor cursor = getDatabase().rawQuery(query, new String[]{String.valueOf(topicId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Song song = cursorToSong(cursor);
            songs.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        return songs;
    }

    public List<Song> findByAuthorId(int authorId)
    {
        List<Song> songs = new ArrayList<Song>();
        String query = "select title,lyrics,verse_order,search_title,search_lyrics,comments " +
                "from songs as s inner join authors_songs as aus on aus.song_id = s.id inner join" +
                " authors as t on aus.author_id = t.id where t.id= ?";
        Cursor cursor = getDatabase().rawQuery(query, new String[]{String.valueOf(authorId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Song song = cursorToSong(cursor);
            songs.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        return songs;
    }

    public List<Song> findBySongBookId(int songBookId)
    {
        List<Song> songs = new ArrayList<>();
        String query = "select title,lyrics,verse_order,search_title,search_lyrics,comments from " +
                "songs as s inner join songs_songbooks as ssb on ssb.song_id = s.id inner join " +
                "song_books as sb on ssb.songbook_id = sb.id where sb.id= ?";
        Cursor cursor = getDatabase().rawQuery(query, new String[]{String.valueOf(songBookId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Song song = cursorToSong(cursor);
            songs.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        return songs;
    }

    public Song findContentsByTitle(String title)
    {
        Song song = findByTitle(title);
        if (song != null) {
            Song parsedSong = new Song();
            parsedSong.setTitle(title);
            parsedSong.setLyrics(song.getLyrics());
            parsedSong.setVerseOrder(song.getVerseOrder());
            parsedSong.setSearchTitle(song.getSearchTitle());
            parsedSong.setSearchLyrics(song.getSearchLyrics());
            parsedSong.setComments(song.getComments());
            parsedSong.setContents(songParser.parseContents(WorshipSongApplication.getContext(), song.getLyrics(), song.getVerseOrder()));
            parsedSong.setUrlKey(songParser.parseMediaUrlKey(song.getComments()));
            parsedSong.setChord(songParser.parseChord(song.getComments()));
            parsedSong.setTamilTitle(songParser.parseTamilTitle(song.getComments()));
            return parsedSong;
        }
        return null;
    }

    public Song findByTitle(String title)
    {
        Song song = null;
        String whereClause = " title" + "=\"" + title + "\"";
        Cursor cursor = getDatabase().query(TABLE_NAME,
                new String[]{"title", "lyrics", "verse_order", "search_title", "search_lyrics", "comments"}, whereClause, null, null, null, "title");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            song = cursorToSong(cursor);
            cursor.close();
        }
        return song;
    }

    private Song cursorToSong(Cursor cursor)
    {
        Song song = new Song();
        song.setTitle(cursor.getString(0));
        song.setLyrics(cursor.getString(1));
        song.setVerseOrder(cursor.getString(2));
        song.setSearchTitle(cursor.getString(3));
        song.setSearchLyrics(cursor.getString(4));
        song.setComments(cursor.getString(5));
        song.setUrlKey(songParser.parseMediaUrlKey(song.getComments()));
        song.setChord(songParser.parseChord(song.getComments()));
        song.setTamilTitle(songParser.parseTamilTitle(song.getComments()));
        return song;
    }

    public long count()
    {
        Cursor c = getDatabase().query(TABLE_NAME, null, null, null, null, null, null);
        int result = c.getCount();
        c.close();
        return result;
    }

    public boolean isValidDataBase()
    {
        try {
            findAll();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
