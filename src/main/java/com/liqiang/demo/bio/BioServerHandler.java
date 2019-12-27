package com.liqiang.demo.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.liqiang.demo.Const.response;

/**
 * BIO服务端处理器
 */
public class BioServerHandler implements Runnable {
    private Socket socket;

    public BioServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        //负责socket读写的输出、输入流
        //try括号内的资源会在try语句结束后自动释放，前提是这些可关闭的资源必须实现 java.lang.AutoCloseable 接口。
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            String message;
            String result;
            //通过输入流读取客户端传输的数据
            //如果已经读到输入流尾部，返回null，退出循环
            //如果得到非空值，就将结果进行业务处理
            while ((message = in.readLine()) != null) {
                System.out.println("Server accept message:" + message);
                result = response(message);
                //将业务结果通过输出流返回给客户端
                out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
