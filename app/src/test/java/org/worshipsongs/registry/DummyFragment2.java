package org.worshipsongs.registry;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.worshipsongs.listener.SongContentViewListener;

/**
 * Author : Madasamy
 * Version : 4.x.x
 */

public class DummyFragment2 extends Fragment implements ITabFragment
{
    @Override
    public int defaultSortOrder()
    {
        return 2;
    }

    @Override
    public String getTitle()
    {
        return "song_books";
    }

    @Override
    public boolean checked()
    {
        return false;
    }

    @Override
    public void setListenerAndBundle(SongContentViewListener songContentViewListener, Bundle bundle)
    {

    }
}
