package yy.springframework.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public interface Resource {

    InputStream getInputStream() throws IOException;

    File getFile() throws IOException;

    String getDescription();

    default boolean canRead(){
        try {
            return getFile().canRead();
        } catch (IOException e) {
            return false;
        }
    }
}
