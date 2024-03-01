package com.iscas.k8scli.services.fileService;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

/**
 * @ClassName: FileService
 * @Description:
 * @Author: wzc
 * @Date: 2023/5/9 10:02
 */
@Service
public class FileService {

    private static final int REPLACE_LEN = 500;

    /**
     * 用字符缓冲流的方法readline()读取，用字符串缓存，将字符串转为字符数组重新写入
     */
    public void fileRevise(File file, String code) throws IOException {
        Reader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = ((BufferedReader) reader).readLine()) != null) {
            sb.append(str);
            sb.append("\n");
        }
        System.out.print(sb);
        int start = sb.indexOf("${presql}");  //替换指定地方
        StringBuilder replace = sb;
        if (start >= 0) {
            replace = sb.replace(start, start + REPLACE_LEN, code);
        }
        reader.close();
        Writer write = new BufferedWriter(new FileWriter(file));
        write.write(String.valueOf(replace));
        write.close();
    }


    public static void callCmd(String locationCmd) {
        try {
            Process child = Runtime.getRuntime().exec("cmd.exe /c start /b " + locationCmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(child.getInputStream())); //虽然cmd
            // 命令可以直接输出，但是通过IO流技术可以保证对数据进行一个缓冲。
            System.out.println("执行该编译");
            String msg = null;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
            }
            br.close();
            child.waitFor();
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void callSh() throws IOException {

        Runtime rt = Runtime.getRuntime();
        String command = "D:\\软件所\\数据中台\\项目\\dataCenter\\dataCenter\\baseService\\buildsh.sh";
        Process pcs = rt.exec(command);
        BufferedReader br = new BufferedReader(new InputStreamReader(pcs.getInputStream()));
        String line = new String();
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        try {
            pcs.waitFor();
        } catch (InterruptedException e) {
            System.err.println("processes was interrupted");
        }
        br.close();
        int ret = pcs.exitValue();
        System.out.println(ret);
        System.out.println("执行完毕!");
    }

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\软件所\\数据中台\\项目\\dataCenter\\dataCenter\\baseService\\src\\main\\resources\\mybatis"
            + "\\mapper\\data\\DataMapper.xml";
        File file = new File(filePath);
        String code = " select * from user\n"
            + "        <trim prefix=\"where\" prefixOverrides=\"and\">\n"
            + "            <if test=\"name != null and name != ''\" >\n"
            + "                name = #{name}\n"
            + "            </if>\n"
            + "        </trim> ";
        new FileService().fileRevise(file, code);

        callCmd("D:\\软件所\\数据中台\\项目\\dataCenter\\dataCenter\\baseService\\build.bat");
//        callSh();
    }
}
