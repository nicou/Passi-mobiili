package fi.softala.passi;

/**
 * Created by villeaaltonen on 13/09/16.
 */

import java.io.File;

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}