/*
 * Copyright (C) 2015. The BoCool Project.
 *
 *          yinglovezhuzhu@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoying.bocool.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;

/**
 * Usage: 文件操作相关工具类
 * 
 * @author zyy_owen@ivg.com
 */
public class FileUtil {
	
	private FileUtil(){
		
	}

    /**
     * Delete file(include not empty directory)
     *
     * @param file
     */
    public static void deleteFile(File file) {
    	if(null == file) {
    		return;
    	}
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file2 : files) {
                    deleteFile(file2);
                }
            }
            file.delete();
        }
    }
    
    /**
     * 清空目录，不会删除目录，只会删除目录下的文件（包含子目录）
     * @param dir
     */
    public static void clearDir(File dir) {
    	if(null == dir) {
    		return;
    	}
    	if(dir.exists() && dir.isDirectory()) {
    		File[] files = dir.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
    	}
    }

    /**
     * Parse a content uri to a file.
     * Some file manager return Uri like "file:///sdcard/test.mp4",
     * In this case Uri.getPath() get the file path in file system,
     * so can create a file object with this path, if this file is exists,
     * means parse file success.
     * Some file manager such as Gallery, return Uri like "content://video/8323",
     * In this case Uri.getPath() can't get file path in file system,
     * but can user ContentResolver to get file path from media database.
     *
     * @param uri
     * @return
     */
    public static File parseUriToFile(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        File file = null;
        String path = uri.getPath();
        file = new File(path); //If this file is exists, means parse file success.
        if (!file.exists()) {
            //Use ContentResolver to get file path from media database.
            ContentResolver cr = context.getContentResolver();
            String[] pro = new String[]{MediaStore.MediaColumns.DATA,};
            Cursor cursor = cr.query(uri, pro, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                    path = cursor.getString(index);
                    if (null != path && !"".equals(path)) {
                        file = new File(path);
                        if (!file.exists()) {
                            file = null;
                        }
                    }
                }
                cursor.close();
            }
        }
        return file;
    }

    /**
     * Get file size(include directory sub files)
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0L;
        if (null != file && file.exists()) {
            size += file.length();
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file2 : files) {
                    size += getFileSize(file2);
                }
            }
        }
        return size;
    }

    /**
     * Change byte to KB/MB/GB...（keep two float point）
     *
     * @param context
     * @param size
     * @return
     */
    public static String formatByte(Context context, long size) {
        return Formatter.formatFileSize(context, size);// Change byte to KB or MB, etc.
    }

    /**
     * Change byte to KB/MB/GB...(Keep Integer)
     *
     * @param context
     * @param size
     * @return
     */
    public static String formatByteFixed(long size) {
        if (size <= 0) return "0B";
        if (size < 1024) return size + "B";
        else size = size / 1024;
        if (size < 1024) return size + "KB";
        else size = size / 1024;
        if (size < 1024) return size + "MB";
        else size = size / 1024;
        if (size < 1024) return size + "GB";
        else size = size / 1024;
        if (size < 1024) return size + "TB";
        else size = size / 1024;
        if (size < 1024) return size + "PB";
        else size = size / 1024;
        if (size < 1024) return size + "EB";
        else size = size / 1024;
        if (size < 1024) return size + "ZB";
        else size = size / 1024;
        if (size < 1024) return size + "YB";
        else size = size / 1024;
        if (size < 1024) return size + "NB";
        else size = size / 1024;
        if (size < 1024) return size + "DB";
        else size = size / 1024;
        return size + "CB";
    }
    
    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    public static File getExternalCacheDir(Context context) {
    	File externalCacheDir = context.getExternalCacheDir();
        if(externalCacheDir != null) {
        	return externalCacheDir;
        }
        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check if OS has external storage.
     *
     * @return
     */
    public static boolean hasExternalStorage() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    
    /**
     * 创建一个 目录<br>
     * <p>默认是在外部存储的根目录下创建一个parentName的文件夹，在下面创建folderName的文件夹，
     * 如果没有外部存储，则在cache目录下创建folderName目录,此时parentName将无效。如果folderName是一个
     * 带有多级目录的路径，也会自动创建多层 目录。
     * @param context
     * @param parentName 父目录，坐落外部存储的根目录，只有在外部存储存在的情况下才有用
     * @param folderName 目录名
     * @return 创建的目录{@link File}对象，如果该目录不可创建，那么返回null
     */
    public static File createDirs(Context context, String parentName, String folderName) {
    	File dirFile = null;
    	if(hasExternalStorage()) {
    		File parentFile = null;
    		if(StringUtils.isEmpty(parentName)) {
    			parentFile = Environment.getExternalStorageDirectory();
    		} else {
    			parentFile = new File(Environment.getExternalStorageDirectory(), parentName);
    		}
    		dirFile = new File(parentFile, folderName);
    	} else {
    		dirFile = new File(context.getCacheDir(), folderName);
    	}
    	if(dirFile.exists() || dirFile.mkdirs()) {
    		return dirFile;
    	}
    	return null;
    }
    
    /**
     * 把raw中文件复制到指定目录下的文件
     * @param context
     * @param id
     * @param dist
     * @return
     */
    public static boolean copyRaw2Dir(Context context, int id, File dist) {

    	InputStream inStream = null;
    	FileOutputStream outStream = null;
        try {
        	inStream = context.getResources().openRawResource(id);
        	outStream = new FileOutputStream(dist);
            byte [] buffer = new byte[1024*1024];
            int size;
            while((size = inStream.read(buffer)) != -1) {
            	outStream.write(buffer, 0, size);
            }
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
        	if(null != outStream) {
        		try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(null != inStream) {
        		try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return true;
    }

    /**
     *
     * @param context
     * @param name
     * @param dist
     * @return
     */
    public static boolean copyAssets2Dir(Context context, String name, File dist) {
    	InputStream inStream = null;
    	FileOutputStream outStream = null;
        try {
        	inStream = context.getResources().getAssets().open(name);
        	outStream = new FileOutputStream(dist);
            byte [] buffer = new byte[1024*1024];
            int size;
            while((size = inStream.read(buffer)) != -1) {
            	outStream.write(buffer, 0, size);
            }
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
        	if(null != outStream) {
        		try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(null != inStream) {
        		try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return true;
    }
    
    /**
     * 读取文本文件内容到String中
     * @param filePath
     * @return
     */
    public static String readStringFromFile(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            return null;
        }
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
            StringBuilder sb = new StringBuilder();
            char [] buffer = new char[1024*1024];
            int len = 0;
            while((len = inputStreamReader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {

        } finally {
            if(null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static String readStringFromAssetFile(Context context, String name) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        int len = 0;
        try {
            inputStream = context.getResources().getAssets().open(name);
            inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder sb = new StringBuilder();
            char [] buffer =  new char[1024*1024];
            while((len = inputStreamReader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
