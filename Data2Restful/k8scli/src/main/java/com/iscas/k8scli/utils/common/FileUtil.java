package com.iscas.k8scli.utils.common;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {


    private static File[] empty = new File[0];


    public static File[] list(String name) {
        try {
            name = patternPath(name);
            File path = new File(name);
            if (path.exists() && path.isDirectory()) {
                return path.listFiles();
            } else {
                return empty;
            }
        } catch (Exception e) {
            return empty;
        }

    }


    /**
     * @param path
     * @return
     */
    public static boolean createFile(String path) {
        boolean isCreated = false;
        try {
            File file = new File(path);
            isCreated = file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isCreated;
    }

    /**
     * @param path
     * @return
     */
    public static boolean makeDir(String path) {
        boolean isCreated = false;
        try {
            File file = new File(path);
            if (!file.exists()) {
                isCreated = file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isCreated;
    }

    /**
     * @param path
     * @return
     */
    public static boolean deleteDir(String path) {
        try {
            File file = new File(path);
            if (file.isDirectory()) {
                String[] children = file.list();
                //递归删除目录中的子目录下
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(path + File.separator + children[i]);
                    if (!success) {
                        return false;
                    }
                }
            }
            // 目录此时为空，可以删除
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        try {
            File file = new File(path);
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            if (file.exists() && file.isFile()) {
                System.gc();
                return file.delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeFile(String content, String path) throws IOException {
        FileOutputStream fop = new FileOutputStream(path);
        // 构建FileOutputStream对象,文件不存在会自动新建
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
        writer.append(content);
        // 写入到缓冲区
//      writer.flush();
        // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
        writer.close();
        //关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
        fop.close();
        // 关闭输出流,释放系统资源
        return true;
    }

    public static String readFile(String path) {
        try {
            //判断是否为中文字符，是则不需要转换，不是需要
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(path);
            if (!m.find()) {
                path = new String(path.getBytes("ISO-8859-1"), "UTF-8");
            }

            FileInputStream fip = new FileInputStream(path);
            // 构建FileInputStream对象
            InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
            // 构建InputStreamReader对象,编码与写入相同

            StringBuffer sb = new StringBuffer();
            while (reader.ready()) {
                sb.append((char) reader.read());
                // 转成char加到StringBuffer对象中
            }
            reader.close();
            // 关闭读取流
            fip.close();
            // 关闭输入流,释放系统资源
            return new String(sb.toString());
        } catch (Exception e) {
            System.err.println("The OS does not support ");
            e.printStackTrace();
            return null;
        }
    }

    public static String patternPath(String path) {
        try {
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(path);
            if (!m.find()) {
                path = new String(path.getBytes("ISO-8859-1"), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static boolean renameFile(String fromPath, String toPath) {
        boolean isRename = false;
        try {
            File oldFile = new File(fromPath);
            File newFile = new File(toPath);
            boolean flag = oldFile.renameTo(newFile);
            if (flag) {
                isRename = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRename;
    }

    public static boolean renameDir(String fromDir, String toDir) {
        boolean isRename = false;
        try {
            File from = new File(fromDir);
            if (!from.exists() || !from.isDirectory()) {
                System.out.println("Directory does not exist: " + fromDir);
            }
            File to = new File(toDir);
            //Rename
            boolean flag = from.renameTo(to);
            if (flag) {
                isRename = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRename;
    }

    /**
     * isExistFile
     */
    public boolean isExistFile(String realpath) {
        try {
            //判断是否为中文字符，是则不需要转换，不是需要
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(realpath);
            if (!m.find()) {
                realpath = new String(realpath.getBytes("ISO-8859-1"), "UTF-8");
            }
            // String path = new String(realpath.getBytes("ISO-8859-1"), "UTF-8");
            System.out.println("*************realpath**********" + realpath);
            boolean isexist = new File(realpath).exists();
            return isexist;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * isExistDir
     */
    public boolean isExistDir(String realpath) {
        try {
            //判断是否为中文字符，是则不需要转换，不是需要
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(realpath);
            if (!m.find()) {
                realpath = new String(realpath.getBytes("ISO-8859-1"), "UTF-8");
            }
            //String path = new String(realpath.getBytes("ISO-8859-1"), "UTF-8");
            System.out.println("*************realpath**********" + realpath);
            return new File(realpath).isDirectory();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isEmpty(String name) {
        File dir = new File(name);
        if (dir.isDirectory()) {
            return dir.list().length == 0;
        }
        return true;
    }

    /**
     * 获取resources 目录下文件内容
     *
     * @param path
     * @return
     */
    public static String getResourceTemplate(String path) {
        BufferedReader br = null;
        try {
            StringBuilder sb = new StringBuilder();
            br = new BufferedReader(new InputStreamReader(new ClassPathResource(path).getInputStream(), "UTF-8"));
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\r\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "print('IO Error')\r\n";
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void fileCopy(File src, File tar) {
        File[] files1 = src.listFiles();
        for (int i = 0; i < files1.length; i++) {
            if (files1[i].isDirectory()) {
                String s = files1[i].getName();
                File file = new File(tar.getPath() + "/" + s);
                file.mkdir();
                fileCopy(files1[i], file);
            } else if (files1[i].isFile()) {
                String s = files1[i].getName();
                BufferedReader br = null;
                BufferedWriter bw = null;
                try {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(files1[i].getPath() + "")));
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tar.getPath() + "/" + s)));
                    String t;
                    while (true) {
                        t = br.readLine();
                        if (t != null) {
                            bw.write(t);
                            bw.write("\n");
                        } else {
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bw != null) {
                        try {
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
