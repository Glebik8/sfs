package com.blackfish.a1pedal.tools_class;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class LaptopServer {
    private static final String LOG_TAG = "myServerApp";
    // ip адрес сервера, который принимает соединения
    private String mServerName = "192.168.0.10";
    // номер порта, на который сервер принимает соединения
    private int mServerPort = 6789;
    // сокет, через которий приложения общается с сервером
   private Socket mSocket = null;


   public LaptopServer() { }

    /**  * Открытие нового соединения. Если сокет уже открыт, то он закрывается.  *
     *  * @throws Exception  *
     *  Если не удалось открыть сокет  */
    public void openConnection() throws Exception {
        /* Освобождаем ресурсы */
        closeConnection();
        try {         /*             Создаем новый сокет. Указываем на каком компютере и порту запущен наш процесс,             который будет принамать наше соединение.         */
            mSocket = new Socket(mServerName,mServerPort);
        } catch (IOException e)
        {
            throw new Exception("Невозможно создать сокет: "+e.getMessage());
        }
    }

    /**  * Метод для закрытия сокета, по которому мы общались.  */
    public void closeConnection() {     /* Проверяем сокет. Если он не зарыт, то закрываем его и освобдождаем соединение.*/
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Невозможно закрыть сокет: " + e.getMessage());
            } finally {
                mSocket = null;
            }
        }
        mSocket = null;

    }
}
