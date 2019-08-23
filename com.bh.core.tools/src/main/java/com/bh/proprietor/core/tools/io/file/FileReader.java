package com.bh.proprietor.core.tools.io.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bh.proprietor.core.tools.util.CharsetUtil;
import com.bh.proprietor.core.tools.io.FileUtil;
import com.bh.proprietor.core.tools.io.IORuntimeException;
import com.bh.proprietor.core.tools.io.IoUtil;

/**
 * 文件读取器
 *
 * @author Looly
 */
public class FileReader extends FileWrapper {

    /**
     * 创建 FileReader
     *
     * @param file    文件
     * @param charset 编码，使用 {@link CharsetUtil}
     * @return {@link FileReader}
     */
    public static FileReader create(File file, Charset charset) {
        return new FileReader(file, charset);
    }

    /**
     * 创建 FileReader, 编码：{@link FileWrapper#DEFAULT_CHARSET}
     *
     * @param file 文件
     * @return {@link FileReader}
     */
    public static FileReader create(File file) {
        return new FileReader(file);
    }

    // ------------------------------------------------------- Constructor start

    /**
     * 构造
     *
     * @param file    文件
     * @param charset 编码，使用 {@link CharsetUtil}
     */
    public FileReader(File file, Charset charset) {
        super(file, charset);
        checkFile();
    }

    /**
     * 构造
     *
     * @param file    文件
     * @param charset 编码，使用 {@link CharsetUtil#charset(String)}
     */
    public FileReader(File file, String charset) {
        this(file, CharsetUtil.charset(charset));
    }

    /**
     * 构造
     *
     * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
     * @param charset  编码，使用 {@link CharsetUtil}
     */
    public FileReader(String filePath, Charset charset) {
        this(FileUtil.file(filePath), charset);
    }

    /**
     * 构造
     *
     * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
     * @param charset  编码，使用 {@link CharsetUtil#charset(String)}
     */
    public FileReader(String filePath, String charset) {
        this(FileUtil.file(filePath), CharsetUtil.charset(charset));
    }

    /**
     * 构造<br>
     * 编码使用 {@link FileWrapper#DEFAULT_CHARSET}
     *
     * @param file 文件
     */
    public FileReader(File file) {
        this(file, DEFAULT_CHARSET);
    }

    /**
     * 构造<br>
     * 编码使用 {@link FileWrapper#DEFAULT_CHARSET}
     *
     * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
     */
    public FileReader(String filePath) {
        this(filePath, DEFAULT_CHARSET);
    }
    // ------------------------------------------------------- Constructor end

    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过 {@link Integer#MAX_VALUE}
     *
     * @return 字节码
     * @throws IOException
     */
    public byte[] readBytes() throws IORuntimeException {
        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new IORuntimeException("File is larger then max array size");
        }

        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
        } catch (Exception e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(in);
        }

        return bytes;
    }

    /**
     * 读取文件内容
     *
     * @return 内容
     * @throws IORuntimeException
     */
    public String readString() throws IORuntimeException {
        return new String(readBytes(), this.charset);
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException
     */
    public <T extends Collection<String>> T readLines(T collection) throws IORuntimeException {
        BufferedReader reader = null;
        try {
            reader = FileUtil.getReader(file, charset);
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null)
                    break;
                collection.add(line);
            }
            return collection;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(reader);
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException
     */
    public List<String> readLines() throws IORuntimeException {
        return readLines(new ArrayList<String>());
    }

    /**
     * 读取JSON
     *
     * @return JSON（包括JSONObject和JSONArray）
     * @throws IORuntimeException
     */
    public JSONObject readJSON() throws IORuntimeException {
        return JSON.parseObject(readString());
    }

    /**
     * 读取JSONObject
     *
     * @return {@link JSONObject}
     * @throws IORuntimeException
     */
    public JSONObject readJSONObject() throws IORuntimeException {
        return JSON.parseObject(readString());
    }

    /**
     * 读取JSONArray
     *
     * @return {@link JSONArray}
     * @throws IORuntimeException
     */
    public JSONArray readJSONArray() throws IORuntimeException {
        return JSON.parseArray(readString());
    }

    /**
     * 按照给定的readerHandler读取文件中的数据
     *
     * @param readerHandler Reader处理类
     * @return 从文件中read出的数据
     * @throws IORuntimeException
     */
    public <T> T read(ReaderHandler<T> readerHandler) throws IORuntimeException {
        BufferedReader reader = null;
        T result = null;
        try {
            reader = FileUtil.getReader(this.file, charset);
            result = readerHandler.handle(reader);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(reader);
        }
        return result;
    }

    /**
     * 获得一个文件读取器
     *
     * @return BufferedReader对象
     * @throws IORuntimeException
     */
    public BufferedReader getReader() throws IORuntimeException {
        try {
            return IoUtil.getReader(getInputStream(), this.charset);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得输入流
     *
     * @return 输入流
     * @throws IORuntimeException
     */
    public BufferedInputStream getInputStream() throws IORuntimeException {
        try {
            return new BufferedInputStream(new FileInputStream(this.file));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 将文件写入流中
     *
     * @param out 流
     * @return File
     * @throws IORuntimeException
     */
    public File writeToStream(OutputStream out) throws IORuntimeException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            IoUtil.copy(in, out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(in);
        }
        return this.file;
    }

    // --------------------------------------------------------------------------
    // Interface start

    /**
     * Reader处理接口
     *
     * @param <T>
     * @author Luxiaolei
     */
    public interface ReaderHandler<T> {
        public T handle(BufferedReader reader) throws IOException;
    }
    // --------------------------------------------------------------------------
    // Interface end

    /**
     * 检查文件
     *
     * @throws IOException
     */
    private void checkFile() throws IORuntimeException {
        if (false == file.exists()) {
            throw new IORuntimeException("File not exist: " + file);
        }
        if (false == file.isFile()) {
            throw new IORuntimeException("Not a file:" + file);
        }
    }
}
