package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class MyService
{
    private LinkedList<Video> addedVids;
    VideoFileManager fileManager;

    @Autowired
    public MyService() throws IOException {
        LinkedList<Video> addedVids= new LinkedList<Video>();
        fileManager = VideoFileManager.get();
    }

    public LinkedList<Video> getVidsLis()
    {
        return addedVids;
    }

    public void getStreamById(int id, InputStream in) throws IOException {
        Video searchedVid = null;
        for(Video vid: addedVids)
        {
            if(vid.getId() == id)
            {
                searchedVid = vid; break;
            }
        }
        if(searchedVid != null)
        {
            fileManager.getVideoData(searchedVid, in);
        }

    }
}
