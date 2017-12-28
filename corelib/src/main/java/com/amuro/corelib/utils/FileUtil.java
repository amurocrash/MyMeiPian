package com.amuro.corelib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2016/3/3.
 */
public class FileUtil
{
    public static void closeIO(Closeable io)
    {
        if(io != null)
        {
            try
            {
                io.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile(File sourceFile, File targetFile)
    {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try
        {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1)
            {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        }
        catch (Exception e)
        {

        }
        finally
        {
            // 关闭流
            try
            {
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
    }

    public static void writeStr(File file, String str)
    {
        writeStr(file, str, true);
    }


    public static void writeStr(File file, String str, boolean append)
    {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try
        {
            fw = new FileWriter(file, append);
            bw = new BufferedWriter(fw);

            bw.write(str);
            bw.flush();
            bw.close();
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            try
            {
                bw.close();
                fw.close();
            }
            catch (IOException e1)
            {
            }
        }
    }

    public static String readStr(File file)
    {
        FileReader fr = null;
        BufferedReader br = null;
        String result = "";

        try
        {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            result = br.readLine();

        }
        catch(Exception e)
        {
        }
        finally
        {
            try
            {
                if(br != null)
                {
                    br.close();
                }
                if(fr != null)
                {
                    fr.close();
                }
            }
            catch (Exception e)
            {
            }
        }

        return result;
    }

    public static String readFromFile(String filePath)
    {
        BufferedInputStream inBuff = null;
        ByteArrayOutputStream outBuff = new ByteArrayOutputStream();
        try
        {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(filePath));
            // 新建文件输出流并对它进行缓冲
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1)
            {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            return new String(outBuff.toByteArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // 关闭流
            try
            {
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public static File getDir(String dir)
    {
        File fileDir = new File(dir);
        if (!fileDir.exists())
        {
            fileDir.mkdirs();
        }
        return fileDir;
    }

    public static String getMd5ByFile(File file)
    {
        String value = null;
        FileInputStream in = null;
        try
        {
            in = new FileInputStream(file);
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        try
        {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != in)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static File unzipSingleFile(String zipFileName, String outputDirectory, String fileName) throws Exception
    {
        File unzipFile = null;

        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
        ZipEntry z = in.getNextEntry();

        if(z != null)
        {
//            String name = z.getName();
            unzipFile = new File(outputDirectory + File.separator + fileName);
            unzipFile.createNewFile();
            FileOutputStream out = new FileOutputStream(unzipFile);
            int ch;
            byte[] buffer = new byte[1024];
            while ((ch = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, ch);
                out.flush();
            }
            out.close();
        }

        in.close();

        return unzipFile;
    }

    public static void unzipAll(String zipFileName, String outputDirectory)
            throws Exception
    {
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
        ZipEntry z;
        String name = "";
        String extractedFile = "";
        int counter = 0;

        while ((z = in.getNextEntry()) != null)
        {
            name = z.getName();
            System.out.println("unzipping file: " + name);
            if (z.isDirectory())
            {
                System.out.println(name + "is a folder");
                // get the folder name of the widget
                name = name.substring(0, name.length() - 1);
                File folder = new File(outputDirectory + File.separator + name);
                folder.mkdirs();
                if (counter == 0)
                {
                    extractedFile = folder.toString();
                }
                counter++;
                System.out.println("mkdir " + outputDirectory + File.separator + name);
            }
            else
            {
                System.out.println(name + "is a normal file");
                File unzipFile = new File(outputDirectory + File.separator + name);
                unzipFile.createNewFile();
                FileOutputStream out = new FileOutputStream(unzipFile);
                int ch;
                byte[] buffer = new byte[1024];
                while ((ch = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, ch);
                    out.flush();
                }
                out.close();
            }
        }

        in.close();

    }

    public static File byteArrayToFile(File dir, byte[] body, String fileName) throws Exception
    {
        File file = new File(dir.getAbsolutePath() + "/"  + fileName);
        if(!file.exists())
        {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(body);
        fos.close();

        return file;
    }

    public static void clearFileContent(File file)
    {
        writeStr(file, "", false);
    }
}