package com.yanzhenjie.andserver.sample.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by watson on 4/12 0012.
 */
public class UnzipFromAssets {
    /**
     * 解压assets的zip压缩文件到指定目录
     */
    public static void unZip(Context context, String assetName, String outputDirectory, boolean isReWrite) throws IOException {
        Log.i("temp", "创建解压目标目录" + outputDirectory);
        File file = new File(outputDirectory);
        if (!file.exists()) {
            Log.i("temp", "如果目标目录不存在，则创建" + outputDirectory);
            file.mkdirs();
        } else {
            Log.i("temp", "目标目录已存在" + outputDirectory);
        }
        Log.i("temp", " 打开压缩文件");
        InputStream inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Log.i("temp", " 读取一个进入点");
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        Log.i("temp", "使用1M buffer");
        byte[] buffer = new byte[1024 * 1024];
        Log.i("temp", "解压时字节计数");
        int count = 0;
        Log.i("temp", " 如果进入点为空说明已经遍历完所有压缩包中文件和目录");
        while (zipEntry != null) {
            if (zipEntry.isDirectory()) {
                Log.i("temp", " 是一个目录");
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                Log.i("temp", " 文件需要覆盖或者是文件不存在" + file.getName());
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                Log.i("temp", " 是文件");
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                Log.i("temp", " 文件需要覆盖或者文件不存在，则解压文件");
                if (isReWrite || !file.exists()) {
                    Log.i("temp", "写入文件file->" + file.getName());
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            Log.i("temp", " 定位到下一个文件入口");
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        Log.i("temp", "解压结束");
    }
}
