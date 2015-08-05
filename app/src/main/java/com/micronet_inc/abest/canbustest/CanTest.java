package com.micronet_inc.abest.canbustest;

import android.util.Log;

import com.micronet.canbus.CanbusFrame;
import com.micronet.canbus.CanbusInterface;
import com.micronet.canbus.CanbusSocket;
import com.micronet.canbus.J1708Frame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by abest on 7/28/15.
 */
public class CanTest {

    private CanbusInterface canbusInterface = new CanbusInterface();
    private CanbusSocket canbusSocket;

    public CanTest() {


    }

    /* For J1708 version of library
    public boolean isJ1708Supported()
    {
        return canbusInterface.isJ1708Supported();
    }
    */



    /// Canbus Reader Thread
    private class CanbusReader implements Runnable
    {
        //private AtomicInteger canbusFrameCount;
        private volatile int canbusFrameCount = 0;
        private volatile int canbusByteCount = 0;
        private volatile int rollovers = 0;
        private volatile int maxdiff = 0;


        public int getCanbusFrameCount() {
            return canbusFrameCount;
        }

        public int getCanbusByteCount() {
            return canbusByteCount;
        }

        public int getRollovers() {
            return rollovers;
        }

        public int getMaxdiff() {
            return maxdiff;
        }

        private int lastcycle = 0;
        @Override
        public void run() {
            while (true) {

                CanbusFrame canbusFrame = null;
                if ((canbusFrame = canbusSocket.read()) != null) {

                    if(canbusFrame.getId() == 0x111111)
                    {
                        ByteBuffer wr = ByteBuffer.wrap(canbusFrame.getData(),4, 4);
                        wr.order(ByteOrder.LITTLE_ENDIAN);
                        int cnt = wr.getShort() & 0x7fff;

                        if(0 == cnt) {
                            lastcycle = 0;
                            ++rollovers;
                        }
                        else
                        {
                            if( (cnt != lastcycle+1))
                            {
                                Log.e("TEST", "missed packet got " + cnt + " expecting " + (lastcycle+1));
                                if(cnt-lastcycle > maxdiff)
                                    maxdiff = cnt-lastcycle;
                            }
                            lastcycle = cnt;
                        }
                        //Log.e("TEST", "cnt = " + cnt);
                        /*
                        if(canbusFrame.getData()[0] == 0x01)
                        {
                            Log.e("TEST", "test1");
                        }*/

                    }
                    //canbusFrameCount.getAndIncrement();
                    ++canbusFrameCount;
                    canbusByteCount += canbusFrame.getData().length;
                }
            }
        }
    }
    private CanbusReader canbusReader = null;
    private Thread canbusReaderThread = null;
    public int getCanbusFrameCount() {
        return canbusReader.getCanbusFrameCount();
    }

    public int getCanbusByteCount() {
        return canbusReader.getCanbusByteCount();
    }

    public int getCanbusRollovers() {
        return canbusReader.getRollovers();
    }
    public int getCanbusMaxdiff() {
        return  canbusReader.getMaxdiff();
    }
    /// End


    /* J1708 Library
    /// J1708 Reader Thread
    private class J1708Reader implements Runnable
    {
        //private AtomicInteger canbusFrameCount;
        private volatile int j1708FrameCount = 0;
        private volatile int j1708ByteCount = 0;

        public int getJ1708FrameCount() {
            return j1708FrameCount;
        }

        public int getJ1708ByteCount() {
            return j1708ByteCount;

        }

        @Override
        public void run() {
            while (true) {
                J1708Frame j1708Frame = null;
                if ((j1708Frame = canbusSocket.readJ1708()) != null) {
                    //canbusFrameCount.getAndIncrement();
                    ++j1708FrameCount;
                    j1708ByteCount += j1708Frame.getData().length;
                }
            }
        }
    }

    private J1708Reader j1708Reader = null;
    private Thread j1708ReaderThread = null;



    public int getJ1939FrameCount() {
        return j1708Reader.getJ1708FrameCount();
    }

    public int getJ1939ByteCount() {
        return j1708Reader.getJ1708ByteCount();
    }
    */

    //////

    private void startThreads() {
        canbusReader = new CanbusReader();
        canbusReaderThread = new Thread(canbusReader);
        canbusReaderThread.start();

        /* For J1708 version of library
        if(canbusInterface.isJ1708Supported()) {
            j1708Reader = new J1708Reader();
            j1708ReaderThread = new Thread(j1708Reader);
            j1708ReaderThread.start();
        }
        */
    }

    public void CreateInterface()
    {
        canbusInterface.create();
        canbusInterface.setBitrate(250000);

        canbusSocket = canbusInterface.createSocket();
        canbusSocket.open();

        startThreads();
    }

    //////

    public void runCanTest1()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                byte[] arr = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

                //ByteBuffer dbuf = ByteBuffer.allocate(8);
                //dbuf.putInt(i);

                for(i = 0; i < 100000; ++i)
                {
                    ByteBuffer dbuf = ByteBuffer.allocate(8);
                    dbuf.order(ByteOrder.LITTLE_ENDIAN);
                    dbuf.putInt(i);
                    byte[] a = dbuf.array();
                    canbusSocket.write(new CanbusFrame(0x111, a));
                }
            }
        });
        thread.start();
    }


    /* for J1708 version of library
    public void runJ1708Test1()
    {
        if(canbusInterface.isJ1708Supported()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    byte[] arr = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

                    //ByteBuffer dbuf = ByteBuffer.allocate(8);
                    //dbuf.putInt(i);

                    for (i = 0; i < 100000; ++i) {
                        ByteBuffer dbuf = ByteBuffer.allocate(8);
                        dbuf.order(ByteOrder.LITTLE_ENDIAN);
                        dbuf.putInt(i);
                        byte[] a = dbuf.array();
                        J1708Frame frame = new J1708Frame(8, 111, a);
                        canbusSocket.writeJ1708(frame);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        }
    }
    */


}
