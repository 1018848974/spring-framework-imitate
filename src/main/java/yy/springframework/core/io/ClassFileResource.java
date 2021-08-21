package yy.springframework.core.io;

import java.io.*;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/11 9:44 下午 <br>
 * @see yy.springframework.core.io <br>
 */
public class ClassFileResource implements Resource {

    private String path;

    private File file;

    public ClassFileResource(File file) {
        this.file = file;
        this.path = file.getPath();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public File getFile() throws IOException {
        return this.file;
    }

    @Override
    public String getDescription() {
        return "file ptah :" + file.getAbsolutePath() + " name : " + file.getName();
    }
}
